package zx.tonyfalk.TDM;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TDMGame implements Listener {
    ShootersBackYard plugin;

    public TDMGame(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }


    public void Gamestatus(Player player) {
        Set<String> maps = this.plugin.getTdmConfig().getConfigurationSection("Locations").getKeys(false);
        List<String> list = new ArrayList<>(maps);
        boolean ready = true;
        for (String arena : list) {
            int R = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.redcount");
            int B = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.bluecount");
            int X = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.Z");
            if (R == 0 || B == 0 ||  X == 0 || Y == 0 || Z == 0) {
                ready = false;
                Chat.PlayerSendMessage(player, "Tdm: " + arena + " is not fully setup please delete or gamemode will not work.");
            } else {
                Chat.PlayerSendMessage(player, "Tdm: " + arena + " is ready!");
            }
        }
    }
    public void PrestartGame(){
        Set<String> maps = this.plugin.getTdmConfig().getConfigurationSection("Locations").getKeys(false);
        List<String> list = new ArrayList<>(maps);
        Collections.shuffle(list);
        String arena = list.get(0);
        this.plugin.gameManager.gameStage = GameStage.Waiting;
        this.plugin.gameManager.runningGame = GameMode.TDM;
        this.plugin.gameManager.arena = arena;
        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
            int X = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
            this.plugin.inventoryManager.setLobbyItems(player);
            player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.TeamSelector")));
        }
    }

    public void startGame(){
        String arena = this.plugin.gameManager.arena;
        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            Team team = this.plugin.gameManager.teams.get(player);
            this.plugin.inventoryManager.setGear(player,team);
            switch (team){
                case Red -> {
                    int max = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.redcount");
                    int num = UMath.random(1,max);
                    World worldnull = this.plugin.getServer().getWorlds().get(0);
                    World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Arenas." + arena + ".Locations.RedTP." + num + ".W",worldnull.getName()));
                    int X = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".X");
                    int Y = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Y");
                    int Z = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Z");
                    Location location = (new Location(W,X,Y,Z));
                    player.teleport(location);
                }
                case Blue -> {
                    int max = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.bluecount");
                    int num = UMath.random(1,max);
                    World worldnull = this.plugin.getServer().getWorlds().get(0);
                    World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Arenas." + arena + ".Locations.BlueTP." + num + ".W",worldnull.getName()));
                    int X = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".X");
                    int Y = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Y");
                    int Z = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Z");
                    Location location = (new Location(W,X,Y,Z));
                    player.teleport(location);
                }
                default -> {
                    int redCount = 0;
                    int blueCount = 0;

                    for (Team teaml : this.plugin.gameManager.teams.values()) {
                        if (team.equals(Team.Red)) {
                            redCount++;
                        } else if (team.equals(Team.Blue)) {
                            blueCount++;
                        }
                    }

                    Team toadd = (redCount < blueCount) ? Team.Red : Team.Blue;
                    this.plugin.gameManager.teams.put(player,toadd);
                    startPlayerGame(player);
                }
            }
        }
    }
    public void startPlayerGame(Player player){
        String arena = this.plugin.gameManager.arena;
        player.getInventory().clear();
        Team team = this.plugin.gameManager.teams.get(player);
        this.plugin.inventoryManager.setGear(player,team);
        switch (team){
            case Red -> {
                int max = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.redcount");
                int num = UMath.random(1,max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Arenas." + arena + ".Locations.RedTP." + num + ".W",worldnull.getName()));
                int X = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".X");
                int Y = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Y");
                int Z = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Z");
                Location location = (new Location(W,X,Y,Z));
                player.teleport(location);
            }
            case Blue -> {
                int max = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.bluecount");
                int num = UMath.random(1,max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Arenas." + arena + ".Locations.BlueTP." + num + ".W",worldnull.getName()));
                int X = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".X");
                int Y = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Y");
                int Z = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Z");
                Location location = (new Location(W,X,Y,Z));
                player.teleport(location);
            }
            default -> {
                int redCount = 0;
                int blueCount = 0;

                for (Team teaml : this.plugin.gameManager.teams.values()) {
                    if (team.equals(Team.Red)) {
                        redCount++;
                    } else if (team.equals(Team.Blue)) {
                        blueCount++;
                    }
                }
                Team toadd = (redCount < blueCount) ? Team.Red : Team.Blue;
                this.plugin.gameManager.teams.put(player,toadd);
            }
        }
    }

    @EventHandler
    public void onRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Team team = this.plugin.gameManager.teams.get(player);
        String arena = this.plugin.gameManager.arena;
        this.plugin.inventoryManager.setGear(player, team);
        switch (team) {
            case Red -> {
                int max = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.redcount");
                int num = UMath.random(1, max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Locations.RedTP." + num + ".W", worldnull.getName()));
                int X = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".X");
                int Y = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Y");
                int Z = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Z");
                Location location = (new Location(W, X, Y, Z));
                player.teleport(location);
            }
            case Blue -> {
                int max = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.bluecount");
                int num = UMath.random(1, max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Arenas." + arena + ".Locations.BlueTP." + num + ".W", worldnull.getName()));
                int X = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".X");
                int Y = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Y");
                int Z = this.plugin.getTdmConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Z");
                Location location = (new Location(W, X, Y, Z));
                player.teleport(location);
            }
            default -> {
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Locations.lobby.W", worldnull.getName()));
                int X = this.plugin.getTdmConfig().getInt("Locations.lobby.X");
                int Y = this.plugin.getTdmConfig().getInt("Locations.lobby.Y");
                int Z = this.plugin.getTdmConfig().getInt("Locations.lobby.Z");
                Location location = (new Location(W, X, Y, Z));
                player.teleport(location);
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getEntity().getKiller() != null) {
            int cash = this.plugin.getTdmConfig().getInt("CASH.Kill");
            int xp = this.plugin.getTdmConfig().getInt("XP.Kill");
            this.plugin.dataManager.updateCash(event.getEntity().getKiller(),cash);
            this.plugin.dataManager.updateXP(event.getEntity().getKiller(),xp);
            Chat.PlayerSendMessage(event.getEntity().getKiller(),this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(this.plugin.gameManager.gameStage == GameStage.Waiting){
            String arena = this.plugin.gameManager.arena;
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
            int X = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getTdmConfig().getInt("Locations." + arena + ".lobby.Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
            this.plugin.inventoryManager.setLobbyItems(player);
            player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.TeamSelector")));
        } else {
            startPlayerGame(event.getPlayer());
        }
    }
}
