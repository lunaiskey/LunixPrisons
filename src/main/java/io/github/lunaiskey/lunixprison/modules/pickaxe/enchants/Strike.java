package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixChanceEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.ShapeUtil;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Strike extends LunixChanceEnchant {
    public Strike() {
        super("Lightning", EnchantType.STRIKE, List.of("Breaks a sphere around the block you last broke."), 1000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        double roll = LunixPrison.getPlugin().getRand().nextDouble();
        if (roll*100 <= getChance(level,p)) {
            long blocksBroken = 0;
            Pair<Integer,Integer> gridLoc = PMineManager.get().getGridLocation(e.getBlock().getLocation());
            PMine mine = PMineManager.get().getPMine(gridLoc.getLeft(),gridLoc.getRight());
            int size = 5+(Math.floorDiv(Math.min(level,getMaxLevel()),getMaxLevel()/10));
            for (Location sphereLoc : ShapeUtil.generateSphere(e.getBlock().getLocation(),size)) {
                if (mine.isInMineRegion(sphereLoc)) {
                    if (sphereLoc.getBlockX() == loc.getBlockX() && sphereLoc.getBlockY() == loc.getBlockY() && sphereLoc.getBlockZ() == loc.getBlockZ()) {
                        continue;
                    }
                    if (sphereLoc.getBlock().getType() != Material.AIR) {
                        nmsBlockChange.setBlock(sphereLoc.getBlockX(),sphereLoc.getBlockY(),sphereLoc.getBlockZ(),Material.AIR);
                        blocksBroken++;
                    }

                }
            }
            onActivationEnd(p,lunixPlayer,mine,blocksBroken);
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
