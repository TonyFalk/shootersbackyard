package zx.tonyfalk.common;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zx.tonyfalk.Data.PlayerData;
import zx.tonyfalk.ShootersBackYard;

public class PlaceholderManager extends PlaceholderExpansion {

    public ShootersBackYard plugin;

    public PlaceholderManager(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "unrealcraft";
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    public String onPlaceholderRequest(Player p, String identifier) {
        PlayerData playerData = DataManager.PlayerDataMap.get(p);
        if (identifier.equals("playerlevel")) {
            return "Lv." + playerData.getPlayerLevel();
        }
        if (identifier.equals("exptolevelup")) {
            return "exp left to levelup!";
        }
        if (identifier.equals("teamcolor")) {
            return this.plugin.leaderboard.PlayerTeamsString(p);
        }
        if (identifier.equals("arena")) {
            return this.plugin.gameManager.arena;
        }
        if (identifier.equals("type")) {
            return String.valueOf(this.plugin.gameManager.runningGame);
        }
        if (identifier.equals("timeleft")) {
            return this.plugin.timer.timeinstring(this.plugin.timer.Counter);
        }
        if (identifier.equals("matchplayerkills")) {
            return String.valueOf(playerData.getMatchKills());
        }
        if (identifier.equals("matchplayerdeaths")) {
            return String.valueOf(playerData.getMatchDeaths());
        }
        if (identifier.equals("matchplayerpoints")) {
            return "counted points during the match";
        }
        if (identifier.equals("playerkills")) {
            return String.valueOf(playerData.getKills());
        }
        if (identifier.equals("playerdeaths")) {
            return String.valueOf(playerData.getDeaths());
        }
        if (identifier.equals("flagcapture_blueflag")) {
            return "flag status";
        }
        if (identifier.equals("flagcapture_redflag")) {
            return "flag status";
        }
        if (identifier.equals("deathmatch_bluekillcount")) {
            return "player total kills";
        }
        if (identifier.equals("deathmatch_redkillcount")) {
            return "player total kills";
        }
        if (identifier.equals("ffa_position")) {
            return "returns your kill count comparing to others?";
        }
        if (identifier.equals("ffa_top")) {
            return "returns the highest kill count at that ffa match";
        }
        if (identifier.equals("weapon_inhand")) {
            String r = "Bare hands";
            try {
                r = p.getItemInUse().getItemMeta().getDisplayName();
            } catch (NullPointerException ignored){}
            return r;
        }
        return null;
    }
}

