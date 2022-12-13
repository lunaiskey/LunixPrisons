package io.github.lunaiskey.lunixprison.mines.upgrades;

import io.github.lunaiskey.lunixprison.mines.upgrades.upgrades.MaxPlayers;
import io.github.lunaiskey.lunixprison.mines.upgrades.upgrades.SellPrice;
import io.github.lunaiskey.lunixprison.mines.upgrades.upgrades.Size;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum PMineUpgradeType {
    SIZE,
    SELL_PRICE,
    MAX_PLAYERS,
    ;

    private static Map<PMineUpgradeType,PMineUpgrade> upgradeMap = new HashMap<>();

    static {
        upgradeMap.put(SIZE,new Size());
        upgradeMap.put(SELL_PRICE,new SellPrice());
        upgradeMap.put(MAX_PLAYERS,new MaxPlayers());
    }

    public static Map<PMineUpgradeType, PMineUpgrade> getUpgradeMap() {
        return upgradeMap;
    }

    public Material getMaterial() {
        return switch (this) {
            case SIZE -> Material.CHEST;
            case SELL_PRICE -> Material.SUNFLOWER;
            case MAX_PLAYERS -> Material.PLAYER_HEAD;
        };
    }

    public String getName() {
        return switch (this) {
            case SIZE -> "&eSize";
            case SELL_PRICE -> "&eSell Price";
            case MAX_PLAYERS -> "&eMax Players";
        };
    }
}
