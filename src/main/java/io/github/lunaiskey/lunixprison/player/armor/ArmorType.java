package io.github.lunaiskey.lunixprison.player.armor;

import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
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

    public int getIntSlot() {
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

    public static List<ArmorType> getSortedList() {
        List<ArmorType> list = new ArrayList<>();
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
