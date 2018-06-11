package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Clip extends DatabaseObject {
    private static Logger log = Logger.getLogger(Clip.class);

    private Source source = null;
    private Mark startMark = null;
    private Mark endMark = null;
    private String fileName = "";
    private Boolean lockedIn = false;
    private EncodedProgress encodedProgress;

    public Clip() {
        super();
    }

    public Clip(UUID uuid, Source source, Mark startMark, Mark endMark, String fileName, Boolean lockedIn, EncodedProgress encodedProgress) {
        super(uuid);
        this.source = source;
        this.startMark = startMark;
        this.endMark = endMark;
        this.fileName = fileName;
        this.lockedIn = lockedIn;
        this.encodedProgress = encodedProgress;
    }

    @JSONMappable("source")
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @JSONMappable("startMark")
    public Mark getStartMark() {
        return startMark;
    }

    public void setStartMark(Mark startMark) {
        this.startMark = startMark;
    }

    @JSONMappable("endMark")
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

    @JSONMappable("lockedIn")
    public Boolean getLockedIn() {
        return lockedIn;
    }

    public void setLockedIn(Boolean lockedIn) {
        this.lockedIn = lockedIn;
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
}
