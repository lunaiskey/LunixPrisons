package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class Nuke extends LunixEnchant {
    public Nuke() {
        super("Nuke", List.of("Obliterates the entire mine."), 5000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Random rand = LunixPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        if (rand.nextDouble()*100 <= getChance(level)) {
            Pair<Integer,Integer> pair = LunixPrison.getPlugin().getPMineManager().getGridLocation(e.getBlock().getLocation());
            PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(pair.getLeft(),pair.getRight());
            long blocks = mine.getArea()-mine.getBlocksBroken();
            LunixPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),blocks);
            mine.reset();
            p.sendMessage(StringUtil.color("&b&lYou have activated nuke in this mine!"));
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
        return BigInteger.valueOf(50000L+(75000L*n));
    }

    private double getChance(int level) {
        return 0.000002D*level;
    }
}
