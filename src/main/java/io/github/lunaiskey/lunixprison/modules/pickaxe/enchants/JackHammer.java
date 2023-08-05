package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixChanceEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class JackHammer extends LunixChanceEnchant {
    public JackHammer() {
        super("Jackhammer", EnchantType.JACK_HAMMER, List.of("Has a chance to break an","entire layer of your mine"), 1000, CurrencyType.TOKENS,   true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {
        Random rand = LunixPrison.getPlugin().getRand();
        Location loc = e.getBlock().getLocation();
        Player p = e.getPlayer();
        double roll = rand.nextDouble();
        if (roll*100 <= getChance(level,p)) {
            Pair<Integer,Integer> gridLoc = PMineManager.get().getGridLocation(e.getBlock().getLocation());
            PMine mine = PMineManager.get().getPMine(gridLoc.getLeft(),gridLoc.getRight());
            if (mine == null) {
                return;
            }
            mine.isInMineRegion(e.getBlock().getLocation());
            long counter = 0;
            for (int x = mine.getMin().getBlockX(); x <= mine.getMax().getBlockX();x++) {
                for (int z = mine.getMin().getBlockZ(); z <= mine.getMax().getBlockZ();z++) {
                    if (x == loc.getBlockX() && z == loc.getBlockZ()) {
                        continue;
                    }
                    Block block = e.getBlock().getWorld().getBlockAt(x,e.getBlock().getY(),z);
                    if (block.getType() != Material.AIR) {
                        nmsBlockChange.setBlock(block.getX(), block.getY(), block.getZ(), Material.AIR);
                        counter++;
                    }
                }
            }
            onActivationEnd(p,lunixPlayer,mine,counter);
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
        //return BigInteger.valueOf(2000+(1500L*n));
        return BigInteger.valueOf(8000+(25000L*n));
    }
    /*
    @Override
    public BigInteger getEquation(int n) {
        return BigInteger.valueOf((long) (2000L+((n+300)*Math.pow(1.018,n/15D))));
    }

     */

    @Override
    public double getChance(int level,Player player) {
        return 0.01D*level;
    }
}
