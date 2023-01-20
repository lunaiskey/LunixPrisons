package io.github.lunaiskey.lunixprison.listeners;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.gui.RenameTagConfirmGUI;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.mines.commands.CommandPMine;
import io.github.lunaiskey.lunixprison.modules.mines.inventories.*;
import io.github.lunaiskey.lunixprison.modules.pickaxe.*;
import io.github.lunaiskey.lunixprison.modules.player.inventories.*;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.items.BoosterItem;
import io.github.lunaiskey.lunixprison.modules.items.items.Voucher;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.modules.pickaxe.enchants.MineBomb;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.apache.commons.lang3.tuple.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private LunixPrison plugin;
    private PMineManager pMineManager;
    private PlayerManager playerManager;
    private Map<UUID, LunixPlayer> playerMap;

    public PlayerEvents(LunixPrison plugin) {
        this.plugin = plugin;
        pMineManager = plugin.getPMineManager();
        playerManager = plugin.getPlayerManager();
        playerMap = playerManager.getPlayerMap();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        Location blockLoc = block.getLocation();
        ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
        //PyrexPickaxe pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).getPickaxe();
        if (e.isCancelled()) {
            return;
        }
        ItemID itemID = NBTTags.getItemID(mainHand);
        if (itemID != null) {
            switch (itemID) {
                case BOOSTER -> new BoosterItem(mainHand).onBlockBreak(e);
                case LUNIX_PICKAXE -> {
                    if (playerMap.containsKey(p.getUniqueId())) {
                        LunixPickaxe pickaxe = playerMap.get(p.getUniqueId()).getPickaxe();
                        pickaxe.onBlockBreak(e);
                    } else {
                        if (blockLoc.getWorld() == Bukkit.getWorld(PMineWorld.getWorldName())) {
                            Pair<Integer,Integer> gridLoc = pMineManager.getGridLocation(block.getLocation());
                            PMine pMine = LunixPrison.getPlugin().getPMineManager().getPMine(gridLoc.getLeft(), gridLoc.getRight());
                            if (pMine != null) {
                                if (pMine.isInMineRegion(block.getLocation())) {
                                    e.setDropItems(false);
                                    e.setExpToDrop(0);
                                    pMine.addMineBlocks(1);
                                }
                            }
                        }
                    }
                }
                default -> {
                    if (LunixPrison.getPlugin().getItemManager().getItemMap().containsKey(itemID)) {
                        LunixPrison.getPlugin().getItemManager().getItemMap().get(itemID).onBlockBreak(e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        CompoundTag tag = NBTTags.getLunixDataMap(e.getItemInHand());
        if (tag.contains("id")) {
            String id = tag.getString("id");
            try {
                ItemID itemID = ItemID.valueOf(id);
                if (LunixPrison.getPlugin().getItemManager().getItemMap().containsKey(itemID)) {
                    LunixPrison.getPlugin().getItemManager().getItemMap().get(itemID).onBlockPlace(e);
                }
                if (itemID == ItemID.BOOSTER) {
                    new BoosterItem(e.getItemInHand()).onBlockPlace(e);
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
        Map<UUID, LunixPlayer> playerMap = LunixPrison.getPlugin().getPlayerManager().getPlayerMap();
        if (!playerMap.containsKey(p.getUniqueId())) {
            LunixPrison.getPlugin().getPlayerManager().createLunixPlayer(p.getUniqueId());
        } else {
            LunixPlayer lunixPlayer = playerMap.get(p.getUniqueId());
            if (!lunixPlayer.getName().equals(p.getName())) {
                lunixPlayer.setName(p.getName());
            }
        }
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        if (mine == null) {
            LunixPrison.getPlugin().getPMineManager().newPMine(p.getUniqueId());
        } else {
            mine.scheduleReset();
        }
        LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
        LunixPrison.getPlugin().getPickaxeHandler().hasOrGivePickaxe(p);
        LunixPrison.getPlugin().getSavePending().add(p.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        LunixPlayer player = playerManager.getPlayerMap().get(e.getPlayer().getUniqueId());
        LunixPrison.getPlugin().getSavePending().remove(e.getPlayer().getUniqueId());
        player.save();
        CommandPMine.getResetCooldown().remove(e.getPlayer().getUniqueId());
        MineBomb.getCooldownMap().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        Player player = e.getPlayer();
        //player.sendMessage(to.getWorld().getName() +" "+ from.getWorld().getName() + " " + e.getCause().name());
        if (to.getWorld() != null) {
            if (to.getWorld().getName().equals(PMineWorld.getWorldName())) {
                if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()-> {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    });
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv.getHolder() instanceof LunixHolder) {
            LunixHolder holder = (LunixHolder) inv.getHolder();
            holder.getInvType().getInventory().onClick(e);
            return;
        }
        if (e.getView().getType() == InventoryType.CRAFTING) {
            switch (e.getRawSlot()) {
                case 5,6,7,8 -> {
                    CompoundTag tag = NBTTags.getLunixDataMap(e.getCurrentItem());
                    if (tag.getString("id").toUpperCase().contains("PYREX_ARMOR_")) {
                        e.setCancelled(true);
                        p.sendMessage(StringUtil.color("&cTo unequip this armor please do so from /armor."));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(InventoryDragEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof LunixHolder) {
            LunixHolder holder = (LunixHolder) inv.getHolder();
            holder.getInvType().getInventory().onDrag(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof LunixHolder) {
            LunixHolder holder = (LunixHolder) inv.getHolder();
            holder.getInvType().getInventory().onOpen(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof LunixHolder) {
            LunixHolder holder = (LunixHolder) inv.getHolder();
            holder.getInvType().getInventory().onClose(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || item.getType() == Material.AIR) return;
        ItemID id = NBTTags.getItemID(item);
        if (id == null) return;
        switch (id) {
            case LUNIX_PICKAXE -> {
                LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
                if (lunixPlayer == null) return;
                lunixPlayer.getPickaxe().onInteract(e);
            }
            case BOOSTER -> new BoosterItem(e.getItem()).onInteract(e);
            case VOUCHER -> {
                Pair<CurrencyType, BigInteger> pair = NBTTags.getVoucherValue(e.getItem());
                if (pair != null) {
                    new Voucher(pair.getLeft(),pair.getRight()).onInteract(e);
                } else {
                    new Voucher(null,null).onInteract(e);
                }
            }
            default -> {
                LunixItem lunixItem = LunixPrison.getPlugin().getItemManager().getItemMap().get(id);
                if (lunixItem == null) return;
                LunixPrison.getPlugin().getItemManager().getItemMap().get(id).onInteract(e);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItemDrop().getItemStack();
        CompoundTag pyrexData = NBTTags.getLunixDataMap(item);
        if (pyrexData.contains("id")) {
            if (pyrexData.getString("id").equalsIgnoreCase(PickaxeManager.getLunixPickaxeId())) {
                e.setCancelled(true);
                LunixPickaxe pickaxe = plugin.getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxe();
                for (EnchantType enchantType : pickaxe.getEnchants().keySet()) {
                    if (!pickaxe.getDisabledEnchants().contains(enchantType)) {
                        LunixEnchant enchant = plugin.getPickaxeHandler().getEnchantments().get(enchantType);
                        if (enchant.isEnabled()) {
                            enchant.onDrop(e,pickaxe.getEnchants().get(enchantType));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
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
            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineBlocksGUI().getInv(p)));
            PMineBlocksGUI.getEditMap().remove(p.getUniqueId());
        } else if (ArmorUpgradeGUI.getCustomColorMap().containsKey(p.getUniqueId())) {
            Map<UUID, ArmorSlot> map = ArmorUpgradeGUI.getCustomColorMap();
            ArmorSlot type = map.get(p.getUniqueId());
            boolean equipped = lunixPlayer.isArmorEquiped();
            try {
                Armor armor = lunixPlayer.getArmor().get(type);
                int color = Numbers.hexToInt(strippedMessage);
                if (color <= 0xFFFFFF && color >= 0) {
                    armor.setCustomColor(Color.fromRGB(color));
                    if (equipped) {
                        p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                    }
                } else {
                    p.sendMessage(StringUtil.color("&cInvalid Color Code."));
                }
            } catch (Exception ignored) {
                p.sendMessage(StringUtil.color("&cInvalid Color Code."));
            }
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new ArmorUpgradeGUI(type).getInv(p)));
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
                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI().getInv(p)));
            } catch (NumberFormatException ignored) {
                p.sendMessage(StringUtil.color("&cInvalid Number."));

            }
            PMineSettingsGUI.getTaxEditSet().remove(p.getUniqueId());
        } else if (PMineSettingsGUI.getKickPlayerSet().contains(p.getUniqueId())) {
            Player kickPlayer = Bukkit.getPlayer(strippedMessage);
            e.setCancelled(true);
            if (kickPlayer != null) {
                if (kickPlayer.getUniqueId() != p.getUniqueId()) {
                    PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
                    if (mine.isInMineIsland(kickPlayer)) {
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()-> LunixPrison.getPlugin().getPMineManager().getPMine(kickPlayer.getUniqueId()).teleportToCenter(kickPlayer,false,true));
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
            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI().getInv(p)));
        }
        if (lunixPlayer.getChatReplyType() != null) {
            switch (lunixPlayer.getChatReplyType()) {
                case RENAME_TAG -> {
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->{
                        boolean hasRenameTagInInv = false;
                        for (ItemStack item : p.getInventory().getStorageContents()) {
                            if (NBTTags.getItemID(item) == ItemID.RENAME_TAG) {
                                hasRenameTagInInv = true;
                                break;
                            }
                        }
                        if (hasRenameTagInInv) {
                            p.openInventory(new RenameTagConfirmGUI(ChatColor.stripColor(e.getMessage()),p.getUniqueId()).getInv(p));
                        } else {
                            p.sendMessage(StringUtil.color("&cNo Rename Tag Found!"));
                        }
                        lunixPlayer.setChatReplyType(null);

                    });
                }
            }
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
        CompoundTag oldMap = NBTTags.getLunixDataMap(oldItem);
        CompoundTag newMap = NBTTags.getLunixDataMap(newItem);
        LunixPickaxe pickaxe = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).getPickaxe();
        if (oldMap.contains("id")) {
            // is custom pickaxe
            if (oldMap.getString("id").equals(PickaxeManager.getLunixPickaxeId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onUnEquip(p,oldItem,pickaxe.getEnchants().get(type));
                }
            }
        }
        if (newMap.contains("id")) {
            // is custom pickaxe
            if (newMap.getString("id").equals(PickaxeManager.getLunixPickaxeId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    if (!pickaxe.getDisabledEnchants().contains(type)) {
                        LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onEquip(p,newItem,pickaxe.getEnchants().get(type));
                    }
                }
            }
        }
    }
}
