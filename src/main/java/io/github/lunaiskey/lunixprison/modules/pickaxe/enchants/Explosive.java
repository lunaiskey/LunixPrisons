package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixChanceEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class Explosive extends LunixChanceEnchant {
    public Explosive() {
        super("Explosive", EnchantType.EXPLOSIVE, List.of("Breaks a cube around the broken block"), 1000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {
        Location loc = e.getBlock().getLocation();
        Random rand = LunixPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        double roll = rand.nextDouble();
        if (roll*100 <= getChance(level,p)) {
            if (level > getMaxLevel()) {
                level = getMaxLevel();
            }
            int width = 5 + (((level - (level%200)) / 200)*2); //always odd, every 200 levels + 2 to width.
            int radius = (width-1)/2;
            Pair<Integer,Integer> gridLoc = PMineManager.get().getGridLocation(e.getBlock().getLocation());
            PMine mine = PMineManager.get().getPMine(gridLoc.getLeft(),gridLoc.getRight());
            long blocks = 0;
            for (int x = loc.getBlockX()-radius;x<= loc.getBlockX()+radius;x++) {
                for (int z = loc.getBlockZ()-radius;z<= loc.getBlockZ()+radius;z++) {
                    for (int y = loc.getBlockY()-(radius*2);y<=loc.getBlockY();y++) {
                        Block block = loc.getWorld().getBlockAt(x,y,z);
                        if (!mine.isInMineRegion(block.getLocation())) {
                            continue;
                        }
                        if (block.getType() == Material.AIR) {
                            continue;
                        }
                        if (x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
                            continue;
                        }
                        nmsBlockChange.setBlock(x,y,z,Material.AIR);
                        blocks++;
                    }
                }
            }
            onActivationEnd(p,lunixPlayer,mine,blocks);
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
        return BigInteger.valueOf(8000+(25000L*n));
    }

    @Override
    public double getChance(int level, Player player) {
        return 0.01D*level;
    }
}
