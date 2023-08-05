package io.github.lunaiskey.lunixprison.modules.player;

import org.bukkit.ChatColor;

public enum CurrencyType {
    TOKENS("Tokens","⛁",ChatColor.YELLOW),
    GEMS("Gems","◎",ChatColor.GREEN),
    LUNIX_POINTS("Lunix Points","☀",ChatColor.AQUA),
    ;

    private String unicode;
    private ChatColor color;
    private String name;

    CurrencyType(String name, String unicode, ChatColor color) {
        this.name = name;
        this.unicode = unicode;
        this.color = color;
    }

    public String getUnicode() {
        return unicode;
    }

    public ChatColor getColorCode() {
        return color;
    }

    public String getName() {
        return name;
    }
}
