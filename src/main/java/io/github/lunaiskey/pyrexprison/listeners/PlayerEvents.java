package io.github.lunaiskey.pyrexprison.listeners;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.commands.CommandPMine;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.pyrexitems.Voucher;
import io.github.lunaiskey.pyrexprison.mines.*;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.mines.inventories.*;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.pickaxe.*;
import io.github.lunaiskey.pyrexprison.pickaxe.inventories.PickaxeAddLevelsGUI;
import io.github.lunaiskey.pyrexprison.pickaxe.inventories.PickaxeEnchantGUI;
import io.github.lunaiskey.pyrexprison.pickaxe.inventories.PickaxeEnchantToggleGUI;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.ViewPlayerHolder;
import io.github.lunaiskey.pyrexprison.player.armor.Armor;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorType;
import io.github.lunaiskey.pyrexprison.player.inventories.ArmorGUI;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorPyrexHolder;
import io.github.lunaiskey.pyrexprison.player.inventories.ArmorUpgradeGUI;
import io.github.lunaiskey.pyrexprison.player.inventories.GemStoneGUI;
import io.github.lunaiskey.pyrexprison.player.inventories.ViewPlayerGUI;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.apache.commons.lang3.tuple.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private PyrexPrison plugin;
    private PMineManager pMineManager;
    private PlayerManager playerManager;

    public PlayerEvents(PyrexPrison plugin) {
        this.plugin = plugin;
        pMineManager = plugin.getPmineManager();
        playerManager = plugin.getPlayerManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
        PyrexPickaxe pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).getPickaxe();
        if (e.isCancelled()) {
            return;
        }
        if (block.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLoc = pMineManager.getGridLocation(block.getLocation());
            PMine pMine = PyrexPrison.getPlugin().getPmineManager().getPMine(gridLoc.getLeft(), gridLoc.getRight());
            if (pMine != null) {
                if (pMine.isInMineRegion(block.getLocation())) {
                    e.setDropItems(false);
                    e.setExpToDrop(0);
                    pMine.addMineBlocks(1);
                    CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(mainHand);
                    if (pyrexDataMap.contains("id")) {
                        // is custom pickaxe
                        if (pyrexDataMap.getString("id").equals(PickaxeHandler.getId())) {
                            for (EnchantType type : pickaxe.getEnchants().keySet()) {
                                PyrexEnchant enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
                                if (enchant.isEnabled()) {
                                    if (!pickaxe.getDisabledEnchants().contains(type)) {
                                        enchant.onBlockBreak(e,pickaxe.getEnchants().get(type));
                                    }
                                }
                            }
                            PyrexPlayer player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId());
                            PyrexPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),1);
                            p.giveExp(1+player.getXPBoostTotal());
                            PyrexPrison.getPlugin().getPlayerManager().tickGemstoneCount(p);
                            pickaxe.setBlocksBroken(pickaxe.getBlocksBroken()+1);
                            PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                        }
                    }
                }
            }
        }
        CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(mainHand);
        if (pyrexDataMap.contains("id")) {
            try {
                ItemID itemID = ItemID.valueOf(pyrexDataMap.getString("id"));
                if (PyrexPrison.getPlugin().getItemManager().getItemMap().containsKey(itemID)) {
                    PyrexPrison.getPlugin().getItemManager().getItemMap().get(itemID).onBlockBreak(e);
                }
            } catch (Exception ignored) {}
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        CompoundTag tag = NBTTags.getPyrexDataMap(e.getItemInHand());
        if (tag.contains("id")) {
            String id = tag.getString("id");
            try {
                ItemID itemID = ItemID.valueOf(id);
                if (PyrexPrison.getPlugin().getItemManager().getItemMap().containsKey(itemID)) {
                    PyrexPrison.getPlugin().getItemManager().getItemMap().get(itemID).onBlockPlace(e);
                }
            } catch (Exception ignored) {}
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld().getName().equals(PMineWorld.getWorldName())) {
            p.setAllowFlight(true);
            p.setFlying(true);
        }
        Map<UUID, PyrexPlayer> playerMap = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap();
        if (!playerMap.containsKey(p.getUniqueId())) {
            PyrexPrison.getPlugin().getPlayerManager().createPyrexPlayer(p.getUniqueId());
        }
        PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId());
        if (mine == null) {
            PyrexPrison.getPlugin().getPmineManager().newPMine(p.getUniqueId());
        } else {
            mine.scheduleReset();
        }
        PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
        PyrexPrison.getPlugin().getPickaxeHandler().hasOrGivePickaxe(p);
        PyrexPrison.getPlugin().getSavePending().add(p.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PyrexPlayer player = playerManager.getPlayerMap().get(e.getPlayer().getUniqueId());
        PyrexPrison.getPlugin().getSavePending().remove(e.getPlayer().getUniqueId());
        player.save();
        CommandPMine.getResetCooldown().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getTo().getWorld() != null && e.getTo().getWorld().getName().equals(PMineWorld.getWorldName())) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN && e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                e.getPlayer().setAllowFlight(true);
                e.getPlayer().setFlying(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getWhoClicked();
            if (inv.getHolder() instanceof PyrexHolder) {
                PyrexHolder holder = (PyrexHolder) inv.getHolder();
                switch(holder.getInvType()) {
                    case PMINE_MAIN -> new PMineGUI(p).onClick(e);
                    case PMINE_BLOCKS -> new PMineBlocksGUI(p).onClick(e);
                    case PICKAXE_ENCHANTS -> new PickaxeEnchantGUI(p).onClick(e);
                    case PICKAXE_ENCHANTS_ADD_LEVELS -> {
                        if (holder instanceof EnchantPyrexHolder) {
                            EnchantPyrexHolder enchantHolder = (EnchantPyrexHolder) holder;
                            new PickaxeAddLevelsGUI(p,enchantHolder.getType()).onClick(e);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    case PICKAXE_ENCHANTS_TOGGLE -> new PickaxeEnchantToggleGUI(p).onClick(e);
                    case ARMOR -> new ArmorGUI(p).onClick(e);
                    case ARMOR_UPGRADES -> {
                        if (holder instanceof ArmorPyrexHolder) {
                            ArmorPyrexHolder armorPyrexHolder = (ArmorPyrexHolder) holder;
                            new ArmorUpgradeGUI(p,armorPyrexHolder.getType()).onClick(e);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    case GEMSTONES -> new GemStoneGUI(p).onClick(e);
                    case VIEW_PLAYER -> {
                        if (holder instanceof ViewPlayerHolder) {
                            ViewPlayerHolder viewPlayerHolder = (ViewPlayerHolder) holder;
                            new ViewPlayerGUI(viewPlayerHolder.getPlayer()).onClick(e);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    case PMINE_UPGRADES -> new PMineUpgradesGUI(p).onClick(e);
                    case PMINE_PUBLIC_MINES -> new PMinePublicGUI().onClick(e);
                    case PMINE_SETTINGS -> new PMineSettingsGUI(p).onClick(e);
                }
            }
            if (e.getView().getType() == InventoryType.CRAFTING) {
                switch (e.getRawSlot()) {
                    case 5,6,7,8 -> {
                        CompoundTag tag = NBTTags.getPyrexDataMap(e.getCurrentItem());
                        if (tag.getString("id").toUpperCase().contains("PYREX_ARMOR_")) {
                            e.setCancelled(true);
                            p.sendMessage(StringUtil.color("&cTo unequip this armor please do so from /armor."));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof PyrexHolder) {
            PyrexHolder holder = (PyrexHolder) inv.getHolder();
            switch(holder.getInvType()) {
                case PMINE_PUBLIC_MINES -> new PMinePublicGUI().onClose(e);
                case PMINE_UPGRADES -> new PMineUpgradesGUI(p).onClose(e);
                case PMINE_BLOCKS -> new PMineBlocksGUI(p).onClose(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof PyrexHolder) {
            PyrexHolder holder = (PyrexHolder) inv.getHolder();
            switch(holder.getInvType()) {
                case PMINE_PUBLIC_MINES -> new PMinePublicGUI().onOpen(e);
                case PMINE_BLOCKS -> new PMineBlocksGUI(p).onOpen(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
            if (NBTTags.hasCustomTagContainer(e.getItem(),"voucher")) {
                Pair<CurrencyType, BigInteger> pair = NBTTags.getVoucherValue(e.getItem());
                new Voucher(pair.getRight(),pair.getLeft()).onInteract(e);
            }
        }
        CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(e.getItem());
        if (pyrexDataMap.contains("id")) {
            // is custom pickaxe
            if (pyrexDataMap.getString("id").equals(PickaxeHandler.getId())) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    p.openInventory(new PickaxeEnchantGUI(p).getInv());
                }
            }
            try {
                ItemID itemID = ItemID.valueOf(pyrexDataMap.getString("id"));
                if (PyrexPrison.getPlugin().getItemManager().getItemMap().containsKey(itemID)) {
                    PyrexPrison.getPlugin().getItemManager().getItemMap().get(itemID).onInteract(e);
                }
            } catch (Exception ignored) {}
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        String strippedMessage = ChatColor.stripColor(e.getMessage());
        if (PMineBlocksGUI.getEditMap().containsKey(p.getUniqueId())) {
            try {
                double percentage = Double.parseDouble(strippedMessage);
                if (percentage < 0) {
                    percentage = 0;
                }
                if (percentage > 100) {
                    percentage = 100;
                }
                double newValue = (Math.floor(percentage)/100);
                PMine mine = pMineManager.getPMine(p.getUniqueId());
                mine.getComposition().put(PMineBlocksGUI.getEditMap().get(p.getUniqueId()),newValue);
            } catch (NumberFormatException ignored) {}
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineBlocksGUI(p).getInv()));
            PMineBlocksGUI.getEditMap().remove(p.getUniqueId());
        } else if (ArmorUpgradeGUI.getCustomColorMap().containsKey(p.getUniqueId())) {
            Map<UUID, ArmorType> map = ArmorUpgradeGUI.getCustomColorMap();
            ArmorType type = map.get(p.getUniqueId());
            try {
                Armor armor = pyrexPlayer.getArmor().get(type);
                int color = Numbers.hexToInt(strippedMessage);
                if (color <= 0xFFFFFF && color >= 0) {
                    armor.setCustomColor(Color.fromRGB(color));
                    p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                } else {
                    p.sendMessage(StringUtil.color("&cInvalid Color Code."));
                }
            } catch (Exception ignored) {
                p.sendMessage(StringUtil.color("&cInvalid Color Code."));
            }
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new ArmorUpgradeGUI(p,type).getInv()));
            ArmorUpgradeGUI.getCustomColorMap().remove(p.getUniqueId());
        } else if (PMineSettingsGUI.getTaxEditSet().contains(p.getUniqueId())) {
            e.setCancelled(true);
            try {
                PMine mine = pMineManager.getPMine(p.getUniqueId());
                double tax = Double.parseDouble(strippedMessage);
                if (tax > 100) {
                    tax = 100;
                } else if (tax < 0) {
                    tax = 0;
                } else {
                    if (tax%1 != 0) {
                        double mid = (Math.floor(tax)+0.5);
                        if (tax > mid) {
                            tax = Math.ceil(tax);
                        } else if (tax < mid){
                            tax = Math.floor(tax);
                        } else {
                            tax = mid;
                        }
                    }
                }
                mine.setMineTax(tax);
                Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI(p).getInv()));
            } catch (NumberFormatException ignored) {
                p.sendMessage(StringUtil.color("&cInvalid Number."));

            }
            PMineSettingsGUI.getTaxEditSet().remove(p.getUniqueId());
        } else if (PMineSettingsGUI.getKickPlayerSet().contains(p.getUniqueId())) {
            Player kickPlayer = Bukkit.getPlayer(strippedMessage);
            e.setCancelled(true);
            if (kickPlayer != null) {
                if (kickPlayer.getUniqueId() != p.getUniqueId()) {
                    PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId());
                    if (mine.isInMineIsland(kickPlayer)) {
                        Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->PyrexPrison.getPlugin().getPmineManager().getPMine(kickPlayer.getUniqueId()).teleportToCenter(kickPlayer,false,true));
                        kickPlayer.sendMessage(StringUtil.color("&eYou've been kicked from "+p.getName()+"'s mine. teleporting to your mine."));
                        p.sendMessage(StringUtil.color("&aSuccessfully kicked &f"+kickPlayer.getName()+" &afrom your mine."));
                    } else {
                        p.sendMessage(StringUtil.color("Player "+kickPlayer.getName()+" isn't in your mine."));
                    }
                } else {
                    p.sendMessage(StringUtil.color("&cYou can't kick yourself."));
                }
            } else {
                p.sendMessage(StringUtil.color("&cPlayer "+strippedMessage+" isn't online."));
            }
            PMineSettingsGUI.getKickPlayerSet().remove(p.getUniqueId());
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI(p).getInv()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location to = e.getTo();
        if (to.getWorld().getName().equals(PMineWorld.getWorldName())) {
            PMine mine = pMineManager.getPMine(p.getUniqueId());
            if (mine != null) {
                if (to.getBlockY() < to.getWorld().getMinHeight()) {
                    p.getVelocity().setY(0);
                    e.setTo(mine.getCenter().add(0.5,1,0.5));
                }
            }
        }
    }

    @EventHandler
    public void onEquip(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack oldItem = p.getInventory().getItem(e.getPreviousSlot()) != null ? p.getInventory().getItem(e.getPreviousSlot()) : new ItemStack(Material.AIR);
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot()) != null ? p.getInventory().getItem(e.getNewSlot()) : new ItemStack(Material.AIR);
        CompoundTag oldMap = NBTTags.getPyrexDataMap(oldItem);
        CompoundTag newMap = NBTTags.getPyrexDataMap(newItem);
        PyrexPickaxe pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).getPickaxe();
        if (oldMap.contains("id")) {
            // is custom pickaxe
            if (oldMap.getString("id").equals(PickaxeHandler.getId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onUnEquip(p,oldItem,pickaxe.getEnchants().get(type));
                }
            }
        }
        if (newMap.contains("id")) {
            // is custom pickaxe
            if (newMap.getString("id").equals(PickaxeHandler.getId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    if (!pickaxe.getDisabledEnchants().contains(type)) {
                        PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onEquip(p,newItem,pickaxe.getEnchants().get(type));
                    }
                }
            }
        }
    }
}
