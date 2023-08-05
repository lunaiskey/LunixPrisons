package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixChanceEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class Nuke extends LunixChanceEnchant {
    public Nuke() {
        super("Nuke", EnchantType.NUKE, List.of("Obliterates the entire mine."), 5000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {
        Random rand = LunixPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        if (rand.nextDouble()*100 <= getChance(level,p)) {
            Pair<Integer,Integer> pair = PMineManager.get().getGridLocation(e.getBlock().getLocation());
            PMine mine = PMineManager.get().getPMine(pair.getLeft(),pair.getRight());
            long blocks = mine.getArea()-mine.getBlocksBroken();
            onActivationEnd(p,lunixPlayer,mine,blocks);
            mine.reset();
        }
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
        return BigInteger.valueOf(50000L+(75000L*n));
    }

    @Override
    public double getChance(int level,Player player) {
        return 0.000002D*level;
    }
}
