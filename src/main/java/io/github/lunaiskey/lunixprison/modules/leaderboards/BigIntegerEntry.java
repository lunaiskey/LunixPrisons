package io.github.lunaiskey.lunixprison.modules.leaderboards;

import java.math.BigInteger;
import java.util.UUID;

public class BigIntegerEntry extends BaseEntry{
    private BigInteger value;

    public BigIntegerEntry(UUID uuid, String name, BigInteger value) {
        super(uuid, name);
        this.value = value;
    }


    public BigInteger getValue() {
        return value;
    }
}
