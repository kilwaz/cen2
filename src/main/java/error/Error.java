package error;

public enum Error {
    // ***** DEFINED ERRORS *****
    SDE_FILE_NOT_FOUND(1, "File not found"),
    CANNOT_RESOLVE_HOST_THIS_MACHINE(2, "Unable to get the named host for the current machine"),
    SQL_BLOB(3, "Error getting SQL Blob input stream"),
    OPEN_DATABASE_CONNECTION(4, "Error opening connection to database"),
    SELECT_QUERY(5, "Error with select query"),
    UPDATE_QUERY(6, "Error with update query"),
    STOP_JOBS(7, "Error stopping all jobs"),
    JOB_MANAGER(8, "Error creating job manager"),
    SCHEDULE_JOB(9, "Error scheduling job"),
    STOP_JOB(148, "Error stopping a jobs"),
    DATABASE_TRANSACTION(97, "Database transaction had problems"),
    PREPARE_QUERY(17, "Error in getting query"),
    CLOSE_DATABASE_CONNECTION(15, "Error closing connection"),
    DATABASE_MIGRATE_SQL_FAILED(99, "Flyway database migration failed from SQL"),
    DATABASE_REBUILD_FAILED(100, "Database rebuild failed"),
    DATABASE_MIGRATE_FAILED(101, "Flyway database migration failed - Database connection might not exist"),
    LOG_OUTPUT(42, "Error decoding log path"),
    RESOURCE_PATH(61, "Error getting resource path"),
    UUID_CREATE_FAILED(91, "UUID could not be converted from string"),
    CREATE_NEW_INSTANCE_ERROR(94, "Unable to create a new instance via the blank constructor"),
    DATABASE_DELETE_CLASS_INIT(90, "Unable to delete object"),
    DATABASE_SAVE_CLASS_INIT(89, "Unable to save object"),
    DATABASE_LOAD_CLASS_INIT(93, "Unable to load object"),
    DATA_LINK_METHOD_NOT_FOUND(87, "While creating the database model we could not find a required method"),
    DATABASE_OBJECT_METHOD_NOT_FOUND(88, "Could not find method for DatabaseObject when trying to update database"),
    FILE_UPLOAD_ERROR(89, "File could not be uploaded"),
    SDE_JOIN_THREAD(58, "Error joining SDE Thread");

    private Integer code;
    private String description;

    Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public RecordedError record() {
        return new RecordedError(code, description, this.name());
    }
}
