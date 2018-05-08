package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Picture;

import java.util.UUID;

public class PictureDatabaseLink extends DatabaseLink {
    public PictureDatabaseLink() {
        super("picture", Picture.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
    }
}
