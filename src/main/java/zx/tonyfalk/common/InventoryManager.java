package zx.tonyfalk.common;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import zx.tonyfalk.Data.Loadout;
import zx.tonyfalk.Data.LoadoutSet;
import zx.tonyfalk.Data.PlayerData;
import zx.tonyfalk.Enums.Team;
import zx.tonyfalk.ShootersBackYard;

public class InventoryManager  {
    ShootersBackYard plugin;

    public InventoryManager(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    public void setLobbyItems(Player player) {
        if (player == null) return;
        player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.loudoutandteamselector")));
    }
    public void addTeamselector(Player player){
        if (player == null) return;
        player.getInventory().addItem(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.loudoutandteamselector")));
    }



    public void setGear(Player player,Team team) {
        setArmor(player,team);
        setLoadOut(player);
    }

    public void setArmor(Player player, Team team) {
        if (player == null) return;
        String path = null;
        switch (team) {
            case Red -> {
                path = this.plugin.getGearConfig().getString("Armors.Red");
            }
            case Blue -> {
                path = this.plugin.getGearConfig().getString("Armors.Blue");
            }
            case None -> {
                path = this.plugin.getGearConfig().getString("Armors.None");
            }
            case Infected -> {
                path = this.plugin.getGearConfig().getString("Armors.Infected");
            }
            default -> {
                path = null;
            }
        }
        ItemStack h = this.plugin.getItemdataConfig().getItemStack(path + ".h");
        ItemStack c = this.plugin.getItemdataConfig().getItemStack(path + ".c");
        ItemStack l = this.plugin.getItemdataConfig().getItemStack(path + ".l");
        ItemStack b = this.plugin.getItemdataConfig().getItemStack(path + ".b");
        player.getInventory().setHelmet(h);
        player.getInventory().setHelmet(c);
        player.getInventory().setHelmet(l);
        player.getInventory().setHelmet(b);
    }

    public void setLoadOut(Player player) {
        if (player == null) return;
        PlayerData playerData = DataManager.PlayerDataMap.get(player);
        LoadoutSet loadoutSet = LoadoutSet.fromString(playerData.getLoadoutSet());
        Loadout loadout = loadoutSet.getLoadout(playerData.getChosenLoadout());
        ItemStack p = this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("primarygun." + loadout.getPrimary() + ".itemdata"));
        ItemStack s = this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("secondarygun." + loadout.getSecondary() + ".itemdata"));
        ItemStack g = this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("grenade." + loadout.getGrenade() + ".itemdata"));
        ItemStack m = this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("meleeweapon." + loadout.getMelee() + ".itemdata"));
        ItemStack pa = this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("primarygun." + loadout.getPrimary() + ".ammoitemdata"));
        ItemStack sa = this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("secondarygun." + loadout.getSecondary() + ".ammoitemdata"));
        player.getInventory().setItem(36, p);
        player.getInventory().setItem(37, s);
        player.getInventory().setItem(38, g);
        player.getInventory().setItem(39, m);
        int paa = this.plugin.getGearConfig().getInt("Ammo.primaryammoamount") + 9;
        int saa = this.plugin.getGearConfig().getInt("Ammo.seconderyammoamount") + 18;
        for (int i = 9;i < paa; i++) {
            player.getInventory().setItem(i,pa);
        }
        for (int k = 18;k < saa; k++){
            player.getInventory().setItem(k,sa);
        }
    }
}


