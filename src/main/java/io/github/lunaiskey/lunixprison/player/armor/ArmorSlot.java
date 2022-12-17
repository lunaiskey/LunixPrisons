package io.github.lunaiskey.lunixprison.player.armor;

import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public enum ArmorSlot {
    HELMET(5),
    CHESTPLATE(6),
    LEGGINGS(7),
    BOOTS(8)
    ;

    private final int slot;

    ArmorSlot(int i) {
        slot=i;
    }

    public int getIntSlot() {
        return slot;
    }

    public String getName() {
        return switch(this) {
            case HELMET -> "Helmet";
            case CHESTPLATE -> "Chestplate";
            case LEGGINGS -> "Leggings";
            case BOOTS -> "Boots";
        };
    }

    public static List<ArmorSlot> getSortedList() {
        List<ArmorSlot> list = new ArrayList<>();
        list.add(HELMET);
        list.add(CHESTPLATE);
        list.add(LEGGINGS);
        list.add(BOOTS);
        return list;
    }

    public EquipmentSlot getSlot() {
        return switch (getIntSlot()) {
            case 5 -> EquipmentSlot.HEAD;
            case 6 -> EquipmentSlot.CHEST;
            case 7 -> EquipmentSlot.LEGS;
            case 8 -> EquipmentSlot.FEET;
            default -> null;
        };
    }
}
