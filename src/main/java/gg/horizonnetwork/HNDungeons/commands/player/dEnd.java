package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import gg.techtide.tidelib.logger.TideLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Objects;

public class dEnd {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dEnd(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.args = args;
        this.execute(p);
    }

    public void execute(Player player) {
        if(args.length < 1) {
            ChatUtil.sendPathMessage(player, "no-argument");
        }

        InstanceManager m = plugin.getInstanceManager();
        if(!m.isWorldInstance(player.getWorld())) {
            ChatUtil.sendPathMessage(player, "instance.not-an-instance");
            return;
        }


        DungeonInstance instance = m.getWorldInstance(player.getWorld());

        if(!instance.getHost().getOfflinePlayer().getUniqueId().equals(player.getUniqueId())) {
            ChatUtil.sendPathMessage(player, "player.not-host");
            return;
        }

        instance.end();
        ChatUtil.sendMessage(player, ChatUtil.formatGeneralInstanceMessage(player, instance, Objects.requireNonNull(ChatUtil.getPathMessage("instance.ended"))));
    }
}
