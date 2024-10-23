package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.api.DungeonWorld;
import gg.horizonnetwork.HNDungeons.storage.InstanceProfile;
import gg.horizonnetwork.HNDungeons.types.InstanceState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InstanceManager {

    private final HNDungeons plugin;
    @Getter
    private List<DungeonInstance> instances;

    public InstanceManager(HNDungeons plugin) {
        this.instances = new ArrayList<DungeonInstance>();
        this.plugin = plugin;
    }


    public DungeonInstance create(String instanceType, DungeonPlayer host, int maxPlayers) {
        DungeonWorld world = new DungeonWorld(instanceType);
        world.generateWorld();

        DungeonInstance instance = new DungeonInstance(this, maxPlayers, plugin);
        instance.setState(InstanceState.GENERATING);
        instance.getParty().add(host);
        instance.setId(UUID.randomUUID());
        instance.setWorld(world);
        instance.setState(InstanceState.WAITING);
        addInstance(instance);

        InstanceProfile profile = new InstanceProfile(instance.getId(), instance);
        profile.load();
        plugin.getInstanceStorage().save(profile);

        return instance;
    }

    public void delete(DungeonInstance instance) {
        instance.teleportPartyOut();
        instance.getWorld().delete();
        removeInstance(instance);
        plugin.getInstanceStorage().remove(instance.getId());
    }

    public DungeonInstance get(UUID id) {
        InstanceProfile profile = plugin.getInstanceStorage().get(id);
        if (profile == null) return null;
        profile.load();
        return profile.getInstance();
    }

    public DungeonInstance getFromCache(UUID id) {
        for(DungeonInstance instance : instances) {
            if (instance.getId().equals(id)) {
                return instance;
            }
        }
        return null;
    }

    public void addInstance(DungeonInstance instance) {
        instances.add(instance);
    }

    public void removeInstance(DungeonInstance instance) {
        instances.remove(instance);
    }
}
