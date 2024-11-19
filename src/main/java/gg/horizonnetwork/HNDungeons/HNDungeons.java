package gg.horizonnetwork.HNDungeons;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import gg.horizonnetwork.HNDungeons.commands.CommandHandler;
import gg.horizonnetwork.HNDungeons.commands.TabCompletion;
import gg.horizonnetwork.HNDungeons.listener.DamageListener;
import gg.horizonnetwork.HNDungeons.managers.ConfigManager;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import gg.techtide.tidelib.logger.TideLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
    @Getter
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        HNDungeons.instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();

        this.saveDefaultConfig();
        ConfigManager.saveData();
        ConfigManager.saveMessages();
        ConfigManager.saveInvites();
        Logger.info("Loading Dungeons...");
        registerEvents();
        Objects.requireNonNull(this.getCommand("dungeons")).setExecutor(new CommandHandler());
        Objects.requireNonNull(this.getCommand("dungeons")).setTabCompleter(new TabCompletion());
        //this.loadStorage();

        this.InstanceManager = new InstanceManager(this);


        Logger.success("Successfully loaded &6&lDungeons&r&b");
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DamageListener(this), this);
    }

    public void onReload() {
        final double start = System.currentTimeMillis();
        //this.DungeonsCommand.register();
        //this.DungeonsCommand.register((CommandMap) new dHelp(this.DungeonsCommand.getPlugin()));

        final double elapsed = System.currentTimeMillis() - start;
    }

}
