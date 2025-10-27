package com.gofortrainings.newsportal.core.transformers;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;

@Component(
        immediate = true,
        service = TransformerFactory.class,
        property = {
                "pipeline.type=newsportal-linkrewriter"
        }
)
// https://medium.com/@toimrank/link-checker-and-transformer-381d4f245d12
public class MyRewriterTransformer implements Transformer, TransformerFactory {

    private ContentHandler contentHandler;

    @Override
    public Transformer createTransformer() {
        return new MyRewriterTransformer();
    }

    @Override
    public void init(ProcessingContext processingContext, ProcessingComponentConfiguration processingComponentConfiguration) throws IOException {

    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setDocumentLocator(Locator locator) {
        contentHandler.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        contentHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        contentHandler.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        contentHandler.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        contentHandler.endPrefixMapping(prefix);
    }

    /*
      This is the main function which is responsible for URL
      main update.
    */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        // This will trim /content/practice/us/en/home to /us/en/home on page load
        if (atts.getIndex("href") > -1 && qName.equalsIgnoreCase("a")) {
            AttributesImpl modifiedAttributes = new AttributesImpl(atts);
            String sortHref = modifiedUrl(atts.getValue("href"));

            modifiedAttributes.setValue(atts.getIndex("href"), sortHref);
            contentHandler.startElement(uri, localName, qName, modifiedAttributes);
        }

        // This will append http://localhost:4502 in front of asset URL
        if (atts.getIndex("src") > -1 && qName.equalsIgnoreCase("img")) {
            AttributesImpl modifiedAttributes = new AttributesImpl(atts);
            String sortHref = "http://localhost:4502"+modifiedUrl(atts.getValue("src"));

            modifiedAttributes.setValue(atts.getIndex("src"), sortHref);
            contentHandler.startElement(uri, localName, qName, modifiedAttributes);
        }
    }

    public static String modifiedUrl(String path) {
        if (StringUtils.isBlank(path)) {
            return path; // blank, return it as is.
        } else {
            if(path.startsWith("/content/newsportal")) {
                return StringUtils.removeAll(path, "/content/newsportal");
            }
        }
        return path;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        contentHandler.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        contentHandler.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        contentHandler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        contentHandler.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        contentHandler.skippedEntity(name);
    }
}
