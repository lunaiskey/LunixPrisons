package io.github.lunaiskey.lunixprison.gangs;

import java.util.UUID;

public class GangMember {

    private UUID playerUUID;
    private String name;
    private GangRankType type;

    public GangMember(UUID playerUUID, String name, GangRankType gangRankType) {
        this.playerUUID = playerUUID;
        this.name = name;
        this.type = gangRankType;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getName() {
        return name;
    }

    public GangRankType getType() {
        return type;
    }

    public void setType(GangRankType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
