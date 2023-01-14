package io.github.lunaiskey.lunixprison.modules.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.util.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.util.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorGUI implements LunixInventory {

    private final String name = "Armor";
    private final int size = 27;
    private final Inventory inv = new LunixHolder(name,size, LunixInvType.ARMOR).getInventory();
    private final Player p;
    private static final Map<Integer, ArmorSlot> armorSlots = new HashMap<>();

    static {
        armorSlots.put(10, ArmorSlot.HELMET);
        armorSlots.put(11, ArmorSlot.CHESTPLATE);
        armorSlots.put(12, ArmorSlot.LEGGINGS);
        armorSlots.put(13, ArmorSlot.BOOTS);
    }

    public ArmorGUI(Player p) {
        this.p = p;
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    @Override
    public void init() {
        for(int i = 0;i<size;i++) {
            switch(i) {
                case 0,9,18,8,17,26 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 10,11,12,13 -> inv.setItem(i, getArmor(i));
                case 16 -> inv.setItem(i,getToggleButton(LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).isArmorEquiped()));
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        boolean armorEquiped = lunixPlayer.isArmorEquiped();
        switch (e.getRawSlot()) {
            case 10,11,12,13 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new ArmorUpgradeGUI(p,armorSlots.get(e.getRawSlot())).getInv()));
            case 16 -> {
                if (armorEquiped) {
                    p.getInventory().setHelmet(null);
                    p.getInventory().setChestplate(null);
                    p.getInventory().setLeggings(null);
                    p.getInventory().setBoots(null);
                    lunixPlayer.setArmorEquiped(false);
                    e.getInventory().setItem(16,getToggleButton(false));
                } else {
                    boolean allSlotsEmpty = true;
                    for (ItemStack item : p.getInventory().getArmorContents()) {
                        if (item == null) continue;
                        CompoundTag armorTag = NBTTags.getLunixDataMap(item);
                        if (armorTag.get("id") != null) {
                            if (!armorTag.get("id").getAsString().contains("LUNIX_ARMOR_")) {
                                allSlotsEmpty = false;
                            }
                        } else {
                            allSlotsEmpty = false;
                        }
                        break;
                    }
                    if (allSlotsEmpty) {
                        p.getInventory().setHelmet(lunixPlayer.getHelmet().getItemStack());
                        p.getInventory().setChestplate(lunixPlayer.getChestplate().getItemStack());
                        p.getInventory().setLeggings(lunixPlayer.getLeggings().getItemStack());
                        p.getInventory().setBoots(lunixPlayer.getBoots().getItemStack());
                        lunixPlayer.setArmorEquiped(true);
                        e.getInventory().setItem(16,getToggleButton(true));
                    } else {
                        p.sendMessage(StringUtil.color("&cPlease remove all items from your armor slots before trying to equip your armor."));
                    }
                }
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }

    private ItemStack getToggleButton(boolean enabled) {
        ItemStack item;
        if (enabled) {
            item = new ItemStack(Material.REDSTONE_TORCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtil.color("&e&lArmor Status &7| &a&lEQUIPED"));
            meta.setLore(List.of(StringUtil.color("&7Click to Unequip!")));
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.TORCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtil.color("&e&lArmor Status &7| &c&lUNEQUIPED"));
            meta.setLore(List.of(StringUtil.color("&7Click to Equip!")));
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack getArmor(int slot) {
        ItemStack item = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getArmor().get(armorSlots.get(slot)).getItemStack();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to view upgrades!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}