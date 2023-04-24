package zx.tonyfalk.CTF;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import zx.tonyfalk.Enums.GameMode;
import zx.tonyfalk.Enums.GameStage;
import zx.tonyfalk.Enums.Team;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Chat;
import zx.tonyfalk.Utils.UMath;
import zx.tonyfalk.common.GameManager;

import java.util.*;

public class CTFGame implements Listener {
    ShootersBackYard plugin;

    public CTFGame(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, (Plugin) this.plugin);
    }

    boolean redflagcaptured = false;
    boolean blueflagcaputred = false;


    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    public void Gamestatus(Player player) {
        Set<String> maps = this.plugin.getCtfConfig().getConfigurationSection("Locations").getKeys(false);
        List<String> list = new ArrayList<>(maps);
        boolean ready = true;
        for (String arena : list) {
            int R = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.redcount");
            int B = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.bluecount");
            int BX = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Blueflag.X");
            int BY = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Blueflag.Y");
            int BZ = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Blueflag.Z");
            int RX = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Redflag.X");
            int RY = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Redflag.Y");
            int RZ = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Redflag.Z");
            int X = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.Z");
            if (R == 0 || B == 0 || BX == 0 || BY == 0 || BZ == 0 || RX == 0 || RY == 0 || RZ == 0 || X == 0 || Y == 0 || Z == 0) {
                ready = false;
                Chat.PlayerSendMessage(player, "CTF: " + arena + " is not fully setup please delete or gamemode will not work.");
            } else {
                Chat.PlayerSendMessage(player, "CTF: " + arena + " is ready!");
            }
        }
    }


    public void PrestartGame(){
        Set<String> maps = this.plugin.getCtfConfig().getConfigurationSection("Locations").getKeys(false);
        List<String> list = new ArrayList<>(maps);
        Collections.shuffle(list);
        String arena = list.get(0);
        this.plugin.gameManager.gameStage = GameStage.Waiting;
        this.plugin.gameManager.runningGame = GameMode.CTF;
        this.plugin.gameManager.arena = arena;
        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
            int X = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
            this.plugin.inventoryManager.setLobbyItems(player);
            player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.TeamSelector")));
        }
    }

    public void startGame(){
        String arena = this.plugin.gameManager.arena;
        spawnBlueFlag(arena);
        spawnRedFlag(arena);
        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            Team team = this.plugin.gameManager.teams.get(player);
            this.plugin.inventoryManager.setGear(player,team);
            switch (team){
                case Red -> {
                    int max = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.redcount");
                    int num = UMath.random(1,max);
                    World worldnull = this.plugin.getServer().getWorlds().get(0);
                    World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.RedTP." + num + ".W",worldnull.getName()));
                    int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".X");
                    int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Y");
                    int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Z");
                    Location location = (new Location(W,X,Y,Z));
                    player.teleport(location);
                }
                case Blue -> {
                    int max = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.bluecount");
                    int num = UMath.random(1,max);
                    World worldnull = this.plugin.getServer().getWorlds().get(0);
                    World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.BlueTP." + num + ".W",worldnull.getName()));
                    int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".X");
                    int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Y");
                    int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Z");
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
                int max = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.redcount");
                int num = UMath.random(1,max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.RedTP." + num + ".W",worldnull.getName()));
                int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".X");
                int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Y");
                int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Z");
                Location location = (new Location(W,X,Y,Z));
                player.teleport(location);
            }
            case Blue -> {
                int max = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.bluecount");
                int num = UMath.random(1,max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.BlueTP." + num + ".W",worldnull.getName()));
                int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".X");
                int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Y");
                int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Z");
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
    public void spawnRedFlag(String arena){
        ItemStack redflag = this.plugin.getCtfConfig().getItemStack("CTF.redflag");
        World worldnull = this.plugin.getServer().getWorlds().get(0);
        World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.Redflag.W",worldnull.getName()));
        int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Redflag.X");
        int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Redflag.Y") + 1;
        int Z =  this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Redflag.Z");
        Location location = (new Location(W,X,Y,Z));
        location.getWorld().dropItemNaturally(location,redflag);
    }
    public void spawnBlueFlag(String arena){
        ItemStack blueflag = this.plugin.getCtfConfig().getItemStack("CTF.blueflag");
        World worldnull = this.plugin.getServer().getWorlds().get(0);
        World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.Blueflag.W",worldnull.getName()));
        int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Blueflag.X");
        int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Blueflag.Y") + 1;
        int Z =  this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.Blueflag.Z");
        Location location = (new Location(W,X,Y,Z));
        location.getWorld().dropItemNaturally(location,blueflag);
    }

    @EventHandler
    public void onPickUpFlag(PlayerPickupItemEvent event){
        event.setCancelled(true);
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem().getItemStack();
        ItemStack blueflag = this.plugin.getCtfConfig().getItemStack("CTF.blueflag");
        ItemStack redflag = this.plugin.getCtfConfig().getItemStack("CTF.redflag");
        if (itemStack.equals(redflag) && this.plugin.gameManager.teams.get(player) == Team.Red && redflagcaptured){
            redflagcaptured = false;
            Chat.BoardcastMessage(PlaceholderAPI.setPlaceholders(player,this.plugin.getCtfConfig().getString("Msgs.OnRedFlagReturn").replace("%Player%",player.getName())));
            int cash = this.plugin.getCtfConfig().getInt("CASH.OnRedFlagReturn");
            int xp = this.plugin.getCtfConfig().getInt("XP.OnRedFlagReturn");
            this.plugin.dataManager.updateCash(player,cash);
            this.plugin.dataManager.updateXP(player,xp);
            Chat.PlayerSendMessage(player,this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
            spawnRedFlag(this.plugin.gameManager.arena);
            event.getItem().remove();
            return;
        }
        if (itemStack.equals(blueflag) && this.plugin.gameManager.teams.get(player) == Team.Blue && blueflagcaputred){
            blueflagcaputred = false;
            Chat.BoardcastMessage(PlaceholderAPI.setPlaceholders(player,this.plugin.getCtfConfig().getString("Msgs.OnBlueFlagReturn").replace("%Player%",player.getName())));
            spawnBlueFlag(this.plugin.gameManager.arena);
            int cash = this.plugin.getCtfConfig().getInt("CASH.OnBlueFlagReturn");
            int xp = this.plugin.getCtfConfig().getInt("XP.OnBlueFlagReturn");
            this.plugin.dataManager.updateCash(player,cash);
            this.plugin.dataManager.updateXP(player,xp);
            Chat.PlayerSendMessage(player,this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
            event.getItem().remove();
            return;
        }
        if (itemStack.equals(blueflag) && this.plugin.gameManager.teams.get(player) == Team.Red && !blueflagcaputred){
            int cash = this.plugin.getCtfConfig().getInt("CASH.OnBlueFlagSteal");
            int xp = this.plugin.getCtfConfig().getInt("XP.OnBlueFlagSteal");
            this.plugin.dataManager.updateCash(player,cash);
            this.plugin.dataManager.updateXP(player,xp);
            Chat.PlayerSendMessage(player,this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
            blueflagcaputred = true;
            Chat.BoardcastMessage(PlaceholderAPI.setPlaceholders(player,this.plugin.getCtfConfig().getString("Msgs.OnBlueFlagSteal").replace("%Player%",player.getName())));
            player.setGlowing(true);
            event.getItem().remove();
        }
        if (itemStack.equals(redflag) && this.plugin.gameManager.teams.get(player) == Team.Blue && !redflagcaptured){
            Chat.BoardcastMessage(PlaceholderAPI.setPlaceholders(player,this.plugin.getCtfConfig().getString("Msgs.OnRedFlagSteal").replace("%Player%",player.getName())));
            redflagcaptured = true;
            player.setGlowing(true);
            int cash = this.plugin.getCtfConfig().getInt("CASH.OnRedFlagSteal");
            int xp = this.plugin.getCtfConfig().getInt("XP.OnBlueFlagSteal");
            this.plugin.dataManager.updateCash(player,cash);
            this.plugin.dataManager.updateXP(player,xp);
            Chat.PlayerSendMessage(player,this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
            event.getItem().remove();
        }
        if (itemStack.equals(redflag) && this.plugin.gameManager.teams.get(player) == Team.Red && !redflagcaptured){
            if (player.isGlowing()) {
                Chat.BoardcastMessage(PlaceholderAPI.setPlaceholders(player, this.plugin.getCtfConfig().getString("Msgs.OnBlueFlagCapture").replace("%Player%", player.getName())));
                spawnBlueFlag(this.plugin.gameManager.arena);
                player.setGlowing(false);
                return;
            }
        }
        if (itemStack.equals(blueflag) && this.plugin.gameManager.teams.get(player) == Team.Blue && !blueflagcaputred){
            if (player.isGlowing()) {
                Chat.BoardcastMessage(PlaceholderAPI.setPlaceholders(player, this.plugin.getCtfConfig().getString("Msgs.OnRedFlagCapture").replace("%Player%", player.getName())));
                spawnRedFlag(this.plugin.gameManager.arena);
                player.setGlowing(false);
                int cash = this.plugin.getCtfConfig().getInt("CASH.OnRedFlagCapture");
                int xp = this.plugin.getCtfConfig().getInt("XP.OnRedFlagCapture");
                this.plugin.dataManager.updateCash(player,cash);
                this.plugin.dataManager.updateXP(player,xp);
                Chat.PlayerSendMessage(player,this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
                return;
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        ItemStack blueflag = this.plugin.getCtfConfig().getItemStack("CTF.blueflag");
        ItemStack redflag = this.plugin.getCtfConfig().getItemStack("CTF.redflag");
        if (player.isGlowing()){
            player.setGlowing(false);
            switch (this.plugin.gameManager.teams.get(player)){
                case Red:
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(),blueflag);
                case Blue:
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(),redflag);
                default:
                    return;
            }
        }
        if (event.getEntity().getKiller() != null) {
            int cash = this.plugin.getCtfConfig().getInt("CASH.Kill");
            int xp = this.plugin.getCtfConfig().getInt("XP.Kill");
            this.plugin.dataManager.updateCash(event.getEntity().getKiller(),cash);
            this.plugin.dataManager.updateXP(event.getEntity().getKiller(),xp);
            Chat.PlayerSendMessage(event.getEntity().getKiller(),this.plugin.getMsgsConfig().getString("gamexpcashearn").replace("%CASH%",String.valueOf(cash)).replace("%XP%",String.valueOf(xp)));
        }
    }
    @EventHandler
    public void onRespawnEvent(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        Team team = this.plugin.gameManager.teams.get(player);
        String arena = this.plugin.gameManager.arena;
        this.plugin.inventoryManager.setGear(player,team);
        switch (team){
            case Red -> {
                int max = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.redcount");
                int num = UMath.random(1,max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.RedTP." + num + ".W",worldnull.getName()));
                int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".X");
                int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Y");
                int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.RedTP." + num + ".Z");
                Location location = (new Location(W,X,Y,Z));
                player.teleport(location);
            }
            case Blue -> {
                int max = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.bluecount");
                int num = UMath.random(1,max);
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.BlueTP." + num + ".W",worldnull.getName()));
                int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".X");
                int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Y");
                int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.BlueTP." + num + ".Z");
                Location location = (new Location(W,X,Y,Z));
                player.teleport(location);
            }
            default -> {
                World worldnull = this.plugin.getServer().getWorlds().get(0);
                World W = Bukkit.getServer().getWorld(this.plugin.getCtfConfig().getString("Arenas." + arena + ".Locations.lobby.W",worldnull.getName()));
                int X = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.lobby.X");
                int Y = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.lobby.Y");
                int Z = this.plugin.getCtfConfig().getInt("Arenas." + arena + ".Locations.lobby.Z");
                Location location = (new Location(W,X,Y,Z));
                player.teleport(location);
            }
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(this.plugin.gameManager.gameStage == GameStage.Waiting){
            String arena = this.plugin.gameManager.arena;
            World worldnull = this.plugin.getServer().getWorlds().get(0);
            World W = Bukkit.getServer().getWorld(this.plugin.getTdmConfig().getString("Locations." + arena + ".lobby.W", worldnull.getName()));
            int X = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.X");
            int Y = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.Y");
            int Z = this.plugin.getCtfConfig().getInt("Locations." + arena + ".lobby.Z");
            Location location = (new Location(W, X, Y, Z));
            player.teleport(location);
            this.plugin.inventoryManager.setLobbyItems(player);
            player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.TeamSelector")));
        } else {
            startPlayerGame(event.getPlayer());
        }
    }
}
