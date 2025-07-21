package gg.horizonnetwork.HNDungeons.listener;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonMob;
import gg.horizonnetwork.HNDungeons.utils.FormatUtil;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import gg.horizonnetwork.HNDungeons.utils.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class DamageListener implements Listener {

    private final HNDungeons plugin;

    public DamageListener(final HNDungeons plugin) {
        this.plugin = plugin;
    }

    public boolean isInvincible(LivingEntity entity) {
        List<MetadataValue> values = entity.getMetadata("isInstanceInvincible");
        return(!values.isEmpty() && values.get(0).value().equals("1"));
    }

    public boolean isFromInstance(LivingEntity entity) {
        List<MetadataValue> values = entity.getMetadata("isFromInstance");
        return(!values.isEmpty() && values.get(0).value().equals("1"));
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        boolean a = plugin.getConfig().getBoolean("enableDamagePopup");
        if(a) {
            if (event instanceof EntityDamageByEntityEvent entityEvent) {
                if(entityEvent.getDamager() instanceof Player) {
                    Entity victim = event.getEntity();
                    if(isFromInstance((LivingEntity) victim)) {
                        Location spawnLocation = RandomUtil.getRandomLocationAround(victim, 0.8);
                        spawnLocation.setY(spawnLocation.getY() + victim.getHeight() + 0.5);
                        Location preTeleportLocation = new Location(victim.getWorld(), spawnLocation.getX(), 0, spawnLocation.getZ());
                        String damages = FormatUtil.formatDbl(event.getDamage());

                        ArmorStand armorStand = (ArmorStand) victim.getWorld().spawnEntity(preTeleportLocation, EntityType.ARMOR_STAND);
                        armorStand.setInvisible(true);
                        armorStand.setGravity(false);
                        armorStand.setCustomName(ChatColor.RED + damages + " ❤");
                        armorStand.setCustomNameVisible(true);
                        armorStand.setInvulnerable(true);
                        armorStand.setMarker(true);
                        armorStand.teleport(spawnLocation);

                        Location endLocation = new Location(spawnLocation.getWorld(), spawnLocation.getX(), spawnLocation.getY() -10, spawnLocation.getZ());

                        Vector vec1 = spawnLocation.toVector();
                        Vector vec2 = endLocation.toVector();

                        Vector move = vec2.subtract(vec1);
                        armorStand.setVelocity(move);

                        Bukkit.getServer().getScheduler().runTaskLater(plugin, armorStand::remove, 20L);
                    }
                }
            }
        }

        boolean b = plugin.getConfig().getBoolean("useVanillaHit");
        if(!event.isCancelled() && !b) {
            double damage = event.getDamage();
            LivingEntity entity = (LivingEntity) event.getEntity();
            if(isFromInstance(entity)) {
                if(!event.isCancelled()) {
                    event.setCancelled(true);
                }
                if(event instanceof EntityDamageByEntityEvent) {
                    if(((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
                        DungeonInstance i = HNDungeons.getInstance().getInstanceManager().getWorldInstance(entity.getWorld());
                        if(i != null) {
                            List<DungeonMob> mobsList = new ArrayList<>(i.getEntities());
                            for(DungeonMob mob : mobsList) {
                                if(mob.getEntity() == null)
                                    continue;
                                if(mob.getEntity().getUniqueId().equals(entity.getUniqueId())) {
                                    if(mob.getHealth() - damage <= 0) {
                                        // Entity death
                                        if(!isInvincible(entity)) {
                                            mob.giveDeathRewards((Player)((EntityDamageByEntityEvent) event).getDamager());
                                            if(mob.isRespawnable()) {
                                                int cooldown = 20 * mob.getConfig().getInt("respawn-data.cooldown-seconds");
                                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                    Logger.info("Spawning a new respawnable mob of " + mob.getName());
                                                    i.summonRespawnableEntity(mob, mob.getConfig());
                                                }, cooldown);
                                            }
                                            mob.despawn();
                                        }
                                        else
                                            mob.setHealth(mob.getMaxHealth());
                                    } else {
                                        if (isInvincible(entity)) {
                                            mob.setHealth(mob.getMaxHealth());
                                        } else {
                                            mob.setHealth(mob.getHealth() - damage);
                                            mob.updateName();
                                        }
                                        mob.giveHitRewards((Player)((EntityDamageByEntityEvent) event).getDamager());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
