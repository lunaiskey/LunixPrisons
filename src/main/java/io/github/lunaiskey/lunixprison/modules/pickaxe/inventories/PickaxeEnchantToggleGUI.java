package io.github.lunaiskey.lunixprison.modules.pickaxe.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
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

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixHolder("Toggle Enchants",54, LunixInvType.PICKAXE_ENCHANTS_TOGGLE).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        for (int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 11,12,13,14,15,20,21,22,23,24,29,30,31,32,33 -> {
                    EnchantType type = EnchantType.getEnchantFromSlot(i);
                    if (type == null) {
                        inv.setItem(i,ItemBuilder.getDefaultFiller());
                    } else {
                        inv.setItem(i, getEnchantPlaceholder(type,p));
                    }
                }
                case 0 -> inv.setItem(i,ItemBuilder.getGoBack());
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
            }
        }
    }

    private ItemStack getEnchantPlaceholder(EnchantType type, Player p) {
        PickaxeStorage pickaxeStorage = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        String name = StringUtil.color("&b" + enchant.getName());
        String status;
        Material mat;
        if (pickaxeStorage.getDisabledEnchants().contains(type)) {
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
        PickaxeStorage pickaxeStorage = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        Set<EnchantType> disabledEnchants = pickaxeStorage.getDisabledEnchants();
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Inventory inv = e.getClickedInventory();
        if (slot == 0) {
            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantGUI().getInv(p)));
            return;
        }
        EnchantType type = EnchantType.getEnchantFromSlot(slot);
        if (type != null && LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().containsKey(type)) {
            EnchantType enchantType = type;
            if (disabledEnchants.contains(enchantType)) {
                disabledEnchants.remove(enchantType);
            } else {
                disabledEnchants.add(enchantType);
            }
            inv.setItem(slot,getEnchantPlaceholder(enchantType,p));
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
}
