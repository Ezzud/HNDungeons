package gg.horizonnetwork.HNDungeons.commands.admin;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.managers.ConfigManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import org.bukkit.entity.Player;

public class dReload {
    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dReload(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.args = args;
        this.execute(p);
    }

    public void execute( Player player ) {
        if (!player.hasPermission(this.permission)) {
            ChatUtil.sendPathMessage(player, "no-permission");
            return;
        }
        Logger.info("Reloading configuration...");
        plugin.reloadConfig();
        plugin.saveConfig();
        ConfigManager.saveMessages();
        ConfigManager.saveData();
        HNDungeons.messageCache = ConfigManager.getMessages();
        HNDungeons.playerCache = ConfigManager.getData();
        Logger.success("Configuration successfully reloaded");
        ChatUtil.sendPathMessage(player, "reloaded");
    }
}
