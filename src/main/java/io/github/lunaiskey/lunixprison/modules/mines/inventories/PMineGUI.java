package io.github.lunaiskey.lunixprison.modules.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PMineGUI implements LunixInventory {

    @Override
    public Inventory getInv(Player p) {
        Inventory inv = new LunixHolder("Personal Mine",45, LunixInvType.PMINE_MAIN).getInventory();
        init(inv,p);
        return inv;
    }

    private void init(Inventory inv, Player p) {
        for (int i = 0; i < inv.getSize();i++) {
            switch(i) {
                case 0,9,18,27,36,45,8,17,26,35,44,53 -> inv.setItem(i, ItemBuilder.getDefaultEdgeFiller());
                case 11 -> inv.setItem(i, getPublicMines());
                case 13 -> inv.setItem(i, getInvitedMines());
                case 15 -> inv.setItem(i,getGangMines());
                case 29 -> inv.setItem(i,getPersonalMineTeleport());
                case 30 -> inv.setItem(i,getPersonalMineReset());
                case 31 -> inv.setItem(i,getPersonalMineUpgrades());
                case 32 -> inv.setItem(i,getPersonalMineBlocks());
                case 33 -> inv.setItem(i,getPersonalMineSettings());
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
            }
        }
    }

    @Override
    public void updateInventory(Player player) {

    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();
        PMine mine = PMineManager.get().getPMine(p.getUniqueId());
        if (mine == null) {
            p.sendMessage(ChatColor.RED+"Your Mine hasn't loaded correctly, Please contact an administrator.");
            return;
        }
        switch (slot) {
            case 11 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMinePublicGUI().getInv(p)));
            case 13,15 -> p.sendMessage(ChatColor.RED + "This feature is currently a work in progress, Please try again later.");
            case 29 -> {mine.teleportToCenter(p,false,false);Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), ()->p.closeInventory());}
            case 30 -> {mine.reset();Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), ()->p.closeInventory());}
            case 31 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineUpgradesGUI().getInv(p)));
            case 32 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineBlocksGUI().getInv(p)));
            case 33 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI().getInv(p)));
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }

    private ItemStack getPublicMines() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click to view mines!");
        return ItemBuilder.createItem("&aPublic Mines",Material.PAPER,lore);
    }

    private ItemStack getInvitedMines() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"All mines that you've been added to.");
        lore.add("");
        lore.add(ChatColor.YELLOW+"Click to view invited mines!");
        return ItemBuilder.createItem("&aInvited Mines &c[WIP]", Material.ITEM_FRAME, lore);
    }

    private ItemStack getGangMines() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click to teleport!");
        return ItemBuilder.createItem("&aGang Mine &c[WIP]", Material.IRON_BLOCK, lore);
    }

    private ItemStack getPersonalMineTeleport() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click to teleport!");
        return ItemBuilder.createItem("&aTeleport to Mine",Material.COMPASS, lore);
    }

    private ItemStack getPersonalMineReset() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click to reset!");
        return ItemBuilder.createItem("&aReset Mine",Material.CLOCK, lore);
    }

    private ItemStack getPersonalMineSettings() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click to manage!");
        return ItemBuilder.createItem("&aMine Settings",Material.COMMAND_BLOCK,lore);
    }

    private ItemStack getPersonalMineBlocks() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click to view menu!");
        return ItemBuilder.createItem("&aChange Mine Blocks",Material.GRASS_BLOCK, lore);
    }

    private ItemStack getPersonalMineUpgrades() {
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&eClick to view!"));
        ItemStack item = ItemBuilder.createItem("&aMine Upgrades",Material.IRON_PICKAXE,lore);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        return item;
    }
}
