package io.github.lunaiskey.lunixprison.leaderboards;

import java.util.UUID;

public class LongEntry extends BaseEntry {
    private long value;

    public LongEntry(UUID uuid, String name, long value) {
        super(uuid, name);
        this.value = value;
    }


    public long getValue() {
        return value;
    }
}
