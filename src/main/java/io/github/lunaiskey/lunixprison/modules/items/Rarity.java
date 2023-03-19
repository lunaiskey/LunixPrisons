package io.github.lunaiskey.lunixprison.modules.items;

import org.bukkit.ChatColor;

public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    ;

    public String getColorCode() {
        return switch (this){
            case COMMON -> "&f";
            case UNCOMMON -> "&a";
            case RARE -> "&9";
            case EPIC -> "&5";
            case LEGENDARY -> "&6";
            case MYTHIC -> "&d";
        };
    }

    public ChatColor getChatColor() {
        return switch (this) {
            case COMMON -> ChatColor.WHITE;
            case UNCOMMON -> ChatColor.GREEN;
            case RARE -> ChatColor.BLUE;
            case EPIC -> ChatColor.DARK_PURPLE;
            case LEGENDARY -> ChatColor.GOLD;
            case MYTHIC -> ChatColor.LIGHT_PURPLE;
        };
    }
}
