package io.github.lunaiskey.lunixprison.modules.armor;

import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public enum ArmorSlot {
    HELMET(5,EquipmentSlot.HEAD),
    CHESTPLATE(6,EquipmentSlot.CHEST),
    LEGGINGS(7,EquipmentSlot.LEGS),
    BOOTS(8,EquipmentSlot.FEET)
    ;

    private final int slot;
    private EquipmentSlot equipmentSlot;

    ArmorSlot(int slot, EquipmentSlot equipmentSlot) {
        this.slot = slot;
        this.equipmentSlot = equipmentSlot;
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

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }
}
