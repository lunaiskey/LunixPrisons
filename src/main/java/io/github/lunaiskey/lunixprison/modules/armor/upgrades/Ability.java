package io.github.lunaiskey.lunixprison.modules.armor.upgrades;

import java.util.List;

public abstract class Ability {

    private final String name;
    private int maxLevel;
    private List<String> description;

    public Ability(String name,List<String> description, int maxLevel) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }

    public abstract long getCost(int level);

    public abstract String getLoreAddon(int level);


    public abstract String getLoreTitle(int level);

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<String> getDescription() {
        return description;
    }
}
