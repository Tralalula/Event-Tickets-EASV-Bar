package event.tickets.easv.bar.dal.database.common;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import event.tickets.easv.bar.util.AppConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    private final SQLServerDataSource dataSource;

    public DBConnector() throws IOException {
        var properties = new Properties();
        properties.load(new FileInputStream(AppConfig.CONFIG_FILE));

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(properties.getProperty(AppConfig.DB_SERVER));
        dataSource.setDatabaseName(properties.getProperty(AppConfig.DB_DATABASE));

        if (Boolean.parseBoolean(properties.getProperty(AppConfig.DB_USE_INTEGRATED_SECURITY))) {
            dataSource.setIntegratedSecurity(true);
        } else {
            dataSource.setUser(properties.getProperty(AppConfig.DB_USERNAME));
            dataSource.setPassword(properties.getProperty(AppConfig.DB_PASSWORD));
        }

        dataSource.setPortNumber(Integer.parseInt(properties.getProperty(AppConfig.DB_PORT)));
        dataSource.setTrustServerCertificate(true);
    }

    public Connection connection() throws SQLServerException {
        return dataSource.getConnection();
    }

    // For testing connection
    public static void main(String[] args) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.connection()) {
            System.out.println("Is it open? " + !conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
