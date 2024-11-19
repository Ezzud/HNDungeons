package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.managers.InviteManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class dJoinParty {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dJoinParty(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.args = args;
        this.execute(p);
    }

    public void execute(Player player) {
        if(args.length < 2) {
            ChatUtil.sendPathMessage(player, "no-argument");
        }

        InstanceManager m = plugin.getInstanceManager();
        DungeonPlayer p = new DungeonPlayer(player);
        DungeonInstance i;
        try {
            UUID id = UUID.fromString(args[1]);
            i = m.getInstance(UUID.fromString(args[1]));
        } catch(IllegalArgumentException e ) {
            return;
        }
        if(i == null) {
            ChatUtil.sendMessage(player, "&cInstance not found");
        }
        assert i != null;
        DungeonPlayer host = i.getHost();
        InviteManager im = new InviteManager(plugin);
        im.accept(host, p);
    }
}
