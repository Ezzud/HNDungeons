package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.api.DungeonWorld;
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

        DungeonInstance instance = new DungeonInstance(this, maxPlayers);
        instance.setState(InstanceState.GENERATING);
        instance.getParty().add(host);
        instance.setId(UUID.randomUUID());
        instance.setWorld(world);
        instance.setState(InstanceState.WAITING);
        addInstance(instance);

        return instance;
    }

    public void delete(DungeonInstance instance) {
        instance.teleportPartyOut();
        instance.getWorld().delete();
        removeInstance(instance);
    }

    public void addInstance(DungeonInstance instance) {
        instances.add(instance);
    }

    public void removeInstance(DungeonInstance instance) {
        instances.remove(instance);
    }
}
