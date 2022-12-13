package io.github.lunaiskey.lunixprison.player;

import org.bukkit.ChatColor;

public enum CurrencyType {
    TOKENS,
    GEMS,
    LUNIX_POINTS,
    ;

    public String getUnicode() {
        return switch (this) {
            case TOKENS -> "⛁";
            case GEMS -> "◎";
            case LUNIX_POINTS -> "☀";
        };
    }

    public ChatColor getColorCode() {
        return switch (this) {
            case TOKENS -> ChatColor.YELLOW;
            case GEMS -> ChatColor.GREEN;
            case LUNIX_POINTS -> ChatColor.DARK_AQUA;
        };
    }
}
