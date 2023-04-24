package zx.tonyfalk.common;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.CLog;
import zx.tonyfalk.Utils.Chat;

import java.io.IOException;

public class GamesCMD implements CommandExecutor {
    ShootersBackYard plugin;

    public GamesCMD(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("games").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("op")) {
                if (args.length < 1) {
                    Chat.PlayerSendMessage(player, "&dUsage: /games <status/startgameserver>");
                    return false;
                }
                if (args[0].equalsIgnoreCase("status")) {
                    this.plugin.ctfGame.Gamestatus(player);
                    this.plugin.tdmGame.Gamestatus(player);
                    this.plugin.dmGame.Gamestatus(player);
                }
                if (args[0].equalsIgnoreCase("startgameserver")) {
                    //todo
                }
            } else {
                Chat.PlayerSendMessage(player,"you are not an admin");
            }
            return true;
        }
        return false;
    }
}
