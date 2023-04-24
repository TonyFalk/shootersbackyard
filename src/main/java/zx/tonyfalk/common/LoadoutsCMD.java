package zx.tonyfalk.common;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.CLog;
import zx.tonyfalk.Utils.Chat;

import java.io.IOException;

public class LoadoutsCMD implements CommandExecutor {
    ShootersBackYard plugin;

    public LoadoutsCMD(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("loadouts").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(this.plugin.guiManager.LoadoutSelectMenu(player));
            if (args.length < 1) {
                Chat.PlayerSendMessage(player, ("&dUsage: /loadouts <edit/give>"));
                return false;
            }
            if (args[0].equalsIgnoreCase("edit")) {
                player.openInventory(this.plugin.guiManager.LoadoutSelectMenu(player));
                return false;
            }
            if (args[0].equalsIgnoreCase("give")) {
                this.plugin.inventoryManager.setLoadOut(player);
                Chat.PlayerSendMessage(player,"you will be able to edit me later armors will be given as well its codeed just for teams...");
            }
        }
        return false;
    }
}
