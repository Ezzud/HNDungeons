package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.utils.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        if(this.originalWorld == null){
            this.originalWorld = new WorldCreator(originalWorldName).createWorld();
            Logger.error("Unable to find world with name " + originalWorldName + " regenerating one...");
        }
        this.originalWorldLocation = originalWorld.getSpawnLocation();

    }

    private void copyFileStructure(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    assert files != null;
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public World copyWorld(World original, String newWorld) {
        copyFileStructure(original.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorld));
        return (new WorldCreator(newWorld).createWorld());
    }

    public void generateWorld() {
        String name = this.originalWorld.getName() +
                "_" +
                UUID.randomUUID();

        originalWorld.save();
        instanciatedWorld = copyWorld(originalWorld, name);
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
