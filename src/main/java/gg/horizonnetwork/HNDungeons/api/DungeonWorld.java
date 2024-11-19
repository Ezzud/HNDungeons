package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.utils.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;
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
        spawnLocation = new Location(
                instanciatedWorld,
                this.originalWorldLocation.getX(),
                this.originalWorldLocation.getY(),
                this.originalWorldLocation.getZ(),
                this.originalWorldLocation.getYaw(),
                this.originalWorldLocation.getPitch()
        );
        instanciatedWorld.setSpawnLocation(spawnLocation);
        instanciatedWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
    }

    public void delete() {
        File folder = instanciatedWorld.getWorldFolder();
        boolean unloaded = Bukkit.unloadWorld(this.instanciatedWorld, false);
        boolean success = true;
        if(unloaded) {
            try {
                FileUtils.deleteDirectory(folder);
            } catch(IOException ioe) {
                success = false;
                ioe.printStackTrace();
                Logger.error("Failed to delete world " + this.instanciatedWorld.getName() + ": " + ioe.getMessage());
            }
            if(success)
                Logger.success("&aWorld " + this.instanciatedWorld.getName() + " deleted");
        } else {
            Logger.warn("Skipped deletion of world " + this.instanciatedWorld.getName() + " because he is not unloaded");
        }


    }

}
