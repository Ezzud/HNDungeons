package gg.horizonnetwork.HNDungeons.storage.json;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.storage.Player;
import gg.techtide.tidelib.patterns.registry.impl.EclipseRegistry;
import gg.techtide.tidelib.revamped.abysslibrary.storage.json.JsonStorage;
import gg.techtide.tidelib.revamped.abysslibrary.utils.file.Files;

import java.util.UUID;

public class PlayerJsonStorage extends JsonStorage<UUID, Player> {

    public PlayerJsonStorage(final HNDungeons plugin) {
        super(Files.file("players.json", plugin), Player.class, new EclipseRegistry<>());
    }

    @Override
    public Player constructValue(final UUID key) {
        return new Player();
    }
}
