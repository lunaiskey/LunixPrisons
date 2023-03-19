package io.github.lunaiskey.lunixprison.modules.armor.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.player.ChatReplyType;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorSlot;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.armor.Armor;
import io.github.lunaiskey.lunixprison.modules.armor.ArmorLunixHolder;
import io.github.lunaiskey.lunixprison.modules.items.items.GemStone;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
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

import java.awt.*;
import java.util.*;
import java.util.List;

public class ArmorUpgradeGUI implements LunixInventory {

    private static Map<UUID, ArmorSlot> customColorSlotMap = new HashMap<>();

    private static final Map<Integer,AbilityType> abilitySlots = new HashMap<>();

    private ArmorSlot armorSlot;

    static {
        abilitySlots.put(21,AbilityType.SALES_BOOST);
        abilitySlots.put(22,AbilityType.ENCHANTMENT_PROC);
        //abilitySlots.put(23,AbilityType.XP_BOOST);
    }

    public ArmorUpgradeGUI(ArmorSlot armorSlot) {
        this.armorSlot = armorSlot;
    }

    public ArmorUpgradeGUI() {
        this.armorSlot = null;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new ArmorLunixHolder(armorSlot.getName()+" Upgrades",36, LunixInvType.ARMOR_UPGRADES,armorSlot).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        for (int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 13 -> inv.setItem(i, lunixPlayer.getArmor().get(armorSlot).getItemStack());
                case 21,22,23 -> inv.setItem(i,abilitySlots.containsKey(i) ? getUpgradeButton(abilitySlots.get(i),p) : ItemBuilder.getComingSoon() );
                case 11 -> inv.setItem(i, getTierUpButton(p));
                case 15 -> inv.setItem(i, getColorButton(p));
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.getDefaultEdgeFiller());
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
            }
        }
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        Inventory inv = e.getClickedInventory();
        Armor armor = lunixPlayer.getArmor().get(armorSlot);
        int slot = e.getRawSlot();
        boolean isEquiped = lunixPlayer.isArmorEquiped();
        switch (slot) {
            case 21,22,23 -> {
                if (!abilitySlots.containsKey(slot)) {
                    return;
                }
                AbilityType abilityType = abilitySlots.get(slot);
                Ability ability = abilityType.getAbility();
                int level = armor.getAbilties().get(abilityType);
                if (level >= ability.getMaxLevel()) {
                    return;
                }
                if (lunixPlayer.getGems() < ability.getCost(level)) {
                    p.sendMessage(ChatColor.RED+"&cYou cannot afford this upgrade.");
                    return;
                }
                lunixPlayer.takeGems(ability.getCost(level));
                armor.getAbilties().put(abilityType,level+1);
                p.sendMessage(StringUtil.color("&aUpgraded "+abilitySlots.get(slot).name()+" to level "+armor.getAbilties().get(abilityType)+"."));
                if (isEquiped) {
                    p.getInventory().setItem(armorSlot.getEquipmentSlot(), armor.getItemStack());
                }
                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), () -> p.openInventory(new ArmorUpgradeGUI(armorSlot).getInv(p)));
            }
            case 11 -> {
                if (armor.getTier() >= armor.getTierMax()) {
                    return;
                }
                int cost = armor.getCostAmount(armor.getTier()+1);
                ItemID gemStoneType = armor.getGemstone(armor.getTier()+1);
                PlayerManager playerManager = LunixPrison.getPlugin().getPlayerManager();
                if (playerManager.getLunixItemCount(p,gemStoneType) < cost) {
                    p.sendMessage(StringUtil.color("&cYou don't have enough of this type of Gemstone."));
                    return;
                }
                playerManager.removeLunixItem(p,gemStoneType,cost);
                armor.setTier(armor.getTier()+1);
                p.sendMessage(StringUtil.color("&aSuccessfully upgraded "+armor.getSlot().getName()+" to Tier "+armor.getTier()+"."));
                if (isEquiped) {
                    p.getInventory().setItem(armorSlot.getEquipmentSlot(),armor.getItemStack());
                }
                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new ArmorUpgradeGUI(armorSlot).getInv(p)));
            }
            case 15 -> {
                switch (e.getClick()) {
                    case LEFT,SHIFT_LEFT -> {
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), p::closeInventory);
                        p.sendMessage("Type in the hex code you want for your piece.");
                        lunixPlayer.setChatReplyType(ChatReplyType.ARMOR_CUSTOM_COLOR_EDIT);
                        customColorSlotMap.put(p.getUniqueId(),armorSlot);
                    }
                    case RIGHT,SHIFT_RIGHT -> {
                        if (armor.getCustomColor() == null) {
                            return;
                        }
                        armor.setCustomColor(null);
                        if (isEquiped) {
                            p.getInventory().setItem(armorSlot.getEquipmentSlot(),armor.getItemStack());
                        }
                        inv.setItem(slot,getColorButton(p));
                        inv.setItem(13,armor.getItemStack());
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
        customColorSlotMap.remove(e.getPlayer().getUniqueId());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }

    private ItemStack getUpgradeButton(AbilityType abilityType,Player p) {
        Material mat = abilityType.getMaterial();
        String name = abilityType.getFormattedName();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        Armor armor = lunixPlayer.getArmor().get(armorSlot);
        int level = armor.getAbilties().get(abilityType);
        Ability ability = abilityType.getAbility();
        int maxLevel = ability.getMaxLevel();
        long cost = ability.getCost(level+1);
        String levelStr = level > 0 ? String.valueOf(level) : "";
        String formattedName = StringUtil.color(name+" "+levelStr);
        List<String> lore = new ArrayList<>();
        for (String str : ability.getDescription()) {
            lore.add(StringUtil.color("&7"+str));
        }
        lore.add("");
        if (level < maxLevel) {
            lore.add(StringUtil.color("&7Upgrade: "+ability.getLoreAddon(level) +" -> "+ability.getLoreAddon(level+1)));
            lore.add(StringUtil.color("&7Cost: &a"+ CurrencyType.GEMS.getUnicode()+"&f"+cost));
            lore.add("");
            lore.add(StringUtil.color("&eClick to upgrade!"));
        } else {
            lore.add(StringUtil.color("&cYou have maxed this upgrade."));
        }
        return ItemBuilder.createItem(formattedName,mat,lore);
    }

    private ItemStack getTierUpButton(Player p) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        Armor armor = lunixPlayer.getArmor().get(armorSlot);
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aTier Up Piece"));
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use Gemstones to upgrade your"));
        lore.add(StringUtil.color("&7Armor Piece's Tier."));
        lore.add(" ");
        if (armor.getTier() < armor.getTierMax()) {
            GemStone gemStone = (GemStone) LunixPrison.getPlugin().getItemManager().getLunixItem(armor.getGemstone(armor.getTier()+1));
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

    private ItemStack getColorButton(Player p) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        ItemStack item = new ItemStack(Material.YELLOW_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aChange "+armorSlot.getName()+" Color"));
        Color color = lunixPlayer.getArmor().get(armorSlot).getAWTCustomColor();
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

    public static Map<UUID, ArmorSlot> getCustomColorSlotMap() {
        return customColorSlotMap;
    }
}
