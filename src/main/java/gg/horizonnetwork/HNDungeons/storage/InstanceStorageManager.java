package gg.horizonnetwork.HNDungeons.storage;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonMob;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.ConfigManager;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InstanceStorageManager {

    private final HNDungeons plugin;
    private ConfigurationSection config;
    private final YamlConfiguration configFile;

    public InstanceStorageManager(HNDungeons plugin){
        this.plugin = plugin;
        this.configFile = ConfigManager.getData();
        assert configFile != null;
        this.config = configFile.getConfigurationSection("instances");
        if(this.config == null)
            this.config = configFile.createSection("instances");
    }

    public boolean has(DungeonInstance instance){
        if(instance.getId() == null) return false;
        return this.config.getKeys(false).contains(instance.getId().toString());
    }
    
    public void save(DungeonInstance instance) {
        if(this.has(instance)){
            return;
        }
        this.config.set(instance.getId() + ".world.instance",
                instance.getWorld().getInstanciatedWorld().getName());
        this.config.set(instance.getId() + ".world.original",
                instance.getWorld().getOriginalWorld().getName());
        this.config.set(instance.getId() + ".level",
                instance.getLevel());
        this.config.set(instance.getId() + ".state",
                instance.getState().toString());
        List<String> memberList = new ArrayList<>();
        for(DungeonPlayer member : instance.getParty().getPlayers()) {
            this.config.set(instance.getId() + ".party." + member.getOfflinePlayer().getUniqueId() + ".originalLocation.x",
                    member.getOriginalLocation().getX()
                    );
            this.config.set(instance.getId() + ".party." + member.getOfflinePlayer().getUniqueId() + ".originalLocation.y",
                    member.getOriginalLocation().getY()
            );
            this.config.set(instance.getId() + ".party." + member.getOfflinePlayer().getUniqueId() + ".originalLocation.z",
                    member.getOriginalLocation().getZ()
            );
            this.config.set(instance.getId() + ".party." + member.getOfflinePlayer().getUniqueId() + ".originalLocation.yaw",
                    member.getOriginalLocation().getYaw()
            );
            this.config.set(instance.getId() + ".party." + member.getOfflinePlayer().getUniqueId() + ".originalLocation.pitch",
                    member.getOriginalLocation().getPitch()
            );
            World w = member.getOriginalLocation().getWorld();
            if(w == null) {
                w = Bukkit.getWorld("world");
            }
            assert w != null;
            this.config.set(instance.getId() + ".party." + member.getOfflinePlayer().getUniqueId() + ".originalLocation.world",
                    w.getName()
            );
        }
        this.config.set(instance.getId() + ".host", instance.getHost().getOfflinePlayer().getUniqueId().toString());
        for(DungeonMob m : instance.getEntities()) {
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".name",
                    m.getName());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".type",
                    m.getEntity().getType().toString());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".invincible",
                    m.getIsInvincible().toString());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".ai",
                    m.getHasAI().toString());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".location.x",
                    m.getLocation().getX());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".location.y",
                    m.getLocation().getY());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".location.z",
                    m.getLocation().getZ());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".location.yaw",
                    m.getLocation().getYaw());
            this.config.set(instance.getId() + ".entities" + m.getEntity().getEntityId() + ".location.pitch",
                    m.getLocation().getPitch());
        }
        try {
            File f = new File("plugins/HN-Dungeons/data.yml");
            this.configFile.save(f);
            ConfigManager.saveData();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void delete(DungeonInstance instance) {
        if(!this.has(instance)){
            Logger.error("Instance not found in data");
            return;
        }
        ConfigurationSection s = this.config.getConfigurationSection(instance.getId().toString());
        if(s == null) {
            Logger.error("Instance not found in data");
        }
        Logger.info("Deleting data value of instance...");
        this.config.set(instance.getId().toString(), null);
        try {
            File f = new File("plugins/HN-Dungeons/data.yml");
            this.configFile.save(f);
        } catch(IOException e){
            e.printStackTrace();
            return;
        }
        return;
    }

    public List<ConfigurationSection> getInstanceData() {
        List<ConfigurationSection> data = new ArrayList<>();
        Set<String> keys = this.config.getKeys(false);
        for(String key : keys) {
            ConfigurationSection section = this.config.getConfigurationSection(key);
            data.add(section);
        }
        return data;
    }
}
