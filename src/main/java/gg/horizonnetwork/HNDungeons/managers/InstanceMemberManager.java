package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;

import java.util.ArrayList;
import java.util.List;

public class InstanceMemberManager {

    private List<DungeonPlayer> players;

    public InstanceMemberManager(){
        this.players = new ArrayList<DungeonPlayer>();
    }

    public void add(DungeonPlayer player){
        this.players.add(player);
    }

    public void remove(DungeonPlayer player){
        this.players.remove(player);
    }

    public void clear() {
        this.players.clear();
    }

    public List<DungeonPlayer> getPlayers(){
        return this.players;
    }

}
