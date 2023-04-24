package zx.tonyfalk.MySQL;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import zx.tonyfalk.ShootersBackYard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;
    private ShootersBackYard plugin;

    public ConnectionPoolManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        config.setJdbcUrl(this.plugin.getDatabaseConfig().getString("DATABASE.JdbcUrl"));
        config.setUsername( this.plugin.getDatabaseConfig().getString("DATABASE.Username"));
        config.setPassword( this.plugin.getDatabaseConfig().getString("DATABASE.Password"));
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        dataSource = new HikariDataSource( config );
    }



    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}

/**
public class ConnectionPoolManager {
    private HikariDataSource dataSource;
    private String type;
    private String address;
    private String port;
    private String database;
    private String username;
    private String password;
    private int maximumConnections;
    private long connectionTimeout;
    private ShootersBackYard plugin;
    private int attemptstoconnect = 0;

    public ConnectionPoolManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        try {
            if(password.equals("123456")){
                CLog.ConsoleWarningMessage("Database is not setup");
                CLog.ConsoleWarningMessage("Please setup database , otherwise server will restart each time till database is setup");
                return;
            }
            init();
            setupPool();
        } catch (Throwable throwable) {
            System.out.println("ADDRESS" + address + "ADDRESS");
            System.out.println("PORT" + port + "PORT");
            System.out.println("DATABASE" + database + "DATABASE");
            System.out.println("USERNAME" + username + "USERNAME");
            System.out.println("PASSWORD" + password + "PASSWORD");
            CLog.ConsoleErrorMessage("Failed to connect database!");
            CLog.ConsoleErrorMessage("This will lead to more errors!");
            CLog.ConsoleErrorMessage("It will print a stacktrace to explain the problem:");
            Bukkit.shutdown();
        }
    }

    private void init() {
        address = "212.192.29.151";//plugin.getDatabaseConfig().getString("DATABASE.address");
        port = "3306";//plugin.getDatabaseConfig().getString("DATABASE.port");
        database = "s74066_testserver";//plugin.getDatabaseConfig().getString("DATABASE.database");
        username = "u74066_nq772DS29v";//plugin.getDatabaseConfig().getString("DATABASE.username");
        password = "HWd.+unw.Vf^psfw2mIlD9J^";//plugin.getDatabaseConfig().getString("DATABASE.password");
        maximumConnections = plugin.getDatabaseConfig().getInt("DATABASE.maximumConnections",20);
        connectionTimeout = plugin.getDatabaseConfig().getLong("DATABASE.connectionTimeout",0);
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("DBCP");
        config.setJdbcUrl("jdbc:mysql://" + "212.192.29.151" + ":" + "3306" + "/" + "s74066_testserver" + "?useSSL=false");
        config.setUsername("u74066_nq772DS29v");
        config.setPassword("HWd.+unw.Vf^psfw2mIlD9J^");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(maximumConnections);
        if (connectionTimeout > 0) config.setConnectionTimeout(connectionTimeout);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SqlConnectionException,SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException sqlException){
            if(attemptstoconnect == 0) {
                CLog.ConsoleErrorMessage("Not able to connect database");
                CLog.ConsoleChangeMessage("In attempt to solve the problem it will try to connect the database more few times");
            }
            if (attemptstoconnect < 6) {
                CLog.ConsoleErrorMessage("Failed to connect address<" + address + "> port<" + port + "> database<" + database + "> username<" + username + "> password<" + password + ">");
                CLog.ConsoleChangeMessage("Trying to connect again attempt #" + attemptstoconnect);
                getConnection();
            } else {
                CLog.ConsoleErrorMessage("Critical Error caused due failure to put peers with the database");
                CLog.ConsoleErrorMessage("solutions check the database if its running and open check wether you able to connect via other server in that scenario open 443/3306port ");
                Bukkit.shutdown();
            }
        }
        throw new SqlConnectionException("Failed to connect database");
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    public class SqlConnectionException extends NullPointerException {

        public SqlConnectionException(String message) {
            super(message);
        }
    }
}
 **/
