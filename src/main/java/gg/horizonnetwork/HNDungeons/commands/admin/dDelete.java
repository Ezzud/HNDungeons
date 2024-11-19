package gg.horizonnetwork.HNDungeons.commands.admin;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class dDelete {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dDelete(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.args = args;
        this.execute(p);
    }

    public void execute(Player player) {
        if (!player.hasPermission(this.permission)) {
            ChatUtil.sendPathMessage(player, "no-permission");
            return;
        }

        if(args.length < 2) {
            ChatUtil.sendPathMessage(player, "no-argument");
        }

        InstanceManager m = plugin.getInstanceManager();
        String instanceId = args[1];

        DungeonInstance instance = m.getInstance(UUID.fromString(instanceId));
        if(instance == null) {
            ChatUtil.sendPathMessage(player, "instance.not-found");
            return;
        }
        instance.end();
        ChatUtil.sendMessage(player, ChatUtil.formatGeneralInstanceMessage(player, instance, Objects.requireNonNull(ChatUtil.getPathMessage("instance.ended"))));
    }
}
