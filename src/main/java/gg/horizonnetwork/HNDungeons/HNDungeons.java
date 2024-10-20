package gg.horizonnetwork.HNDungeons;


import gg.horizonnetwork.HNDungeons.commands.Dungeons;
import gg.horizonnetwork.HNDungeons.commands.admin.dReloadCommand;
import gg.horizonnetwork.HNDungeons.commands.player.dHelp;
import gg.techtide.tidelib.logger.TideLogger;
import gg.techtide.tidelib.revamped.abysslibrary.config.TideConfig;
import gg.techtide.tidelib.revamped.abysslibrary.plugin.TidePlugin;
import gg.techtide.tidelib.revamped.abysslibrary.storage.common.CommonStorageImpl;
import gg.techtide.tidelib.revamped.abysslibrary.storage.type.StorageType;
import gg.techtide.tidelib.revamped.abysslibrary.text.message.cache.MessageCache;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class HNDungeons extends TidePlugin {


    public static HNDungeons instance;

    private final TideConfig storageConfig = this.getYml("storage");
    private final StorageType storageType = StorageType.valueOf(this.storageConfig.getString("storage.type"));

    private TideConfig settingsConfig = this.getYml("settings");

    private MessageCache messageCache = new MessageCache(this.getYml("lang"));

    private Dungeons dungeons = new Dungeons(this);
    private CommonStorageImpl<UUID, Player> storage;


    @Override
    public String pluginName() {
        return "HNDungeons";
    }

    @Override
    public void onEnable() {
        HNDungeons.instance = this;

        TideLogger.console("Loading &fDungeons&b");
        this.loadMessages(this.messageCache, this.getYml("lang"));

        this.dungeons.register();
        this.dungeons.register(new dHelp(this.));

        TideLogger.console("Successfully loaded &fTideHubCore&b");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
