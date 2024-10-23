package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.managers.InstanceMemberManager;
import gg.horizonnetwork.HNDungeons.types.InstanceState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

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

    public DungeonInstance(InstanceManager instanceManager, int maxPlayers, HNDungeons plugin) {
        this.manager = instanceManager;
        this.party = new InstanceMemberManager(this);
        this.maxPlayers = maxPlayers;
        this.plugin = plugin;
    }

    public void teleportPartyIn() {
        for(DungeonPlayer p : party.getPlayers()) {
            p.getPlayer().teleport(world.getSpawnLocation());
        }
    }

    public void teleportPartyOut() {
        for(DungeonPlayer p : party.getPlayers()) {
            p.getPlayer().teleport(p.getOriginalLocation());
        }
    }

    public void start() {
        teleportPartyIn();
        summonEntities();
    }

    public void end() {
        this.manager.delete(this);
    }

    public void summonEntities() {
        List<DungeonMob> entities = new ArrayList<>();
        ConfigurationSection mobsToSpawn = plugin.getConfig().getConfigurationSection("dungeons." + this.world.getOriginalWorld().getName() + ".mobs");
        if(mobsToSpawn != null) {
            Set<String> allMobs = mobsToSpawn.getKeys(true);
            for (String mobKey : allMobs) {
                ConfigurationSection mob = mobsToSpawn.getConfigurationSection(mobKey);
                if(mob != null) {
                    String type = mob.getString("type");
                    String name = mob.getString("name");
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

                        DungeonMob entity = new DungeonMob(EntityType.valueOf(type), name);
                        entity.spawn(spawnLob);
                        if(!ai) entity.disableAI();
                        if(invincible) entity.makeInvincible();
                    }


                }

            }
        }

    }

}
