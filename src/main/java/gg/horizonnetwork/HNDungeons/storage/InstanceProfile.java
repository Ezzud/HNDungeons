package gg.horizonnetwork.HNDungeons.storage;

import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.techtide.tidelib.revamped.abysslibrary.storage.id.Id;
import lombok.Data;

import java.util.UUID;

@Data
public final class InstanceProfile {

    @Id
    private final UUID uuid;
    private final DungeonInstance instance;

    public InstanceProfile(final UUID uuid, DungeonInstance instance) {
        this.uuid = uuid;
        this.instance = instance;
    }

    private transient boolean loaded;

    public void load() {
        if (this.loaded) {
            return;
        }

        this.loaded = true;
    }
}

