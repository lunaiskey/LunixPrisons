package io.github.lunaiskey.lunixprison.modules.items.gui;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixPickaxe;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class RenameTagConfirmGUI implements LunixInventory {

    private UUID playerUUID;
    private String renameText;

    public RenameTagConfirmGUI(String renameText, UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.renameText = renameText;
    }

    public RenameTagConfirmGUI() {
        this(null,null);
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixRenameTagHolder("Confirm Rename",27,LunixInvType.RENAME_TAG_CONFIRM,renameText).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        for (int slot = 0;slot<inv.getSize();slot++) {
            switch (slot) {
                case 0,1,2,9,10,11,18,19,20 -> inv.setItem(slot,ItemBuilder.createItem("&a&lCONFIRM RENAME!", Material.LIME_STAINED_GLASS_PANE,null));
                case 6,7,8,15,16,17,24,25,26 -> inv.setItem(slot,ItemBuilder.createItem("&c&lCANCEL RENAME!", Material.RED_STAINED_GLASS_PANE,null));
                case 13 -> inv.setItem(slot,getPickaxePreview());
                default -> inv.setItem(slot, ItemBuilder.getDefaultFiller());
            }
        }
    }

    private ItemStack getPickaxePreview() {
        ItemStack item = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(playerUUID).getPickaxe().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(StringUtil.color(renameText));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        LunixPickaxe lunixPickaxe = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId()).getPickaxe();
        LunixRenameTagHolder tagHolder = (LunixRenameTagHolder) e.getView().getTopInventory().getHolder();
        switch (e.getRawSlot()) {
            case 0,1,2,9,10,11,18,19,20 -> {
                lunixPickaxe.setRename(StringUtil.color(tagHolder.getText()));
                LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(player);
                if (NBTTags.getItemID(player.getInventory().getItemInMainHand()) == ItemID.RENAME_TAG) {
                    ItemStack main = player.getInventory().getItemInMainHand();
                    main.setAmount(main.getAmount()-1);
                } else if (NBTTags.getItemID(player.getInventory().getItemInOffHand()) == ItemID.RENAME_TAG) {
                    ItemStack off = player.getInventory().getItemInOffHand();
                    off.setAmount(off.getAmount()-1);
                } else {
                    ItemStack[] itemStacks = player.getInventory().getStorageContents();
                    for (int i = 0;i<itemStacks.length;i++) {
                        if (NBTTags.getItemID(itemStacks[i]) == ItemID.RENAME_TAG) {
                            ItemStack slotItem = itemStacks[i].clone();
                            slotItem.setAmount(slotItem.getAmount()-1);
                            player.getInventory().setItem(i,slotItem);
                        }
                    }
                }
                player.sendMessage(StringUtil.color("&aSuccessfully renamed your pickaxe to "+tagHolder.getText()+"&a."));
                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), player::closeInventory);
            }
            case 6,7,8,15,16,17,24,25,26 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), player::closeInventory);
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

    public class LunixRenameTagHolder extends LunixHolder {

        private String text;

        public LunixRenameTagHolder(String name, int size, LunixInvType invType, String text) {
            super(name, size, invType);
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
