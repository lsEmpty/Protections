package protections.DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BridgeConnection {
    private HikariDataSource dataSource;
    private String url;
    private String user;
    private String password;


    public BridgeConnection(String host, String port, String database_name, String optional_parameters, String user, String password) {
        this.url = optional_parameters.isEmpty() ?
                "jdbc:mysql://<host>:<port>/<database_name>" :
                "jdbc:mysql://<host>:<port>/<database_name>?<optional_parameters>";

        this.url = this.url
                .replace("<host>", host)
                .replace("<port>", port)
                .replace("<database_name>", database_name)
                .replace("<optional_parameters>", optional_parameters);
        this.user = user;
        this.password = password;

        openConnection();
    }

    public void openConnection(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        // Pool
        config.setMaximumPoolSize(10); // Maximum pool connection
        config.setMinimumIdle(2); // Minimum pool inactive connection (ready to use)
        config.setIdleTimeout(30000); // Time in milliseconds to close inactive connections
        config.setConnectionTimeout(30000); // Maximum time to wait an available connection
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource = new HikariDataSource();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
