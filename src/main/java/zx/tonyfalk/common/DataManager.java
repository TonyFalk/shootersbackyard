package zx.tonyfalk.common;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import zx.tonyfalk.Data.PlayerData;
import zx.tonyfalk.MySQL.SQLManager;
import zx.tonyfalk.ShootersBackYard;

import java.util.HashMap;

public class DataManager implements Listener {
    ShootersBackYard plugin;

    public DataManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, (Plugin) this.plugin);
    }

    public static HashMap<Player, PlayerData> PlayerDataMap = new HashMap<>();

    @EventHandler
    public void opj(PlayerJoinEvent event){
        Player player = event.getPlayer();
        SQLManager db = this.plugin.getSqlManager();
        if(!db.playerExists(player.getName())){
            db.registerPlayer(player);
        }
        PlayerData playerData = new PlayerData(db.getKills(player),db.getDeaths(player),db.getCash(player), db.getXP(player), db.getChoosenLoadout(player), db.getLoadOutSet(player));
        PlayerDataMap.put(player,playerData);
    }
    @EventHandler
    public void opq(PlayerQuitEvent event){
        Player player = event.getPlayer();
        SQLManager db = this.plugin.getSqlManager();
        PlayerData playerData = PlayerDataMap.get(player);
        this.plugin.getSqlManager().updatePlayer(player,playerData.getKills(),playerData.getDeaths(),playerData.getCash(),playerData.getXp(),playerData.getChosenLoadout(),playerData.getLoadoutSet());
        PlayerDataMap.remove(player);
    }

    @EventHandler
    public void opk(PlayerKickEvent event){
        Player player = event.getPlayer();
        SQLManager db = this.plugin.getSqlManager();
        PlayerData playerData = PlayerDataMap.get(player);
        this.plugin.getSqlManager().updatePlayer(player,playerData.getKills(),playerData.getDeaths(),playerData.getCash(),playerData.getXp(),playerData.getChosenLoadout(),playerData.getLoadoutSet());
        PlayerDataMap.remove(player);
    }

    @EventHandler
    public void pdk(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();
        updateMatchDeaths(player,+1);
        updateKillStreak(player,0);
        if (event.getEntity().getKiller() != null) {
            updateMatchKills(killer,+1);
            updateKillStreak(killer,+1);
        }
    }

    public void onMatchEnd(Player player){
        updateKills(player, +PlayerDataMap.get(player).getMatchKills());
        updateDeaths(player, +PlayerDataMap.get(player).getMatchDeaths());
        updateKillStreak(player,0);
        updateMatchDeaths(player,0);
        updateMatchKills(player,0);
    }


    public void updateKills(Player player, int kills) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setKills(kills);
        PlayerDataMap.put(player, playerData);
    }

    public void updateDeaths(Player player, int deaths) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setDeaths(deaths);
        PlayerDataMap.put(player, playerData);
    }

    public void updateCash(Player player, int cash) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setCash(cash);
        PlayerDataMap.put(player, playerData);
    }

    public void updateXP(Player player, int xp) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setXp(xp);
        PlayerDataMap.put(player, playerData);
    }

    public void updateChosenLoadout(Player player, int chosenLoadout) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setChosenLoadout(chosenLoadout);
        PlayerDataMap.put(player, playerData);
    }

    public void updateLoadoutSet(Player player, String loadoutSet) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setLoadoutSet(loadoutSet);
        PlayerDataMap.put(player, playerData);
    }

    public void updateMatchKills(Player player, int matchKills) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setMatchKills(matchKills);
        PlayerDataMap.put(player, playerData);
    }

    public void updateMatchDeaths(Player player, int matchDeaths) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setMatchDeaths(matchDeaths);
        PlayerDataMap.put(player, playerData);
    }

    public void updateKillStreak(Player player, int killStreak) {
        PlayerData playerData = PlayerDataMap.get(player);
        playerData.setKillStreak(killStreak);
        PlayerDataMap.put(player, playerData);
    }
}
