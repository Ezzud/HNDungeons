package gg.horizonnetwork.HNDungeons.api;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DungeonPlayer {
    @Getter
    private final Player player;
    private DungeonInstance instance;
    @Getter
    public final Location originalLocation;

    public DungeonPlayer(Player p) {
        this.player = p;
        this.originalLocation = p.getLocation();
    }

    private DungeonInstance getInstance() {
        return instance;
    }

    private void setInstance(DungeonInstance instance) {
        this.instance = instance;
    }

}
