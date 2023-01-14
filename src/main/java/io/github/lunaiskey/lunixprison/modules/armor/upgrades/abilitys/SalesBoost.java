package io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys;

import io.github.lunaiskey.lunixprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.util.StringUtil;

import java.util.List;

public class SalesBoost extends Ability {

    private final int[] costArray = {
            0,250,500,750,1000,1500,2000,2500,3000,4000,5000,
            6250,7500,8750,10000,12500,15000,17500,20000,25000,30000
    };

    public SalesBoost() {
        super("Sales Boost",List.of("Increases your sales multiplier."),20);
    }

    @Override
    public long getCost(int level) {
        if (level < costArray.length) {
            return costArray[level];
        }
        return costArray[0];
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
