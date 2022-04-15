package io.github.lunaiskey.pyrexprison.player.armor;

import org.bukkit.Material;

import java.util.List;

public enum ArmorType {
    HELMET(5),
    CHESTPLATE(6),
    LEGGINGS(7),
    BOOTS(8)
    ;

    private final int slot;

    ArmorType(int i) {
        slot=i;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        String str;
        switch(this) {
            case HELMET -> str = "Helmet";
            case CHESTPLATE -> str = "Chestplate";
            case LEGGINGS -> str = "Leggings";
            case BOOTS -> str = "Boots";
            default -> str = "";
        }
        return str;
    }
}