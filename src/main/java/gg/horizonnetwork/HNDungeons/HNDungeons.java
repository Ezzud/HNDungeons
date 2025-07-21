package gg.horizonnetwork.HNDungeons;

import gg.horizonnetwork.HNDungeons.api.DungeonExpansion;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonMob;
import gg.horizonnetwork.HNDungeons.commands.CommandHandler;
import gg.horizonnetwork.HNDungeons.commands.TabCompletion;
import gg.horizonnetwork.HNDungeons.listener.DamageListener;
import gg.horizonnetwork.HNDungeons.managers.ConfigManager;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class HNDungeons extends JavaPlugin {

    @Getter
    public static HNDungeons instance;
    @Getter
    public static YamlConfiguration settingsConfig;
    @Getter
    public static YamlConfiguration messageCache;
    @Getter
    public static YamlConfiguration playerCache;

    @Getter
    private InstanceManager InstanceManager;

    @Override
    public void onEnable() {
        HNDungeons.instance = this;
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            Logger.info("&aEnabling PAPI Hook");
            new DungeonExpansion(this).register(); //

        }

        this.saveDefaultConfig();
        ConfigManager.saveData();
        ConfigManager.saveMessages();
        ConfigManager.saveInvites();
        ConfigManager.saveAllDungeons();
        Logger.info("Loading Dungeons...");
        registerEvents();
        Objects.requireNonNull(this.getCommand("dungeons")).setExecutor(new CommandHandler());
        Objects.requireNonNull(this.getCommand("dungeons")).setTabCompleter(new TabCompletion());
        this.InstanceManager = new InstanceManager(this);

        Logger.success("&aSuccessfully loaded &6&lDungeons&r&b");
    }

    @Override
    public void onDisable() {
        Logger.info("&eUnloading all instance entities...");
        int entityCount = 0;
        for(DungeonInstance i : this.getInstanceManager().getInstances()) {
            List<DungeonMob> mobs = new ArrayList<>(i.getEntities());
            Logger.info("&eUnloading &6"+mobs.size()+" entities &efrom instance " + i.getId().toString());
            for (DungeonMob mob : mobs) {
                Logger.info("&eUnloading " + mob.getName() + " &efrom instance " + i.getId().toString());
                mob.despawn();
                entityCount += 1;
            }
        }
        Logger.success("&aSuccessfully unloaded &e" + entityCount + " &aentities!");
        Logger.success("&aSuccessfully unloaded &6&lDungeons&r&b");
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DamageListener(this), this);
    }
}
