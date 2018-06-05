package utils;

import data.model.objects.json.JSONContainer;
import data.model.objects.json.JSONDataSource;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JSONData<JSONDataClass> {
    private static Logger log = Logger.getLogger(JSONData.class);

    private String getJSONData(String classPath, String jsonDataSource) {
        try {
            Class<?> classToResolve = Class.forName(classPath);

            try {
                JSONDataClass jsonDataObject = (JSONDataClass) classToResolve.getDeclaredConstructor().newInstance();

                Method[] methods = classToResolve.getMethods();

                for (Method method : methods) {
                    JSONDataSource jsonDataSourceMethod = method.getAnnotation(JSONDataSource.class);
                    if (jsonDataSourceMethod != null && jsonDataSourceMethod.value().equals(jsonDataSource)) {
                        JSONContainer<JSONDataClass> jsonDataClassJSONContainer = new JSONContainer<>();
                        jsonDataClassJSONContainer.dbDataList((List<JSONDataClass>) method.invoke(jsonDataObject));
                        return jsonDataClassJSONContainer.writeToString();
                    }
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return "[]";
    }

    public static String buildJsonData(String classPath, String jsonDataSource) {
        JSONData jsonData = new JSONData();
        return jsonData.getJSONData(classPath, jsonDataSource);
    }
}
