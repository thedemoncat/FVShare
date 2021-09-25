package com.googlecode.mp4parser.authoring.tracks.ttml;

import com.google.android.vending.expansion.downloader.Constants;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TtmlHelpers {
    public static final NamespaceContext NAMESPACE_CONTEXT = new TextTrackNamespaceContext((TextTrackNamespaceContext) null);
    public static final String SMPTE_TT_NAMESPACE = "http://www.smpte-ra.org/schemas/2052-1/2010/smpte-tt";
    public static final String TTML_NAMESPACE = "http://www.w3.org/ns/ttml";
    static byte[] namespacesStyleSheet1 = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n    <xsl:output method=\"text\"/>\n    <xsl:key name=\"kElemByNSURI\"\n             match=\"*[namespace::*[not(. = ../../namespace::*)]]\"\n              use=\"namespace::*[not(. = ../../namespace::*)]\"/>\n    <xsl:template match=\"/\">\n        <xsl:for-each select=\n            \"//namespace::*[not(. = ../../namespace::*)]\n                           [count(..|key('kElemByNSURI',.)[1])=1]\">\n            <xsl:value-of select=\"concat(.,'&#xA;')\"/>\n        </xsl:for-each>\n    </xsl:template>\n</xsl:stylesheet>".getBytes();

    public static void main(String[] args) throws URISyntaxException, ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Track t = new TtmlTrackImpl("a.xml", TtmlSegmenter.split(dbf.newDocumentBuilder().parse("C:\\dev\\mp4parser\\a.xml"), 60));
        Movie m = new Movie();
        m.addTrack(t);
        new DefaultMp4Builder().build(m).writeContainer(new FileOutputStream("output.mp4").getChannel());
    }

    public static String[] getAllNamespaces(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new ByteArrayInputStream(namespacesStyleSheet1)));
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            List<String> r = new ArrayList<>(new LinkedHashSet(Arrays.asList(sw.getBuffer().toString().split("\n"))));
            return (String[]) r.toArray(new String[r.size()]);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static String toTimeExpression(long ms) {
        return toTimeExpression(ms, -1);
    }

    public static String toTimeExpression(long ms, int frames) {
        String minus = ms >= 0 ? "" : Constants.FILENAME_SEQUENCE_SEPARATOR;
        long ms2 = Math.abs(ms);
        long hours = ((ms2 / 1000) / 60) / 60;
        long ms3 = ms2 - (((1000 * hours) * 60) * 60);
        long minutes = (ms3 / 1000) / 60;
        long ms4 = ms3 - ((1000 * minutes) * 60);
        long seconds = ms4 / 1000;
        long ms5 = ms4 - (1000 * seconds);
        if (frames >= 0) {
            return String.format("%s%02d:%02d:%02d:%d", new Object[]{minus, Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds), Integer.valueOf(frames)});
        }
        return String.format("%s%02d:%02d:%02d.%03d", new Object[]{minus, Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds), Long.valueOf(ms5)});
    }

    public static long toTime(String expr) {
        long ms;
        Matcher m = Pattern.compile("(-?)([0-9][0-9]):([0-9][0-9]):([0-9][0-9])([\\.:][0-9][0-9]?[0-9]?)?").matcher(expr);
        if (m.matches()) {
            String minus = m.group(1);
            String hours = m.group(2);
            String minutes = m.group(3);
            String seconds = m.group(4);
            String fraction = m.group(5);
            if (fraction == null) {
                fraction = ".000";
            }
            String fraction2 = fraction.replace(":", ".");
            long ms2 = (Long.parseLong(hours) * 60 * 60 * 1000) + (Long.parseLong(minutes) * 60 * 1000) + (Long.parseLong(seconds) * 1000);
            if (fraction2.contains(":")) {
                ms = (long) (((double) ms2) + (Double.parseDouble("0" + fraction2.replace(":", ".")) * 40.0d * 1000.0d));
            } else {
                ms = (long) (((double) ms2) + (Double.parseDouble("0" + fraction2) * 1000.0d));
            }
            return ((long) (Constants.FILENAME_SEQUENCE_SEPARATOR.equals(minus) ? -1 : 1)) * ms;
        }
        throw new RuntimeException("Cannot match '" + expr + "' to time expression");
    }

    private static class TextTrackNamespaceContext implements NamespaceContext {
        private TextTrackNamespaceContext() {
        }

        /* synthetic */ TextTrackNamespaceContext(TextTrackNamespaceContext textTrackNamespaceContext) {
            this();
        }

        public String getNamespaceURI(String prefix) {
            if (prefix.equals("ttml")) {
                return TtmlHelpers.TTML_NAMESPACE;
            }
            if (prefix.equals("smpte")) {
                return TtmlHelpers.SMPTE_TT_NAMESPACE;
            }
            return null;
        }

        public Iterator getPrefixes(String val) {
            return Arrays.asList(new String[]{"ttml", "smpte"}).iterator();
        }

        public String getPrefix(String uri) {
            if (uri.equals(TtmlHelpers.TTML_NAMESPACE)) {
                return "ttml";
            }
            if (uri.equals(TtmlHelpers.SMPTE_TT_NAMESPACE)) {
                return "smpte";
            }
            return null;
        }
    }

    public static void pretty(Document document, OutputStream outputStream, int indent) throws IOException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");
            if (indent > 0) {
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent));
            }
            try {
                transformer.transform(new DOMSource(document), new StreamResult(outputStream));
            } catch (TransformerException e) {
                throw new IOException(e);
            }
        } catch (TransformerConfigurationException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static long getStartTime(Node p) {
        long time = 0;
        Node current = p;
        while (true) {
            current = current.getParentNode();
            if (current == null) {
                break;
            } else if (!(current.getAttributes() == null || current.getAttributes().getNamedItem("begin") == null)) {
                time += toTime(current.getAttributes().getNamedItem("begin").getNodeValue());
            }
        }
        if (p.getAttributes() == null || p.getAttributes().getNamedItem("begin") == null) {
            return time;
        }
        return time + toTime(p.getAttributes().getNamedItem("begin").getNodeValue());
    }

    public static long getEndTime(Node p) {
        long time = 0;
        Node current = p;
        while (true) {
            current = current.getParentNode();
            if (current == null) {
                break;
            } else if (!(current.getAttributes() == null || current.getAttributes().getNamedItem("begin") == null)) {
                time += toTime(current.getAttributes().getNamedItem("begin").getNodeValue());
            }
        }
        if (p.getAttributes() == null || p.getAttributes().getNamedItem(TtmlNode.END) == null) {
            return time;
        }
        return time + toTime(p.getAttributes().getNamedItem(TtmlNode.END).getNodeValue());
    }

    public static void deepCopyDocument(Document ttml, File target) throws IOException {
        try {
            NodeList nl = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@backgroundImage").evaluate(ttml, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                URI backgroundImageUri = URI.create(nl.item(i).getNodeValue());
                if (!backgroundImageUri.isAbsolute()) {
                    copyLarge(new URI(ttml.getDocumentURI()).resolve(backgroundImageUri).toURL().openStream(), new File(target.toURI().resolve(backgroundImageUri).toURL().getFile()));
                }
            }
            copyLarge(new URI(ttml.getDocumentURI()).toURL().openStream(), target);
        } catch (XPathExpressionException e) {
            throw new IOException(e);
        } catch (URISyntaxException e2) {
            throw new IOException(e2);
        }
    }

    private static long copyLarge(InputStream input, File outputFile) throws IOException {
        byte[] buffer = new byte[16384];
        long count = 0;
        outputFile.getParentFile().mkdirs();
        FileOutputStream output = new FileOutputStream(outputFile);
        while (true) {
            try {
                int n = input.read(buffer);
                if (-1 == n) {
                    return count;
                }
                output.write(buffer, 0, n);
                count += (long) n;
            } finally {
                output.close();
            }
        }
    }
}
