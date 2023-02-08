package io.github.lunaiskey.lunixprison;

import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.ChatColor;

public enum Messages {
    NO_PERMISSION(ChatColor.RED+"No Permission."),
    NOT_ENOUGH_ARGS(ChatColor.RED+"Not Enough Arguments."),
    PLAYER_ONLY(ChatColor.RED+"This is a player only command!")
    ;


    private final String text;
    Messages(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
