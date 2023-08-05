package io.github.lunaiskey.lunixprison.modules.pickaxe.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.pickaxe.*;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
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
        PickaxeStorage pickaxe = PlayerManager.get().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        for(int i = 0; i < inv.getSize();i++) {
            switch(i) {
                case 20,21,22,23,24,29,30,31,32,33,38,39,40,41,42 -> {
                    EnchantType type = EnchantType.getEnchantFromSlot(i);
                    if (type != null) {
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
        lore.add(StringUtil.color("&eL-Click to toggle enchants!"));
        lore.add(StringUtil.color("&bR-Click to toggle messages!"));
        return ItemBuilder.createItem(name,mat,lore);
    }

    private ItemStack getEnchantPlaceholder(EnchantType type, Player p) {
        LunixEnchant enchant = PickaxeManager.get().getLunixEnchant(type);
        Material mat = Material.ENCHANTED_BOOK;
        if (!enchant.isEnabled()) {
            mat = Material.BARRIER;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        CurrencyType currencyType = enchant.getCurrencyType();
        PickaxeStorage pickaxe = PlayerManager.get().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
        int level = pickaxe.getEnchants().getOrDefault(type, 0);
        meta.setDisplayName(StringUtil.color("&b"+enchant.getName()+" &8[&7"+level+" -> "+(level+1)+"&8]"));
        List<String> lore = new ArrayList<>();
        if (enchant.getDescription() != null && !enchant.getDescription().isEmpty()) {
            for (String desc : enchant.getDescription()) {
                lore.add(StringUtil.color("&7"+desc));
            }
        }
        lore.add(" ");
        String activationChance;
        if (enchant instanceof LunixChanceEnchant) {
            LunixChanceEnchant chanceEnchant = (LunixChanceEnchant) enchant;
            activationChance = ""+BigDecimal.valueOf(chanceEnchant.getChance(level, p)).stripTrailingZeros().toPlainString();
        } else {
            activationChance = null;
        }
        if (enchant.isEnabled()) {
            if (level >= enchant.getMaxLevel()) {
                lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
                if (activationChance != null) {
                    lore.add(" ");
                    lore.add(ChatColor.GRAY+"Activation Chance: "+ChatColor.WHITE+activationChance+"%");
                }
                lore.add(" ");
                lore.add(StringUtil.color("&7Enchant is max level!"));
            } else {
                lore.add(StringUtil.color("&7Cost: "+currencyType.getColorCode()+currencyType.getUnicode()+"&f"+ Numbers.formattedNumber(enchant.getCost(level))));
                lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
                if (activationChance != null) {
                    lore.add(" ");
                    lore.add(ChatColor.GRAY+"Activation Chance: "+ChatColor.WHITE+activationChance+"%");
                }
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
            case 11 -> {
                switch (e.getClick()) {
                    case LEFT,SHIFT_LEFT -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(LunixInvType.PICKAXE_ENCHANTS_TOGGLE.getInventory().getInv(p)));
                    case RIGHT,SHIFT_RIGHT -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(LunixInvType.PICKAXE_ENCHANTS_TOGGLE_MESSAGES.getInventory().getInv(p)));
                }
            }
            case 20,21,22,23,24,29,30,31,32,33,38,39,40,41,42 -> {
                EnchantType type = EnchantType.getEnchantFromSlot(slot);
                if (type != null) {
                    PickaxeManager.get().getLunixEnchant(type);
                    LunixEnchant enchant = PickaxeManager.get().getLunixEnchant(type);
                    PickaxeStorage pickaxe = PlayerManager.get().getPlayerMap().get(p.getUniqueId()).getPickaxeStorage();
                    int level = pickaxe.getEnchants().getOrDefault(type, 0);
                    if (enchant.isEnabled()) {
                        if (level < enchant.getMaxLevel()) {
                            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), () -> p.openInventory(new PickaxeEnchantAddLevelsGUI(type).getInv(p)));
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
