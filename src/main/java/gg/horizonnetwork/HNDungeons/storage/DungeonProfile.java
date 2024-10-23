package gg.horizonnetwork.HNDungeons.storage;

import gg.techtide.tidelib.revamped.abysslibrary.storage.id.Id;
import lombok.Data;

import java.util.UUID;

@Data
public final class DungeonProfile {

    @Id
    private final UUID uuid;

    public DungeonProfile(final UUID uuid) {
        this.uuid = uuid;
    }

    private transient boolean loaded;

    public void load() {
        if (this.loaded) {
            return;
        }

        this.loaded = true;
    }
}

