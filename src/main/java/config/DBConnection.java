package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
    private static HikariDataSource dataSource;
    private static HikariConfig config;

    static {
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("db.properties"))
        {
            Properties properties = new Properties();
            properties.load(inputStream);

            config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername("db.user");
            config.setPassword("db.password");

            dataSource = new HikariDataSource(config);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
