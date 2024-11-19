package gg.horizonnetwork.HNDungeons.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static void log(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void info(String msg) {
        Logger.log("&b[INFO] &e" + msg);
    }

    public static void warn(String msg) {
        Logger.log("&6[WARN] &e" + msg);
    }

    public static void error(String msg) {
        Logger.log("&4[ERROR] &e" + msg);
    }

    public static void success(String msg) {
        Logger.log("&a[SUCCESS] &e" + msg);
    }

}
