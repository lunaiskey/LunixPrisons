package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixChanceEnchant;
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
import java.util.Random;


public class GemFinder extends LunixChanceEnchant {
    public GemFinder() {
        super("Gemstone Finder", EnchantType.GEM_FINDER, List.of("Gives a chance to find a random Gemstone."),1000, CurrencyType.TOKENS,  true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {
        Player player = e.getPlayer();
        Random rand = LunixPrison.getPlugin().getRand();
        double roll = rand.nextDouble();
        if (roll*100 <= getChance(level,player)) {
            if (level > getMaxLevel()) {
                level = getMaxLevel();
            }
            int j = getMaxLevel()/10;
            int gemNum = rand.nextInt((((level-1)-((level-1) % j))/j)+1)+1;
            //int gemNum = (int) (Math.random() * (level / 100)) + 1;
            LunixItem gemstone = ItemManager.get().getLunixItem(getGemstone(gemNum));
            ItemStack gemstoneItem = gemstone.getItemStack();
            player.getInventory().addItem(gemstoneItem);
            onActivationEnd(player,lunixPlayer);
        }
        //PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).giveGems(0);
    }

    @Override
    public double getChance(int level, Player player) {
        return 0.001D * level;
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
        if (n <= 500) {
            return BigInteger.valueOf(12500+(12500L*(n)));
        } else {
            return getCost(500).add(BigInteger.valueOf(25000+(25000L*(n-500))));
        }
    }

    public ItemID getGemstone(int num){
        return switch (num){
            case 1 -> ItemID.AMETHYST_GEMSTONE;
            case 2 -> ItemID.JASPER_GEMSTONE;
            case 3 -> ItemID.OPAL_GEMSTONE;
            case 4 -> ItemID.JADE_GEMSTONE;
            case 5 -> ItemID.TOPAZ_GEMSTONE;
            case 6 -> ItemID.AMBER_GEMSTONE;
            case 7 -> ItemID.SAPPHIRE_GEMSTONE;
            case 8 -> ItemID.EMERALD_GEMSTONE;
            case 9 -> ItemID.RUBY_GEMSTONE;
            case 10 -> ItemID.DIAMOND_GEMSTONE;
            default -> ItemID.AMETHYST_GEMSTONE;
        };
    }
}
