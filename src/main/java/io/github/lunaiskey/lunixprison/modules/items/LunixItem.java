package io.github.lunaiskey.lunixprison.modules.items;

import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class LunixItem {

    private ItemID id;
    private String displayName;
    private List<String> description;
    private Material mat;
    private Rarity rarity;

    public LunixItem(ItemID id, String displayName, List<String> description, Rarity rarity, Material mat) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        if (rarity != null) {
            this.rarity = rarity;
        } else {
            this.rarity = Rarity.COMMON;
        }

        this.mat = mat;
    }

    public LunixItem(ItemID id, String displayName, List<String> description, Material mat) {
        this(id, displayName, description, Rarity.COMMON,mat);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(mat);
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        ItemMeta meta = item.getItemMeta();
        if (getDisplayName() != null) {
            meta.setDisplayName(StringUtil.color(getDisplayName()));
        }
        List<String> lore = new ArrayList<>();
        if (getDescription() != null) {
            for (String str : getDescription()) {
                lore.add(StringUtil.color(str));
            }
            meta.setLore(lore);
        } else {
        }
        if (rarity != null) {
            if (lore.size() > 0) {
                lore.add("");
            }
            lore.add(StringUtil.color(rarity.getColorCode()+"&l"+rarity.name()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public abstract void onBlockBreak(BlockBreakEvent e);
    public abstract void onBlockPlace(BlockPlaceEvent e);
    public abstract void onInteract(PlayerInteractEvent e);

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getDescription() {
        return description;
    }

    public ItemID getItemID() {
        return id;
    }

    public Material getMaterial() {
        return mat;
    }

    public Rarity getRarity() {
        return rarity;
    }

    protected void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    protected void setDescription(List<String> description) {
        this.description = description;
    }

    protected void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }
}
