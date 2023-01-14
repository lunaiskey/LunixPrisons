package io.github.lunaiskey.lunixprison.modules.armor.upgrades.abilitys;

import io.github.lunaiskey.lunixprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.util.StringUtil;

import java.util.List;

public class EnchantmentProc extends Ability {

    private final int[] costArray = {
            0,500,750,1000,1500,2000,2500,3000,3500,4250,5000,
            6000,7500,10000,15000,20000
    };

    public EnchantmentProc() {
        super("Enchant Proc", List.of("Increases the chance your enchantments will proc."), 15);
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
        return StringUtil.color("&a+"+getProcBoost(level)+"%");
    }

    @Override
    public String getLoreTitle(int level) {
        return StringUtil.color("&7"+getName()+": ");
    }

    public double getProcBoost(int level) {
        return level;
    }
}
