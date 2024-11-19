package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigManager {
    static HNDungeons plugin = HNDungeons.getInstance();

    public static void saveData() {
        File f = new File("plugins/HN-Dungeons/data.yml");
        if(!f.exists()) {
            plugin.saveResource("data.yml", false);
        }
    }

    public static void saveMessages() {
        File f = new File("plugins/HN-Dungeons/lang.yml");
        if(!f.exists()) {
            plugin.saveResource("lang.yml", false);
        }
    }

    public static void saveInvites() {
        File f = new File("plugins/HN-Dungeons/invites.yml");
        if(!f.exists()) {
            plugin.saveResource("invites.yml", false);
        }
    }

    public static YamlConfiguration getData() {
        saveData();
        InputStream is = null;
        try {
            is = new FileInputStream("plugins/HN-Dungeons/data.yml");
            Reader rd = new InputStreamReader(is);
            return YamlConfiguration.loadConfiguration(rd);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static YamlConfiguration getInvites() {
        saveInvites();
        InputStream is = null;
        try {
            is = new FileInputStream("plugins/HN-Dungeons/invites.yml");
            Reader rd = new InputStreamReader(is);
            return YamlConfiguration.loadConfiguration(rd);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static YamlConfiguration getMessages() {
        saveMessages();
        InputStream is = null;
        try {
            is = new FileInputStream("plugins/HN-Dungeons/lang.yml");
            Reader rd = new InputStreamReader(is);
            return YamlConfiguration.loadConfiguration(rd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
