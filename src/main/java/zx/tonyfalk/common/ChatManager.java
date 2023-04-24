package zx.tonyfalk.common;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Color;

public class ChatManager implements Listener {
    ShootersBackYard plugin;

    public ChatManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, (Plugin)this.plugin);
    }

    @EventHandler
    public void C1(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String playerusername = player.getName();
        String message = event.getMessage();
        String prefix = this.plugin.getMsgsConfig().getString("Chat.Prefix");
        String suffix = this.plugin.getMsgsConfig().getString("Chat.Suffix");
        String format = prefix + playerusername + suffix;
        event.setFormat(PlaceholderAPI.setPlaceholders(event.getPlayer(), Color.set(format)) + message);
    }

    @EventHandler
    public void C2(PlayerDeathEvent event){
        event.setDeathMessage(null);
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();
        String ab = "a";
        if (event.getEntity().getKiller() != null) {
            ab = "b";
        } else {
            killer = player;
        }
        switch(event.getEntity().getLastDamageCause().getCause()){
            case PROJECTILE:
                dmsgsetup("projectile",ab,player,killer);
            case FALL:
                dmsgsetup("fall",ab,player,killer);
            case VOID:
                dmsgsetup("void",ab,player,killer);
            case DROWNING:
                dmsgsetup("drowning",ab,player,killer);
            case ENTITY_ATTACK:
                dmsgsetup("melee",ab,player,killer);
            case FIRE:
                dmsgsetup("fire",ab,player,killer);
            case LAVA:
                dmsgsetup("lava",ab,player,killer);
            case LIGHTNING:
                dmsgsetup("lightning",ab,player,killer);
            case ENTITY_EXPLOSION:
            case BLOCK_EXPLOSION:
                dmsgsetup("explosion",ab,player,killer);
            default:
                dmsgsetup("other",ab,player,killer);
        }
    }

    public void dmsgsetup(String path,String ab,Player player,Player killer){
        String dmsg = this.plugin.getMsgsConfig().getString("deathmessages." + path + "." + ab);
        dmsg.replace("<victim>",player.getName().replace("<killer>",killer.getName()));
        dmsg = PlaceholderAPI.setPlaceholders(player, dmsg);
        dmsg.replace("$","%");
        dmsg = PlaceholderAPI.setPlaceholders(killer,dmsg);
        Bukkit.broadcastMessage(Color.set(dmsg));
    }

}
