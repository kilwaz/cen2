package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Clip;
import data.model.objects.Source;

import java.util.UUID;

public class ClipDatabaseLink extends DatabaseLink {
    public ClipDatabaseLink() {
        super("clip", Clip.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("source", method("getSourceUUID"), method("setSource", Source.class)); // 2
        link("start_time", method("getStartTime"), method("setStartTime", Double.class)); // 3
        link("end_time", method("getEndTime"), method("setEndTime", Double.class)); // 4
        link("file_name", method("getFileName"), method("setFileName", String.class)); // 5
    }
}
