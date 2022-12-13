package io.github.lunaiskey.lunixprison.leaderboards;

import java.util.UUID;

public abstract class BaseEntry {
    private UUID uuid;
    private String name;

    public BaseEntry(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }
}
