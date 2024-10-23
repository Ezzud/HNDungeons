package gg.horizonnetwork.HNDungeons.storage.sql;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.storage.DungeonProfile;
import gg.techtide.tidelib.revamped.abysslibrary.storage.credentials.Credentials;
import gg.techtide.tidelib.revamped.abysslibrary.storage.sql.SQLStorage;

import java.util.UUID;

public class PlayerSQLStorage extends SQLStorage<UUID, DungeonProfile> {

    public PlayerSQLStorage(final HNDungeons plugin) {
        super(UUID.class, DungeonProfile.class, "hndungeons", Credentials.from(plugin.getStorageConfig()));
    }

    @Override
    public DungeonProfile constructValue(final UUID key) {
        return new DungeonProfile(key);
    }
}
