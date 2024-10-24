package gg.horizonnetwork.HNDungeons.utils;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RandomUtil {
    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static double getRandomDouble(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static Block getRandomBlockAround(Entity player, int radius) {
        Location playerLoc = player.getLocation();
        double pX = playerLoc.getX();
        double pY = playerLoc.getY();
        double pZ = playerLoc.getZ();

        for (int x = -(radius); x <= radius; x ++)
        {
            for (int y = -(radius); y <= radius; y ++)
            {
                for (int z = -(radius); z <= radius;)
                {
                    Block b = player.getWorld().getBlockAt((int)pX+x, (int)pY+y, (int)pZ+z);
                    return(b);
                }
            }
        }
        return null;
    }

    public static Location getRandomLocationAround(Entity player, double radius) {
        Location playerLoc = player.getLocation();
        double pX = playerLoc.getX();
        double pY = playerLoc.getY();
        double pZ = playerLoc.getZ();

        return(new Location(player.getWorld(), pX+getRandomDouble(-radius, radius), pY, pZ+getRandomDouble(-radius, radius)));
    }

}
