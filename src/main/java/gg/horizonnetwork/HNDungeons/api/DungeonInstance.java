package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.managers.InstanceMemberManager;
import gg.horizonnetwork.HNDungeons.types.InstanceState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class DungeonInstance {

    @Getter
    private final InstanceMemberManager party;
    @Getter
    private final InstanceManager manager;
    @Getter
    @Setter
    private DungeonWorld world = null;
    @Setter
    private UUID id;
    public int maxPlayers;
    @Getter
    @Setter
    private InstanceState state;

    public DungeonInstance(InstanceManager instanceManager, int maxPlayers) {
        this.manager = instanceManager;
        this.party = new InstanceMemberManager(this);
        this.maxPlayers = maxPlayers;
    }

    private UUID getId() {
        return id;
    }

    public void teleportPartyOut() {
        for(DungeonPlayer p : party.getPlayers()) {
            p.getPlayer().teleport(p.getOriginalLocation());
        }
    }

    public void end() {
        this.manager.delete(this);
    }

}
