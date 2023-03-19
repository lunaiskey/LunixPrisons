package io.github.lunaiskey.lunixprison.modules.pickaxe.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PickaxeEnchantGUI implements LunixInventory {

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixHolder("Enchantments",54, LunixInvType.PICKAXE_ENCHANTS).getInventory();
        init(inv,player);
        return inv;
    }

    private void init(Inventory inv, Player p) {
        PickaxeStorage pickaxe = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        for(int i = 0; i < inv.getSize();i++) {
            switch(i) {
                case 20,21,22,23,24,29,30,31,32,33,38,39,40,41,42 -> {
                    EnchantType type = EnchantType.getEnchantFromSlot(i);
                    if (type != null && LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().containsKey(type)) {
                        inv.setItem(i,getEnchantPlaceholder(type,p));
                    } else {
                        inv.setItem(i, ItemBuilder.getComingSoon());
                    }
                }
                case 11 -> inv.setItem(i,getEnchantToggleIcon());
                case 13 -> inv.setItem(i, pickaxe.getItemStack());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    private ItemStack getEnchantToggleIcon() {
        String name = "&aToggle Enchants";
        Material mat = Material.GREEN_DYE;
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&eClick to view!"));
        return ItemBuilder.createItem(name,mat,lore);
    }

    private ItemStack getEnchantPlaceholder(EnchantType type, Player p) {
        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        Material mat = Material.ENCHANTED_BOOK;
        if (!enchant.isEnabled()) {
            mat = Material.BARRIER;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        CurrencyType currencyType = enchant.getCurrencyType();
        PickaxeStorage pickaxe = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        int level = pickaxe.getEnchants().getOrDefault(type, 0);
        meta.setDisplayName(StringUtil.color("&b"+enchant.getName()+" &8[&7"+level+" -> "+(level+1)+"&8]"));
        List<String> lore = new ArrayList<>();
        if (enchant.getDescription() != null && !enchant.getDescription().isEmpty()) {
            for (String desc : enchant.getDescription()) {
                lore.add(StringUtil.color("&7"+desc));
            }
        }
        lore.add(" ");
        if (enchant.isEnabled()) {
            if (level >= enchant.getMaxLevel()) {
                lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
                lore.add(" ");
                lore.add(StringUtil.color("&7Enchant is max level!"));
            } else {
                lore.add(StringUtil.color("&7Cost: "+currencyType.getColorCode()+currencyType.getUnicode()+"&f"+ Numbers.formattedNumber(enchant.getCost(level))));
                lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
                lore.add(" ");
                lore.add(StringUtil.color("&eL-Click to purchase levels."));
            }
        } else {
            lore.add(StringUtil.color("&cEnchantment is currently disabled."));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void updateInventory(Player player) {

    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        switch (slot) {
            case 11 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantToggleGUI().getInv(p)));
            case 20,21,22,23,24,29,30,31,32,33,38,39,40,41,42 -> {
                EnchantType type = EnchantType.getEnchantFromSlot(slot);
                if (type != null && LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().containsKey(type)) {
                    LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
                    PickaxeStorage pickaxe = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
                    int level = pickaxe.getEnchants().getOrDefault(type, 0);
                    if (enchant.isEnabled()) {
                        if (level < enchant.getMaxLevel()) {
                            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantAddLevelsGUI(type).getInv(p)));
                        } else {
                            p.sendMessage(StringUtil.color("&cYou have maxed out this enchantment."));
                        }
                    } else {
                        p.sendMessage(StringUtil.color("&cThis enchantment is currently unavailable."));
                    }
                }
            }
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
