package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.UUID;

public class Source extends DatabaseObject {
    private static Logger log = Logger.getLogger(Source.class);

    private String fileName = "";
    private String encodedFileName = "";
    private String name = "";
    private String url = "";
    private String encodedUrl = "";
    private EncodedProgress encodedProgress;
    private JSONObject sourceInfo;

    public Source() {
        super();
    }

    public Source(UUID uuid, String fileName, String encodedFileName, String url, String encodedUrl, String name, JSONObject sourceInfo) {
        super(uuid);
        this.fileName = fileName;
        this.encodedFileName = encodedFileName;
        this.url = url;
        this.encodedUrl = encodedUrl;
        this.name = name;
        this.sourceInfo = sourceInfo;
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

    @JSONMappable("encodedProgress")
    public EncodedProgress getEncodedProgress() {
        return encodedProgress;
    }

    public void setEncodedProgress(EncodedProgress encodedProgress) {
        this.encodedProgress = encodedProgress;
    }

    public String getEncodedProgressUUID() {
        if (encodedProgress != null) {
            return encodedProgress.getUuidString();
        }

        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JSONMappable("sourceInfo")
    public JSONObject getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(JSONObject sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public String getEncodedFileName() {
        return encodedFileName;
    }

    public void setEncodedFileName(String encodedFileName) {
        this.encodedFileName = encodedFileName;
    }

    @JSONMappable("encodedUrl")
    public String getEncodedUrl() {
        return encodedUrl;
    }

    public void setEncodedUrl(String encodedUrl) {
        this.encodedUrl = encodedUrl;
    }
}
