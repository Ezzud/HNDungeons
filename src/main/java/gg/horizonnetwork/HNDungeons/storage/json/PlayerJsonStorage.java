package gg.horizonnetwork.HNDungeons.storage.json;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.storage.DungeonProfile;
import org.bukkit.entity.Player;
import gg.techtide.tidelib.patterns.registry.impl.EclipseRegistry;
import gg.techtide.tidelib.revamped.abysslibrary.storage.json.JsonStorage;
import gg.techtide.tidelib.revamped.abysslibrary.utils.file.Files;

import java.util.UUID;

public class PlayerJsonStorage extends JsonStorage<UUID, DungeonProfile> {

    public PlayerJsonStorage(final HNDungeons plugin) {
        super(Files.file("players.json", plugin), DungeonProfile.class);
    }

    @Override
    public DungeonProfile constructValue(final UUID key) {
        return new DungeonProfile(key);
    }
}