package io.github.lunaiskey.lunixprison.modules.player.datastorages;

import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ArmorStorage {

    private Map<ArmorSlot, Armor> armor;
    private boolean isArmorEquiped;
    private ItemID selectedGemstone;
    private int gemstoneCount;

    public ArmorStorage(Map<ArmorSlot,Armor> armor, boolean isArmorEquiped, ItemID selectedGemstone, int gemstoneCount) {
        this.isArmorEquiped = isArmorEquiped;
        this.gemstoneCount = gemstoneCount;
        this.armor = Objects.requireNonNullElseGet(armor, LinkedHashMap::new);
        for (ArmorSlot slot : ArmorSlot.values()) {
            this.armor.putIfAbsent(slot,new Armor(slot));
        }
        this.selectedGemstone = Objects.requireNonNullElse(selectedGemstone, ItemID.AMETHYST_GEMSTONE);
    }

    public ArmorStorage() {
        this(null,false,null,0);
    }

    public Map<ArmorSlot, Armor> getArmor() {
        return armor;
    }

    public boolean isArmorEquiped() {
        return isArmorEquiped;
    }

    public int getGemstoneCount() {
        return gemstoneCount;
    }

    public ItemID getSelectedGemstone() {
        return selectedGemstone;
    }

    public void setArmor(Map<ArmorSlot, Armor> armor) {
        this.armor = armor;
    }

    public void setArmorEquiped(boolean armorEquiped) {
        isArmorEquiped = armorEquiped;
    }

    public void setGemstoneCount(int gemstoneCount) {
        this.gemstoneCount = gemstoneCount;
    }

    public void setSelectedGemstone(ItemID selectedGemstone) {
        this.selectedGemstone = selectedGemstone;
    }
}
