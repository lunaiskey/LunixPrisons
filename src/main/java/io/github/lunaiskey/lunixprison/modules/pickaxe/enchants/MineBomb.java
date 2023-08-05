package io.github.lunaiskey.lunixprison.modules.pickaxe.enchants;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.ShapeUtil;
import io.github.lunaiskey.lunixprison.util.TimeUtil;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
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

public class MineBomb extends LunixEnchant {
    public MineBomb() {
        super("Mine Bomb", EnchantType.MINE_BOMB, List.of("&c[WIP]"), 2000, CurrencyType.TOKENS, true);
    }

    private static Map<UUID,Long> cooldownMap = new HashMap<>();

    @Override
    public void onBlockBreak(BlockBreakEvent e, LunixPlayer lunixPlayer, int level, NMSBlockChange nmsBlockChange) {

    }

    @Override
    public void onDrop(PlayerDropItemEvent e, LunixPlayer lunixPlayer, int level) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        Block block = p.getTargetBlockExact(50);
        int cooldown = 60;
        long cooldownRaw = cooldown * 1000;
        Long lastUse = cooldownMap.get(p.getUniqueId());
        if (lastUse != null && lastUse+cooldownRaw >= System.currentTimeMillis()) {
            p.sendMessage("MineBomb is on timer for "+ TimeUtil.countdown(lastUse+cooldownRaw));
            return;
        }
        if (block == null) {
            return;
        }
        Location blockLocation = block.getLocation();
        if (blockLocation.getWorld().getName().equalsIgnoreCase(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLocation = PMineManager.get().getGridLocation(blockLocation);
            PMine mine = PMineManager.get().getPMine(gridLocation.getLeft(),gridLocation.getRight());
            if (mine != null) {
                List<Location> blockList = ShapeUtil.generateSphere(blockLocation,7);
                int amount = 0;
                NMSBlockChange blockChange = new NMSBlockChange(blockLocation.getWorld());
                for (Location loc : blockList) {
                    if (mine.isInMineRegion(loc)) {
                        if (loc.getBlock().getType() != Material.AIR) {
                            blockChange.setBlock(loc.getBlockX(), loc.getBlockY(),loc.getBlockZ(),Material.AIR);
                            amount++;
                        }
                    }
                }
                onActivationEnd(p,lunixPlayer,mine,amount);
                blockChange.update();
            }
        }
        cooldownMap.put(p.getUniqueId(),System.currentTimeMillis());

    }

    @Override
    public void onEquip(Player player, LunixPlayer lunixPlayer, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, LunixPlayer lunixPlayer, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.valueOf(5000L+(2500L*n));
    }

    public static Map<UUID, Long> getCooldownMap() {
        return cooldownMap;
    }
}
