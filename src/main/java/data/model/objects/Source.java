package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Source extends DatabaseObject {
    private static Logger log = Logger.getLogger(Source.class);

    private String fileName = "";
    private String name = "";
    private String url = "";
    private EncodedProgress encodedProgress;

    public Source() {
        super();
    }

    public Source(UUID uuid, String fileName, String url, String name) {
        super(uuid);
        this.fileName = fileName;
        this.url = url;
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JSONMappable("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONMappable("url")
    public String getUrl() {
        return url;
    }

    public EncodedProgress getEncodedProgress() {
        return encodedProgress;
    }

    public void setEncodedProgress(EncodedProgress encodedProgress) {
        log.info("Set the encoded progress " + encodedProgress.getUuidString());
        this.encodedProgress = encodedProgress;
    }

    public String getEncodedProgressUUID() {
        log.info("Trying to save encoded progress");
        if (encodedProgress != null) {
            log.info("Returning the UUID!! " + encodedProgress.getUuidString());
            return encodedProgress.getUuidString();
        }

        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
