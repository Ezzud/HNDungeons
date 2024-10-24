package gg.horizonnetwork.HNDungeons.listener;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.utils.RandomUtil;
import gg.techtide.tidelib.logger.TideLogger;
import gg.techtide.tidelib.revamped.abysslibrary.listener.SimpleTideListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public class DamageListener extends SimpleTideListener<HNDungeons> {

    private static final Pattern REGEX = compile("(\\d+(?:\\.\\d+)?)([KMG]?)");
    private static final String[] KMG = new String[] {"", "K", "M", "G", "T", "T", "T", "T", "T", "T", "Q"};

    public DamageListener(final HNDungeons plugin) {
        super(plugin);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    static String formatDbl(double d) {
        double d2 = round(d, 2);
        int i = 0;
        while (d2 >= 1000) { i++; d2 /= 1000; }
        return d2 + KMG[i];
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        boolean a = plugin.getSettingsConfig().getBoolean("enableDamagePopup");
        if(a) {
            if (event instanceof EntityDamageByEntityEvent entityEvent) {
                if(entityEvent.getDamager() instanceof Player) {
                    Entity victim = event.getEntity();
                    Location spawnLocation = RandomUtil.getRandomLocationAround(victim, 0.3);
                    String damages = formatDbl(event.getDamage());
                    ArmorStand armorStand = (ArmorStand) victim.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
                    armorStand.setGravity(false);
                    armorStand.setInvisible(true);
                    armorStand.setCustomName(ChatColor.RED + damages + " ❤");
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvulnerable(true);

                    TideLogger.console("Vector " + victim.getVelocity().getX() + " " + victim.getVelocity().getY() + " " + victim.getVelocity().getZ());
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, armorStand::remove, 20L);
                }
            }
        }

    }
}
