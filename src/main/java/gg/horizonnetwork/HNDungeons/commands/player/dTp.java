package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class dTp {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dTp(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.args = args;
        this.execute(p);
    }

    public void execute(Player player) {
        if(args.length < 1) {
            ChatUtil.sendPathMessage(player, "no-argument");
        }

        InstanceManager m = plugin.getInstanceManager();
        DungeonPlayer p = new DungeonPlayer(player);

        if(m.isOnInstance(p)) {
            DungeonInstance i = m.getPlayerInstance(p);
            player.teleport(i.getWorld().getInstanciatedWorld().getSpawnLocation());
            ChatUtil.sendPathMessage(player, "instance.teleported");
        } else {
            ChatUtil.sendPathMessage(player, "instance.no-instance");
        }
    }
}
