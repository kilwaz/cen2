package data.model.objects;

import data.model.DatabaseObject;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Appearance extends DatabaseObject {
    private static Logger log = Logger.getLogger(Appearance.class);

    private Person person = null;
    private Clip clip = null;

    public Appearance() {
        super();
    }

    public Appearance(UUID uuid, Person person, Clip clip) {
        super(uuid);
        this.person = person;
        this.clip = clip;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public String getClipUUID() {
        if (clip == null) {
            return null;
        } else {
            return clip.getUuidString();
        }
    }

    public String getPersonUUID() {
        if (person == null) {
            return null;
        } else {
            return person.getUuidString();
        }
    }
}
