package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigInteger;
import java.util.List;

public class Haste extends LunixEnchant {

    public Haste() {
        super("Haste", EnchantType.HASTE, List.of("Gives you permanent Haste ","while holding your pickaxe."), 3, CurrencyType.TOKENS,  true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {

    }

    @Override
    public void onDrop(PlayerDropItemEvent e, LunixPlayer lunixPlayer, int level) {

    }

    @Override
    public void onEquip(Player player, LunixPlayer lunixPlayer, ItemStack pickaxe, int level) {
        if (level <= 0) {
            this.onUnEquip(player,lunixPlayer, pickaxe, level);
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level-1, true, true));
    }

    @Override
    public void onUnEquip(Player player, LunixPlayer lunixPlayer, ItemStack pickaxe, int level) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.ZERO;
    }
}
