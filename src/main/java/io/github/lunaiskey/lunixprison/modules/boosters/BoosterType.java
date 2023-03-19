package io.github.lunaiskey.lunixprison.modules.boosters;

import org.bukkit.ChatColor;

public enum BoosterType {
    SALES,
    ENCHANT_PROC,
    GEMSTONE,
    ;

    public String getName() {
        return switch (this) {
            case SALES -> "Sales Booster";
            case ENCHANT_PROC -> "Enchant Proc-rate Booster";
            case GEMSTONE -> "Gemstone Booster";
        };
    }

    public ChatColor getColor() {
        return switch (this) {
            case SALES -> ChatColor.YELLOW;
            case ENCHANT_PROC -> ChatColor.RED;
            case GEMSTONE -> ChatColor.LIGHT_PURPLE;
        };
    }

    public String getColoredName() {
        return getColor()+getName();
    }
}
