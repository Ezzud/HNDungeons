package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Objects;

public class dJoin {

    private final HNDungeons plugin = HNDungeons.getInstance();
    private final String permission;
    private final String[] args;


    public dJoin(Player p, String[] args) {
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

        String worldType = args[1].toLowerCase();
        ConfigurationSection conf = plugin.getConfig().getConfigurationSection("dungeons." + worldType);
        if(conf == null) {
            ChatUtil.sendPathMessage(player, "no-dungeons");
        }

        int level = 1;
        if(args.length > 2) {
            String levelString = args[2];
            try {
                level = Integer.parseInt(levelString);
            } catch(NumberFormatException e) {
                level = 1;
            }

        }

        InstanceManager m = plugin.getInstanceManager();
        DungeonPlayer p = new DungeonPlayer(player);
        p.setOriginalLocation(player.getLocation());
        if(m.isOnInstance(p)) {
            Logger.warn("Unloading old instance of player " + p.getPlayer().getName());
            DungeonInstance d = m.getPlayerInstance(p);
            if(d != null) {
                if(d.getParty().getPlayers().size() == 1) {
                    d.end();
                }
            }
        }

        ChatUtil.sendMessage(player, ChatUtil.formatGeneralMessage(player, worldType, Objects.requireNonNull(ChatUtil.getPathMessage("instance.generating"))));

        Logger.info("Generating new instance of dungeon " + worldType + " for " + p.getPlayer().getName());
        DungeonInstance instance = m.create(worldType, p, 4, level);
        if(instance == null) {
            ChatUtil.sendMessage(player, ChatUtil.formatGeneralMessage(player, worldType, Objects.requireNonNull(ChatUtil.getPathMessage("instance.error"))));
            return;
        }
        ChatUtil.sendMessage(player, ChatUtil.formatGeneralMessage(player, worldType, Objects.requireNonNull(ChatUtil.getPathMessage("instance.done"))));
        instance.start();
        Logger.success("Successfully generated instance of dungeon " + worldType + " for " + p.getPlayer().getName());
        ChatUtil.sendMessage(player, ChatUtil.formatWelcomeMessage(player, instance, Objects.requireNonNull(ChatUtil.getPathMessage("instance.welcome-message"))));
    }
}

