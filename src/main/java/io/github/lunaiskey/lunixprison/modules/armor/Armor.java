package io.github.lunaiskey.lunixprison.modules.armor;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.lunixprison.modules.armor.upgrades.AbilityType;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.lunixitems.GemStone;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Armor {

    private final ArmorSlot type;
    private Color customColor;
    private int tier;
    private Map<AbilityType, Integer> abilties;


    public Armor(ArmorSlot type, int tier, Color customColor, Map<AbilityType,Integer> abilityMap) {
        this.type = type;
        this.customColor = customColor;
        this.tier = tier;
        if (abilityMap == null) abilityMap = new HashMap<>();
        for (AbilityType abilityType : AbilityType.values()) {
            abilityMap.putIfAbsent(abilityType,0);
        }
        this.abilties = abilityMap;
    }

    public Armor(ArmorSlot type) {
        this(type,0,null,null);
    }

    public Armor(ArmorSlot type, int tier) {
        this(type,tier,null,null);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial(type));
        ItemMeta meta = item.getItemMeta();
        if (tier == 0) {
            meta.setDisplayName(StringUtil.color("&fStarter "+type.getName()));
        } else {
            meta.setDisplayName(((GemStone) LunixPrison.getPlugin().getItemManager().getItemMap().get(getGemstone(tier))).getName()+" "+type.getName());
        }
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&f&lPiece Abilities:"));
        boolean hasAbilities = false;
        Map<AbilityType, Ability> abilityMap = LunixPrison.getPlugin().getPlayerManager().getArmorAbilityMap();
        for (AbilityType type : AbilityType.getSortedList()) {
            Ability ability = abilityMap.get(type);
            int level = abilties.get(type);
            if (level > 0) {
                hasAbilities = true;
                lore.add(" "+ability.getLoreTitle(level)+ability.getLoreAddon(level));
            }
        }
        if (!hasAbilities) {
            lore.add(StringUtil.color(" &7&oNo abilities found..."));
        }

        lore.add(" ");
    lore.add(ChatColor.of(getAWTTierColor(tier))+""+ChatColor.BOLD+"TIER "+tier+" "+type.name());
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DYE);
        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
            if (customColor != null) {
                leatherMeta.setColor(getCustomColor());
            } else {
                leatherMeta.setColor(getTierColor(tier));
            }
            item.setItemMeta(leatherMeta);
        } else {
            item.setItemMeta(meta);
        }
        item = NBTTags.addLunixData(item,"id","LUNIX_ARMOR_"+type.name());
        return item;
    }

    public Material getMaterial(ArmorSlot type) {
        Material mat = Material.AIR;
        switch(type) {
            case HELMET -> mat = Material.LEATHER_HELMET;
            case CHESTPLATE -> mat = Material.LEATHER_CHESTPLATE;
            case LEGGINGS -> mat = Material.LEATHER_LEGGINGS;
            case BOOTS -> mat = Material.LEATHER_BOOTS;
        }
        return mat;
    }

    public ItemID getGemstone(int tier) {
        return switch (tier) {
            case 1 -> ItemID.AMETHYST_GEMSTONE;
            case 2 -> ItemID.JASPER_GEMSTONE;
            case 3 -> ItemID.OPAL_GEMSTONE;
            case 4 -> ItemID.JADE_GEMSTONE;
            case 5 -> ItemID.TOPAZ_GEMSTONE;
            case 6 -> ItemID.AMBER_GEMSTONE;
            case 7 -> ItemID.SAPPHIRE_GEMSTONE;
            case 8 -> ItemID.EMERALD_GEMSTONE;
            case 9 -> ItemID.RUBY_GEMSTONE;
            case 10 -> ItemID.DIAMOND_GEMSTONE;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
    }

    public int getCostAmount(int tier) {
        return switch(tier) {
            case 1 -> 64;
            case 2 -> 96;
            case 3 -> 128;
            case 4 -> 192;
            case 5 -> 256;
            case 6 -> 384;
            case 7 -> 512;
            case 8 -> 768;
            case 9 -> 1024;
            case 10 -> 1536;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setCustomColor(Color customColor) {
        this.customColor = customColor;
    }

    public int getTierMax() {
        return 10;
    }

    public ArmorSlot getType() {
        return type;
    }

    public Map<AbilityType, Integer> getAbilties() {
        return abilties;
    }

    public Color getTierColor(int tier) {
        if (tier == 0) {
            return Color.WHITE;
        } else if (tier <= getTierMax()) {
            return Color.fromRGB(((GemStone) LunixPrison.getPlugin().getItemManager().getItemMap().get(getGemstone(tier))).getIntFromHex());
        } else {
            return Color.BLACK;
        }
    }

    public java.awt.Color getAWTTierColor(int tier) {
        return new java.awt.Color(getTierColor(tier).asRGB());
    }

    public Color getCustomColor() {
        if (customColor != null) {
            return customColor;
        } else {
            return getTierColor(tier);
        }
    }

    public boolean hasCustomColor() {
        return customColor != null;
    }

    public java.awt.Color getAWTCustomColor() {
        return new java.awt.Color(getCustomColor().asRGB());
    }
}
