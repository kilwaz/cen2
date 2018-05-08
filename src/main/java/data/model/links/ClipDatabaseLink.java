package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Clip;

import java.util.UUID;

public class ClipDatabaseLink extends DatabaseLink {
    public ClipDatabaseLink() {
        super("clip", Clip.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
    }
}
