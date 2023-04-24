package zx.tonyfalk.CTF;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.CLog;
import zx.tonyfalk.Utils.Chat;

import java.io.IOException;

public class CTFCMD implements CommandExecutor {
    ShootersBackYard plugin;

    public CTFCMD(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("ctf").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("op")) {
                if (args.length < 1) {
                    Chat.PlayerSendMessage(player, ("&dUsage: /ctf <setred/setblue/setblueflag/setredflag/setlobby>"));
                    return false;
                }
                if (args[0].equalsIgnoreCase("setred")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        int redcount = this.plugin.getCtfConfig().getInt("Arenas."+ arena +".Locations.redcount");
                        int bluecount = this.plugin.getCtfConfig().getInt("Arenas."+ arena +".Locations.bluecount");
                        int nextcount = redcount + 1;
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.RedTP." + nextcount + ".X", player.getLocation().getBlockX());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.RedTP." + nextcount + ".Y", player.getLocation().getBlockY());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.RedTP." + nextcount + ".Z", player.getLocation().getBlockZ());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.RedTP." + nextcount + ".W", player.getLocation().getWorld().getName());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.RedTPCounter", redcount + 1);
                        try {
                            this.plugin.getCtfConfig().save(this.plugin.getCtfFile());
                            Chat.PlayerSendMessage(player, "&d[ctf]the red spawn point was saved! arena: " + arena);
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the point");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /ctf setred <arena>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setblue")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        int redcount = this.plugin.getCtfConfig().getInt("Arenas."+ arena +".Locations.redcount");
                        int bluecount = this.plugin.getCtfConfig().getInt("Arenas."+ arena +".Locations.bluecount");
                        int nextcount = bluecount + 1;
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.BlueTP." + nextcount + ".X", player.getLocation().getBlockX());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.BlueTP." + nextcount + ".Y", player.getLocation().getBlockY());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.BlueTP." + nextcount + ".Z", player.getLocation().getBlockZ());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.BlueTP." + nextcount + ".W", player.getLocation().getWorld().getName());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.BlueTPCounter", bluecount + 1);
                        try {
                            this.plugin.getCtfConfig().save(this.plugin.getCtfFile());
                            Chat.PlayerSendMessage(player, "&d[ctf]the Blue spawn point was saved!");
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the point");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /ctf setblue <arena>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setlobby")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.lobby.X", player.getLocation().getBlockX());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.lobby.Y", player.getLocation().getBlockY());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.lobby.Z", player.getLocation().getBlockZ());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.lobby.W", player.getLocation().getWorld().getName());
                        try {
                            this.plugin.getCtfConfig().save(this.plugin.getCtfFile());
                            Chat.PlayerSendMessage(player, "&d[ctf]the lobby was saved!");
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the spawn");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /ctf setlobby <arena>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setredflag")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Redflag.X", player.getLocation().getBlockX());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Redflag.Y", player.getLocation().getBlockY());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Redflag.Z", player.getLocation().getBlockZ());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Redflag.W", player.getLocation().getWorld().getName());
                        try {
                            this.plugin.getCtfConfig().save(this.plugin.getCtfFile());
                            Chat.PlayerSendMessage(player, "&d[ctf]the Redflag was saved!");
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the spawn");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /ctf setredflag <arena>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setblueflag")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Blueflag.X", player.getLocation().getBlockX());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Blueflag.Y", player.getLocation().getBlockY());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Blueflag.Z", player.getLocation().getBlockZ());
                        this.plugin.getCtfConfig().set("Arenas." + arena + ".Locations.Blueflag.W", player.getLocation().getWorld().getName());
                        try {
                            this.plugin.getCtfConfig().save(this.plugin.getCtfFile());
                            Chat.PlayerSendMessage(player, "&d[ctf]the Blueflag was saved!");
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the spawn");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /ctf setblueflag <arena>");
                        return false;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
        return false;
    }
}
