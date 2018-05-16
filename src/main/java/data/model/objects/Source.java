package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;

import java.util.UUID;

public class Source extends DatabaseObject {
    private String fileName = "";
    private String name = "";
    private String url = "";

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

    public void setUrl(String url) {
        this.url = url;
    }
}
