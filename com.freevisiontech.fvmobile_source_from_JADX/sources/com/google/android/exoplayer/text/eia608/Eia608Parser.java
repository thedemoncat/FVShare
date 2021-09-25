package com.google.android.exoplayer.text.eia608;

import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;

public final class Eia608Parser {
    private static final int[] BASIC_CHARACTER_SET = {32, 33, 34, 35, 36, 37, 38, 39, 40, 41, CompanyIdentifierResolver.DANLERS_LTD, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, CompanyIdentifierResolver.JOLLY_LOGIC_LLC, CompanyIdentifierResolver.KENT_DISPLAYS_INC, 250, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CompanyIdentifierResolver.KS_TECHNOLOGIES, CompanyIdentifierResolver.VSN_TECHNOLOGIES_INC, CompanyIdentifierResolver.POLAR_ELECTRO_EUROPE_BV, CompanyIdentifierResolver.WITRON_TECHNOLOGY_LIMITED, 9632};
    private static final int COUNTRY_CODE = 181;
    private static final int PAYLOAD_TYPE_CC = 4;
    private static final int PROVIDER_CODE = 49;
    private static final int[] SPECIAL_CHARACTER_SET = {CompanyIdentifierResolver.OMEGAWAVE_OY, CompanyIdentifierResolver.PASSIF_SEMICONDUCTOR_CORP, 189, CompanyIdentifierResolver.STALMART_TECHNOLOGY_LIMITED, 8482, CompanyIdentifierResolver.VERTU_CORPORATION_LIMITED, CompanyIdentifierResolver.META_WATCH_LTD, 9834, 224, 32, CompanyIdentifierResolver.ACTS_TECHNOLOGIES, CompanyIdentifierResolver.SEMILINK_INC, CompanyIdentifierResolver.NIELSENKELLERMAN_COMPANY, CompanyIdentifierResolver.ABOVE_AVERAGE_OUTCOMES_INC, 244, CompanyIdentifierResolver.KOUKAAM_AS};
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET = {193, CompanyIdentifierResolver.EVLUMA, CompanyIdentifierResolver.TAIXINGBANG_TECHNOLOGY_HK_CO_LTD, CompanyIdentifierResolver.TXTR_GMBH, CompanyIdentifierResolver.PROCTER_GAMBLE, CompanyIdentifierResolver.DELPHI_CORPORATION, 8216, CompanyIdentifierResolver.SRMEDIZINELEKTRONIK, 42, 39, 8212, CompanyIdentifierResolver.MAGNETI_MARELLI_SPA, 8480, 8226, 8220, 8221, 192, 194, CompanyIdentifierResolver.QUUPPA_OY, 200, CompanyIdentifierResolver.MC10, CompanyIdentifierResolver.BINAURIC_SE, CompanyIdentifierResolver.SERVER_TECHNOLOGY_INC, CompanyIdentifierResolver.ELGATO_SYSTEMS_GMBH, CompanyIdentifierResolver.ARCHOS_SA, CompanyIdentifierResolver.BITSPLITTERS_GMBH, CompanyIdentifierResolver.KAWANTECH, CompanyIdentifierResolver.VOYETRA_TURTLE_BEACH, CompanyIdentifierResolver.STICKNFIND, CompanyIdentifierResolver.BIOSENTRONICS, CompanyIdentifierResolver.INGENIEURSYSTEMGRUPPE_ZAHN_GMBH, CompanyIdentifierResolver.SPOWER_ELECTRONICS_LIMITED};
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET = {195, CompanyIdentifierResolver.INMUSIC_BRANDS_INC, CompanyIdentifierResolver.MICROCHIP_TECHNOLOGY_INC, CompanyIdentifierResolver.BEATS_ELECTRONICS, CompanyIdentifierResolver.BIORESEARCH_ASSOCIATES, CompanyIdentifierResolver.DIALOG_SEMICONDUCTOR_BV, CompanyIdentifierResolver.MORSE_PROJECT_INC, CompanyIdentifierResolver.AUSTCO_COMMUNICATION_SYSTEMS, 245, 123, 125, 92, 94, 95, 124, 126, 196, CompanyIdentifierResolver.LS_RESEARCH_INC, CompanyIdentifierResolver.TIMEX_GROUP_USA_INC, CompanyIdentifierResolver.ELCOMETER_LIMITED, CompanyIdentifierResolver.MISFIT_WEARABLES_CORP, CompanyIdentifierResolver.OTL_DYNAMICS_LLC, CompanyIdentifierResolver.LINAK_A_S, 9474, 197, CompanyIdentifierResolver.EDEN_SOFTWARE_CONSULTANTS_LTD, CompanyIdentifierResolver.QUALCOMM_CONNECTED_EXPERIENCES_INC, CompanyIdentifierResolver.ACEUNI_CORP_LTD, 9484, 9488, 9492, 9496};
    private static final int USER_DATA_TYPE_CODE = 3;
    private static final int USER_ID = 1195456820;
    private final ArrayList<ClosedCaption> captions = new ArrayList<>();
    private final ParsableBitArray seiBuffer = new ParsableBitArray();
    private final StringBuilder stringBuilder = new StringBuilder();

    Eia608Parser() {
    }

    /* access modifiers changed from: package-private */
    public boolean canParse(String mimeType) {
        return mimeType.equals(MimeTypes.APPLICATION_EIA608);
    }

    /* access modifiers changed from: package-private */
    public ClosedCaptionList parse(SampleHolder sampleHolder) {
        if (sampleHolder.size < 10) {
            return null;
        }
        this.captions.clear();
        this.stringBuilder.setLength(0);
        this.seiBuffer.reset(sampleHolder.data.array());
        this.seiBuffer.skipBits(67);
        int ccCount = this.seiBuffer.readBits(5);
        this.seiBuffer.skipBits(8);
        for (int i = 0; i < ccCount; i++) {
            this.seiBuffer.skipBits(5);
            if (!this.seiBuffer.readBit()) {
                this.seiBuffer.skipBits(18);
            } else if (this.seiBuffer.readBits(2) != 0) {
                this.seiBuffer.skipBits(16);
            } else {
                this.seiBuffer.skipBits(1);
                byte ccData1 = (byte) this.seiBuffer.readBits(7);
                this.seiBuffer.skipBits(1);
                byte ccData2 = (byte) this.seiBuffer.readBits(7);
                if (ccData1 != 0 || ccData2 != 0) {
                    if ((ccData1 == 17 || ccData1 == 25) && (ccData2 & 112) == 48) {
                        this.stringBuilder.append(getSpecialChar(ccData2));
                    } else if ((ccData1 == 18 || ccData1 == 26) && (ccData2 & 96) == 32) {
                        backspace();
                        this.stringBuilder.append(getExtendedEsFrChar(ccData2));
                    } else if ((ccData1 == 19 || ccData1 == 27) && (ccData2 & 96) == 32) {
                        backspace();
                        this.stringBuilder.append(getExtendedPtDeChar(ccData2));
                    } else if (ccData1 < 32) {
                        addCtrl(ccData1, ccData2);
                    } else {
                        this.stringBuilder.append(getChar(ccData1));
                        if (ccData2 >= 32) {
                            this.stringBuilder.append(getChar(ccData2));
                        }
                    }
                }
            }
        }
        addBufferedText();
        if (this.captions.isEmpty()) {
            return null;
        }
        ClosedCaption[] captionArray = new ClosedCaption[this.captions.size()];
        this.captions.toArray(captionArray);
        return new ClosedCaptionList(sampleHolder.timeUs, sampleHolder.isDecodeOnly(), captionArray);
    }

    private static char getChar(byte ccData) {
        return (char) BASIC_CHARACTER_SET[(ccData & Byte.MAX_VALUE) - 32];
    }

    private static char getSpecialChar(byte ccData) {
        return (char) SPECIAL_CHARACTER_SET[ccData & 15];
    }

    private static char getExtendedEsFrChar(byte ccData) {
        return (char) SPECIAL_ES_FR_CHARACTER_SET[ccData & 31];
    }

    private static char getExtendedPtDeChar(byte ccData) {
        return (char) SPECIAL_PT_DE_CHARACTER_SET[ccData & 31];
    }

    private void addBufferedText() {
        if (this.stringBuilder.length() > 0) {
            this.captions.add(new ClosedCaptionText(this.stringBuilder.toString()));
            this.stringBuilder.setLength(0);
        }
    }

    private void addCtrl(byte ccData1, byte ccData2) {
        addBufferedText();
        this.captions.add(new ClosedCaptionCtrl(ccData1, ccData2));
    }

    private void backspace() {
        addCtrl(ClosedCaptionCtrl.MISC_CHAN_1, ClosedCaptionCtrl.BACKSPACE);
    }

    public static boolean isSeiMessageEia608(int payloadType, int payloadLength, ParsableByteArray payload) {
        if (payloadType != 4 || payloadLength < 8) {
            return false;
        }
        int startPosition = payload.getPosition();
        int countryCode = payload.readUnsignedByte();
        int providerCode = payload.readUnsignedShort();
        int userIdentifier = payload.readInt();
        int userDataTypeCode = payload.readUnsignedByte();
        payload.setPosition(startPosition);
        if (countryCode == 181 && providerCode == 49 && userIdentifier == USER_ID && userDataTypeCode == 3) {
            return true;
        }
        return false;
    }
}
