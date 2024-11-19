package gg.horizonnetwork.HNDungeons.listener;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.utils.RandomUtil;
import gg.techtide.tidelib.logger.TideLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public class DamageListener implements Listener {

    private static final Pattern REGEX = compile("(\\d+(?:\\.\\d+)?)([KMG]?)");
    private static final String[] KMG = new String[] {"", "K", "M", "G", "T", "T", "T", "T", "T", "T", "Q"};
    private final HNDungeons plugin;

    public DamageListener(final HNDungeons plugin) {
        this.plugin = plugin;
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
        boolean a = plugin.getConfig().getBoolean("enableDamagePopup");
        if(a) {
            if (event instanceof EntityDamageByEntityEvent entityEvent) {
                if(entityEvent.getDamager() instanceof Player) {
                    Entity victim = event.getEntity();
                    Location spawnLocation = RandomUtil.getRandomLocationAround(victim, 0.8);
                    spawnLocation.setY(spawnLocation.getY() + 2.4);
                    String damages = formatDbl(event.getDamage());

                    ArmorStand armorStand = (ArmorStand) victim.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
                    armorStand.setInvisible(true);
                    armorStand.setGravity(false);
                    armorStand.setCustomName(ChatColor.RED + damages + " ❤");
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvulnerable(true);
                    armorStand.setMarker(true);

                    Bukkit.getServer().getScheduler().runTaskLater(plugin, armorStand::remove, 20L);
                }
            }
        }

        List<MetadataValue> values = event.getEntity().getMetadata("isFromInstance");
        if (!values.isEmpty() && values.get(0).value().equals("1")) {
            LivingEntity en = (LivingEntity) event.getEntity();
            en.setHealth(Objects.requireNonNull(en.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }
}
