package gg.horizonnetwork.HNDungeons.storage.json;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.storage.DungeonProfile;
import gg.horizonnetwork.HNDungeons.storage.InstanceProfile;
import gg.techtide.tidelib.revamped.abysslibrary.storage.json.JsonStorage;
import gg.techtide.tidelib.revamped.abysslibrary.utils.file.Files;

import java.util.UUID;

public class InstanceJSONStorage extends JsonStorage<UUID, InstanceProfile>  {
    public InstanceJSONStorage(final HNDungeons plugin) {
        super(Files.file("instances.json", plugin), InstanceProfile.class);
    }

    public InstanceProfile constructValue(final UUID key, DungeonInstance instance) {
        return new InstanceProfile(key, instance);
    }
}
