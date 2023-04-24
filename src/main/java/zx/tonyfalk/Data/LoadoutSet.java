package zx.tonyfalk.Data;

import zx.tonyfalk.Utils.CLog;

public class LoadoutSet {
    private Loadout[] loadouts;

    public LoadoutSet() {
        // create 7 Loadout objects with default values
        loadouts = new Loadout[7];
        for (int i = 0; i < loadouts.length; i++) {
            loadouts[i] = new Loadout(1, 1, 1, 1, 1);
        }
    }

    public Loadout getLoadout(int index) {
        if (index < 0 || index >= loadouts.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return loadouts[index];
    }

    public void setLoadout(int index, Loadout newLoadout) {
        if (index < 0 || index >= loadouts.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        loadouts[index] = newLoadout;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoadoutSet {\n");
        for (int i = 0; i < loadouts.length; i++) {
            sb.append("  Loadout ");
            sb.append(i);
            sb.append(": ");
            sb.append(loadouts[i].toString());
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public static LoadoutSet fromString(String str) {
        LoadoutSet loadoutSet = new LoadoutSet();
        String[] lines = str.split("\n");
        for (int i = 0; i < loadoutSet.loadouts.length; i++) {
            if (i + 1 < lines.length) { // Check if lines array has at least two elements
                String line = lines[i + 1].trim();
                String[] tokens = line.split(":");
                if (tokens.length > 1) { // Check if tokens array has at least two elements
                    Loadout loadout = Loadout.fromString(tokens[1].trim());
                    loadoutSet.setLoadout(i, loadout);
                } else {
                    CLog.ConsoleErrorMessage("NoEnough elements" + str);
                }
            } else {
                CLog.ConsoleErrorMessage("NoEnough elements" + str);
            }
        }
        return loadoutSet;
    }
}