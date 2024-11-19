package gg.horizonnetwork.HNDungeons.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DungeonPlayer {
    @Setter
    private Player player;
    @Getter
    @Setter
    private OfflinePlayer offlinePlayer;
    @Getter
    @Setter
    private DungeonInstance instance;
    @Getter
    @Setter
    public Location originalLocation;

    public DungeonPlayer(Player p) {
        this.player = p;
        if(p != null) {
            this.offlinePlayer = Bukkit.getOfflinePlayer(p.getUniqueId());
        }
    }

    public Player getPlayer() {
        if(this.player == null) {
            this.player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
        }
        return this.player;
    }

}
