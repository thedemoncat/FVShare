package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.MultiFileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.tracks.AbstractH26XTrack;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderSpecificInfo;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.googlecode.mp4parser.util.Path;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class H263TrackImpl extends AbstractH26XTrack {
    private static Logger LOG = Logger.getLogger(ESDescriptor.class.getName());
    int BINARY = 1;
    int BINARY_ONLY = 2;
    int GRAYSCALE = 3;
    int RECTANGULAR = 0;
    boolean esdsComplete = false;
    List<ByteBuffer> esdsStuff = new ArrayList();
    int fixed_vop_time_increment = -1;
    List<Sample> samples = new ArrayList();
    SampleDescriptionBox stsd;
    int vop_time_increment_resolution = 0;

    public H263TrackImpl(DataSource dataSource) throws IOException {
        super(dataSource, false);
        AbstractH26XTrack.LookAhead la = new AbstractH26XTrack.LookAhead(dataSource);
        ArrayList arrayList = new ArrayList();
        int visual_object_verid = 0;
        VisualSampleEntry mp4v = new VisualSampleEntry(VisualSampleEntry.TYPE1);
        this.stsd = new SampleDescriptionBox();
        this.stsd.addBox(mp4v);
        long last_sync_point = 0;
        long last_time_code = -1;
        while (true) {
            ByteBuffer nal = findNextNal(la);
            if (nal == null) {
                this.decodingTimes = Mp4Arrays.copyOfAndAppend(this.decodingTimes, this.decodingTimes[this.decodingTimes.length - 1]);
                ESDescriptor esDescriptor = new ESDescriptor();
                esDescriptor.setEsId(1);
                DecoderConfigDescriptor decoderConfigDescriptor = new DecoderConfigDescriptor();
                decoderConfigDescriptor.setObjectTypeIndication(32);
                decoderConfigDescriptor.setStreamType(4);
                DecoderSpecificInfo decoderSpecificInfo = new DecoderSpecificInfo();
                Sample s = createSampleObject(this.esdsStuff);
                byte[] data = new byte[CastUtils.l2i(s.getSize())];
                s.asByteBuffer().get(data);
                decoderSpecificInfo.setData(data);
                decoderConfigDescriptor.setDecoderSpecificInfo(decoderSpecificInfo);
                esDescriptor.setDecoderConfigDescriptor(decoderConfigDescriptor);
                SLConfigDescriptor slConfigDescriptor = new SLConfigDescriptor();
                slConfigDescriptor.setPredefined(2);
                esDescriptor.setSlConfigDescriptor(slConfigDescriptor);
                ESDescriptorBox esds = new ESDescriptorBox();
                esds.setEsDescriptor(esDescriptor);
                mp4v.addBox(esds);
                this.trackMetaData.setTimescale((long) this.vop_time_increment_resolution);
                return;
            }
            ByteBuffer origNal = nal.duplicate();
            int type = IsoTypeReader.readUInt8(nal);
            if (type == 176 || type == 181 || type == 0 || type == 32 || type == 178) {
                if (!this.esdsComplete) {
                    this.esdsStuff.add(origNal);
                    if (type == 32) {
                        parse0x20Unit(nal, visual_object_verid, mp4v);
                    } else if (type == 181) {
                        visual_object_verid = parse0x05Unit(nal);
                    }
                }
            } else if (type == 179) {
                this.esdsComplete = true;
                int time_code = new BitReaderBuffer(nal).readBits(18);
                last_sync_point = (long) ((time_code & 63) + (((time_code >>> 7) & 63) * 60) + (((time_code >>> 13) & 31) * 60 * 60));
                this.stss.add(Integer.valueOf(this.samples.size() + 1));
                arrayList.add(origNal);
            } else if (type == 182) {
                BitReaderBuffer brb = new BitReaderBuffer(nal);
                brb.readBits(2);
                while (brb.readBool()) {
                    last_sync_point++;
                }
                brb.readBool();
                int i = 0;
                while (this.vop_time_increment_resolution >= (1 << i)) {
                    i++;
                }
                int vop_time_increment = brb.readBits(i);
                long time_code2 = (((long) this.vop_time_increment_resolution) * last_sync_point) + ((long) (vop_time_increment % this.vop_time_increment_resolution));
                if (last_time_code != -1) {
                    this.decodingTimes = Mp4Arrays.copyOfAndAppend(this.decodingTimes, time_code2 - last_time_code);
                }
                System.err.println("Frame increment: " + (time_code2 - last_time_code) + " vop time increment: " + vop_time_increment + " last_sync_point: " + last_sync_point + " time_code: " + time_code2);
                last_time_code = time_code2;
                arrayList.add(origNal);
                this.samples.add(createSampleObject(arrayList));
                arrayList.clear();
            } else {
                throw new RuntimeException("Got start code I don't know. Ask Sebastian via mp4parser mailing list what to do");
            }
        }
    }

    private int parse0x05Unit(ByteBuffer nal) {
        BitReaderBuffer brb = new BitReaderBuffer(nal);
        if (!brb.readBool()) {
            return 0;
        }
        int visual_object_verid = brb.readBits(4);
        brb.readBits(3);
        return visual_object_verid;
    }

    private void parse0x20Unit(ByteBuffer nal, int visual_object_verid, VisualSampleEntry mp4v) {
        BitReaderBuffer brb = new BitReaderBuffer(nal);
        brb.readBool();
        brb.readBits(8);
        int video_object_layer_verid = visual_object_verid;
        if (brb.readBool()) {
            video_object_layer_verid = brb.readBits(4);
            brb.readBits(3);
        }
        if (brb.readBits(4) == 15) {
            brb.readBits(8);
            brb.readBits(8);
        }
        if (brb.readBool()) {
            brb.readBits(2);
            brb.readBool();
            if (brb.readBool()) {
                throw new RuntimeException("Implemented when needed");
            }
        }
        int video_object_layer_shape = brb.readBits(2);
        if (video_object_layer_shape == this.GRAYSCALE && video_object_layer_verid != 1) {
            brb.readBits(4);
        }
        brb.readBool();
        this.vop_time_increment_resolution = brb.readBits(16);
        brb.readBool();
        if (brb.readBool()) {
            LOG.info("Fixed Frame Rate");
            int i = 0;
            while (this.vop_time_increment_resolution >= (1 << i)) {
                i++;
            }
            this.fixed_vop_time_increment = brb.readBits(i);
        }
        if (video_object_layer_shape == this.BINARY_ONLY) {
            throw new RuntimeException("Please implmenet me");
        } else if (video_object_layer_shape == this.RECTANGULAR) {
            brb.readBool();
            mp4v.setWidth(brb.readBits(13));
            brb.readBool();
            mp4v.setHeight(brb.readBits(13));
            brb.readBool();
        }
    }

    /* access modifiers changed from: protected */
    public Sample createSampleObject(List<? extends ByteBuffer> nals) {
        byte[] bArr = new byte[3];
        bArr[2] = 1;
        ByteBuffer startcode = ByteBuffer.wrap(bArr);
        ByteBuffer[] data = new ByteBuffer[(nals.size() * 2)];
        for (int i = 0; i < nals.size(); i++) {
            data[i * 2] = startcode;
            data[(i * 2) + 1] = (ByteBuffer) nals.get(i);
        }
        return new SampleImpl(data);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public String getHandler() {
        return "vide";
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public static void main1(String[] args) throws IOException {
        File[] files = new File("C:\\dev\\mp4parser\\frames").listFiles();
        Arrays.sort(files);
        Movie m = new Movie();
        m.addTrack(new H263TrackImpl(new MultiFileDataSourceImpl(files)));
        new DefaultMp4Builder().build(m).writeContainer(Channels.newChannel(new FileOutputStream("output.mp4")));
    }

    public static void main(String[] args) throws IOException {
        DataSource ds = new FileDataSourceImpl("C:\\content\\bbb.h263");
        Movie m = new Movie();
        m.addTrack(new H263TrackImpl(ds));
        new DefaultMp4Builder().build(m).writeContainer(Channels.newChannel(new FileOutputStream("output.mp4")));
    }

    public static void main2(String[] args) throws IOException {
        ESDescriptorBox esds = (ESDescriptorBox) Path.getPath((Container) new IsoFile("C:\\content\\bbb.mp4"), "/moov[0]/trak[0]/mdia[0]/minf[0]/stbl[0]/stsd[0]/mp4v[0]/esds[0]");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        esds.getBox(Channels.newChannel(baos));
        System.err.println(Hex.encodeHex(baos.toByteArray()));
        System.err.println(esds.getEsDescriptor());
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        esds.getBox(Channels.newChannel(baos2));
        System.err.println(Hex.encodeHex(baos2.toByteArray()));
    }
}
