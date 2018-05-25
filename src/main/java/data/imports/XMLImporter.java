package data.imports;

import core.process.Prober;
import data.model.objects.EncodedProgress;
import data.model.objects.Source;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

            var dbf = DocumentBuilderFactory.newInstance();
            var db = dbf.newDocumentBuilder();

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
                    String url = sourceNode.getElementsByTagName("url").item(0).getTextContent();

                    log.info("Name = " + name);
                    log.info("FileName = " + fileName);
                    log.info("URL = " + url);

                    Source newSource = Source.create(Source.class);
                    EncodedProgress newEncodedProgress = EncodedProgress.create(EncodedProgress.class);
                    newEncodedProgress.setPassPhase(0);

                    newSource.setFileName(fileName);
                    newSource.setName(name);
                    newSource.setUrl(url);
                    newSource.setEncodedProgress(newEncodedProgress);
                    newSource.save();
                    newEncodedProgress.save();

                    Prober prober = new Prober().source(newSource);
                    prober.execute();
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
    }
}