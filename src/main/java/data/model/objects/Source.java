package data.model.objects;

import data.model.DatabaseObject;

import java.util.UUID;

public class Source extends DatabaseObject {
    private String fileName = "";
    private String name = "";

    public Source() {
        super();
    }

    public Source(UUID uuid, String fileName, String name) {
        super(uuid);
        this.fileName = fileName;
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
