package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.Objects;

public class dHelp {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;


    public dHelp(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.execute(p);
    }

    public void execute(Player player) {
        String content = ChatUtil.getPathMessage("help.header") + ChatUtil.getPathMessage("help.user-commands");
        if(player.hasPermission(permission)) {
            content += ChatUtil.getPathMessage("help.admin-commands");
        }
        content += ChatUtil.getPathMessage("help.footer");
        ChatUtil.sendMessage(player, content);
    }
}
