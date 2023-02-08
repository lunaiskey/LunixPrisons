package io.github.lunaiskey.lunixprison.modules.gangs;

import java.util.Map;

public enum GangRankType {
    MEMBER(0),
    MOD(1),
    OWNER(2),
    ;

    GangRankType(int weight) {
        this.weight = weight;
    }

    private final int weight;

    public int getWeight() {
        return weight;
    }
}
