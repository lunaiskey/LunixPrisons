package io.github.lunaiskey.lunixprison.modules.armor.upgrades;

import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.EnchantmentProc;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys.XPBoost;
import org.bukkit.Material;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public enum AbilityType {
    SALES_BOOST(new SalesBoost()),
    ENCHANTMENT_PROC(new EnchantmentProc()),
    XP_BOOST(new XPBoost()),
    ;

    private Ability ability;
    AbilityType(Ability ability) {
        this.ability = ability;
    }

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

    public Ability getAbility() {
        return ability;
    }
}
