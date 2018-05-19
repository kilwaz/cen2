package data;

import core.builders.resources.ResourceFileMapper;
import error.Error;
import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.internal.sqlscript.FlywaySqlScriptException;
import utils.SDEUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static Logger log = Logger.getLogger(DBConnection.class);
    private String username = "";
    private String password = "";
    private String connectionString = "";
    private Boolean isApplicationConnection = false;
    private Connection connection = null;

    public DBConnection(String connectionString) {
        this.connectionString = connectionString;
    }

    public DBConnection(String connectionString, String username, String password) {
        this.password = password;
        this.username = username;
        this.connectionString = connectionString;
    }

    public DBConnection(String connectionString, String username, String password, Boolean isApplicationConnection) {
        this.password = password;
        this.username = username;
        this.connectionString = connectionString;
        this.isApplicationConnection = isApplicationConnection;
    }

    public Boolean connect() {
        try {
            if (username.isEmpty() && password.isEmpty()) {
                log.info("Connecting to " + connectionString);
            } else {
                log.info("Connecting to " + connectionString + " " + username + "/" + password);
            }

            connection = DriverManager.getConnection(connectionString, username, password);
            log.info("Successfully connected to " + connectionString);
            return true;
        } catch (SQLException ex) {
            Error.OPEN_DATABASE_CONNECTION.record().additionalInformation("Connection String:" + connectionString + " Username:" + username + " Password:" + password).create(ex);
            // Opening the settings window as there is a connection issue to the database
            return false;
        }
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (Exception ex) {
            Error.PREPARE_QUERY.record().create(ex);
        }
        return null;
    }

    public Boolean isConnected() {
        if (connection == null) {
            return false;
        }
        try {
            return !connection.isClosed();
        } catch (SQLException ex) {
            Error.CLOSE_DATABASE_CONNECTION.record().create(ex);
        }
        return false;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Error.CLOSE_DATABASE_CONNECTION.record().create(ex);
        }
    }

    public Boolean isApplicationConnection() {
        return isApplicationConnection;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void rebuildDatabase() {
        try {
            getConnection().setAutoCommit(false);

            File resourcesFile = SDEUtils.getResourceAsFile("sql/", "blankdb.sql");
            String resourcePath = "/" + resourcesFile.getParent() + "/" + resourcesFile.getName();

            log.info("final path " + resourcePath);

            String content = "";
            String currentQuery = "";
            try {
                InputStream stream = ResourceFileMapper.class.getResourceAsStream(resourcePath);

                byte[] encoded = stream.readAllBytes();
                content = new String(encoded, "UTF8");

                String[] sqlQuery = content.split(";");

                for (String query : sqlQuery) {
                    currentQuery = query;
                    getPreparedStatement(query).execute();
                }
            } catch (IOException | SQLException ex) {
                Error.DATABASE_REBUILD_FAILED.record().additionalInformation(currentQuery).create(ex);
            }

            getConnection().commit();
            getConnection().setAutoCommit(true);

            migrateFlyway();
        } catch (SQLException ex) {
            Error.DATABASE_REBUILD_FAILED.record().create(ex);
        }
    }

    public void migrateFlyway() {
        try {
            // Migrate the database
            Flyway flyway = new Flyway();
            log.info("Migrating " + connectionString);
            flyway.setDataSource(connectionString, username, password);

            String sqlMigrationPath = "sql/migration/";

            flyway.setLocations(sqlMigrationPath);

            String[] flywayLocations = flyway.getLocations();
            for (String aLoc : flywayLocations) {
                log.info("Flyway location for sql = " + aLoc);
            }

            flyway.setBaselineOnMigrate(true);
            flyway.migrate();
        } catch (FlywaySqlScriptException ex) {
            Error.DATABASE_MIGRATE_SQL_FAILED.record().create(ex);
        } catch (FlywayException ex) {
            Error.DATABASE_MIGRATE_FAILED.record().create(ex);
        }
    }
}
