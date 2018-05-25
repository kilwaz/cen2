package data.model.objects;

import data.model.DatabaseObject;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Clip extends DatabaseObject {
    private static Logger log = Logger.getLogger(Clip.class);

    private Source source;
    private Double startTime = 0d;
    private Double endTime = 0d;
    private String fileName = "";

    public Clip() {
        super();
    }

    public Clip(UUID uuid, Source source, Double startTime, Double endTime, String fileName) {
        super(uuid);
        this.source = source;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fileName = fileName;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getSourceUUID() {
        return source.getUuidString();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
