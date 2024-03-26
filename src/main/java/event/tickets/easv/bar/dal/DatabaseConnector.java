package event.tickets.easv.bar.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class DatabaseConnector {
    private static final String PROP_FILE = "config/config.settings";
    private SQLServerDataSource dataSource;

    public DatabaseConnector() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream((PROP_FILE)));

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(databaseProperties.getProperty("db.server"));
        dataSource.setDatabaseName(databaseProperties.getProperty("db.database"));
        dataSource.setUser(databaseProperties.getProperty("db.user"));
        dataSource.setPassword(databaseProperties.getProperty("db.password"));
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }
}
