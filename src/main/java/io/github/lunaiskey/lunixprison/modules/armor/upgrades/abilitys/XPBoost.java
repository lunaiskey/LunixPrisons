package io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys;

import io.github.lunaiskey.lunixprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.util.StringUtil;

import java.util.List;

public class XPBoost extends Ability {

    private final int[] costArray = {
            0,1000,2500,5000,7500,10000
    };

    public XPBoost() {
        super("XP Boost",List.of("Boosts XP while mining."),5);
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
