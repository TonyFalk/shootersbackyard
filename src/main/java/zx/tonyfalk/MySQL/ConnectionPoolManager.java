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
