import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener("Creates a connection pool that is stored in the Servlet's context for later use.")
public class DBCPDataSource {

  // NEVER store sensitive information below in plain text!

  private static final String HOST_NAME = System.getenv("HOST_NAME");
  private static final String USERNAME = System.getenv("USERNAME");
  private static final String PASSWORD = System.getenv("PASSWORD");
  private static final String DATABASE = "CS6650";
  private static HikariConfig config = new HikariConfig();
  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    // The configuration object specifies behaviors for the connection pool.


// Configure which instance and what database user to connect with.
    config.setJdbcUrl(String.format("jdbc:mysql:///%s",DATABASE));
    config.setUsername(USERNAME); // e.g. "root", "postgres"
    config.setPassword(PASSWORD); // e.g. "my-password"

// For Java users, the Cloud SQL JDBC Socket Factory can provide authenticated connections.
// See https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory for details.
    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
    config.addDataSourceProperty("cloudSqlInstance", HOST_NAME);
    config.addDataSourceProperty("useSSL", "false");
    config.setMaximumPoolSize(100);
    config.setMinimumIdle(10);
    config.setConnectionTimeout(10000);
    config.setIdleTimeout(600000); // 10 minutes
    config.setMaxLifetime(1800000); //
  }
  private static DataSource dataSource = new HikariDataSource(config);

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
