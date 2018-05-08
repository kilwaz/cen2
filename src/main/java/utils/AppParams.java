package utils;

import error.Error;
import utils.managers.LogManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppParams {
    // Application information
    public final static String APP_TITLE = "CEN";
    public final static String APP_VERSION = "v0.0.1";

    // Current user logged into the application
    public static final String CURRENT_USER = "alex";

    // Database information - Defaults are displayed here and can be overridden by the SDE.xml file
    private static String REMOTE_DATABASE_CONNECTION = "jdbc:mysql://127.0.0.1/cen";
    private static String REMOTE_DATABASE_USERNAME = "kilwaz";
    private static String REMOTE_DATABASE_PASSWORD = "CenDatabase123";

    // Uploaded files settings
    private static String UPLOAD_FILE_TMP_LOCATION = "/home/kilwaz/tmp/";

    // Log settings
    private static Boolean IN_APP_LOG_VIEW = false;
    private static String LOG_DIRECTORY = "";

    // Browser settings
    private static Integer BROWSER_DEFAULT_RETRY_COUNT = 3;

    // Database
    private static Integer DATABASE_DELETE_LIMIT = 100;

    public static String getRemoteDatabaseConnection() {
        return REMOTE_DATABASE_CONNECTION;
    }

    public static void setRemoteDatabaseConnection(String databaseConnection) {
        REMOTE_DATABASE_CONNECTION = databaseConnection;
    }

    public static String getRemoteDatabaseUsername() {
        return REMOTE_DATABASE_USERNAME;
    }

    public static void setRemoteDatabaseUsername(String remoteDatabaseUsername) {
        REMOTE_DATABASE_USERNAME = remoteDatabaseUsername;
    }

    public static String getRemoteDatabasePassword() {
        return REMOTE_DATABASE_PASSWORD;
    }

    public static void setRemoteDatabasePassword(String remoteDatabasePassword) {
        REMOTE_DATABASE_PASSWORD = remoteDatabasePassword;
    }

    public static Boolean getInAppLogView() {
        return IN_APP_LOG_VIEW;
    }

    public static void setInAppLogView(Boolean inAppLogView) {
        IN_APP_LOG_VIEW = inAppLogView;
    }

    public static Integer getDatabaseDeleteLimit() {
        return DATABASE_DELETE_LIMIT;
    }

    public static String getJVMProperty(String key) {
        return System.getProperties().getProperty(key);
    }

    public static String getLogDirectory() {
        return LOG_DIRECTORY;
    }

    public static String getConfiguredLogDirectory() {
        if ("".equals(LOG_DIRECTORY)) {
            LOG_DIRECTORY = LogManager.getInstance().getLogOutputDirectoryCanonicalPath();
        }
        return LOG_DIRECTORY;
    }

    public static void setLogDirectory(String logDirectory) {
        LOG_DIRECTORY = logDirectory;
    }

    public static void setBrowserDefaultRetryCount(Integer browserDefaultRetryCount) {
        BROWSER_DEFAULT_RETRY_COUNT = browserDefaultRetryCount;
    }

    public static Integer getBrowserDefaultRetryCount() {
        return BROWSER_DEFAULT_RETRY_COUNT;
    }

    public static String getMachineName() {
        String hostname = "Unknown Machine";

        try {
            InetAddress address = InetAddress.getLocalHost();
            hostname = address.getHostName();
        } catch (UnknownHostException ex) {
            Error.CANNOT_RESOLVE_HOST_THIS_MACHINE.record().create(ex);
        }

        return hostname;
    }

    public static String getUploadFileTmpLocation() {
        return UPLOAD_FILE_TMP_LOCATION;
    }
}
