package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Source;

import java.util.UUID;

public class SourceDatabaseLink extends DatabaseLink {
    public SourceDatabaseLink() {
        super("source", Source.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("file_name", method("getFileName"), method("setFileName", String.class)); // 2
        link("name", method("getName"), method("setName", String.class)); // 3
    }
}
