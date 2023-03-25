package io.github.lunaiskey.lunixprison.modules.shop;

import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import org.bukkit.ChatColor;

public enum ShopItemCostCurrencyType {
    TOKEN(CurrencyType.TOKENS.getColorCode()+CurrencyType.TOKENS.getUnicode(),""),
    GEM(CurrencyType.GEMS.getColorCode()+CurrencyType.GEMS.getUnicode(),""),
    LUNIX_POINT(CurrencyType.LUNIX_POINTS.getColorCode()+CurrencyType.LUNIX_POINTS.getUnicode(),""),
    XP("",ChatColor.AQUA+"XP"),
    ;

    private String prefix;
    private String suffix;

    ShopItemCostCurrencyType(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
