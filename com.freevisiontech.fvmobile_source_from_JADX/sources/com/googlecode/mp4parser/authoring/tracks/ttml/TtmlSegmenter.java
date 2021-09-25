package com.googlecode.mp4parser.authoring.tracks.ttml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TtmlSegmenter {
    public static List<Document> split(Document doc, int splitTimeInSeconds) throws XPathExpressionException {
        boolean thereIsMore;
        int splitTime = splitTimeInSeconds * 1000;
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xp = xpath.compile("//*[name()='p']");
        ArrayList arrayList = new ArrayList();
        do {
            long segmentStartTime = (long) (arrayList.size() * splitTime);
            long segmentEndTime = (long) ((arrayList.size() + 1) * splitTime);
            Document d = (Document) doc.cloneNode(true);
            NodeList timedNodes = (NodeList) xp.evaluate(d, XPathConstants.NODESET);
            thereIsMore = false;
            for (int i = 0; i < timedNodes.getLength(); i++) {
                Node p = timedNodes.item(i);
                long startTime = TtmlHelpers.getStartTime(p);
                long endTime = TtmlHelpers.getEndTime(p);
                if (startTime < segmentStartTime && endTime > segmentStartTime) {
                    changeTime(p, "begin", segmentStartTime - startTime);
                    startTime = segmentStartTime;
                }
                if (startTime >= segmentStartTime && startTime < segmentEndTime && endTime > segmentEndTime) {
                    changeTime(p, TtmlNode.END, segmentEndTime - endTime);
                    startTime = segmentStartTime;
                    endTime = segmentEndTime;
                }
                if (startTime > segmentEndTime) {
                    thereIsMore = true;
                }
                if (startTime < segmentStartTime || endTime > segmentEndTime) {
                    p.getParentNode().removeChild(p);
                } else {
                    changeTime(p, "begin", -segmentStartTime);
                    changeTime(p, TtmlNode.END, -segmentStartTime);
                }
            }
            trimWhitespace(d);
            Element body = (Element) xpath.compile("/*[name()='tt']/*[name()='body'][1]").evaluate(d, XPathConstants.NODE);
            String beginTime = body.getAttribute("begin");
            String endTime2 = body.getAttribute(TtmlNode.END);
            if (beginTime == null || "".equals(beginTime)) {
                body.setAttribute("begin", TtmlHelpers.toTimeExpression(segmentStartTime));
            } else {
                changeTime(body, "begin", segmentStartTime);
            }
            if (endTime2 == null || "".equals(endTime2)) {
                body.setAttribute(TtmlNode.END, TtmlHelpers.toTimeExpression(segmentEndTime));
            } else {
                changeTime(body, TtmlNode.END, segmentEndTime);
            }
            arrayList.add(d);
        } while (thereIsMore);
        return arrayList;
    }

    public static void changeTime(Node p, String attribute, long amount) {
        int frames;
        if (p.getAttributes() != null && p.getAttributes().getNamedItem(attribute) != null) {
            String oldValue = p.getAttributes().getNamedItem(attribute).getNodeValue();
            long nuTime = TtmlHelpers.toTime(oldValue) + amount;
            if (oldValue.contains(".")) {
                frames = -1;
            } else {
                frames = ((int) (nuTime - ((nuTime / 1000) * 1000))) / 44;
            }
            p.getAttributes().getNamedItem(attribute).setNodeValue(TtmlHelpers.toTimeExpression(nuTime, frames));
        }
    }

    public static Document normalizeTimes(Document doc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        NodeList timedNodes = (NodeList) xpath.compile("//*[name()='p']").evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < timedNodes.getLength(); i++) {
            pushDown(timedNodes.item(i));
        }
        for (int i2 = 0; i2 < timedNodes.getLength(); i2++) {
            Node p = timedNodes.item(i2);
            removeAfterPushDown(p, "begin");
            removeAfterPushDown(p, TtmlNode.END);
        }
        return doc;
    }

    private static void pushDown(Node p) {
        long time = 0;
        Node current = p;
        while (true) {
            current = current.getParentNode();
            if (current == null) {
                break;
            } else if (!(current.getAttributes() == null || current.getAttributes().getNamedItem("begin") == null)) {
                time += TtmlHelpers.toTime(current.getAttributes().getNamedItem("begin").getNodeValue());
            }
        }
        if (!(p.getAttributes() == null || p.getAttributes().getNamedItem("begin") == null)) {
            p.getAttributes().getNamedItem("begin").setNodeValue(TtmlHelpers.toTimeExpression(TtmlHelpers.toTime(p.getAttributes().getNamedItem("begin").getNodeValue()) + time));
        }
        if (p.getAttributes() != null && p.getAttributes().getNamedItem(TtmlNode.END) != null) {
            p.getAttributes().getNamedItem(TtmlNode.END).setNodeValue(TtmlHelpers.toTimeExpression(TtmlHelpers.toTime(p.getAttributes().getNamedItem(TtmlNode.END).getNodeValue()) + time));
        }
    }

    private static void removeAfterPushDown(Node p, String begin) {
        Node current = p;
        while (true) {
            current = current.getParentNode();
            if (current != null) {
                if (!(current.getAttributes() == null || current.getAttributes().getNamedItem(begin) == null)) {
                    current.getAttributes().removeNamedItem(begin);
                }
            } else {
                return;
            }
        }
    }

    public static void trimWhitespace(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == 3) {
                child.setTextContent(child.getTextContent().trim());
            }
            trimWhitespace(child);
        }
    }
}
