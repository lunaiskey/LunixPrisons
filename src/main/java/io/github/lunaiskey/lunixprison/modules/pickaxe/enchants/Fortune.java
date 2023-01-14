package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Fortune extends LunixEnchant {


    public Fortune() {
        super("Fortune", List.of("Increases blocks received while mining"),  1000, CurrencyType.TOKENS,  true);
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
        return BigInteger.valueOf(125000L+(125000L*(n-5)));
    }

}
