package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigInteger;
import java.util.List;

public class Haste extends PyrexEnchant {


    public Haste() {
        super("Haste", List.of("Gives you permanent Haste ","while holding your pickaxe."), 3, CurrencyType.TOKENS,  true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {

    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {
        if (level <= 0) {
            this.onUnEquip(player, pickaxe, level);
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level-1, true, true));
    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public BigInteger getEquation(int n) {
        return BigInteger.ZERO;
    }
}