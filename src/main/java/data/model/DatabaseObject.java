package data.model;

import data.model.objects.json.JSONMappable;
import data.model.objects.json.JSONMapper;
import error.Error;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import utils.managers.DatabaseObjectManager;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class DatabaseObject {
    private static Logger log = Logger.getLogger(DatabaseObject.class);
    private UUID uuid = UUID.randomUUID();
    private Class jsonMapper = null;

    public DatabaseObject() {
        // Here the UUID is generated for us
    }

    public DatabaseObject(UUID uuid) {
        this.uuid = uuid;
        this.load();
    }

    public static <DBObject extends DatabaseObject> DBObject load(UUID uuid, Class<DBObject> clazz) {
        DatabaseObjectManager databaseObjectManager = DatabaseObjectManager.getInstance();
        DatabaseObject loadedObject;

//        try {
//            databaseObject = databaseObjectManager.getDatabaseObjects().get(uuid.toString(), () -> {
//                DatabaseObject loadedObject = create(clazz);
//                loadedObject.setUuid(uuid);
//                //loadedObject.load();
//
//                return loadedObject;
//            });
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        if (databaseObject != null) {
//            databaseObject.load();
//        }

        if (databaseObjectManager.objectExists(uuid)) {
            loadedObject = databaseObjectManager.getDatabaseObject(uuid);
        } else {
            loadedObject = create(clazz);
            loadedObject.setUuid(uuid);
            databaseObjectManager.addObject(loadedObject);
            loadedObject.load();
        }

        return (DBObject) loadedObject;
    }

    public static <DBObject extends DatabaseObject> DBObject create(Class<DBObject> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Error.CREATE_NEW_INSTANCE_ERROR.record().create(ex);
        }

        return null;
    }

    @JSONMappable("uuid")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUuidFromString(String uuidStr) {
        uuid = UUID.fromString(uuidStr);
    }

    public String getUuidStringWithoutHyphen() {
        return uuid.toString().replace("-", "");
    }

    public String getUuidString() {
        return uuid.toString();
    }

    public void scheduleToSave() {

    }

    public void save() {
        try {
            new DatabaseAction<>().save(this, (DatabaseLink) DatabaseLink.getLinkClass(this.getClass()).getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Error.DATABASE_SAVE_CLASS_INIT.record().additionalInformation("Class " + this.getClass()).create(ex);
        } catch (NullPointerException ex) {
            Error.DATABASE_SAVE_CLASS_INIT.record()
                    .additionalInformation("Class/DBLink likely not defined")
                    .additionalInformation("Class " + this.getClass()).create(ex);
        }
    }

    public void delete() {
        try {
            new DatabaseAction<>().delete(this, (DatabaseLink) DatabaseLink.getLinkClass(this.getClass()).getDeclaredConstructor().newInstance());
        } catch (NullPointerException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Error.DATABASE_DELETE_CLASS_INIT.record().additionalInformation("Class " + this.getClass()).create(ex);
        }
    }

    public void load() {
        try {
            new DatabaseAction<>().load(this, (DatabaseLink) DatabaseLink.getLinkClass(this.getClass()).getDeclaredConstructor().newInstance());
        } catch (NullPointerException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Error.DATABASE_LOAD_CLASS_INIT.record().additionalInformation("Class " + this.getClass()).create(ex);
        }
    }

    public JSONObject asJSON() {
        return JSONMapper.build().process(this);
    }
}
