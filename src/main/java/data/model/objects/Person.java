package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Person extends DatabaseObject {
    private static Logger log = Logger.getLogger(Person.class);

    private String firstName = "";
    private String lastName = "";

    public Person() {
        super();
    }

    public Person(UUID uuid, String firstName, String lastName) {
        super(uuid);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JSONMappable("firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JSONMappable("lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
