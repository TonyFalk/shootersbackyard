package zx.tonyfalk.common;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import zx.tonyfalk.Enums.GameMode;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Chat;
import zx.tonyfalk.Utils.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SCTManager implements Listener {
    public static HashMap<Player, Leaderboard> leaderboards = new HashMap<>();
    ShootersBackYard plugin;

    public SCTManager(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    public void StartLeaderboard(Player player) {
        GameMode gameMode = this.plugin.gameManager.runningGame;
        ArrayList<String> lines = new ArrayList<>();
        String displayName = null;
        switch (gameMode) {
            case DM -> {
                displayName = Color.set(this.plugin.getDmConfig().getString("ScoreBoard.Title"));
                lines = (ArrayList<String>) Color.setlistwithplaceholders(player,this.plugin.getDmConfig().getStringList("ScoreBoard.lines"));
            }
            case CTF -> {
                displayName = Color.set(this.plugin.getCtfConfig().getString("ScoreBoard.Title"));
                lines = (ArrayList<String>) Color.setlistwithplaceholders(player,this.plugin.getCtfConfig().getStringList("ScoreBoard.lines"));
            }
            case TDM -> {
                displayName = Color.set(this.plugin.getTdmConfig().getString("ScoreBoard.Title"));
                lines = (ArrayList<String>) Color.setlistwithplaceholders(player,this.plugin.getTdmConfig().getStringList("ScoreBoard.lines"));
            }
            case Apocalypse -> {
                displayName = Color.set(this.plugin.getTdmConfig().getString("ScoreBoard.Title"));
                lines = (ArrayList<String>) Color.setlistwithplaceholders(player,this.plugin.getDmConfig().getStringList("ScoreBoard.lines"));
            }
            case hub -> {
                displayName = Color.set(this.plugin.getHubConfig().getString("ScoreBoard.Title"));
                lines = (ArrayList<String>) Color.setlistwithplaceholders(player,this.plugin.getHubConfig().getStringList("ScoreBoard.lines"));
            }
            default -> {
                displayName = Color.set(this.plugin.getHubConfig().getString("ScoreBoard.Title"));
                lines = (ArrayList<String>) Color.setlistwithplaceholders(player,this.plugin.getHubConfig().getStringList("ScoreBoard.lines"));
            }
        }
        Leaderboard leaderboard = new Leaderboard(displayName, new ArrayList<>(), player);
        leaderboards.put(player, leaderboard);
        ArrayList<String> finalLines = lines;
        (new BukkitRunnable() {
            public void run() {
                leaderboard.UpdateLeaderboard(finalLines);
            }
        }).runTaskTimer((Plugin) this, 20L, 20L);
    }


    public void UpdateAllPlayerNames() {
        for (Player player : Bukkit.getOnlinePlayers())
            player.setPlayerListName(this.plugin.leaderboard.PlayerTeamsString(player) + player.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StartLeaderboard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (leaderboards.get(player) != null)
            leaderboards.remove(player);
    }
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        Player player = event.getPlayer();
        if (leaderboards.get(player) != null)
            leaderboards.remove(player);
    }
}
