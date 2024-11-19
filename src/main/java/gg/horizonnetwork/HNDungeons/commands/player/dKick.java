package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.managers.InviteManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class dKick {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dKick(Player p, String[] args) {
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
            DungeonInstance i = m.getPlayerInstance(p);
            if(m.isOnInstance(t)) {
                if(m.getPlayerInstance(p).getId().equals(m.getPlayerInstance(t).getId())) {
                    if(i.getHost().getOfflinePlayer().getUniqueId().equals(player.getUniqueId())) {
                        if(t.getOfflinePlayer().getUniqueId().equals(i.getHost().getOfflinePlayer().getUniqueId())) {
                            ChatUtil.sendMessage(player, "&cYou can't kick yourself");
                        }
                        i.getParty().remove(t);
                        i.teleportPlayerOut(t);
                        ChatUtil.sendPartyMessage(i, "&e" + target.getName() + " &cgot kicked from the party by &e" + player.getName());
                    } else {
                        ChatUtil.sendMessage(target, "&cYou are not the host of the instance");
                    }
                } else {
                    ChatUtil.sendMessage(target, "&e" + target.getName()  + " &cis not on the instance");
                }
            } else {
                ChatUtil.sendMessage(target, "&e" + target.getName()  + " &cis not on the instance");
            }
        } else {
            ChatUtil.sendMessage(player, "&aYou are not on an instance");
        }}
}
