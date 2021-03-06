package com.google.android.exoplayer.extractor.mp4;

import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

final class Sniffer {
    private static final int[] COMPATIBLE_BRANDS = {Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("iso3"), Util.getIntegerCodeForString("iso4"), Util.getIntegerCodeForString("iso5"), Util.getIntegerCodeForString("iso6"), Util.getIntegerCodeForString(VisualSampleEntry.TYPE3), Util.getIntegerCodeForString(VisualSampleEntry.TYPE6), Util.getIntegerCodeForString(VisualSampleEntry.TYPE7), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV")};
    private static final int SEARCH_LENGTH = 4096;

    public static boolean sniffFragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, true);
    }

    public static boolean sniffUnfragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, false);
    }

    private static boolean sniffInternal(ExtractorInput input, boolean fragmented) throws IOException, InterruptedException {
        long inputLength = input.getLength();
        if (inputLength == -1 || inputLength > 4096) {
            inputLength = 4096;
        }
        int bytesToSearch = (int) inputLength;
        ParsableByteArray buffer = new ParsableByteArray(64);
        int bytesSearched = 0;
        boolean foundGoodFileType = false;
        boolean isFragmented = false;
        while (true) {
            if (bytesSearched >= bytesToSearch) {
                break;
            }
            int headerSize = 8;
            input.peekFully(buffer.data, 0, 8);
            buffer.setPosition(0);
            long atomSize = buffer.readUnsignedInt();
            int atomType = buffer.readInt();
            if (atomSize == 1) {
                headerSize = 16;
                input.peekFully(buffer.data, 8, 8);
                atomSize = buffer.readUnsignedLongToLong();
            }
            if (atomSize >= ((long) headerSize)) {
                bytesSearched += headerSize;
                if (atomType != Atom.TYPE_moov) {
                    if (atomType != Atom.TYPE_moof && atomType != Atom.TYPE_mvex) {
                        if ((((long) bytesSearched) + atomSize) - ((long) headerSize) >= ((long) bytesToSearch)) {
                            break;
                        }
                        int atomDataSize = (int) (atomSize - ((long) headerSize));
                        bytesSearched += atomDataSize;
                        if (atomType == Atom.TYPE_ftyp) {
                            if (atomDataSize < 8) {
                                return false;
                            }
                            if (buffer.capacity() < atomDataSize) {
                                buffer.reset(new byte[atomDataSize], atomDataSize);
                            }
                            input.peekFully(buffer.data, 0, atomDataSize);
                            int brandsCount = atomDataSize / 4;
                            int i = 0;
                            while (true) {
                                if (i >= brandsCount) {
                                    break;
                                }
                                if (i == 1) {
                                    buffer.skipBytes(4);
                                } else if (isCompatibleBrand(buffer.readInt())) {
                                    foundGoodFileType = true;
                                    break;
                                }
                                i++;
                            }
                            if (!foundGoodFileType) {
                                return false;
                            }
                        } else if (atomDataSize != 0) {
                            input.advancePeekPosition(atomDataSize);
                        }
                    } else {
                        isFragmented = true;
                    }
                }
            } else {
                return false;
            }
        }
        isFragmented = true;
        if (!foundGoodFileType || fragmented != isFragmented) {
            return false;
        }
        return true;
    }

    private static boolean isCompatibleBrand(int brand) {
        if ((brand >>> 8) == Util.getIntegerCodeForString("3gp")) {
            return true;
        }
        for (int compatibleBrand : COMPATIBLE_BRANDS) {
            if (compatibleBrand == brand) {
                return true;
            }
        }
        return false;
    }

    private Sniffer() {
    }
}
