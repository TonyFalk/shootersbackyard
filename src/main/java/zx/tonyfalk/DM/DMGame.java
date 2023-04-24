package zx.tonyfalk.DM;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import zx.tonyfalk.Enums.GameMode;
import zx.tonyfalk.Enums.GameStage;
import zx.tonyfalk.Enums.Team;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Chat;
import zx.tonyfalk.Utils.UMath;
import zx.tonyfalk.common.InventoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DMGame implements Listener {
    ShootersBackYard plugin;

    public DMGame(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }



    public void Gamestatus(Player player) {
        Set<String> maps = this.plugin.getDmConfig().getConfigurationSection("Locations").getKeys(false);
        List<String> list = new ArrayList<>(maps);
        boolean ready = true;
        for (String arena : list) {
            int R = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP");
            int X = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Z");
            if (R == 0 || X == 0 || Y == 0 || Z == 0) {
                ready = false;
                Chat.PlayerSendMessage(player, "DM: " + arena + " is not fully setup please delete or gamemode will not work.");
            } else {
                Chat.PlayerSendMessage(player, "DM: " + arena + " is ready!");
            }
        }
    }

    public void PrestartGame(){
        Set<String> maps = this.plugin.getDmConfig().getConfigurationSection("Locations").getKeys(false);
        List<String> list = new ArrayList<>(maps);
        Collections.shuffle(list);
        String arena = list.get(0);
        this.plugin.gameManager.gameStage = GameStage.Waiting;
        this.plugin.gameManager.runningGame = GameMode.DM;
        this.plugin.gameManager.arena = arena;
        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            this.plugin.gameManager.teams.put(player,Team.None);
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getDmConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
            int X = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
            this.plugin.inventoryManager.setLobbyItems(player);
            //this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.TeamSelector")
        }
    }

    public void startGame(){
        String arena = this.plugin.gameManager.arena;
        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            this.plugin.gameManager.teams.put(player,Team.None);
            Team team = this.plugin.gameManager.teams.get(player);
            this.plugin.inventoryManager.setGear(player,team);
            int max = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP");
            int num = UMath.random(1, max);
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getDmConfig().getString(".Locations.RandomTP." + num + ".W", worldnull.getName()));
            int X = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".X");
            int Y = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".Y");
            int Z = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
        }
    }


    @EventHandler
    public void onRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Team team = this.plugin.gameManager.teams.get(player);
        String arena = this.plugin.gameManager.arena;
        this.plugin.inventoryManager.setGear(player, team);
        switch (team) {
            case None -> {
                int max = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP");
                int num = UMath.random(1, max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getDmConfig().getString(".Locations.RandomTP." + num + ".W", worldnull.getName()));
                int X = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".X");
                int Y = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".Y");
                int Z = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".Z");
                Location location = (new Location(W, X, Y, Z));
                player.teleport(location);
            }
            default -> {
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getDmConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
                int X = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.X");
                int Y = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Y");
                int Z = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Z");
                Location location = (new Location(W, X, Y, Z));
                player.teleport(location);
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getEntity().getKiller() != null) {
            int cash = this.plugin.getDmConfig().getInt("CASH.Kill");
            int xp = this.plugin.getDmConfig().getInt("XP.Kill");
            this.plugin.dataManager.updateCash(event.getEntity().getKiller(),cash);
            this.plugin.dataManager.updateXP(event.getEntity().getKiller(),xp);
            Chat.PlayerSendMessage(event.getEntity().getKiller(),this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String arena = this.plugin.gameManager.arena;
        if(this.plugin.gameManager.gameStage == GameStage.Waiting){
            player.getInventory().clear();
            this.plugin.gameManager.teams.put(player,Team.None);
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getDmConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
            int X = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getDmConfig().getInt("Locations." + arena + ".lobby.Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
            this.plugin.inventoryManager.setLobbyItems(player);
        }
        if(this.plugin.gameManager.gameStage == GameStage.Running){
            player.getInventory().clear();
            this.plugin.gameManager.teams.put(player,Team.None);
            Team team = this.plugin.gameManager.teams.get(player);
            this.plugin.inventoryManager.setGear(player,team);
            int max = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP");
            int num = UMath.random(1, max);
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getDmConfig().getString(".Locations.RandomTP." + num + ".W", worldnull.getName()));
            int X = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".X");
            int Y = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".Y");
            int Z = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTP." + num + ".Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
        }
    }
}
