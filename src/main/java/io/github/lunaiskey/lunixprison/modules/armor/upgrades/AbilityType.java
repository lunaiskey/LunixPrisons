package io.github.lunaiskey.lunixprison.modules.armor.upgrades;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum AbilityType {
    SALES_BOOST,
    ENCHANTMENT_PROC,
    XP_BOOST,
    ;

    public static List<AbilityType> getSortedList() {
        List<AbilityType> list = new ArrayList<>();
        list.add(SALES_BOOST);
        list.add(ENCHANTMENT_PROC);
        list.add(XP_BOOST);
        return list;
    }

    public Material getMaterial() {
        return switch (this) {
            case SALES_BOOST -> Material.SUNFLOWER;
            case ENCHANTMENT_PROC -> Material.NETHER_STAR;
            case XP_BOOST -> Material.EXPERIENCE_BOTTLE;
        };
    }

    public String getFormattedName() {
        return switch (this) {
            case SALES_BOOST -> "&eSales Boost";
            case ENCHANTMENT_PROC -> "&eEnchantment Proc";
            case XP_BOOST -> "&eXP Boost";
        };
    }
}
