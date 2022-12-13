package io.github.lunaiskey.lunixprison.pickaxe.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.lunixprison.pickaxe.LunixPickaxe;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PickaxeEnchantToggleGUI implements LunixInventory {

    private String name = "Toggle Enchants";
    private int size = 54;
    private Player p;
    private LunixPlayer lunixPlayer;
    private LunixPickaxe lunixPickaxe;
    private Set<EnchantType> disabledEnchants;

    private Inventory inv = new LunixHolder(name,size, LunixInvType.PICKAXE_ENCHANTS_TOGGLE).getInventory();

    private Map<Integer, EnchantType> enchantLocation = new HashMap<>();

    public PickaxeEnchantToggleGUI(Player p) {
        this.p = p;
        this.lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        this.lunixPickaxe = lunixPlayer.getPickaxe();
        this.disabledEnchants = lunixPickaxe.getDisabledEnchants();
        enchantLocation.put(11,EnchantType.EFFICIENCY);
        enchantLocation.put(12,EnchantType.HASTE);
        enchantLocation.put(13,EnchantType.SPEED);
        enchantLocation.put(14,EnchantType.JUMP_BOOST);
        enchantLocation.put(15,EnchantType.NIGHT_VISION);
        enchantLocation.put(20,EnchantType.FORTUNE);
        enchantLocation.put(21,EnchantType.JACK_HAMMER);
        enchantLocation.put(22,EnchantType.STRIKE);
        enchantLocation.put(23,EnchantType.EXPLOSIVE);
        enchantLocation.put(24,EnchantType.MINE_BOMB);
        enchantLocation.put(29,EnchantType.NUKE);
        enchantLocation.put(30,EnchantType.GEM_FINDER);
        enchantLocation.put(31,EnchantType.KEY_FINDER);
        enchantLocation.put(32,EnchantType.LOOT_FINDER);
        enchantLocation.put(33,EnchantType.XP_BOOST);
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 11,12,13,14,15,20,21,22,23,24,29,30,31,32,33 -> inv.setItem(i, getEnchantPlaceholder(enchantLocation.get(i)));
                case 0 -> inv.setItem(i,ItemBuilder.getGoBack());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    private ItemStack getEnchantPlaceholder(EnchantType type) {
        PyrexEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        String name = StringUtil.color("&b"+enchant.getName());
        String status = disabledEnchants.contains(type) ? "&cDisabled" : "&aEnabled";
        Material mat = disabledEnchants.contains(type) ? Material.GRAY_DYE : Material.LIME_DYE;
        List<String> lore = new ArrayList<>();
        if (enchant.getDescription() != null && !enchant.getDescription().isEmpty()) {
            for (String desc : enchant.getDescription()) {
                lore.add(StringUtil.color("&7"+desc));
            }
            lore.add(" ");
        }
        lore.add(StringUtil.color("&7Status: "+status));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to toggle!"));
        return ItemBuilder.createItem(name,mat,lore);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Inventory inv = e.getClickedInventory();
        if (slot == 0) {
            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantGUI(p).getInv()));
            return;
        }
        if (enchantLocation.containsKey(slot)) {
            EnchantType enchantType = enchantLocation.get(slot);
            if (disabledEnchants.contains(enchantType)) {
                disabledEnchants.remove(enchantType);
            } else {
                disabledEnchants.add(enchantType);
            }
            inv.setItem(slot,getEnchantPlaceholder(enchantType));
        }
    }
}
