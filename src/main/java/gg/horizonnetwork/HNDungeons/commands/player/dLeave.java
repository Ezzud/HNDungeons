package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import gg.techtide.tidelib.logger.TideLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class dLeave {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dLeave(Player p, String[] args) {
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
            ChatUtil.sendMessage(player, "&cThis world is not an instanciated world");
            return;
        }
        DungeonInstance instance = m.getWorldInstance(player.getWorld());
        if(instance.getParty().getPlayers().size() == 1) {
            ChatUtil.sendMessage(player, "&cYou are alone in the party, ending it");
            instance.end();
        } else {
            DungeonPlayer p = new DungeonPlayer(player);
            p.setOriginalLocation(player.getLocation());
            instance.getParty().remove(p);
            instance.getManager().getStorage().save(instance);
            DungeonPlayer newHost = instance.getParty().getPlayers().get(0);
            instance.setHost(newHost);
            ChatUtil.sendMessage(player, "&aYou left the party and gave leadership to " + newHost.getPlayer().getName());
            ChatUtil.sendPartyMessage(instance, "&e" + player.getName() + "&c left the party\n&a" + newHost.getPlayer().getName() + " &eis now the host of the instance");
            instance.teleportPlayerOut(p);
        }
    }
}
