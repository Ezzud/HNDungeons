package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.Objects;

public class dInfo {

    private final HNDungeons plugin = HNDungeons.getInstance();


    public dInfo(Player p, String[] args) {
        String permission = plugin.getConfig().getString("command.permission");
        this.execute(p);
    }

    public void execute(Player player) {
        InstanceManager m = plugin.getInstanceManager();
        DungeonPlayer p = new DungeonPlayer(player);

        if(m.getWorldInstance(player.getWorld()) != null) {
            DungeonInstance i = m.getWorldInstance(player.getWorld());
            StringBuilder party = new StringBuilder();
            for(DungeonPlayer member : i.getParty().getPlayers()) {
                party.append("&6- &c").append(member.getOfflinePlayer().getName()).append("\n");
            }
            String content = ChatUtil.formatWelcomeMessage(player, i, Objects.requireNonNull(ChatUtil.getPathMessage("instance.info-message")));
            ChatUtil.sendMessage(player, content);
        } else {
            ChatUtil.sendPathMessage(player, "instance.not-an-instance");
        }
    }
}
