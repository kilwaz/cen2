package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Mark;
import data.model.objects.Source;

import java.util.UUID;

public class MarkDatabaseLink extends DatabaseLink {
    public MarkDatabaseLink() {
        super("mark", Mark.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("source", method("getSourceUUID"), method("setSource", Source.class)); // 2
        link("time", method("getTime"), method("setTime", Double.class)); // 3
    }
}
