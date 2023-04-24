package zx.tonyfalk.MySQL;

import org.bukkit.entity.Player;
import zx.tonyfalk.Data.LoadoutSet;
import zx.tonyfalk.ShootersBackYard;


import java.nio.ByteBuffer;
import java.sql.*;
import java.util.UUID;

public class SQLManager {

    private ShootersBackYard plugin;
    private ConnectionPoolManager pool;

    public SQLManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        pool = plugin.getConnectionPoolManager();
    }

    public void makeTable() throws SQLException {
        try (Connection conn = pool.getConnection(); Statement statement = conn.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players "
                    + "(PLAYERNAME VARCHAR(255), KILLS INTEGER , DEATHS INTEGER , CASH INTEGER , XP INTEGER ,CHOOSENLOADOUT INTEGER , LOADOUTSET VARCHAR(255) , PRIMARY KEY (PLAYERNAME))");
        }
    }

    public void registerPlayer(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO players (PLAYERNAME,KILLS,DEATHS,CASH,XP,CHOOSENLOADOUT,LOADOUTSET) VALUES (?,?,?,?,?,?,?)")) {
            ps.setString(1, p.getName());
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setString(7, new LoadoutSet().toString());
            ps.executeUpdate();
        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(Player player,int kills,int deaths,int cash,int xp,int choosenLoadout,String loadoutset) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET KILLS=?, DEATHS=?, CASH=?, XP=?, CHOOSENLOADOUT=?, LOADOUTSET=?  WHERE PLAYERNAME=?")) {
            ps.setInt(1, kills);
            ps.setInt(2, deaths);
            ps.setInt(3, cash);
            ps.setInt(4, xp);
            ps.setInt(5, choosenLoadout);
            ps.setString(6, loadoutset);
            ps.setString(7, player.getName());
            ps.executeUpdate();
        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(String playerName) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT PLAYERNAME FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, playerName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getLoadOutSet(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT LOADOUTSET FROM players WHERE (PLAYERNAME) = (?)")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("LOADOUTSET");
            }
        } catch (SQLException  e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateKills(Player p, int kills) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET KILLS = ? WHERE PLAYERNAME = ?")) {
            ps.setInt(1, kills);
            ps.setString(2, p.getName());
            ps.executeUpdate();
        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    // Get KILLS for a player
    public int getKills(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT KILLS FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("KILLS");
            }
        } catch (SQLException  e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Update DEATHS for a player
    public void updateDeaths(Player p, int deaths) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET DEATHS = ? WHERE PLAYERNAME = ?")) {
            ps.setInt(1, deaths);
            ps.setString(2, p.getName());
            ps.executeUpdate();
                } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    // Get DEATHS for a player
    public int getDeaths(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT DEATHS FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("DEATHS");
            }
                } catch (SQLException  e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Update CASH for a player
    public void updateCash(Player p, int cash) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET CASH = ? WHERE PLAYERNAME = ?")) {
            ps.setInt(1, cash);
            ps.setString(2, p.getName());
            ps.executeUpdate();
                } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    // Get CASH for a player
    public int getCash(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT CASH FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("CASH");
            }
                } catch (SQLException  e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get XP for a player
    public int getXP(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT XP FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("XP");
            }
                } catch (SQLException  e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Set XP for a player
    public void setXP(Player p, int xp) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET XP = ? WHERE PLAYERNAME = ?")) {
            ps.setInt(1, xp);
            ps.setString(2, p.getName());
            ps.executeUpdate();
                } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    // Get the chosen loadout for a player
    public int getChoosenLoadout(Player p) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT CHOOSENLOADOUT FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("CHOOSENLOADOUT");
            }
                } catch (SQLException  e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Set the chosen loadout for a player
    public void setChoosenLoadout(Player p, int chosenLoadout) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET CHOOSENLOADOUT = ? WHERE PLAYERNAME = ?")) {
            ps.setInt(1, chosenLoadout);
            ps.setString(2, p.getName());
            ps.executeUpdate();
                } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    // Get the loadout set for a player
    public LoadoutSet getLoadoutSet(Player p) {
        LoadoutSet loadoutSet = new LoadoutSet();
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT LOADOUTSET FROM players WHERE PLAYERNAME = ?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String loadoutSetString = rs.getString("LOADOUTSET");
                loadoutSet.fromString(loadoutSetString);
            }
                } catch (SQLException  e) {
            e.printStackTrace();
        }
        return loadoutSet;
    }

    // Set the loadout set for a player
    public void setLoadoutSet(Player p, LoadoutSet loadoutSet) {
        try (Connection conn = plugin.getConnectionPoolManager().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE players SET LOADOUTSET = ? WHERE PLAYERNAME = ?")) {
            ps.setString(1, loadoutSet.toString());
            ps.setString(2, p.getName());
            ps.executeUpdate();
                } catch (SQLException  e) {
            e.printStackTrace();
        }
    }


    private byte[] covertUUID(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits());
        return bytes;
    }

    private UUID convertBinary(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(bytes);
        buffer.flip();
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public void onDisable() {
        pool.closePool();
    }
}
