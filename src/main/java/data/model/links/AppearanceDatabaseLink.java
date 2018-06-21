package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Appearance;
import data.model.objects.Clip;
import data.model.objects.Person;

import java.util.UUID;

public class AppearanceDatabaseLink extends DatabaseLink {
    public AppearanceDatabaseLink() {
        super("appearance", Appearance.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("clip_id", method("getClipUUID"), method("setClip", Clip.class)); // 2
        link("person_id", method("getPersonUUID"), method("setPerson", Person.class)); // 3
    }
}
