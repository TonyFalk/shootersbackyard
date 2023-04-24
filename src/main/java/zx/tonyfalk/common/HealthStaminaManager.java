package zx.tonyfalk.common;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import zx.tonyfalk.ShootersBackYard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HealthStaminaManager implements Listener,Runnable {
    ShootersBackYard plugin;

    public HealthStaminaManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, (Plugin)this.plugin);
    }

    public static HashMap<Player, Integer> RegenCounter = new HashMap<>();
    public static HashMap<Player, Integer> SprintCounter = new HashMap<>();

    private final int RegenTime = 20;

    @EventHandler
    public void test(PlayerItemConsumeEvent event){
        if (event.getItem().getType() == Material.GOLDEN_APPLE){
            this.plugin.guiManager.LoadoutSelectMenu(event.getPlayer());
        }
    }

    @EventHandler
    public void pje(PlayerJoinEvent event){
        SprintCounter.put(event.getPlayer(),80600);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void oeh(EntityDamageByEntityEvent event) {
        if (event.getDamage() == 0.0D)
            return;
        if (event.isCancelled())
            return;
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            RegenCounter.put(player, RegenTime);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void oeh(EntityDamageEvent event) {
        if (event.getDamage() == 0.0D)
            return;
        if (event.isCancelled())
            return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) &&
                event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            RegenCounter.put(player, RegenTime);
        }
    }
    @EventHandler
    public void dhr(EntityRegainHealthEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    public void dfr(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }
    @Override
    public void run() { // every 0.25 sec
        for (Player player : SprintCounter.keySet()) {
                player.setFoodLevel(SprintCounter.get(player) / 4000);
                if (!player.isSprinting()) {
                    if (SprintCounter.get(player) % 1000 < 600) {
                        SprintCounter.put(player, SprintCounter.get(player) / 1000 * 1000 + ((SprintCounter.get(player) % 1000) + 50));
                    } else {
                        if (SprintCounter.get(player) < 80000) {
                            SprintCounter.put(player, SprintCounter.get(player) + 2000);
                        }
                    }
                } else {
                    if (SprintCounter.get(player) % 1000 != 0) {
                        SprintCounter.put(player, SprintCounter.get(player) / 1000 * 1000);
                    }
                    SprintCounter.put(player, SprintCounter.get(player) - 1000);
                }
            }
        for (Player player : RegenCounter.keySet()) {
            if (RegenCounter.get(player) <= 1) {
                if (player.getMaxHealth() > player.getHealth()) {
                    if (!(player.getHealth() + (player.getMaxHealth() / 10) > player.getMaxHealth())) {
                        player.setHealth(player.getHealth() + (player.getMaxHealth() / 10));
                    } else {
                        player.setHealth(player.getMaxHealth());
                    }
                }
            } else {
                RegenCounter.put(player, Integer.valueOf(RegenCounter.get(player) - 1));
            }
        }
    }
}
