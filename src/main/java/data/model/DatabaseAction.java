package data.model;

import data.*;
import data.model.dao.DAO;
import data.model.objects.EncodedProgress;
import data.model.objects.Mark;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import error.Error;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import utils.managers.DatabaseObjectManager;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseAction<DBObject extends DatabaseObject, DBLink extends DatabaseLink> {
    private static Logger log = Logger.getLogger(DatabaseAction.class);

    private static ConcurrentHashMap<String, DelayedLoad> delayedLoadedObjects = new ConcurrentHashMap<>();

    public void save(DBObject dbObject, DBLink dbLink) {
        // Building the update query string
        var updateQueryBuilder = new StringBuilder();
        updateQueryBuilder.append("update ")
                .append(dbLink.getTableName());
        Boolean firstColumn = true;
        for (ModelColumn modelColumn : dbLink.getModelColumns()) {
            if (firstColumn) {
                updateQueryBuilder.append(" set ")
                        .append(modelColumn.getColumnName())
                        .append(" = ?");
                firstColumn = false;
            } else {
                updateQueryBuilder.append(", ")
                        .append(modelColumn.getColumnName())
                        .append(" = ?");
            }
        }

        updateQueryBuilder.append(" where uuid = ?");

        // Building the update query object
        UpdateQuery updateQuery = new UpdateQuery(updateQueryBuilder.toString());

        for (ModelColumn modelColumn : dbLink.getModelColumns()) {
            try {
                updateQuery.addParameter(modelColumn.getObjectSaveMethod().invoke(dbObject));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Error.DATABASE_OBJECT_METHOD_NOT_FOUND
                        .record()
                        .additionalInformation("Column: " + modelColumn.getColumnName())
                        .additionalInformation("Method: " + modelColumn.getObjectSaveMethod().getName())
                        .additionalInformation("Linked Class: " + dbLink.getLinkClass())
                        .create(ex);
            }
        }

        // The id to update
        updateQuery.addParameter(dbObject.getUuidString());

        // Execute the update query
        var updateResult = (UpdateResult) updateQuery.execute();

        // If record does not exist insert a new one..
        if (updateResult.getResultNumber() == 0) {
            // Build the insert statement
            updateQueryBuilder = new StringBuilder();
            updateQueryBuilder.append("insert into ")
                    .append(dbLink.getTableName())
                    .append("(");

            firstColumn = true;
            for (ModelColumn modelColumn : dbLink.getModelColumns()) {
                if (firstColumn) {
                    updateQueryBuilder.append(modelColumn.getColumnName());
                    firstColumn = false;
                } else {
                    updateQueryBuilder.append(",").append(modelColumn.getColumnName());
                }
            }

            updateQueryBuilder.append(")")
                    .append(" values (?")
                    .append(StringUtils.repeat(", ?", dbLink.getModelColumns().size() - 1))
                    .append(")");

            // Create query object and fill in parameters
            UpdateQuery insertQuery = new UpdateQuery(updateQueryBuilder.toString());
            for (ModelColumn modelColumn : dbLink.getModelColumns()) {
                try {
                    insertQuery.addParameter(modelColumn.getObjectSaveMethod().invoke(dbObject));
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Error.DATABASE_OBJECT_METHOD_NOT_FOUND.record().create(ex);
                }
            }

            // Run the insert query
            insertQuery.execute();
        }
    }

    public void delete(DBObject dbObject, DBLink dbLink) {
        // Create query object and fill in parameters
        var deleteQuery = new UpdateQuery("delete from " + dbLink.getTableName() + " where uuid = ?");
        for (ModelColumn modelColumn : dbLink.getModelColumns()) {
            try {
                if ("uuid".equals(modelColumn.getColumnName())) {
                    deleteQuery.addParameter(modelColumn.getObjectSaveMethod().invoke(dbObject));
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Error.DATABASE_OBJECT_METHOD_NOT_FOUND.record().create(ex);
            }
        }

        // Run the delete query
        deleteQuery.execute();
    }

    public void load(DBObject dbObject, DBLink dbLink) {
        // Building the select query string
        var selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("select ");
        var firstColumn = true;
        for (ModelColumn modelColumn : dbLink.getModelColumns()) {
            if (firstColumn) {
                selectQueryBuilder
                        .append(modelColumn.getColumnName())
                        .append(" ");
                firstColumn = false;
            } else {
                selectQueryBuilder.append(", ")
                        .append(modelColumn.getColumnName())
                        .append(" ");
            }
        }
        selectQueryBuilder
                .append("from ")
                .append(dbLink.getTableName())
                .append(" where uuid = ?");

        var selectResult = (SelectResult) new SelectQuery(selectQueryBuilder.toString())
                .addParameter(dbObject.getUuidString())
                .execute();

        for (var resultRow : selectResult.getResults()) {
            for (var modelColumn : dbLink.getModelColumns()) {
                try {
                    if (modelColumn.getObjectLoadMethod() != null) {
                        var loadMethodParameter = modelColumn.getObjectLoadMethod().getParameterTypes();
                        Class loadParameterClass = null;

                        if (loadMethodParameter.length > 0) {
                            loadParameterClass = loadMethodParameter[0];
                        }
                        if (loadParameterClass != null) {
                            if (loadParameterClass.equals(String.class)) {  // STRING
                                modelColumn.getObjectLoadMethod().invoke(dbObject, resultRow.getString(modelColumn.getColumnName()));
                            } else if (loadParameterClass.equals(Double.class)) { // DOUBLE
                                modelColumn.getObjectLoadMethod().invoke(dbObject, resultRow.getDouble(modelColumn.getColumnName()));
                            } else if (loadParameterClass.equals(Integer.class)) { // INTEGER
                                modelColumn.getObjectLoadMethod().invoke(dbObject, resultRow.getInt(modelColumn.getColumnName()));
                            } else if (loadParameterClass.equals(Boolean.class)) { // BOOLEAN
                                modelColumn.getObjectLoadMethod().invoke(dbObject, resultRow.getBoolean(modelColumn.getColumnName()));
                            } else if (loadParameterClass.equals(JSONObject.class)) { // JSON OBJECT
                                var jsonStr = resultRow.getString(modelColumn.getColumnName());
                                if (jsonStr != null) {
                                    var jsonContainer = new JSONContainer(jsonStr);
                                    modelColumn.getObjectLoadMethod().invoke(dbObject, jsonContainer.toJSONObject());
                                }
                            } else if (loadParameterClass.equals(UUID.class)) { // UUID
                                String uuid = resultRow.getString(modelColumn.getColumnName());
                                if (uuid != null && !uuid.isEmpty()) {
                                    modelColumn.getObjectLoadMethod().invoke(dbObject, DAO.UUIDFromString(uuid));
                                }
                            } else if (loadParameterClass.equals(EncodedProgress.class)) { // ENCODED PROGRESS
                                String uuidStr = resultRow.getString(modelColumn.getColumnName());
                                if (uuidStr != null && !uuidStr.isEmpty()) {
                                    EncodedProgress encodedProgress = loadCachedObject(uuidStr, EncodedProgress.class);
                                    modelColumn.getObjectLoadMethod().invoke(dbObject, encodedProgress);
                                }
                            } else if (loadParameterClass.equals(Mark.class)) { // MARK
                                String uuidStr = resultRow.getString(modelColumn.getColumnName());
                                if (uuidStr != null && !uuidStr.isEmpty()) {
                                    Mark mark = loadCachedObject(uuidStr, Mark.class);
                                    modelColumn.getObjectLoadMethod().invoke(dbObject, mark);
                                }
                            } else if (loadParameterClass.equals(Source.class)) { // SOURCE
                                String uuidStr = resultRow.getString(modelColumn.getColumnName());
                                if (uuidStr != null && !uuidStr.isEmpty()) {
                                    Source source = loadCachedObject(uuidStr, Source.class);
                                    modelColumn.getObjectLoadMethod().invoke(dbObject, source);
                                }
                            } else if (loadParameterClass.equals(Object.class)) { // OBJECT
                                modelColumn.getObjectLoadMethod().invoke(dbObject, resultRow.getColumnObject(modelColumn.getColumnName()));
                            }
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Error.DATABASE_OBJECT_METHOD_NOT_FOUND.record().create(ex);
                } catch (IllegalArgumentException ex) {
                    Error.DATABASE_OBJECT_METHOD_NOT_FOUND.record().additionalInformation("Method name: " + modelColumn.getObjectLoadMethod().getName()).create(ex);
                }
            }
        }
    }

    private <DBObject extends DatabaseObject> DBObject loadCachedObject(String uuidStr, Class<DBObject> clazz) {
        DatabaseObjectManager databaseObjectManager = DatabaseObjectManager.getInstance();
        if (uuidStr != null && !uuidStr.isEmpty()) {
            UUID uuid = DAO.UUIDFromString(uuidStr);
            DatabaseObject databaseObject = null;

//            try {
//                databaseObject = databaseObjectManager.getDatabaseObjects().get(uuid.toString(), () -> DatabaseObject.load(uuid, clazz));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }

            if (databaseObjectManager.objectExists(uuid)) {
                databaseObject = databaseObjectManager.getDatabaseObject(uuid);
            } else {
                if (!delayedLoadedObjects.containsKey(uuid.toString())) {
                    delayedLoadedObjects.put(uuid.toString(), new DelayedLoad(uuid.toString(), clazz, null));
                    databaseObject = DatabaseObject.load(uuid, clazz);
                    databaseObjectManager.addObject(databaseObject);
                    delayedLoadedObjects.remove(uuid.toString());
                }
            }

            return (DBObject) databaseObject;
        }
        return null;
    }
}