package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.api.DungeonWorld;
import gg.horizonnetwork.HNDungeons.storage.InstanceStorageManager;
import gg.horizonnetwork.HNDungeons.types.InstanceState;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InstanceManager {

    private final HNDungeons plugin;
    @Getter
    private final InstanceStorageManager storage;
    @Getter
    private List<DungeonInstance> instances;

    public InstanceManager(HNDungeons plugin) {
        this.instances = new ArrayList<>();
        this.plugin = plugin;
        this.storage = new InstanceStorageManager(plugin);
        this.loadFromData();
    }


    public DungeonInstance create(String instanceType, DungeonPlayer host, int maxPlayers, int level) {
        DungeonWorld world = new DungeonWorld(instanceType);
        world.generateWorld();

        DungeonInstance instance = new DungeonInstance(this, maxPlayers, plugin);
        host.setInstance(instance);
        instance.setState(InstanceState.GENERATING);
        instance.getParty().add(host);
        instance.setHost(host);
        instance.setId(UUID.randomUUID());
        instance.setLevel(level);
        instance.setWorld(world);
        instance.setState(InstanceState.WAITING);
        addInstance(instance);

        return instance;
    }

    public void delete(DungeonInstance instance) {
        Logger.warn("Instance " + instance.getWorld().getInstanciatedWorld().getName() + " will be deleted in 8 seconds...");
        instance.setState(InstanceState.STOPPED);
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            Logger.info("Deleting instance " + instance.getWorld().getInstanciatedWorld().getName());
            instance.getWorld().delete();
        }, 200L);

        removeInstance(instance);
    }

    public boolean isOnInstance(DungeonPlayer p) {
        for(DungeonInstance instance : instances) {
            for(DungeonPlayer pl : instance.getParty().getPlayers()) {
                if(pl.getOfflinePlayer().getUniqueId().equals(p.getOfflinePlayer().getUniqueId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWorldInstance(World world) {
        for(DungeonInstance instance : instances) {
            if(instance.getWorld().getInstanciatedWorld().getName().equals(world.getName())) return true;
        }
        return false;
    }

    public DungeonInstance getWorldInstance(World world) {
        for(DungeonInstance instance : instances) {
            if(instance.getWorld().getInstanciatedWorld().getName().equals(world.getName())) return instance;
        }
        return null;
    }

    public DungeonInstance getPlayerInstance(DungeonPlayer p) {
        for(DungeonInstance instance : instances) {
            for(DungeonPlayer pl : instance.getParty().getPlayers()) {
                if(pl.getOfflinePlayer().getUniqueId().equals(p.getOfflinePlayer().getUniqueId())) {
                    return instance;
                }
            }
        }
        return null;
    }

    public DungeonInstance getInstance(UUID id) {
        for(DungeonInstance instance : instances) {
            if (instance.getId().equals(id)) {
                return instance;
            }
        }
        return null;
    }

    public void addInstance(DungeonInstance instance) {
        storage.save(instance);
        instances.add(instance);
    }

    public void removeInstance(DungeonInstance instance) {
        storage.delete(instance);
        instances.remove(instance);
    }

    public void loadFromData() {
        List<ConfigurationSection> data = storage.getInstanceData();
        for (ConfigurationSection section : data) {
            DungeonInstance instance = new DungeonInstance(this, 4, plugin);
            instance.setId(UUID.fromString(section.getName()));
            instance.setLevel(section.getInt("level"));
            DungeonWorld world = new DungeonWorld(section.getString("world.original"));
            String worldName = section.getString("world.instance");
            assert worldName != null;
            World instanceWorld = new WorldCreator(worldName).createWorld();
            assert instanceWorld != null;
            Logger.info("Loading instance " + instanceWorld.getName());

            Player player = Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(section.getString("host"))));
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(section.getString("host"))));
            DungeonPlayer p = new DungeonPlayer(player);
            p.setInstance(instance);
            p.setOfflinePlayer(offlinePlayer);
            for(String s : Objects.requireNonNull(section.getConfigurationSection("party")).getKeys(false)) {
                OfflinePlayer offlinePlayerMember = Bukkit.getOfflinePlayer(UUID.fromString(s));
                Player playerMember = offlinePlayerMember.getPlayer();
                ConfigurationSection playerData = section.getConfigurationSection("party." + s + ".originalLocation");
                DungeonPlayer pl = new DungeonPlayer(playerMember);
                pl.setOfflinePlayer(offlinePlayerMember);
                pl.setInstance(instance);
                assert playerData != null;
                World originalWorld  = Bukkit.getWorld(Objects.requireNonNull(playerData.getString("world")));
                if(originalWorld == null) {
                    originalWorld = Bukkit.getWorlds().get(0);
                }
                pl.setOriginalLocation(
                        new Location(
                                originalWorld,
                                playerData.getDouble("x"),
                                playerData.getDouble("y"),
                                playerData.getDouble("z"),
                                playerData.getLong("yaw"),
                                playerData.getLong("pitch")
                        )
                );
                if(pl.getOfflinePlayer().getUniqueId().equals(p.getOfflinePlayer().getUniqueId())) {
                    p.setOriginalLocation(pl.getOriginalLocation());
                }

                instance.getParty().add(pl);
            }

            instance.setHost(p);
            world.setInstanciatedWorld(instanceWorld);
            instance.setWorld(world);
            instance.summonEntities();
            instances.add(instance);
            Logger.info("Successfully imported " + instanceWorld.getName());
        }
        Logger.success("Loaded " + instances.size() + " instances from cache");
    }

}
