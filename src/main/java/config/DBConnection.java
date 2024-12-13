package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
    private static final HikariDataSource dataSource;

    static {
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("db.properties"))
        {
            Properties properties = new Properties();
            properties.load(inputStream);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));

            dataSource = new HikariDataSource(config);
        }
        catch (IOException e) {
            throw new RuntimeException("DBConnection is failed", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
