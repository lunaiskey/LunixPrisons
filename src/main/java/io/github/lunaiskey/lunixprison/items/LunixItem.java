package io.github.lunaiskey.lunixprison.items;

import io.github.lunaiskey.lunixprison.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LunixItem {

    private ItemID id;
    private String name;
    private List<String> lore;
    private Material mat;
    private Rarity rarity;

    public LunixItem(ItemID id, String name, List<String> lore, Rarity rarity, Material mat) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        if (rarity != null) {
            this.rarity = rarity;
        } else {
            this.rarity = Rarity.COMMON;
        }

        this.mat = mat;
    }

    public LunixItem(ItemID id, String name, List<String> lore, Material mat) {
        this(id,name,lore, Rarity.COMMON,mat);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(mat);
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(StringUtil.color(name));
        }
        if (getLore() != null) {
            List<String> lore = new ArrayList<>();
            for (String str : getLore()) {
                lore.add(StringUtil.color(str));
            }
            lore.add(StringUtil.color(rarity.getColorCode()+rarity.name()));
            meta.setLore(lore);
        } else {
            meta.setLore(Collections.singletonList(StringUtil.color(rarity.getColorCode() + rarity.name())));
        }
        item.setItemMeta(meta);
        return item;
    }

    public abstract void onBlockBreak(BlockBreakEvent e);
    public abstract void onBlockPlace(BlockPlaceEvent e);
    public abstract void onInteract(PlayerInteractEvent e);

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
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
}
