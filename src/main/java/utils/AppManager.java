package utils;

import data.DBConnectionManager;

public class AppManager {
    private static DBConnectionManager dbConnectionManager;

    public static void init() {
        dbConnectionManager = DBConnectionManager.getInstance();
        dbConnectionManager.createApplicationConnection();
    }
}
