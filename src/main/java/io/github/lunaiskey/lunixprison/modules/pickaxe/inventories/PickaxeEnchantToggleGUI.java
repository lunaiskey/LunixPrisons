package io.github.lunaiskey.lunixprison.modules.pickaxe.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeManager;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PickaxeEnchantToggleGUI implements LunixInventory {

    private final boolean isEnchantToggle;

    public PickaxeEnchantToggleGUI(boolean isEnchantToggle) {
        this.isEnchantToggle = isEnchantToggle;
    }

    @Override
    public Inventory getInv(Player player) {
        String title = isEnchantToggle ? "s" : " Messages";
        LunixInvType invType = isEnchantToggle ? LunixInvType.PICKAXE_ENCHANTS_TOGGLE : LunixInvType.PICKAXE_ENCHANTS_TOGGLE_MESSAGES ;
        Inventory inv = new LunixHolder("Toggle Enchant"+title,54, invType).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        LunixHolder holder = (LunixHolder) inv.getHolder();
        boolean isEnchantToggle = holder.getInvType() == LunixInvType.PICKAXE_ENCHANTS_TOGGLE;
        for (int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 11,12,13,14,15,20,21,22,23,24,29,30,31,32,33 -> {
                    EnchantType type = EnchantType.getEnchantFromSlot(i);
                    if (type == null) {
                        inv.setItem(i,ItemBuilder.getDefaultFiller());
                    } else {
                        inv.setItem(i, getEnchantPlaceholder(type,p,isEnchantToggle));
                    }
                }
                case 0 -> inv.setItem(i,ItemBuilder.getGoBack());
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
            }
        }
    }

    private ItemStack getEnchantPlaceholder(EnchantType type, Player p, boolean isEnchantToggle) {
        PickaxeStorage pickaxeStorage = PlayerManager.get().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        LunixEnchant enchant = PickaxeManager.get().getLunixEnchant(type);
        String name = StringUtil.color("&b" + enchant.getName() + (isEnchantToggle?"":" Message"));
        boolean isEnabled = isEnchantToggle ? pickaxeStorage.getDisabledEnchants().contains(type) : pickaxeStorage.getDisabledMessages().contains(type);
        String status;
        Material mat;
        if (isEnabled) {
            status = "&cDisabled";
            mat = Material.GRAY_DYE;
        } else {
            status = "&aEnabled";
            mat = Material.LIME_DYE;
        }
        List<String> lore = new ArrayList<>();
        if (enchant.getDescription() != null && !enchant.getDescription().isEmpty()) {
            for (String desc : enchant.getDescription()) {
                lore.add(StringUtil.color("&7" + desc));
            }
            lore.add(" ");
        }
        lore.add(StringUtil.color("&7Status: " + status));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to toggle!"));
        return ItemBuilder.createItem(name, mat, lore);
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PickaxeStorage pickaxeStorage = PlayerManager.get().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        LunixHolder holder = (LunixHolder) e.getInventory().getHolder();
        boolean isEnchantToggle = holder.getInvType() == LunixInvType.PICKAXE_ENCHANTS_TOGGLE;
        Set<EnchantType> disabledEnchants = isEnchantToggle ? pickaxeStorage.getDisabledEnchants() : pickaxeStorage.getDisabledMessages();
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Inventory inv = e.getClickedInventory();
        if (slot == 0) {
            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantGUI().getInv(p)));
            return;
        }

        EnchantType type = EnchantType.getEnchantFromSlot(slot);
        if (type == null) {
            return;
        }
        if (disabledEnchants.contains(type)) {
            disabledEnchants.remove(type);
        } else {
            disabledEnchants.add(type);
        }
        inv.setItem(slot,getEnchantPlaceholder(type,p,isEnchantToggle));
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
}
