package io.github.lunaiskey.lunixprison.pickaxe;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.mines.PMine;
import io.github.lunaiskey.lunixprison.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.nms.NBTTags;
import io.github.lunaiskey.lunixprison.pickaxe.inventories.PickaxeEnchantGUI;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LunixPickaxe {

    private UUID player;
    private Map<EnchantType,Integer> enchants;
    private Set<EnchantType> disabledEnchants;
    private long blocksBroken;

    public LunixPickaxe(UUID player, Map<EnchantType,Integer> enchants, Set<EnchantType> disabledEnchants, long blocksBroken) {
        this.player = player;
        if (enchants == null) {
            enchants = new HashMap<>();
        }
        for (EnchantType type : EnchantType.getDefaultEnchants().keySet()) {
            if (enchants.containsKey(type)) {
                if (enchants.get(type) < EnchantType.getDefaultEnchants().get(type)) {
                    enchants.put(type,EnchantType.getDefaultEnchants().get(type));
                }
            } else {
                enchants.put(type,EnchantType.getDefaultEnchants().get(type));
            }
        }
        this.enchants = enchants;
        if (disabledEnchants == null) {
            this.disabledEnchants = new HashSet<>();
        } else {
            this.disabledEnchants = disabledEnchants;
        }
        this.blocksBroken = blocksBroken;
    }

    public LunixPickaxe(UUID player) {
        this(player,null,null,0);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        item = NBTTags.addLunixData(item,"id",PickaxeHandler.getId());
        return LunixPrison.getPlugin().getPickaxeHandler().updatePickaxe(item,player);
    }


    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        Location blockLoc = block.getLocation();
        if (blockLoc.getWorld().getName().equals(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLoc = LunixPrison.getPlugin().getPmineManager().getGridLocation(blockLoc);
            PMine pMine = LunixPrison.getPlugin().getPmineManager().getPMine(gridLoc.getLeft(), gridLoc.getRight());
            if (pMine != null) {
                if (pMine.isInMineRegion(blockLoc)) {
                    e.setDropItems(false);
                    e.setExpToDrop(0);
                    pMine.addMineBlocks(1);
                    for (EnchantType type : getEnchants().keySet()) {
                        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
                        if (enchant.isEnabled()) {
                            if (!getDisabledEnchants().contains(type)) {
                                enchant.onBlockBreak(e,getEnchants().get(type));
                            }
                        }
                    }
                    LunixPlayer player = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId());
                    LunixPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),1);
                    //p.giveExp(1+player.getXPBoostTotal()); //Not Implemented yet...
                    LunixPrison.getPlugin().getPlayerManager().tickGemstoneCount(p);
                    setBlocksBroken(getBlocksBroken()+1);
                    LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                }
            }
        }
    }

    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            p.openInventory(new PickaxeEnchantGUI(p).getInv());
        }
    }

    public long getBlocksBroken() {
        return blocksBroken;
    }

    public Map<EnchantType, Integer> getEnchants() {
        return enchants;
    }

    public Set<EnchantType> getDisabledEnchants() {
        return disabledEnchants;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }
}
