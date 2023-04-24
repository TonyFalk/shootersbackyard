package zx.tonyfalk.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class Color {

    public static String set(String s){
        return (s).replace("&","§");
    }

    public static List setlist(List list){
        List l = list;
        for (int i = 0; i < l.size(); i++) {
            String updatedString = Color.set((String) l.get(i));
            l.set(i, updatedString);
        }
        return l;
    }

    public static List setlistwithplaceholders(Player player,List list){
        List l = list;
        for (int i = 0; i < l.size(); i++) {
            String updatedString = PlaceholderAPI.setPlaceholders(player,Color.set((String) l.get(i)));
            l.set(i, updatedString);
        }
        return l;
    }
}
