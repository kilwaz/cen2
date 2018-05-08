package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Person;

import java.util.UUID;

public class PersonDatabaseLink extends DatabaseLink {
    public PersonDatabaseLink() {
        super("person", Person.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
    }
}
