package io.github.lunaiskey.lunixprison.modules.mines.upgrades;

import java.util.List;

public abstract class PMineUpgrade {

    private final String name;
    private final int maxLevel;
    private final List<String> description;

    public PMineUpgrade(String name,List<String> description, int maxLevel) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }

    public abstract long getCost(int level);

    public abstract String getUpgradeLore(int level);

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
