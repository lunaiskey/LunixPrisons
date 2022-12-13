package io.github.lunaiskey.lunixprison.mines.upgrades.upgrades;

import io.github.lunaiskey.lunixprison.mines.upgrades.PMineUpgrade;

import java.util.List;

public class Size extends PMineUpgrade {
    public Size() {
        super("Size", List.of("Increases the size of your mine."), 38);
    }

    @Override
    public long getCost(int level) {
        return 250L*level;
    }

    @Override
    public String getUpgradeLore(int level) {
        int defaultSize = 25;
        int newSize = defaultSize + (getRadiusIncrease(level)*2);
        return newSize+" x "+newSize;
    }

    public int getRadiusIncrease(int level) {
        return level;
    }
}
