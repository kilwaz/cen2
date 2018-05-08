package data.model.objects.json;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JSONMapper<DatabaseObject> {
    private static Logger log = Logger.getLogger(JSONMapper.class);

    public JSONObject process(DatabaseObject databaseObject) {
        JSONObject jsonObject = new JSONObject();
        Class mappingClass = databaseObject.getClass();

        Method[] methods = mappingClass.getMethods();

        for (Method method : methods) {
            JSONMappable jsonMappable = method.getAnnotation(JSONMappable.class);
            if (jsonMappable != null) { // Mappable annotation exists
                try {
                    jsonObject.put(jsonMappable.value(), method.invoke(databaseObject));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonObject;
    }

    public JSONArray process(List<DatabaseObject> databaseObjectList) {
        JSONArray objects = new JSONArray();

        for (DatabaseObject databaseObject : databaseObjectList) {
            objects.put(process(databaseObject));
        }

        return objects;
    }

    public static JSONMapper build() {
        return new JSONMapper();
    }
}
