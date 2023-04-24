package zx.tonyfalk.Hub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.CLog;
import zx.tonyfalk.Utils.Chat;

import java.io.IOException;

public class HubCMD implements CommandExecutor {
    ShootersBackYard plugin;

    public HubCMD(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("hubset").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("op")) {
                if (args.length < 1) {
                    Chat.PlayerSendMessage(player, ("&dUsage: /hubset <setlobby/soon more...>"));
                    return false;
                }
                if (args[0].equalsIgnoreCase("setlobby")) {
                    this.plugin.getHubConfig().set("Locations.lobby.X", player.getLocation().getBlockX());
                    this.plugin.getHubConfig().set("Locations.lobby.Y", player.getLocation().getBlockY());
                    this.plugin.getHubConfig().set("Locations.lobby.Z", player.getLocation().getBlockZ());
                    this.plugin.getHubConfig().set("Locations.lobby.W", player.getLocation().getWorld().getName());
                    try {
                        this.plugin.getHubConfig().save(this.plugin.getHubFile());
                        Chat.PlayerSendMessage(player, "&d[Hub]the lobby was saved!");
                        CLog.ConsoleChangeMessage("a new point was saved" + player.getLocation().getBlockX() + player.getLocation().getBlockY() + player.getLocation().getBlockZ() + player.getWorld().getName());
                    } catch (IOException e) {
                        CLog.ConsoleErrorMessage("Failed to save the spawn");
                    }
                    return false;
                }
            } else {
                Chat.PlayerSendMessage(player, ("&e&l3 Monkeys can change a light together"));
            }
        } else {
            sender.sendMessage("no console fucking bastard idiot cunt fuck u fuck u fuck NO ENGLISH just Bangladesh");
        }
        return false;
    }
}

