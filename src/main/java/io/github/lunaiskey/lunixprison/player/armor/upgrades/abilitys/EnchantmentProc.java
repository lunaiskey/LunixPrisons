package io.github.lunaiskey.lunixprison.player.armor.upgrades.abilitys;

import io.github.lunaiskey.lunixprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.util.StringUtil;

import java.util.List;

public class EnchantmentProc extends Ability {

    public EnchantmentProc(int level) {
        super("Enchant Proc", List.of("Increases the chance your enchantments will proc."), level, 15);
    }

    @Override
    public long getCost(int level) {
        long cost = 0;
        switch (level+1) {
            case 1 -> cost = 500;
            case 2 -> cost = 750;
            case 3 -> cost = 1000;
            case 4 -> cost = 1500;
            case 5 -> cost = 2000;
            case 6 -> cost = 2500;
            case 7 -> cost = 3000;
            case 8 -> cost = 3500;
            case 9 -> cost = 4250;
            case 10 -> cost = 5000;
            case 11 -> cost = 6000;
            case 12 -> cost = 7500;
            case 13 -> cost = 10000;
            case 14 -> cost = 15000;
            case 15 -> cost = 20000;
        }

        return cost;
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
