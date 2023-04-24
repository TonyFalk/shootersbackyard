package zx.tonyfalk.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Gui {


    public static ItemStack guiItem(Material material, String name, String... lore){
        return guiItem(material, name, 1, lore);
    }

    public static ItemStack guiItemID(ItemStack itemStack,int ID){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lst = itemMeta.getLore();
        lst.add("ID" + ID);
        return itemStack;
    }
    public static String TitleStringIDCleaner(String s, String original) {
        int numCharsToTrim = Math.min(original.length(), s.length());
        String cleanst = s.substring(0, s.length() - numCharsToTrim);
        return cleanst;
    }
    public static String TitleStringIDGetter(String s,String Original){
        int numCharsToTrim = Math.min(s.length(), Original.length() + 2);
        String cleanst = s.substring(numCharsToTrim, s.length() - numCharsToTrim);
        return cleanst;
    }
    public static Integer IDfromLore(List<String> lore){
        String lastString = lore.get(lore.size() - 1);
        return Integer.valueOf(lastString.replace("ID",""));
    }

    public static ItemStack guiItem(Material material, String name, int amount, String... lore){
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Color.set(name));
        ArrayList<String> itemLore = new ArrayList<>();
        for(String s : lore){
            itemLore.add(Color.set(s));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DESTROYS,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }


    public static void setEmptyAreas(Inventory inventory){
        ItemStack guiFillItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        setEmptyAreas(inventory, guiFillItem);
    }

    public static void setEmptyAreas(Inventory inventory, ItemStack guiFillItem){
        int i;
        ItemMeta guiFillItemMeta = guiFillItem.getItemMeta();
        guiFillItemMeta.setDisplayName(" ");
        guiFillItem.setItemMeta(guiFillItemMeta);
        for(i=0;i<inventory.getSize();i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, guiFillItem);
            }
        }
    }
}
