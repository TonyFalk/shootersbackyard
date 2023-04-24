package zx.tonyfalk.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Chat {
    public static void PlayerSendMessage(Player player, String message){
        player.sendMessage((message).replace("&","§"));
    }
    public static void BoardcastMessage(String message){
        Bukkit.broadcastMessage((message).replace("&","§"));
    }
}
