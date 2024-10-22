package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InstanceMemberManager {

    private final DungeonInstance instance;
    @Getter
    private List<DungeonPlayer> players;

    public InstanceMemberManager(DungeonInstance instance){
        this.players = new ArrayList<DungeonPlayer>();
        this.instance = instance;
    }

    public void add(DungeonPlayer player){
        this.players.add(player);
        player.setInstance(instance);
    }

    public void remove(DungeonPlayer player){
        this.players.remove(player);
        player.setInstance(null);
    }

    public void clear() {
        for(DungeonPlayer player : players){
            player.setInstance(null);
        }
        this.players.clear();
    }

}
