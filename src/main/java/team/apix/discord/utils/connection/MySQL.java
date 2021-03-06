package team.apix.discord.utils.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.yaml.snakeyaml.Yaml;
import team.apix.discord.utils.vars.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * SourceBot (2017) was created by ApixTeam (C) 2016-2018
 * in association with TheSourceCode (C) 2016-2018
 */
public class MySQL {
    private static HikariDataSource ds;
    private static HikariConfig config = new HikariConfig();

    static {
        Yaml yaml = new Yaml();
        try {
            Map<String, Map<String, String>> values = yaml.load(new FileInputStream(new File(System.getProperty("user.dir") + "/config.yml")));

            boolean b = false;
            String host = "localhost", database = "discord", username = "root", password = "", port = "3306", ssl = "false";

            for (String key : values.keySet()) {
                Map<String, String> subValues = values.get(key);

                for (String subValueKey : subValues.keySet()) {
                    if (subValueKey.equalsIgnoreCase("host"))
                        host = subValues.get(subValueKey);
                    else if (subValueKey.equalsIgnoreCase("port"))
                        port = subValues.get(subValueKey);
                    else if (subValueKey.equalsIgnoreCase("database"))
                        database = subValues.get(subValueKey);
                    else if (subValueKey.equalsIgnoreCase("username"))
                        username = subValues.get(subValueKey);
                    else if (subValueKey.equalsIgnoreCase("password"))
                        password = subValues.get(subValueKey);
                    else if (subValueKey.equalsIgnoreCase("use-ssl"))
                        ssl = subValues.get(subValueKey);
                }
                b = true;
            }

            if (b) {
                config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useSSL=%s", host, port, database, ssl));
                config.setUsername(username);
                config.setPassword(password);
            }
            Lists.setTestingEnvironment(false);
        } catch (Exception e) {
            System.out.println("[ERROR] An error occurred while trying to retrieve database credentials, forcing dev-local database!");
            config.setJdbcUrl("jdbc:mysql://localhost:3306/discord");
            config.setUsername("root");
            config.setPassword("");
            Lists.setTestingEnvironment(true);
        }

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    private MySQL() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            ds = new HikariDataSource(config);
            return ds.getConnection();
        }
    }
}
