package io.github.lunaiskey.lunixprison.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigInteger;
import java.util.List;

public class NightVision extends LunixEnchant {

    public NightVision() {
        super("Night Vision", List.of("Gives you permanent Night Vision ","while holding your pickaxe."), 1, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {

    }

    @Override
    public void onDrop(PlayerDropItemEvent e, int level) {

    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {
        if (level <= 0) {
            this.onUnEquip(player, pickaxe, level);
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, level-1, true, true));
    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.ZERO;
    }
}
