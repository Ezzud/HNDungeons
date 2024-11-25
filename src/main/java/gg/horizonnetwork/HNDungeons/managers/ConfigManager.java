package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {
    static HNDungeons plugin = HNDungeons.getInstance();

    public static Set<String> listFilesUsingJavaIO(String dir) {
        File f = new File(dir);
        boolean res = f.mkdir();
        return Stream.of(Objects.requireNonNull(f.listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

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

    public static void saveAllDungeons() {
        Set<String> files = listFilesUsingJavaIO("plugins/HN-Dungeons/dungeons");
        if(files.isEmpty()) {
            saveDungeons("default");
            return;
        }
        for (String file : files) {
            saveDungeons(file.replaceAll(".yml", ""));
        }
    }

    public static void saveDungeons(String dungeonName) {
        File f = new File("plugins/HN-Dungeons/dungeons/" + dungeonName + ".yml");
        if(!f.exists()) {
            InputStream is = plugin.getResource("default.yml");
            boolean res = f.getParentFile().mkdirs();
            try {
                assert is != null;
                FileUtils.copyInputStreamToFile(is, f);
            } catch(IOException e) {
                e.printStackTrace();
            }
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

    public static YamlConfiguration getDungeonConfig(String dungeonName) {
        saveDungeons(dungeonName);
        InputStream is = null;
        try {
            is = new FileInputStream("plugins/HN-Dungeons/dungeons/" + dungeonName + ".yml");
            Reader rd = new InputStreamReader(is);
            return YamlConfiguration.loadConfiguration(rd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
