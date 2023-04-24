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

public class ItemData implements CommandExecutor {
    ShootersBackYard plugin;

    public ItemData(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("itemdata").setExecutor(this);
    }

    public ItemStack getItem(String id){
        return this.plugin.getItemdataConfig().getItemStack("Items.id".replace("id",id));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("op")) {
                if (args.length < 1) {
                    Chat.PlayerSendMessage(player, "&dUsage: /ItemData <register/delete/give>");
                    return false;
                }
                if (args[0].equalsIgnoreCase("register")) {
                    String itemname = args[1].toUpperCase();
                    if (!itemname.equals("")) {
                        if (!this.plugin.getItemdataConfig().contains("Items.%NAME%".replace("%NAME%", itemname))) {
                            ItemStack inhand = player.getInventory().getItemInHand();
                            this.plugin.getItemdataConfig().set("Items.%NAME%".replace("%NAME%", itemname), inhand);
                            try {
                                this.plugin.getItemdataConfig().save(this.plugin.getItemDataFile());
                                Chat.PlayerSendMessage(player, "&d[ItemData]%NAME% was saved".replace("%NAME%", itemname));
                                CLog.ConsoleChangeMessage("a new item %NAME% was saved".replace("%NAME%", itemname));
                            } catch (IOException e) {
                                CLog.ConsoleErrorMessage("ItemData failed to save an item");
                            }
                        } else {
                            Chat.PlayerSendMessage(player, "&d[ItemData]%NAME% already exists delete it first!".replace("%NAME%", itemname));
                            return false;
                        }
                    } else {
                        Chat.PlayerSendMessage(player, "&d[ItemData]Usage: /itemdata register <name>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    String itemname = args[1].toUpperCase();
                    if (!itemname.equals("")) {
                        if (this.plugin.getItemdataConfig().contains("Items.%NAME%".replace("%NAME%", itemname))) {
                            this.plugin.getItemdataConfig().set("Items.%NAME%".replace("%NAME%", itemname), null);
                            try {
                                this.plugin.getItemdataConfig().save(this.plugin.getItemDataFile());
                                Chat.PlayerSendMessage(player, "&d[ItemData]%NAME% was deleted".replace("%NAME%", itemname));
                                CLog.ConsoleChangeMessage("a new item %NAME% was deleted".replace("%NAME%", itemname));
                            } catch (IOException e) {
                                CLog.ConsoleErrorMessage("ItemData failed to delete an item");
                            }
                        }
                    } else {
                        Chat.PlayerSendMessage(player, "&d[ItemData]Usage: /itemdata delete <name>");
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("give")) {
                    String itemname = args[1].toUpperCase();
                    if (!itemname.equals("")) {
                        if (this.plugin.getItemdataConfig().contains("Items.%NAME%".replace("%NAME%", itemname)) && this.plugin.getItemdataConfig().getItemStack("Items.%NAME%".replace("%NAME%", itemname)) != null) {
                            player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack("Items.%NAME%".replace("%NAME%", itemname)));
                        } else {
                            Chat.PlayerSendMessage(player, "&d[ItemData] %NAME% does not exist!".replace("%NAME%", itemname));
                        }
                    } else {
                        Chat.PlayerSendMessage(player, "&d[ItemData]Usage: /itemdata give <name>");
                        return false;
                    }
                }
            } else {
                Chat.PlayerSendMessage(player, ("&dto be removed!?!").replace('&', '§'));
                return true;
            }
        } else {
            sender.sendMessage("console cant work with itemdata");
            return false;
        }
        return true;
    }
}
