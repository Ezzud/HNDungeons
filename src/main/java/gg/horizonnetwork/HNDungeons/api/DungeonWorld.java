package gg.horizonnetwork.HNDungeons.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
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
    private World instanciatedWorld;

    public void generateWorld() {
        String name = this.originalWorld.getName() +
                "_" +
                UUID.randomUUID();
        WorldCreator wc = new WorldCreator(name);
        WorldCreator nwc = wc.copy(originalWorld);

        instanciatedWorld = nwc.createWorld();
    }

    public void delete() {
        File folder = instanciatedWorld.getWorldFolder();
        Bukkit.unloadWorld(this.instanciatedWorld, false);
        folder.delete()
    }
}
