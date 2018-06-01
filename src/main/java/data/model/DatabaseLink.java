package data.model;

import data.model.links.*;
import data.model.objects.*;
import data.model.objects.Process;
import error.Error;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseLink {
    private static HashMap<Class, Class> linkClasses = new HashMap<>();

    // Need to find an alternative to doing this, something automated
    static {
        linkClasses.put(Source.class, SourceDatabaseLink.class);
        linkClasses.put(Process.class, ProcessDatabaseLink.class);
        linkClasses.put(EncodedProgress.class, EncodedProgressDatabaseLink.class);
        linkClasses.put(Clip.class, ClipDatabaseLink.class);
        linkClasses.put(Mark.class, MarkDatabaseLink.class);
    }

    private String tableName = "";
    private List<ModelColumn> modelColumns = new ArrayList<>();
    private List<DeleteColumn> onDeleteColumns = new ArrayList<>();
    private Class linkClass;

    public DatabaseLink(String tableName, Class linkClass) {
        this.tableName = tableName;
        this.linkClass = linkClass;
    }

    public static Class getLinkClass(Class clazz) {
        return linkClasses.get(clazz);
    }

    public String getTableName() {
        return tableName;
    }

    public List<ModelColumn> getModelColumns() {
        return modelColumns;
    }

    public List<DeleteColumn> getOnDeleteColumns() {
        return onDeleteColumns;
    }

    public void link(String databaseColumn, Method objectSaveMethod, Method objectLoadMethod) {
        modelColumns.add(new ModelColumn(databaseColumn, objectSaveMethod, objectLoadMethod, ModelColumn.STANDARD_COLUMN));
    }

    public void linkBlob(String databaseColumn, Method objectSaveMethod, Method objectLoadMethod) {
        modelColumns.add(new ModelColumn(databaseColumn, objectSaveMethod, objectLoadMethod, ModelColumn.BLOB_COLUMN));
    }

    public Class getLinkClass() {
        return linkClass;
    }

    public Method method(String methodName, Class<?>... parameterTypes) {
        try {
            return getLinkClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            Error.DATA_LINK_METHOD_NOT_FOUND.record()
                    .additionalInformation("Class: " + getLinkClass().getName())
                    .additionalInformation("MethodName: " + methodName)
                    .create(ex);
        }

        return null;
    }

    public Method externalMethod(String methodName, Class clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            Error.DATA_LINK_METHOD_NOT_FOUND.record().create(ex);
        }

        return null;
    }

    public void onDelete(String columnName, Class clazz) {
        onDeleteColumns.add(new DeleteColumn(columnName, clazz));
    }
}
