package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.managers.InstanceMemberManager;
import gg.horizonnetwork.HNDungeons.types.InstanceState;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DungeonInstance {

    @Getter
    private final InstanceMemberManager party;
    @Getter
    private final InstanceManager manager;
    private final HNDungeons plugin;
    @Getter
    @Setter
    private DungeonWorld world = null;
    @Getter
    @Setter
    private UUID id;
    public int maxPlayers;
    @Getter
    @Setter
    private InstanceState state;
    @Getter
    @Setter
    private int level;
    @Getter
    @Setter
    public DungeonPlayer host;
    @Getter
    private List<DungeonMob> entities = new ArrayList<DungeonMob>();

    public DungeonInstance(InstanceManager instanceManager, int maxPlayers, HNDungeons plugin) {
        this.manager = instanceManager;
        this.party = new InstanceMemberManager(this);
        this.maxPlayers = maxPlayers;
        this.level = 1;
        this.plugin = plugin;
    }

    public void teleportPartyIn() {
        for(DungeonPlayer p : party.getPlayers()) {
            if(p.getPlayer() != null)
                p.getPlayer().teleport(world.getInstanciatedWorld().getSpawnLocation());
        }
    }

    public void teleportPartyOut() {
        for(DungeonPlayer p : party.getPlayers()) {
            if(p.getPlayer() != null) {
                if (p.getPlayer().getWorld().getName().equals(world.getInstanciatedWorld().getName()))
                    p.getPlayer().teleport(p.getOriginalLocation());
            }
        }
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getWorld().getName().equals(this.getWorld().getInstanciatedWorld().getName())) {
                p.teleport(this.getWorld().getOriginalWorld().getSpawnLocation());
            }
        }
    }

    public void teleportPlayerOut(DungeonPlayer p) {
        if(p.getPlayer() != null)
            p.getPlayer().teleport(p.getOriginalLocation());
    }

    public void teleportPlayerIn(DungeonPlayer p) {
        if(p.getPlayer() != null)
            p.getPlayer().teleport(world.getInstanciatedWorld().getSpawnLocation());
    }

    public void start() {
        teleportPartyIn();
        summonEntities();
    }

    public void end() {
        teleportPartyOut();
        this.manager.delete(this);
    }

    public void summonEntities() {
        List<DungeonMob> entities = new ArrayList<>();
        ConfigurationSection mobsToSpawn = plugin.getConfig().getConfigurationSection("dungeons." + this.world.getOriginalWorld().getName() + ".mobs");
        double levelMultiplier = plugin.getConfig().getDouble("levelMultiplier");
        if(mobsToSpawn != null) {
            Set<String> allMobs = mobsToSpawn.getKeys(true);
            for (String mobKey : allMobs) {
                ConfigurationSection mob = mobsToSpawn.getConfigurationSection(mobKey);
                if(mob != null) {
                    String type = mob.getString("type");
                    String name = mob.getString("name");
                    double health = mob.getDouble("health");
                    boolean invincible = mob.getBoolean("invincible");
                    boolean ai = mob.getBoolean("ai");
                    ConfigurationSection location = mob.getConfigurationSection("location");
                    if(location != null) {
                        Location spawnLob = new Location(this.getWorld().getInstanciatedWorld(),
                                location.getDouble("x"),
                                location.getDouble("y"),
                                location.getDouble("z"),
                                location.getLong("yaw"),
                                location.getLong("pitch"));

                        DungeonMob entity = new DungeonMob(EntityType.valueOf(type), name, this);
                        this.entities.add(entity);
                        entity.spawn(spawnLob);
                        Logger.info("Spawning entity &b" + ChatUtil.formatDungeonMobsName(entity, entity.getName()) + " &ein world " + spawnLob.getWorld().getName());
                        if(!ai) entity.disableAI();
                        if(invincible) {
                            entity.makeInvincible();
                        }
                        if(health > 0) entity.setMaxHealth(health + (levelMultiplier * level));
                    }
                }

            }
        }

    }

}
