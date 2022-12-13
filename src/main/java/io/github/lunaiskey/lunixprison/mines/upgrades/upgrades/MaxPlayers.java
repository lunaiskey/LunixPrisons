package io.github.lunaiskey.lunixprison.mines.upgrades.upgrades;

import io.github.lunaiskey.lunixprison.mines.upgrades.PMineUpgrade;

import java.util.List;

public class MaxPlayers extends PMineUpgrade {
    public MaxPlayers() {
        super("Max Players", List.of("&c[WIP]"), 4);
    }

    @Override
    public long getCost(int level) {
        return 0;
    }

    @Override
    public String getUpgradeLore(int level) {
        return "null";
    }
}
