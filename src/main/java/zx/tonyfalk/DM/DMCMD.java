package zx.tonyfalk.DM;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.CLog;
import zx.tonyfalk.Utils.Chat;

import java.io.IOException;

public class DMCMD implements CommandExecutor {
    ShootersBackYard plugin;

    public DMCMD(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("ctf").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("op")) {
                if (args.length < 1) {
                    Chat.PlayerSendMessage(player, ("&dUsage: /dm <setrandom/setlobby>"));
                    return false;
                }
                if (args[0].equalsIgnoreCase("setrandom")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        int count = this.plugin.getDmConfig().getInt("Arenas." + arena + ".Locations.RandomTPCounter");
                        int nextcount = count + 1;
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.RandomTP." + nextcount + ".X", player.getLocation().getBlockX());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.RandomTP." + nextcount + ".Y", player.getLocation().getBlockY());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.RandomTP." + nextcount + ".Z", player.getLocation().getBlockZ());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.RandomTP." + nextcount + ".W", player.getLocation().getWorld().getName());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.RandomTPCounter", count + 1);
                        try {
                            this.plugin.getDmConfig().save(this.plugin.getDmFile());
                            Chat.PlayerSendMessage(player, "&d[Dm]the red spawn point was saved!");
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the point");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /dm setrandom <arena>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setlobby")) {
                    String arena = args[1].toUpperCase();
                    if (!arena.equals("")) {
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.lobby.X", player.getLocation().getBlockX());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.lobby.Y", player.getLocation().getBlockY());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.lobby.Z", player.getLocation().getBlockZ());
                        this.plugin.getDmConfig().set("Arenas." + arena + ".Locations.lobby.W", player.getLocation().getWorld().getName());
                        try {
                            this.plugin.getDmConfig().save(this.plugin.getDmFile());
                            Chat.PlayerSendMessage(player, "&d[Dm]the lobby was saved!");
                            CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                        } catch (IOException e) {
                            CLog.ConsoleErrorMessage("Failed to save the spawn");
                        }
                        return false;
                    } else {
                        Chat.PlayerSendMessage(player, "&dUsage: /dm settlobby <arena>");
                        return false;
                    }
                }
            } else {
                Chat.PlayerSendMessage(player, ("&e&l3 Monkeys can change a light together"));
            }
        } else {
            sender.sendMessage("no console");
        }
        return false;
    }
}

