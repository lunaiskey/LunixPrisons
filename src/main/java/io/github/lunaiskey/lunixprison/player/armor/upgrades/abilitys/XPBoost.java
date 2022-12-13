package io.github.lunaiskey.lunixprison.player.armor.upgrades.abilitys;

import io.github.lunaiskey.lunixprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.util.StringUtil;

import java.util.List;

public class XPBoost extends Ability {
    public XPBoost(int level) {
        super("XP Boost", List.of("Boosts XP while mining."), level, 5);
    }

    @Override
    public long getCost(int level) {
        long cost = 0;
        switch (level+1) {
            case 1 -> cost = 1000;
            case 2 -> cost = 2500;
            case 3 -> cost = 5000;
            case 4 -> cost = 7500;
            case 5 -> cost = 10000;
        }
        return cost;
    }

    @Override
    public String getLoreAddon(int level) {
        return StringUtil.color("&a+"+getBoost(level)+"XP");
    }

    @Override
    public String getLoreTitle(int level) {
        return StringUtil.color("&7"+getName()+": ");
    }

    public int getBoost(int level) {
        return level;
    }
}
