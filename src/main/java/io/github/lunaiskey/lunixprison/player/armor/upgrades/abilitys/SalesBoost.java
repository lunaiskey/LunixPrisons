package io.github.lunaiskey.lunixprison.player.armor.upgrades.abilitys;

import io.github.lunaiskey.lunixprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.util.StringUtil;

import java.util.List;

public class SalesBoost extends Ability {

    public SalesBoost(int level) {
        super("Sales Boost", List.of("Increases your sales multiplier."), level,20);
    }

    @Override
    public long getCost(int level) {
        long cost = 0;
        switch (level+1) {
            case 1 -> cost = 250;
            case 2 -> cost = 500;
            case 3 -> cost = 750;
            case 4 -> cost = 1000;
            case 5 -> cost = 1500;
            case 6 -> cost = 2000;
            case 7 -> cost = 2500;
            case 8 -> cost = 3000;
            case 9 -> cost = 4000;
            case 10 -> cost = 5000;
            case 11 -> cost = 6250;
            case 12 -> cost = 7500;
            case 13 -> cost = 8750;
            case 14 -> cost = 10000;
            case 15 -> cost = 12500;
            case 16 -> cost = 15000;
            case 17 -> cost = 17500;
            case 18 -> cost = 20000;
            case 19 -> cost = 25000;
            case 20 -> cost = 30000;
        }
        return cost;
    }

    @Override
    public String getLoreAddon(int level) {
        return StringUtil.color("&a+"+getMultiplier(level));
    }

    @Override
    public String getLoreTitle(int level) {
        return StringUtil.color("&7"+getName()+": ");
    }

    public double getMultiplier(int level) {
        return 0.5*level;
    }
}
