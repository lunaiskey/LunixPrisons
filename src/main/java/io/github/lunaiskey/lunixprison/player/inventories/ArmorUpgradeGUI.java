package io.github.lunaiskey.lunixprison.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.player.armor.Armor;
import io.github.lunaiskey.lunixprison.player.armor.ArmorLunixHolder;
import io.github.lunaiskey.lunixprison.player.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.items.lunixitems.GemStone;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ArmorUpgradeGUI implements LunixInventory {

    private String name;
    private int size = 36;
    private final Player player;
    private final LunixPlayer lunixPlayer;
    private final Inventory inv;
    private ArmorSlot type;
    private static Map<Integer,AbilityType> abilitySlots = new HashMap<>();
    private static Map<UUID, ArmorSlot> customColorMap = new HashMap<>();

    static {
        abilitySlots.put(21,AbilityType.SALES_BOOST);
        abilitySlots.put(22,AbilityType.ENCHANTMENT_PROC);
        //abilitySlots.put(23,AbilityType.XP_BOOST);
    }

    public ArmorUpgradeGUI(Player player, ArmorSlot type) {
        this.player = player;
        this.lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        this.type = type;
        this.name = type.getName() + " Upgrades";
        this.inv = new ArmorLunixHolder(name,size, LunixInvType.ARMOR_UPGRADES,type).getInventory();

    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 13 -> inv.setItem(i, lunixPlayer.getArmor().get(type).getItemStack());
                case 21,22,23 -> {
                    if (abilitySlots.containsKey(i)) {
                        inv.setItem(i,getUpgradeButton(abilitySlots.get(i)));
                    } else {
                        inv.setItem(i,ItemBuilder.getComingSoon());
                    }
                }
                case 11 -> inv.setItem(i, getTierUpButton());
                case 15 -> inv.setItem(i, getColorButton());
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        Armor armor = lunixPlayer.getArmor().get(type);
        int slot = e.getRawSlot();
        boolean isEquiped = lunixPlayer.isArmorEquiped();
        switch (slot) {
            case 21,22,23 -> {
                if (abilitySlots.containsKey(slot)) {
                    AbilityType abilityType = abilitySlots.get(slot);
                    Ability ability = LunixPrison.getPlugin().getPlayerManager().getArmorAbilityMap().get(abilityType);
                    int level = armor.getAbilties().get(abilityType);
                    if (level < ability.getMaxLevel()) {
                        if (lunixPlayer.getGems() >= ability.getCost(level)) {
                            lunixPlayer.takeGems(ability.getCost(level));
                            armor.getAbilties().put(abilityType,level+1);
                            player.sendMessage(StringUtil.color("&aUpgraded " + abilitySlots.get(slot).name() + " to level " + armor.getAbilties().get(abilityType) + "."));
                            if (isEquiped) {
                                p.getInventory().setItem(type.getSlot(), armor.getItemStack());
                            }

                            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), () -> player.openInventory(new ArmorUpgradeGUI(player, type).getInv()));
                        } else {
                            player.sendMessage(StringUtil.color("&cYou cannot afford this upgrade."));
                        }
                    }
                }
            }
            case 11 -> {
                if (armor.getTier() < armor.getTierMax()) {
                    int cost = armor.getCostAmount(armor.getTier()+1);
                    ItemID gemStoneType = armor.getGemstone(armor.getTier()+1);
                    if (LunixPrison.getPlugin().getPlayerManager().getLunixItemCount(player,gemStoneType) >= cost) {
                        LunixPrison.getPlugin().getPlayerManager().removeLunixItem(player,gemStoneType,cost);
                        armor.setTier(armor.getTier()+1);
                        player.sendMessage(StringUtil.color("&aSuccessfully upgraded armor to Tier "+armor.getTier()+"."));
                        if (isEquiped) {
                            p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                        }
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->player.openInventory(new ArmorUpgradeGUI(player,type).getInv()));
                    } else {
                        player.sendMessage(StringUtil.color("&cYou don't have enough of this type of Gemstone."));
                    }
                }
            }
            case 15 -> {
                switch (e.getClick()) {
                    case LEFT,SHIFT_LEFT -> {
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), p::closeInventory);
                        p.sendMessage("Type in the hex code you want for your piece.");
                        customColorMap.put(p.getUniqueId(),type);
                    }
                    case RIGHT,SHIFT_RIGHT -> {
                        if (armor.getCustomColor() != null) {
                            armor.setCustomColor(null);
                            if (isEquiped) {
                                p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                            }
                            inv.setItem(slot,getColorButton());
                            inv.setItem(13,armor.getItemStack());
                        }
                    }
                }
            }
        }
    }

    private ItemStack getUpgradeButton(AbilityType abilityType) {
        Material mat = abilityType.getMaterial();
        String name = abilityType.getFormattedName();
        Armor armor = lunixPlayer.getArmor().get(type);
        int level = armor.getAbilties().get(abilityType);
        Ability ability = LunixPrison.getPlugin().getPlayerManager().getArmorAbilityMap().get(abilityType);
        int maxLevel = ability.getMaxLevel();
        long cost = ability.getCost(level+1);
        String levelStr = level > 0 ? ""+level : "";
        String formattedName = StringUtil.color(name+" "+levelStr);
        List<String> lore = new ArrayList<>();
        for (String str : ability.getDescription()) {
            lore.add(StringUtil.color("&7"+str));
        }
        if (level < maxLevel) {
            lore.add(" ");
            lore.add(StringUtil.color("&7Upgrade: "+ability.getLoreAddon(level) +" -> "+ability.getLoreAddon(level+1)));
            lore.add(StringUtil.color("&7Cost: &a"+ CurrencyType.GEMS.getUnicode()+"&f"+cost));
            lore.add("");
            lore.add(StringUtil.color("&eClick to upgrade!"));
        } else {
            lore.add(" ");
            lore.add(StringUtil.color("&cYou have maxed this upgrade."));
        }
        return ItemBuilder.createItem(formattedName,mat,lore);
    }

    private ItemStack getTierUpButton() {
        Armor armor = lunixPlayer.getArmor().get(type);
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aTier Up Piece"));
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use Gemstones to upgrade your"));
        lore.add(StringUtil.color("&7Armor Piece's Tier."));
        lore.add(" ");
        if (armor.getTier() < armor.getTierMax()) {
            GemStone gemStone = (GemStone) LunixPrison.getPlugin().getItemManager().getItemMap().get(armor.getGemstone(armor.getTier()+1));
            lore.add(StringUtil.color("&7Cost:"));
            lore.add(StringUtil.color("&8- "+gemStone.getName()+" &7x")+armor.getCostAmount(armor.getTier()+1));
            lore.add(" ");
            lore.add(StringUtil.color("&eClick to upgrade."));
        } else {
            lore.add(StringUtil.color("&cYou've reached Max Tier!"));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getColorButton() {
        ItemStack item = new ItemStack(Material.YELLOW_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aChange "+type.getName()+" Color"));
        Color color = lunixPlayer.getArmor().get(type).getAWTCustomColor();
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use this to update your"));
        lore.add(StringUtil.color("&7Armor Piece's color."));
        lore.add(" ");
        lore.add(StringUtil.color("&7Current Color: [#")+ChatColor.of(color)+Integer.toHexString(color.getRGB()).substring(2).toUpperCase()+ChatColor.GRAY+"]");
        lore.add(" ");
        lore.add(StringUtil.color("&eL-Click to change!"));
        lore.add(StringUtil.color("&eR-Click to reset!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static Map<UUID, ArmorSlot> getCustomColorMap() {
        return customColorMap;
    }
}
