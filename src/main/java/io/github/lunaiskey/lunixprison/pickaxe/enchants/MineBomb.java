package io.github.lunaiskey.lunixprison.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.mines.PMine;
import io.github.lunaiskey.lunixprison.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import io.github.lunaiskey.lunixprison.util.ShapeUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MineBomb extends PyrexEnchant {
    public MineBomb() {
        super("Mine Bomb", List.of("&c[WIP]"), 2000, CurrencyType.TOKENS, true);
    }

    private static Map<UUID,Long> cooldownMap = new HashMap<>();

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {

    }

    @Override
    public void onDrop(PlayerDropItemEvent e, int level) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        Block block = p.getTargetBlockExact(50);
        int cooldown = 60;
        long cooldownRaw = cooldown * 1000;
        if (!cooldownMap.containsKey(p.getUniqueId()) || cooldownMap.get(p.getUniqueId())+cooldownRaw <= System.currentTimeMillis()) {
            if (block != null) {
                Location blockLocation = block.getLocation();
                if (blockLocation.getWorld() == Bukkit.getWorld(PMineWorld.getWorldName())) {
                    Pair<Integer,Integer> gridLocation = LunixPrison.getPlugin().getPmineManager().getGridLocation(blockLocation);
                    PMine mine = LunixPrison.getPlugin().getPmineManager().getPMine(gridLocation.getLeft(),gridLocation.getRight());
                    if (mine != null) {
                        List<Location> blockList = ShapeUtil.generateSphere(blockLocation,7);
                        int amount = 0;
                        for (Location loc : blockList) {
                            if (mine.isInMineRegion(loc)) {
                                if (loc.getBlock().getType() != Material.AIR) {
                                    loc.getBlock().setType(Material.AIR);
                                    amount++;
                                }
                            }
                        }
                        LunixPrison.getPlugin().getPlayerManager().payForBlocks(p,amount);
                    }
                }
                cooldownMap.put(p.getUniqueId(),System.currentTimeMillis());
            }
        }
    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.valueOf(5000L+(2500L*n));
    }

    public static Map<UUID, Long> getCooldownMap() {
        return cooldownMap;
    }
}
