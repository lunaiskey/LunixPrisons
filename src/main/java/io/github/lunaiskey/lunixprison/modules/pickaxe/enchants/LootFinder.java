package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LootFinder extends LunixEnchant {
    public LootFinder() {
        super("Loot Finder", List.of("Gives a chance to find Geodes."), 250, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Random rand = LunixPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        if (rand.nextDouble()*100 <= getChance(level)) {
            if (level > getMaxLevel()) {
                level = getMaxLevel();
            }
            int bound = (((level-1) - ((level-1)%(getMaxLevel()/5)))/(getMaxLevel()/5))+1;
            p.getInventory().addItem(getGeode(rand.nextInt(bound)));
        }
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
        if (n <= 50) {
            return BigInteger.valueOf(12500+(12500L*(n)));
        } else if (n <= 100){
            return getCost(50).add(BigInteger.valueOf(25000+(25000L*(n-50))));
        } else {
            return getCost(100).add(BigInteger.valueOf(50000+(50000L*(n-100))));
        }
    }

    private double getChance(int level) {
        return 0.001*level;
    }

    private ItemStack getGeode(int rarity) {
        ItemManager itemManager = LunixPrison.getPlugin().getItemManager();
        return switch(rarity) {
            case 0 -> itemManager.getLunixItem(ItemID.COMMON_GEODE).getItemStack();
            case 1 -> itemManager.getLunixItem(ItemID.UNCOMMON_GEODE).getItemStack();
            case 2 -> itemManager.getLunixItem(ItemID.RARE_GEODE).getItemStack();
            case 3 -> itemManager.getLunixItem(ItemID.EPIC_GEODE).getItemStack();
            case 4 -> itemManager.getLunixItem(ItemID.LEGENDARY_GEODE).getItemStack();
            default -> throw new IllegalStateException("Unexpected value: " + rarity);
        };
    }
}
