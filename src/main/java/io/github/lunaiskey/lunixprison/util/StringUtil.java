package io.github.lunaiskey.lunixprison.util;

import org.bukkit.ChatColor;

import java.util.List;

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

    public static List<String> color(List<String> stringList) {
        stringList.replaceAll(StringUtil::color);
        return stringList;
    }
}
