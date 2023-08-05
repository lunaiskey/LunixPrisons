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

import java.math.BigInteger;
import java.util.List;

public class XPBoost extends LunixEnchant {

    public XPBoost() {
        super("XP Boost", EnchantType.XP_BOOST, List.of("&c[WIP]","Boosts the XP gained per block."), 100, CurrencyType.TOKENS, false);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {

    }

    @Override
    public void onDrop(PlayerDropItemEvent e, LunixPlayer lunixPlayer, int level) {

    }

    @Override
    public void onEquip(Player player, LunixPlayer lunixPlayer, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, LunixPlayer lunixPlayer, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.ZERO;
    }
}
