package zx.tonyfalk.common;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import zx.tonyfalk.Data.Loadout;
import zx.tonyfalk.Data.LoadoutSet;
import zx.tonyfalk.Data.PlayerData;
import zx.tonyfalk.Enums.Team;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Color;
import zx.tonyfalk.Utils.Gui;

import java.util.ArrayList;
import java.util.List;

public class GuiManager implements Listener {
    ShootersBackYard plugin;

    public GuiManager(ShootersBackYard plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, (Plugin)this.plugin);
    }


    ItemStack itemStack404 = new ItemStack(Material.BARRIER, 1);
    String string404 = "404";
    List<String> list404 = new ArrayList<>();


    public Inventory LoadoutSelectMenu(Player p) {
        final Inventory gui = Bukkit.createInventory((InventoryHolder) p, 9, Color.set(this.plugin.getGuiConfig().getString("LoadOutsMenu.MenuName",string404)));
        PlayerData playerData = DataManager.PlayerDataMap.get(p);
        int playerlevel = playerData.getPlayerLevel();
        for (int i = 0; i < gui.getSize(); i++) {
            ItemStack selected = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta selectedMeta = selected.getItemMeta();
            ItemStack unlocked = new ItemStack(Material.BOOK);
            ItemMeta unlockedMeta = unlocked.getItemMeta();
            ItemStack locked = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta lockedMeta = locked.getItemMeta();
            boolean unlockedloadout = true;
            String name = Color.set(this.plugin.getGuiConfig().getString("LoadOutsMenu." + i + ".name",string404));
            String lockname = Color.set(this.plugin.getGuiConfig().getString("LoadOutsMenu." + i + ".lockname",string404));
            List<String> lore = Color.setlist(this.plugin.getGuiConfig().getStringList("LoadOutsMenu." + i + "description"));
            List<String> lockedlore = Color.setlist(this.plugin.getGuiConfig().getStringList("LoadOutsMenu." + i + "lockdescription"));
            unlockedMeta.setDisplayName(name);
            unlockedMeta.setLore(lore);
            lockedMeta.setDisplayName(lockname);
            lockedMeta.setLore(lockedlore);
            lockedlore.add("ID" + i);
            selectedMeta.setDisplayName("Selected");
            selectedMeta.setLore(lore);
            lore.add("ID" + i);
            locked.setItemMeta(lockedMeta);
            unlocked.setItemMeta(unlockedMeta);
            selected.setItemMeta(selectedMeta);
            if (this.plugin.getGuiConfig().getInt("LoadOutsMenu." + i + ".level",0) > playerlevel) {
                    gui.setItem(i, unlocked);
                    unlockedloadout = false;
                }
            if (this.plugin.getGuiConfig().getString("LoadOutsMenu." + i + ".permission") != null){
                    if (p.hasPermission(this.plugin.getGuiConfig().getString("LoadOutsMenu." + i + ".permission"))) {
                    unlockedloadout = false;
                }
            }
            if (unlockedloadout) {
                if (playerData.getChosenLoadout() == i){
                    gui.setItem(i,selected);
                }
                if (unlockedloadout){
                    gui.setItem(i,unlocked);
                }
            } else {
                gui.setItem(i,locked);
            }
        }
        return gui;
    }


    public Inventory LoadOutEditMenu(Player p,int selected) {
        final Inventory gui = Bukkit.createInventory((InventoryHolder) p, 9, Color.set(this.plugin.getGuiConfig().getString("LoadOutsMenu.MenuName",string404)) + "ID" + selected);
        PlayerData playerData = DataManager.PlayerDataMap.get(p);
        Loadout loadout = LoadoutSet.fromString(playerData.getLoadoutSet()).getLoadout(selected);
        ItemStack primary = Gui.guiItemID(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("primarygun." + loadout.getPrimary() + "itemdata")), loadout.getPrimary());
        ItemStack secondery = Gui.guiItemID(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("seconderygun." + loadout.getSecondary() + "itemdata")), loadout.getSecondary());
        ItemStack grenedes = Gui.guiItemID(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("seconderygun." + loadout.getGrenade() + "itemdata")), loadout.getGrenade());
        ItemStack melee = Gui.guiItemID(this.plugin.getItemdataConfig().getItemStack(this.plugin.getGuiConfig().getString("melee." + loadout.getMelee() + "itemdata")),loadout.getMelee());
        gui.setItem(1, primary);
        gui.setItem(2, secondery);
        gui.setItem(3, grenedes);
        gui.setItem(4, melee);
        gui.setItem(5, this.plugin.getItemdataConfig().getItemStack("Items.equipment.itemdata",itemStack404));
        gui.setItem(9, Gui.guiItem(Material.GREEN_WOOL,"&a&lSAVE LOADOUT","ID9"));
        Gui.setEmptyAreas(gui);
        return gui;
    }

    public Inventory teamselect(Player p){
        final Inventory gui = Bukkit.createInventory((InventoryHolder) p, 9, Color.set("&aSelect team"));
        int redCount = 0;
        int blueCount = 0;
        for (Team team : this.plugin.gameManager.teams.values()) {
            if (team.equals(Team.Red)) {
                redCount++;
            } else if (team.equals(Team.Blue)) {
                blueCount++;
            }
        }
        gui.setItem(1, Gui.guiItem(Material.BLUE_BANNER,"&9&lBLUE TEAM","&9" + blueCount));
        gui.setItem(2,Gui.guiItem(Material.RED_BANNER,"&c&lRED TEAM","&c" + redCount));
        return gui;
    }

    public Inventory guibuilder(Player p,String inventory,int selected) {
        final Inventory gui = Bukkit.createInventory((InventoryHolder) p, this.plugin.getGuiConfig().getInt(inventory + ".size",9), Color.set(this.plugin.getGuiConfig().getString(inventory + ".MenuName" + "ID" + selected,string404 + "ID" + selected)));
        PlayerData playerData = DataManager.PlayerDataMap.get(p);
        int playerlevel = playerData.getPlayerLevel();
        for (int i = 0; i > gui.getSize(); i++) {
            boolean unlocked = true;
            if (!(this.plugin.getGuiConfig().getString(inventory + "s." + i + "itemdata").equals(null))) {
                String item = this.plugin.getGuiConfig().getString(inventory + "s." + i + "itemdata",string404);
                if (this.plugin.getItemdataConfig().contains("Items." + item)) {
                    ItemStack itemStack = (this.plugin.getItemdataConfig().getItemStack("Items." + item));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    String displayname = this.plugin.getGuiConfig().getString(inventory + "s." + i + "name");
                    List<String> lore = this.plugin.getGuiConfig().getStringList(inventory + "s." + i + "description");
                    /**
                    if (this.plugin.getGuiConfig().getString(inventory + "s." + i + "permission") != null) {
                        if (!p.hasPermission(this.plugin.getGuiConfig().getString(inventory + "s." + i + "itemdata"))){
                            unlocked = false;
                        }
                    }
                    if (Integer.valueOf(this.plugin.getGuiConfig().getInt(inventory + "s." + i + "level")) != null ) {
                        if (playerlevel < Integer.valueOf(this.plugin.getGuiConfig().getInt(inventory + "s." + i + "level"))){
                            unlocked = false;
                        }
                    }
                    if (unlocked == false) {
                        displayname = this.plugin.getGuiConfig().getString(inventory + "s." + i + "lockname");
                        lore = this.plugin.getGuiConfig().getStringList(inventory + "s." + i + "lockdescription");
                    } **/
                    itemMeta.setDisplayName(displayname);
                    lore.add("ID" + i);
                    itemMeta.setLore(lore);
                    gui.setItem(i, itemStack);
                }
            }
        }
        Gui.setEmptyAreas(gui);
        return gui;
    }


    @EventHandler
    public void onPlayerInteractWithItem(PlayerInteractEvent event){
        Player player = (Player) event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.LoadoutEditor"))) {
            LoadoutSelectMenu(player);
        }
        if (item == this.plugin.getItemdataConfig().getItemStack(this.plugin.getGearConfig().getString("Lobby.TeamSelector"))){
            teamselect(player);
        }
    }
    @EventHandler
    public void InventoryClick(InventoryClickEvent event){
        event.setCancelled(true);
        String loadoutmenu = Color.set(this.plugin.getGuiConfig().getString("LoadOutsMenu.MenuName"));
        String primaryname = Color.set(this.plugin.getGuiConfig().getString( "primarygun.MenuName"));
        String secondaryname = Color.set(this.plugin.getGuiConfig().getString( "secondarygun.MenuName"));
        String grendesname = Color.set(this.plugin.getGuiConfig().getString( "grenades.MenuName"));
        String meleename = Color.set(this.plugin.getGuiConfig().getString( "melee.MenuName"));
        ItemStack clicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        PlayerData playerData = DataManager.PlayerDataMap.get(player);
        int slotIndex = event.getSlot();
        if (loadoutmenu.equals(event.getView().getTitle())) {
            if (event.getClick().isLeftClick()) {
                playerData.setChosenLoadout(slotIndex);
                player.openInventory(LoadoutSelectMenu(player));
            } else {
                player.openInventory(LoadOutEditMenu(player, slotIndex));
            }
        }
        if(event.getView().getTitle().equals("&aSelect team")){
            int redCount = 0;
            int blueCount = 0;
            for (Team team : this.plugin.gameManager.teams.values()) {
                if (team.equals(Team.Red)) {
                    redCount++;
                } else if (team.equals(Team.Blue)) {
                    blueCount++;
                }
            }
            if (clicked.getType() == Material.RED_BANNER){
                if (redCount + 1 < blueCount) {
                    return;
                } else {
                    this.plugin.gameManager.teams.put(player,Team.Red);
                }
            }
            if (clicked.getType() == Material.BLUE_BANNER){
                if (blueCount + 1 < redCount) {
                    return;
                } else {
                    this.plugin.gameManager.teams.put(player,Team.Blue);
                }
            }
        }
        if (loadoutmenu.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),loadoutmenu)) && !loadoutmenu.equals(event.getView().getTitle())) {
            switch (slotIndex) {
                case 1 -> {
                    player.openInventory(guibuilder(player, "primarygun",Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(), loadoutmenu))));
                }
                case 2 -> {
                    player.openInventory(guibuilder(player, "secondarygun",Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(), loadoutmenu))));
                }
                case 3 -> {
                    player.openInventory(guibuilder(player, "grenedes",Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(), loadoutmenu))));
                }
                case 4 -> {
                    player.openInventory(guibuilder(player, "melee",Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(), loadoutmenu))));
                }
                case 9 -> {
                    LoadoutSet loadoutSet = LoadoutSet.fromString(playerData.getLoadoutSet());
                    loadoutSet.setLoadout(Integer.valueOf(Gui.TitleStringIDGetter(event.getView().getTitle(),loadoutmenu)),LoadoutSet.fromString(playerData.getLoadoutSet()).getLoadout(Integer.valueOf(Gui.TitleStringIDGetter(event.getView().getTitle(),loadoutmenu))));
                }
            }
        }

        if (primaryname.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),primaryname)) && !primaryname.equals(event.getView().getTitle())){
            int clickedslot = event.getSlot();
            if (this.plugin.getGuiConfig().getString("primarygun" + clickedslot + "lockname").equals(clicked.getItemMeta().getDisplayName())){
                return;
            } else {
                LoadoutSet loadoutSet = LoadoutSet.fromString(playerData.getLoadoutSet());
                Loadout loadout = loadoutSet.getLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),loadoutmenu)));
                loadout.setPrimary(clickedslot);
                loadoutSet.setLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),primaryname)),loadout);
                player.openInventory(LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),primaryname)))));
                return;
            }
        }
        if (secondaryname.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),secondaryname)) && !secondaryname.equals(event.getView().getTitle())){
            int clickedslot = event.getSlot();
            if (this.plugin.getGuiConfig().getString("secondarygun" + clickedslot + "lockname").equals(clicked.getItemMeta().getDisplayName())){
                return;
            } else {
                LoadoutSet loadoutSet = LoadoutSet.fromString(playerData.getLoadoutSet());
                Loadout loadout = loadoutSet.getLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),loadoutmenu)));
                loadout.setSecondary(clickedslot);
                loadoutSet.setLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),secondaryname)),loadout);
                player.openInventory(LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),secondaryname)))));
                return;
            }
        }
        if (grendesname.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),grendesname)) && !grendesname.equals(event.getView().getTitle())){
            int clickedslot = event.getSlot();
            if (this.plugin.getGuiConfig().getString("grenades" + clickedslot + "lockname").equals(clicked.getItemMeta().getDisplayName())){
                return;
            } else {
                LoadoutSet loadoutSet = LoadoutSet.fromString(playerData.getLoadoutSet());
                Loadout loadout = loadoutSet.getLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),loadoutmenu)));
                loadout.setGrenade(clickedslot);
                loadoutSet.setLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),grendesname)),loadout);
                player.openInventory(LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),grendesname)))));
                return;
            }
        }
        if (meleename.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),meleename)) && !meleename.equals(event.getView().getTitle())){
            int clickedslot = event.getSlot();
            if (this.plugin.getGuiConfig().getString("melee" + clickedslot + "lockname").equals(clicked.getItemMeta().getDisplayName())){
                return;
            } else {
                LoadoutSet loadoutSet = LoadoutSet.fromString(playerData.getLoadoutSet());
                Loadout loadout = loadoutSet.getLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),loadoutmenu)));
                loadout.setMelee(clickedslot);
                loadoutSet.setLoadout(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),meleename)),loadout);
                player.openInventory( LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),meleename)))));
                return;
            }
        }
    }
    @EventHandler
    public void InventoryClose(InventoryCloseEvent event){
        String loadoutmenu = Color.set(this.plugin.getGuiConfig().getString("LoadOutsMenu.MenuName"));
        String primaryname = Color.set(this.plugin.getGuiConfig().getString( "primarygun.MenuName"));
        String secondaryname = Color.set(this.plugin.getGuiConfig().getString( "secondarygun.MenuName"));
        String grendesname = Color.set(this.plugin.getGuiConfig().getString( "grenades.MenuName"));
        String meleename = Color.set(this.plugin.getGuiConfig().getString( "melee.MenuName"));
        Player player = (Player) event.getPlayer();
        PlayerData playerData = DataManager.PlayerDataMap.get(player);
        if (loadoutmenu.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),loadoutmenu)) && !loadoutmenu.equals(event.getView().getTitle())) {
            player.openInventory(LoadoutSelectMenu(player));
        }
        if (primaryname.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),primaryname)) && !primaryname.equals(event.getView().getTitle())){
            player.openInventory(LoadOutEditMenu(player,Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(), primaryname))));
        }
        if (secondaryname.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),secondaryname)) && !secondaryname.equals(event.getView().getTitle())){
            player.openInventory(LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),secondaryname)))));
        }
        if (grendesname.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),grendesname)) && !grendesname.equals(event.getView().getTitle())) {
            player.openInventory(LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),grendesname)))));
        }
        if (meleename.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),meleename)) && !meleename.equals(event.getView().getTitle())){
            player.openInventory(LoadOutEditMenu(player,(Integer.parseInt(Gui.TitleStringIDGetter(event.getView().getTitle(),meleename)))));
        }
        if (loadoutmenu.equals(Gui.TitleStringIDCleaner(event.getView().getTitle(),loadoutmenu)) && !loadoutmenu.equals(event.getView().getTitle())) {
            player.openInventory(LoadoutSelectMenu(player));
        }
    }
}

