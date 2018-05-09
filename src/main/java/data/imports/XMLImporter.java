package data.imports;

import data.model.objects.Source;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLImporter {
    private static Logger log = Logger.getLogger(XMLImporter.class);

    private File file;

    public XMLImporter file(File file) {
        this.file = file;
        return this;
    }

    public static XMLImporter build() {
        return new XMLImporter();
    }

    public void execute() {
        log.info("Executing");

        try {
            Document dom;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;

            db = dbf.newDocumentBuilder();
            dom = db.parse(file);

            Element docEle = dom.getDocumentElement();

            log.info("Doc element is called " + docEle.getTagName());
            if ("Sources".equals(docEle.getTagName())) {
                log.info("Found Sources");
                NodeList sourceNodeList = docEle.getElementsByTagName("Source");

                log.info("Sources total = " + sourceNodeList.getLength());

                for (int i = 0; i < sourceNodeList.getLength(); i++) { // Loop through each Source
                    Element sourceNode = (Element) sourceNodeList.item(i);

                    String name = sourceNode.getElementsByTagName("name").item(0).getTextContent();
                    String fileName = sourceNode.getElementsByTagName("fileName").item(0).getTextContent();

                    log.info("Name = " + name);
                    log.info("FileName = " + fileName);

                    Source newSource = Source.create(Source.class);
                    newSource.setFileName(fileName);
                    newSource.setName(name);
                    newSource.save();
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
    }
}