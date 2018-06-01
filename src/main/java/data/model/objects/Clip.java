package data.model.objects;

import data.model.DatabaseObject;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Clip extends DatabaseObject {
    private static Logger log = Logger.getLogger(Clip.class);

    private Source source = null;
    private Mark startMark = null;
    private Mark endMark = null;
    private String fileName = "";

    public Clip() {
        super();
    }

    public Clip(UUID uuid, Source source, Mark startMark, Mark endMark, String fileName) {
        super(uuid);
        this.source = source;
        this.startMark = startMark;
        this.endMark = endMark;
        this.fileName = fileName;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Mark getStartMark() {
        return startMark;
    }

    public void setStartMark(Mark startMark) {
        this.startMark = startMark;
    }

    public Mark getEndMark() {
        return endMark;
    }

    public void setEndMark(Mark endMark) {
        this.endMark = endMark;
    }

    public String getStartMarkUUID() {
        if (startMark == null) {
            return null;
        } else {
            return startMark.getUuidString();
        }
    }

    public String getEndMarkUUID() {
        if (endMark == null) {
            return null;
        } else {
            return endMark.getUuidString();
        }
    }

    public String getSourceUUID() {
        if (source == null) {
            return null;
        } else {
            return source.getUuidString();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
