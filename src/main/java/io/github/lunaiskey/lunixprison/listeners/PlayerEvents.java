package io.github.lunaiskey.lunixprison.listeners;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.InventoryManager;
import io.github.lunaiskey.lunixprison.modules.armor.inventories.ArmorUpgradeGUI;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.items.gui.RenameTagConfirmGUI;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaBoosterItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaCurrencyVoucher;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.mines.commands.CommandPMine;
import io.github.lunaiskey.lunixprison.modules.mines.inventories.*;
import io.github.lunaiskey.lunixprison.modules.pickaxe.*;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.items.BoosterItem;
import io.github.lunaiskey.lunixprison.modules.items.items.CurrencyVoucher;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.modules.player.ChatReplyType;
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

import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftRecipe;
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
import org.bukkit.inventory.RecipeChoice;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private LunixPrison plugin;
    private PMineManager pMineManager;
    private PlayerManager playerManager;
    private InventoryManager inventoryManager;
    private Map<UUID, LunixPlayer> playerMap;

    public PlayerEvents(LunixPrison plugin) {
        this.plugin = plugin;
        pMineManager = PMineManager.get();
        playerManager = PlayerManager.get();
        inventoryManager = InventoryManager.get();
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
                case BOOSTER -> new BoosterItem().onBlockBreak(e);
                case LUNIX_PICKAXE -> {
                    if (playerMap.containsKey(p.getUniqueId())) {
                        PickaxeStorage pickaxe = playerMap.get(p.getUniqueId()).getPickaxeStorage();
                        pickaxe.onBlockBreak(e);
                    } else {
                        if (blockLoc.getWorld() == Bukkit.getWorld(PMineWorld.getWorldName())) {
                            Pair<Integer,Integer> gridLoc = pMineManager.getGridLocation(block.getLocation());
                            PMine pMine = PMineManager.get().getPMine(gridLoc.getLeft(), gridLoc.getRight());
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
                    LunixItem lunixItem = ItemManager.get().getLunixItem(itemID);
                    if (lunixItem != null) {
                        lunixItem.onBlockBreak(e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        ItemID itemID = NBTTags.getItemID(e.getItemInHand());
        if (itemID != null) {
            LunixItem lunixItem = ItemManager.get().getLunixItem(itemID);
            if (lunixItem != null) {
                lunixItem.onBlockPlace(e);
            }
            if (itemID == ItemID.BOOSTER) {
                new BoosterItem().onBlockPlace(e);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Map<UUID, LunixPlayer> playerMap = PlayerManager.get().getPlayerMap();
        if (!playerMap.containsKey(p.getUniqueId())) {
            PlayerManager.get().createLunixPlayer(p.getUniqueId());
        } else {
            LunixPlayer lunixPlayer = playerMap.get(p.getUniqueId());
            if (!lunixPlayer.getName().equals(p.getName())) {
                lunixPlayer.setName(p.getName());
            }
        }
        PMine mine = PMineManager.get().getPMine(p.getUniqueId());
        if (mine == null) {
            PMineManager.get().newPMine(p.getUniqueId());
        } else {
            mine.scheduleReset();
        }
        PickaxeManager.get().updateInventoryPickaxe(p);
        PickaxeManager.get().hasOrGivePickaxe(p);
        LunixPrison.getPlugin().getSavePending().add(p.getUniqueId());
        if (p.getWorld().getName().equals(PMineWorld.getWorldName())) {
            p.setAllowFlight(true);
            p.setFlying(true);
            PMine currentPMine = PMineManager.get().getPMineAtPlayer(p);
            if (currentPMine != null) {
                Bukkit.getScheduler().runTask(plugin,()->currentPMine.sendBorder(p));
            }
        }
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
        if (to == null || to.getWorld() == null) {
            return;
        }
        PMine fromPMine = PMineManager.get().getPMineAtPlayer(player);
        PMine toPMine = PMineManager.get().getPMineAtLocation(to);
        if (to.getWorld().getName().equals(PMineWorld.getWorldName())) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()-> {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    }
                    if (toPMine == null) {
                        player.setWorldBorder(null);
                        return;
                    }
                    if (fromPMine == null || !toPMine.getOwner().equals(fromPMine.getOwner())) {
                        toPMine.sendBorder(player);
                    }
                });
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
        inventoryManager.onClick(e);
        if (e.getView().getType() == InventoryType.CRAFTING) {
            switch (e.getRawSlot()) {
                case 5,6,7,8 -> {
                    CompoundTag tag = NBTTags.getLunixDataTag(e.getCurrentItem());
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
        inventoryManager.onDrag(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent e) {
        inventoryManager.onOpen(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        inventoryManager.onClose(e);
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
                LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
                if (lunixPlayer == null) return;
                lunixPlayer.getPickaxeStorage().onInteract(e);
            }
            case BOOSTER -> new BoosterItem().onInteract(e);
            case CURRENCY_VOUCHER -> new CurrencyVoucher().onInteract(e);
            default -> {
                LunixItem lunixItem = ItemManager.get().getLunixItem(id);
                if (lunixItem == null) return;
                lunixItem.onInteract(e);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        LunixPlayer lunixPlayer = PlayerManager.get().getLunixPlayer(p.getUniqueId());
        ItemStack item = e.getItemDrop().getItemStack();
        CompoundTag pyrexData = NBTTags.getLunixDataTag(item);
        if (pyrexData.contains("id")) {
            if (pyrexData.getString("id").equalsIgnoreCase(PickaxeManager.getLunixPickaxeId())) {
                e.setCancelled(true);
                PickaxeStorage pickaxe =lunixPlayer.getPickaxeStorage();
                for (EnchantType enchantType : pickaxe.getEnchants().keySet()) {
                    if (!pickaxe.getDisabledEnchants().contains(enchantType)) {
                        LunixEnchant enchant = PickaxeManager.get().getLunixEnchant(enchantType);
                        if (enchant.isEnabled()) {
                            enchant.onDrop(e,lunixPlayer,pickaxe.getEnchants().get(enchantType));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
        String strippedMessage = ChatColor.stripColor(e.getMessage());
        if (lunixPlayer.getChatReplyType() != null) {
            e.setCancelled(true);
            ChatReplyType replyType = lunixPlayer.getChatReplyType();
            switch (replyType) {
                case RENAME_TAG -> {
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
                    });
                }
                case PMINE_BLOCK_CHANCE_EDIT-> {
                    try {
                        double percentage = Double.parseDouble(strippedMessage);
                        if (percentage < 0) {
                            percentage = 0;
                        }
                        if (percentage > 100) {
                            percentage = 100;
                        }
                        percentage = (int) percentage;
                        double newValue = (percentage/100);
                        PMine mine = pMineManager.getPMine(p.getUniqueId());
                        mine.getComposition().put(PMineBlocksGUI.getEditMap().get(p.getUniqueId()),newValue);
                    } catch (NumberFormatException ignored) {}
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineBlocksGUI().getInv(p)));
                }
                case PMINE_TAX_EDIT -> {
                    PMine mine = pMineManager.getPMine(p.getUniqueId());
                    double tax;
                    try {
                        tax = Double.parseDouble(strippedMessage);
                    } catch (NumberFormatException ignored) {
                        p.sendMessage(StringUtil.color("&cInvalid Number."));
                        return;
                    }
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
                }
                case PMINE_KICK_PLAYER -> {
                    Player kickPlayer = Bukkit.getPlayer(strippedMessage);
                    if (kickPlayer != null) {
                        if (kickPlayer.getUniqueId() != p.getUniqueId()) {
                            PMine mine = PMineManager.get().getPMine(p.getUniqueId());
                            if (mine.isInMineIsland(kickPlayer)) {
                                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()-> PMineManager.get().getPMine(kickPlayer.getUniqueId()).teleportToCenter(kickPlayer,false,true));
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
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI().getInv(p)));
                }
                case ARMOR_CUSTOM_COLOR_EDIT -> {
                    ArmorSlot type = ArmorUpgradeGUI.getCustomColorSlotMap().get(p.getUniqueId());
                    if (type == null) {
                        p.sendMessage(ChatColor.RED+"Failed to find Armor Piece, please report this!");
                        lunixPlayer.setChatReplyType(null);
                        return;
                    }
                    boolean equipped = lunixPlayer.isArmorEquiped();
                    try {
                        Armor armor = lunixPlayer.getArmor().get(type);
                        int color = Numbers.hexToInt(strippedMessage);
                        if (color <= 0xFFFFFF && color >= 0) {
                            armor.setCustomColor(Color.fromRGB(color));
                            if (equipped) {
                                p.getInventory().setItem(type.getEquipmentSlot(),armor.getItemStack());
                            }
                        } else {
                            p.sendMessage(StringUtil.color("&cInvalid Color Code."));
                        }
                    } catch (Exception ignored) {
                        p.sendMessage(StringUtil.color("&cInvalid Color Code."));
                    }
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new ArmorUpgradeGUI(type).getInv(p)));
                }
            }
            lunixPlayer.setChatReplyType(null);
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
        CompoundTag oldMap = NBTTags.getLunixDataTag(oldItem);
        CompoundTag newMap = NBTTags.getLunixDataTag(newItem);
        LunixPlayer lunixPlayer = PlayerManager.get().getLunixPlayer(p.getUniqueId());
        PickaxeStorage pickaxe = lunixPlayer.getPickaxeStorage();
        if (oldMap.contains("id")) {
            // is custom pickaxe
            if (oldMap.getString("id").equals(PickaxeManager.getLunixPickaxeId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    PickaxeManager.get().getLunixEnchant(type).onUnEquip(p,lunixPlayer,oldItem,pickaxe.getEnchants().get(type));
                }
            }
        }
        if (newMap.contains("id")) {
            // is custom pickaxe
            if (newMap.getString("id").equals(PickaxeManager.getLunixPickaxeId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    if (!pickaxe.getDisabledEnchants().contains(type)) {
                        PickaxeManager.get().getLunixEnchant(type).onEquip(p,lunixPlayer,newItem,pickaxe.getEnchants().get(type));
                    }
                }
            }
        }
    }
}
