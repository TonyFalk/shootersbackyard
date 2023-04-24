package zx.tonyfalk.Data;

import org.bukkit.util.StringUtil;

public class Loadout {
    private int primary;
    private int secondary;
    private int grenade;
    private int melee;
    private int equipment;

    public Loadout(int primary, int secondary, int grenade, int melee, int equipment) {
        this.primary = primary;
        this.secondary = secondary;
        this.grenade = grenade;
        this.melee = melee;
        this.equipment = equipment;
    }


    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public int getSecondary() {
        return secondary;
    }

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public int getGrenade() {
        return grenade;
    }

    public void setGrenade(int grenade) {
        this.grenade = grenade;
    }

    public int getMelee() {
        return melee;
    }

    public void setMelee(int melee) {
        this.melee = melee;
    }

    public int getEquipment() {
        return equipment;
    }

    public void setEquipment(int equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "Loadout{" +
                "primary=" + primary +
                ", secondary=" + secondary +
                ", melee=" + melee +
                ", grenade=" + grenade +
                ", equipment=" + equipment +
                '}';
    }

    public static Loadout fromString(String str) {
        str = str.substring(str.indexOf("{") + 1);
        str = str.substring(0, str.indexOf("}"));
        String[] parts = str.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid string format: " + str);
        }
        int primaryWeapon = Integer.parseInt(parts[0].split("=")[1].trim());
        int secondaryWeapon = Integer.parseInt(parts[1].split("=")[1].trim());
        int meleeWeapon = Integer.parseInt(parts[2].split("=")[1].trim());
        int grenade = Integer.parseInt(parts[3].split("=")[1].trim());
        int equipment = Integer.parseInt(parts[4].split("=")[1].trim());
        return new Loadout(primaryWeapon, secondaryWeapon, meleeWeapon, grenade, equipment);
    }
}