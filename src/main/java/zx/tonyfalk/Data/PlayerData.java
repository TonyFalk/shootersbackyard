package zx.tonyfalk.Data;

public class PlayerData {
    private int kills;
    private int deaths;
    private int cash;
    private int xp;
    private int chosenLoadout;
    private String loadoutSet;
    private int matchKills;
    private int matchDeaths;
    private int killStreak;
    private int matchXp;

    public PlayerData(int kills, int deaths, int cash, int xp, int chosenLoadout, String loadoutSet) {
        this.kills = kills;
        this.deaths = deaths;
        this.cash = cash;
        this.xp = xp;
        this.chosenLoadout = chosenLoadout;
        this.loadoutSet = loadoutSet;
        this.matchKills = 0;
        this.matchDeaths = 0;
        this.killStreak = 0;
        this.matchXp = 0;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getChosenLoadout() {
        return chosenLoadout;
    }

    public void setChosenLoadout(int chosenLoadout) {
        this.chosenLoadout = chosenLoadout;
    }

    public String getLoadoutSet() {
        return loadoutSet;
    }

    public void setLoadoutSet(String loadoutSet) {
        this.loadoutSet = loadoutSet;
    }

    public int getMatchKills() {
        return matchKills;
    }

    public void setMatchKills(int matchKills) {
        this.matchKills = matchKills;
    }

    public int getMatchDeaths() {
        return matchDeaths;
    }

    public void setMatchDeaths(int matchDeaths) {
        this.matchDeaths = matchDeaths;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public void setKillStreak(int killStreak) {
        this.killStreak = killStreak;
    }

    public int getMatchXp() {
        return matchXp;
    }

    public void setMatchXp(int matchXp) {
        this.matchXp = matchXp;
    }
    public int getPlayerLevel() {
        int level = 1;
        int xpNeeded = 100;
        int xp = getXp();
        while (xp >= xpNeeded) {
            level++;
            xp -= xpNeeded;
            xpNeeded += 50*level;
        }
        return level;
    }
}