package gg.horizonnetwork.HNDungeons.api;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.utils.Logger;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class DungeonExpansion extends PlaceholderExpansion {

    private HNDungeons plugin; //

    public DungeonExpansion( HNDungeons plugin ) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "Ezzud";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "hndungeons";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getRequiredPlugin() {
        return "HN-Dungeons";
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("level")) {
            DungeonPlayer p = new DungeonPlayer(player.getPlayer());
            p.setOfflinePlayer(player);
            if(plugin.getInstanceManager().isOnInstance(p)) {
                DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(p);
                return String.valueOf(i.getLevel());
            } else {
                return " ";
            }
        }

        if (params.equalsIgnoreCase("name")) {
            DungeonPlayer p = new DungeonPlayer(player.getPlayer());
            p.setOfflinePlayer(player);
            if(plugin.getInstanceManager().isOnInstance(p)) {
                DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(p);
                return i.getWorld().getOriginalWorld().getName();
            } else {
                return " ";
            }
        }

        if (params.equalsIgnoreCase("id")) {
            DungeonPlayer p = new DungeonPlayer(player.getPlayer());
            p.setOfflinePlayer(player);
            if(plugin.getInstanceManager().isOnInstance(p)) {
                DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(p);
                return i.getId().toString();
            } else {
                return " ";
            }
        }

        if (params.equalsIgnoreCase("hostname")) {
            DungeonPlayer p = new DungeonPlayer(player.getPlayer());
            p.setOfflinePlayer(player);
            if(plugin.getInstanceManager().isOnInstance(p)) {
                DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(p);
                return i.getHost().getOfflinePlayer().getName();
            } else {
                return " ";
            }
        }

        if (params.equalsIgnoreCase("instance_name")) {
            DungeonPlayer p = new DungeonPlayer(player.getPlayer());
            p.setOfflinePlayer(player);
            if(plugin.getInstanceManager().isOnInstance(p)) {
                DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(p);
                return i.getWorld().getInstanciatedWorld().getName();
            } else {
                return " ";
            }
        }

        if (params.equalsIgnoreCase("state")) {
            DungeonPlayer p = new DungeonPlayer(player.getPlayer());
            p.setOfflinePlayer(player);
            if(plugin.getInstanceManager().isOnInstance(p)) {
                DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(p);
                return i.getState().toString();
            } else {
                return " ";
            }
        }

        return null; //
    }
}
