package gg.horizonnetwork.HNDungeons.api;

import gg.techtide.tidelib.TideLibSpigot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.UUID;

public class DungeonWorld {

    @Getter
    @Setter
    private World originalWorld;
    @Getter
    @Setter
    private Location originalWorldLocation;

    @Getter
    @Setter
    private World instanciatedWorld;
    @Getter
    @Setter
    private Location spawnLocation;

    public DungeonWorld(String originalWorldName) {
        this.originalWorld = Bukkit.getWorld(originalWorldName);
        this.originalWorldLocation = originalWorld.getSpawnLocation();
    }

    public void generateWorld() {
        String name = this.originalWorld.getName() +
                "_" +
                UUID.randomUUID();
        WorldCreator wc = new WorldCreator(name);
        WorldCreator nwc = wc.copy(originalWorld);

        instanciatedWorld = nwc.createWorld();
        spawnLocation = originalWorldLocation;
        spawnLocation.setWorld(instanciatedWorld);
    }

    public void delete() {
        File folder = instanciatedWorld.getWorldFolder();
        Bukkit.unloadWorld(this.instanciatedWorld, false);
        folder.delete();
    }
}
