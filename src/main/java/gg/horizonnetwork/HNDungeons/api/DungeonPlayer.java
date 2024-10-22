package gg.horizonnetwork.HNDungeons.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DungeonPlayer {
    @Getter
    private final Player player;
    @Getter
    @Setter
    private DungeonInstance instance;
    @Getter
    public final Location originalLocation;

    public DungeonPlayer(Player p) {
        this.player = p;
        this.originalLocation = p.getLocation();
    }

}
