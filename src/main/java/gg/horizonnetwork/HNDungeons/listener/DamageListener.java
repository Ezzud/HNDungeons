package gg.horizonnetwork.HNDungeons.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
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

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
                    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

                    PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);

                    // Entity ID
                    int entityID = new Random().nextInt(9999);
                    packet.getIntegers().write(0, entityID);
                    // Entity Type
                    packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
                    packet.getIntegers().write(7, 0);
                    packet.getUUIDs().write(0, UUID.randomUUID());
                    // Set location
                    packet.getDoubles().write(1, spawnLocation.getY());
                    packet.getDoubles().write(0, spawnLocation.getX());
                    packet.getDoubles().write(2, spawnLocation.getZ());

                    try {
                        protocolManager.sendServerPacket((Player)entityEvent.getDamager(), packet);
                        ((Player)entityEvent.getDamager()).sendMessage("show78");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    /*
                    ArmorStand armorStand = (ArmorStand) victim.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
                    armorStand.setGravity(false);
                    armorStand.setInvisible(true);
                    armorStand.setCustomName(ChatColor.RED + damages + " ❤");
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvulnerable(true);
                    */
                    TideLogger.console("Vector " + victim.getVelocity().getX() + " " + victim.getVelocity().getY() + " " + victim.getVelocity().getZ());
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                        PacketContainer delpacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
                        List<Integer> entityIDs = new ArrayList<>();
                        entityIDs.add(entityID);
                        delpacket.getIntLists().write(0, entityIDs);

                        try {
                            protocolManager.sendServerPacket((Player) entityEvent.getDamager(), delpacket);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }, 20L);
                }
            }
        }

    }
}
