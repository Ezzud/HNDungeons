package gg.horizonnetwork.HNDungeons;

import gg.horizonnetwork.HNDungeons.commands.Dungeons;
import gg.horizonnetwork.HNDungeons.commands.player.dHelp;
import gg.horizonnetwork.HNDungeons.listener.StorageJoinLeaveListener;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.storage.DungeonProfile;
import gg.horizonnetwork.HNDungeons.storage.InstanceProfile;
import gg.horizonnetwork.HNDungeons.storage.json.InstanceJSONStorage;
import gg.horizonnetwork.HNDungeons.storage.json.PlayerJsonStorage;
import gg.horizonnetwork.HNDungeons.storage.sql.PlayerSQLStorage;
import gg.techtide.tidelib.logger.TideLogger;
import gg.techtide.tidelib.revamped.abysslibrary.PlaceholderReplacer;
import gg.techtide.tidelib.revamped.abysslibrary.config.TideConfig;
import gg.techtide.tidelib.revamped.abysslibrary.plugin.TidePlugin;
import gg.techtide.tidelib.revamped.abysslibrary.storage.common.CommonStorageImpl;
import gg.techtide.tidelib.revamped.abysslibrary.storage.type.StorageType;
import gg.techtide.tidelib.revamped.abysslibrary.text.message.cache.MessageCache;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandMap;

import java.util.UUID;

@Getter
public final class HNDungeons extends TidePlugin {

    public static HNDungeons instance;

    private final TideConfig storageConfig = this.getYml("storage");
    private final StorageType storageType = StorageType.valueOf(this.storageConfig.getString("storage.type"));

    private TideConfig settingsConfig = this.getYml("settings");

    private MessageCache messageCache = new MessageCache(this.getYml("lang"));

    private Dungeons DungeonsCommand = new Dungeons(this);
    private CommonStorageImpl<UUID, DungeonProfile> storage;
    private CommonStorageImpl<UUID, InstanceProfile> instanceStorage;
    private InstanceManager InstanceManager;

    @Override
    public String pluginName() {
        return "HNDungeons";
    }

    @Override
    public void onEnable() {
        HNDungeons.instance = this;

        TideLogger.console("Loading &fDungeons&b");
        this.loadMessages(this.messageCache, this.getYml("lang"));

        this.loadStorage();

        this.InstanceManager = new InstanceManager(this);
        this.DungeonsCommand.register();
        this.DungeonsCommand.register((CommandMap) new dHelp(this.DungeonsCommand.getPlugin()));

        TideLogger.console("Successfully loaded &fDungeons&b");
    }

    @Override
    public void onDisable() {
        this.storage.write();


    }

    public void onReload() {
        final double start = System.currentTimeMillis();

        this.configs.remove("lang");
        this.configs.remove("settings");

        this.settingsConfig = this.getYml("settings");

        this.messageCache = new MessageCache(this.getYml("lang"));
        this.loadMessages(this.messageCache, this.getYml("lang"));

        this.DungeonsCommand = new Dungeons(this);

        this.DungeonsCommand.register();
        this.DungeonsCommand.register((CommandMap) new dHelp(this.DungeonsCommand.getPlugin()));

        final double elapsed = System.currentTimeMillis() - start;

        Bukkit.getOperators().stream().filter(OfflinePlayer::isOnline).forEach(player -> this.messageCache.sendMessage(player.getPlayer(), "messages.reloaded", new PlaceholderReplacer().addPlaceholder("%time%", Utils.format(elapsed))));
    }

    private void loadStorage() {
        switch (this.storageType) {
            case JSON: {
                this.storage = new CommonStorageImpl<>(new PlayerJsonStorage(this));
                this.instanceStorage = new CommonStorageImpl<>(new InstanceJSONStorage(this));
                break;
            }

            case SQL: {
                this.storage = new CommonStorageImpl<>(new PlayerSQLStorage(this));

                new StorageJoinLeaveListener(this);
                break;
            }
        }

        TideLogger.console("  - Successfully loaded Storage! &8(&f" + this.storageType + "&7 Storage Method&8)");
    }

}
