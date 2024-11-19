package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.managers.InviteManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class dInvite {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dInvite(Player p, String[] args) {
        this.permission = plugin.getConfig().getString("command.permission");
        this.args = args;
        this.execute(p);
    }

    public void execute(Player player) {
        if(args.length < 2) {
            ChatUtil.sendPathMessage(player, "no-argument");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if(target == null) {
            ChatUtil.sendMessage(player, "&cPlayer not found");
            return;
        }

        InstanceManager m = plugin.getInstanceManager();
        DungeonPlayer p = new DungeonPlayer(player);

        if(m.isOnInstance(p)) {
            InviteManager im = new InviteManager(plugin);
            DungeonPlayer t = new DungeonPlayer(target);
            if(m.isOnInstance(t)) {
                ChatUtil.sendMessage(target, "&e" + target.getName()  + " &cis already on a party or an instance");
                return;
            }
            im.send(p, t);
        } else {
            ChatUtil.sendMessage(player, "&aYou are not on an instance");
        }
    }
}
