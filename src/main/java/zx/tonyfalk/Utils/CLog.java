package zx.tonyfalk.Utils;

import org.bukkit.Bukkit;

public class CLog {
    private static final String PluginPrefix = Color.set("&6[&4&lUnrealcraft Gunpoint&6]&r");
    private static final String ChangeFormat = PluginPrefix + Color.set("&e[CHANGE]");
    private static final String WarningFormat = PluginPrefix + Color.set("&c[WARNING]");
    private static final String ErrorFormat = PluginPrefix + Color.set("&4[ERROR]");
    private static final String DefaultFormat = PluginPrefix + Color.set("&a[DEFAULT]");
    private static final String DebugFormat = PluginPrefix + Color.set("&6[Debug]");
    public static void ConsoleDefaultMessage(String message){
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + message);
    }
    public static void OffiicalMessage(String message){
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + message);
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⣀⣀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣛⣛⣻⣿⣦⣀⠀⢀⣀⣀⣏⣹");
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + "⠀⢠⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠭⠭⠽⠽⠿⠿⠭⠭⠭⠽⠿⠿⠛ ");
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + "⠀⠈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⠛⠉⢻⣿⣿⣿⡟⠏⠉⠉⣿⢿⣿⣿⣿⣇⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + "⠀⠀⣿⣿⣿⣿⣿⣿⣿⡿⠿⠛⠉⠁⠀⠀⠀⢠⣿⣿⣿⠋⠑⠒⠒⠚⠙⠸⣿⣿⣿⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + "⠀⠀⣿⣿⡿⠿⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⣰⣿⣿⡿⠃⠀⠀⠀⠀⠀⠀⠀⢻⣿⣿⣿⣿⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        Bukkit.getConsoleSender().sendMessage(DefaultFormat + "⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠛⠛⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⣿⣿⣿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    }
    public static void ConsoleDebugMessage(String message){
        Bukkit.getConsoleSender().sendMessage(DebugFormat + message);
    }
    public static void ConsoleWarningMessage(String message){
        Bukkit.getConsoleSender().sendMessage(WarningFormat + message);
    }
    public static void ConsoleChangeMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChangeFormat + message);
    }
    public static void ConsoleErrorMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ErrorFormat + message);
    }
}
