package io.github.lunaiskey.lunixprison.util;

import org.bukkit.ChatColor;

public class StringUtil {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }

    public static String[] color(String... strings) {
        for (int i = 0;i< strings.length;i++) {
            strings[i] = ChatColor.translateAlternateColorCodes('&',strings[i]);
        }
        return strings;
    }
}
