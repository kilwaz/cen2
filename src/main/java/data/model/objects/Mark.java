package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Mark extends DatabaseObject {
    private static Logger log = Logger.getLogger(Mark.class);

    private Source source;
    private Double time = 0d;

    public Mark() {
        super();
    }

    public Mark(UUID uuid, Source source, Double time) {
        super(uuid);
        this.source = source;
        this.time = time;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @JSONMappable("time")
    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public String getSourceUUID() {
        return source.getUuidString();
    }
}
