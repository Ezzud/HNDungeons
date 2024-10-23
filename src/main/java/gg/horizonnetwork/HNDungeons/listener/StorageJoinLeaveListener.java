package gg.horizonnetwork.HNDungeons.listener;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.storage.DungeonProfile;
import gg.techtide.tidelib.revamped.abysslibrary.listener.SimpleTideListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StorageJoinLeaveListener extends SimpleTideListener<HNDungeons> {

    public StorageJoinLeaveListener(final HNDungeons plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final DungeonProfile profile = this.plugin.getStorage().get(player.getUniqueId());

        if (profile.isLoaded()) {
            return;
        }

        profile.load();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final DungeonProfile profile = this.plugin.getStorage().get(player.getUniqueId());

        if (!profile.isLoaded()) {
            return;
        }

        profile.setLoaded(false);

        this.plugin.getStorage().save(profile);
        this.plugin.getStorage().cache().unregister(player.getUniqueId());
    }
}
