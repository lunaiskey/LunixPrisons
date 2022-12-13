package io.github.lunaiskey.lunixprison.player.armor.upgrades;

import java.util.List;

public abstract class Ability {

    private final String name;
    private int level;
    private int maxLevel;
    private List<String> description;

    public Ability(String name,List<String> description, int level, int maxLevel) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.maxLevel = maxLevel;
    }

    public abstract long getCost(int level);

    public long getCost() {
        return getCost(level);
    }

    public long getNextCost() {
        return getCost(level+1);
    }

    public abstract String getLoreAddon(int level);

    public String getLoreAddon() {
        return getLoreAddon(level);
    }

    public abstract String getLoreTitle(int level);

    public String getLoreTitle() {
        return getLoreTitle(level);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
