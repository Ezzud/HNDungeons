package gg.horizonnetwork.HNDungeons.storage;

import gg.techtide.tidelib.revamped.abysslibrary.storage.id.Id;
import lombok.Data;

import java.util.UUID;

@Data
public class Player {

    @Id
    private final UUID uuid;
    private boolean doubleJumpEnabled;

    public Player() {
        this.uuid = uuid;
        this.doubleJumpEnabled = false;
    }

    private transient boolean loaded;

    public void load() {
        if (this.loaded) {
            return;
        }

        this.loaded = true;
    }
}
