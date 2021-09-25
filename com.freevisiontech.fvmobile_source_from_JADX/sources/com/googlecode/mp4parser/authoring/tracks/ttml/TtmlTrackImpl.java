package com.googlecode.mp4parser.authoring.tracks.ttml;

import android.support.p001v4.internal.view.SupportMenu;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.mp4parser.iso14496.part30.XMLSubtitleSampleEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TtmlTrackImpl extends AbstractTrack {
    SampleDescriptionBox sampleDescriptionBox = new SampleDescriptionBox();
    private long[] sampleDurations;
    List<Sample> samples = new ArrayList();
    SubSampleInformationBox subSampleInformationBox = new SubSampleInformationBox();
    TrackMetaData trackMetaData = new TrackMetaData();
    XMLSubtitleSampleEntry xmlSubtitleSampleEntry = new XMLSubtitleSampleEntry();

    public TtmlTrackImpl(String name, List<Document> ttmls) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, URISyntaxException {
        super(name);
        extractLanguage(ttmls);
        Set<String> mimeTypes = new HashSet<>();
        this.sampleDurations = new long[ttmls.size()];
        XPathFactory.newInstance().newXPath().setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        for (int sampleNo = 0; sampleNo < ttmls.size(); sampleNo++) {
            Document ttml = ttmls.get(sampleNo);
            SubSampleInformationBox.SubSampleEntry subSampleEntry = new SubSampleInformationBox.SubSampleEntry();
            this.subSampleInformationBox.getEntries().add(subSampleEntry);
            subSampleEntry.setSampleDelta(1);
            this.sampleDurations[sampleNo] = extractDuration(ttml);
            List<byte[]> images = extractImages(ttml);
            mimeTypes.addAll(extractMimeTypes(ttml));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TtmlHelpers.pretty(ttml, baos, 4);
            SubSampleInformationBox.SubSampleEntry.SubsampleEntry xmlEntry = new SubSampleInformationBox.SubSampleEntry.SubsampleEntry();
            xmlEntry.setSubsampleSize((long) baos.size());
            subSampleEntry.getSubsampleEntries().add(xmlEntry);
            for (byte[] image : images) {
                baos.write(image);
                SubSampleInformationBox.SubSampleEntry.SubsampleEntry imageEntry = new SubSampleInformationBox.SubSampleEntry.SubsampleEntry();
                imageEntry.setSubsampleSize((long) image.length);
                subSampleEntry.getSubsampleEntries().add(imageEntry);
            }
            final byte[] finalSample = baos.toByteArray();
            this.samples.add(new Sample() {
                public void writeTo(WritableByteChannel channel) throws IOException {
                    channel.write(ByteBuffer.wrap(finalSample));
                }

                public long getSize() {
                    return (long) finalSample.length;
                }

                public ByteBuffer asByteBuffer() {
                    return ByteBuffer.wrap(finalSample);
                }
            });
        }
        this.xmlSubtitleSampleEntry.setNamespace(join(",", TtmlHelpers.getAllNamespaces(ttmls.get(0))));
        this.xmlSubtitleSampleEntry.setSchemaLocation("");
        this.xmlSubtitleSampleEntry.setAuxiliaryMimeTypes(join(",", (String[]) new ArrayList(mimeTypes).toArray(new String[mimeTypes.size()])));
        this.sampleDescriptionBox.addBox(this.xmlSubtitleSampleEntry);
        this.trackMetaData.setTimescale(30000);
        this.trackMetaData.setLayer(SupportMenu.USER_MASK);
    }

    public static String getLanguage(Document document) {
        return document.getDocumentElement().getAttribute("xml:lang");
    }

    protected static List<byte[]> extractImages(Document ttml) throws XPathExpressionException, URISyntaxException, IOException {
        NodeList nl = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@backgroundImage").evaluate(ttml, XPathConstants.NODESET);
        LinkedHashMap<String, String> internalNames2Original = new LinkedHashMap<>();
        int p = 1;
        for (int i = 0; i < nl.getLength(); i++) {
            Node bgImageNode = nl.item(i);
            String uri = bgImageNode.getNodeValue();
            String ext = uri.substring(uri.lastIndexOf("."));
            String internalName = internalNames2Original.get(uri);
            if (internalName == null) {
                internalName = "urn:mp4parser:" + p + ext;
                internalNames2Original.put(internalName, uri);
                p++;
            }
            bgImageNode.setNodeValue(internalName);
        }
        List<byte[]> images = new ArrayList<>();
        if (!internalNames2Original.isEmpty()) {
            for (Map.Entry<String, String> internalName2Original : internalNames2Original.entrySet()) {
                images.add(streamToByteArray(new URI(ttml.getDocumentURI()).resolve(internalName2Original.getValue()).toURL().openStream()));
            }
        }
        return images;
    }

    private static String join(String joiner, String[] i) {
        int i2 = 0;
        StringBuilder result = new StringBuilder();
        for (String s : i) {
            result.append(s).append(joiner);
        }
        if (result.length() > 0) {
            i2 = result.length() - 1;
        }
        result.setLength(i2);
        return result.toString();
    }

    private static long latestTimestamp(Document document) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        try {
            NodeList timedNodes = (NodeList) xpath.compile("//*[name()='p']").evaluate(document, XPathConstants.NODESET);
            long lastTimeStamp = 0;
            for (int i = 0; i < timedNodes.getLength(); i++) {
                lastTimeStamp = Math.max(TtmlHelpers.getEndTime(timedNodes.item(i)), lastTimeStamp);
            }
            return lastTimeStamp;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] streamToByteArray(InputStream input) throws IOException {
        byte[] buffer = new byte[8096];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while (true) {
            int n = input.read(buffer);
            if (-1 == n) {
                return output.toByteArray();
            }
            output.write(buffer, 0, n);
        }
    }

    /* access modifiers changed from: protected */
    public long firstTimestamp(Document document) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        try {
            NodeList timedNodes = (NodeList) xpath.compile("//*[@begin]").evaluate(document, XPathConstants.NODESET);
            long firstTimestamp = Long.MAX_VALUE;
            for (int i = 0; i < timedNodes.getLength(); i++) {
                firstTimestamp = Math.min(TtmlHelpers.getStartTime(timedNodes.item(i)), firstTimestamp);
            }
            return firstTimestamp;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public long lastTimestamp(Document document) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        try {
            NodeList timedNodes = (NodeList) xpath.compile("//*[@end]").evaluate(document, XPathConstants.NODESET);
            long lastTimeStamp = 0;
            for (int i = 0; i < timedNodes.getLength(); i++) {
                lastTimeStamp = Math.max(TtmlHelpers.getEndTime(timedNodes.item(i)), lastTimeStamp);
            }
            return lastTimeStamp;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void extractLanguage(List<Document> ttmls) {
        String firstLang = null;
        for (Document ttml : ttmls) {
            String lang = getLanguage(ttml);
            if (firstLang == null) {
                firstLang = lang;
                this.trackMetaData.setLanguage(Locale.forLanguageTag(lang).getISO3Language());
            } else if (!firstLang.equals(lang)) {
                throw new RuntimeException("Within one Track all sample documents need to have the same language");
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<String> extractMimeTypes(Document ttml) throws XPathExpressionException {
        NodeList nl = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@smpte:backgroundImage").evaluate(ttml, XPathConstants.NODESET);
        Set<String> mimeTypes = new LinkedHashSet<>();
        for (int i = 0; i < nl.getLength(); i++) {
            String uri = nl.item(i).getNodeValue();
            String ext = uri.substring(uri.lastIndexOf("."));
            if (ext.contains("jpg") || ext.contains("jpeg")) {
                mimeTypes.add("image/jpeg");
            } else if (ext.contains("png")) {
                mimeTypes.add("image/png");
            }
        }
        return new ArrayList(mimeTypes);
    }

    /* access modifiers changed from: package-private */
    public long extractDuration(Document ttml) {
        return lastTimestamp(ttml) - firstTimestamp(ttml);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        long[] adoptedSampleDuration = new long[this.sampleDurations.length];
        for (int i = 0; i < adoptedSampleDuration.length; i++) {
            adoptedSampleDuration[i] = (this.sampleDurations[i] * this.trackMetaData.getTimescale()) / 1000;
        }
        return adoptedSampleDuration;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public String getHandler() {
        return "subt";
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.subSampleInformationBox;
    }

    public void close() throws IOException {
    }
}
