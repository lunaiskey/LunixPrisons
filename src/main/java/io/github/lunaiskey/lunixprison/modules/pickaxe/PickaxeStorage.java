package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeEnchantGUI;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
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

public class PickaxeStorage {

    private UUID pUUID;
    private Map<EnchantType,Integer> enchants;
    private Set<EnchantType> disabledEnchants;
    private Set<EnchantType> disabledMessages;
    private long blocksBroken;
    private String rename;

    public PickaxeStorage(UUID pUUID, Map<EnchantType,Integer> enchants, Set<EnchantType> disabledEnchants,Set<EnchantType> disabledMessages, long blocksBroken, String rename) {
        this.pUUID = pUUID;
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
        this.disabledEnchants = Objects.requireNonNullElseGet(disabledEnchants, HashSet::new);
        this.disabledMessages = Objects.requireNonNullElseGet(disabledMessages, HashSet::new);
        this.blocksBroken = blocksBroken;
        this.rename = rename;
    }

    public PickaxeStorage(UUID pUUID) {
        this(pUUID,null,null,null,0,null);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        item = NBTTags.addLunixData(item,"id", PickaxeManager.getLunixPickaxeId());
        return PickaxeManager.get().updatePickaxe(item, pUUID);
    }


    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Location blockLoc = block.getLocation();
        if (blockLoc.getWorld().getName().equals(PMineWorld.getWorldName())) {
            PMine pMine = PMineManager.get().getPMineAtPlayer(player);
            if (pMine != null) {
                if (pMine.isInMineRegion(blockLoc)) {
                    //e.setDropItems(false);
                    //e.setExpToDrop(0);
                    e.setCancelled(true);
                    pMine.addMineBlocks(1);
                    LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(e.getPlayer().getUniqueId());
                    Map<EnchantType, Integer> enchants = getEnchants();
                    NMSBlockChange blockChange = new NMSBlockChange(blockLoc.getWorld());
                    for (Map.Entry<EnchantType,Integer> entry : enchants.entrySet()) {
                        LunixEnchant enchant = entry.getKey().getLunixEnchant();
                        if (enchant.isEnabled()) {
                            if (!getDisabledEnchants().contains(entry.getKey())) {
                                enchant.onBlockBreak(e,lunixPlayer,entry.getValue(),blockChange);
                            }
                        }
                    }
                    blockChange.setBlock(blockLoc.getBlockX(),blockLoc.getBlockY(),blockLoc.getBlockZ(),Material.AIR);
                    blockChange.update();
                    PlayerManager.get().payForBlocks(player,lunixPlayer,1);
                    PlayerManager.get().tickGemstoneCount(player);
                    PickaxeManager.get().updateInventoryPickaxe(player);
                    incrementBlocksBroken();
                }
            }
        }
    }

    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            p.openInventory(new PickaxeEnchantGUI().getInv(p));
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
    public Set<EnchantType> getDisabledMessages() {
        return disabledMessages;
    }

    public String getRename() {
        return rename;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public void incrementBlocksBroken() {
        this.blocksBroken += 1;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }
}
