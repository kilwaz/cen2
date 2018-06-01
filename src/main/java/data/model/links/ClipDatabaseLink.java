package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Clip;
import data.model.objects.Mark;
import data.model.objects.Source;

import java.util.UUID;

public class ClipDatabaseLink extends DatabaseLink {
    public ClipDatabaseLink() {
        super("clip", Clip.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("source", method("getSourceUUID"), method("setSource", Source.class)); // 2
        link("start_mark", method("getStartMarkUUID"), method("setStartMark", Mark.class)); // 3
        link("end_mark", method("getEndMarkUUID"), method("setEndMark", Mark.class)); // 4
        link("file_name", method("getFileName"), method("setFileName", String.class)); // 5
    }
}
