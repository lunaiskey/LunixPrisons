package io.github.lunaiskey.lunixprison.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Efficiency extends PyrexEnchant {

    public Efficiency() {
        super("Efficiency", List.of("Increases your block break speed."), 100,CurrencyType.TOKENS,  true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
    }

    @Override
    public void onDrop(PlayerDropItemEvent e, int level) {

    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.ZERO;
    }
}
