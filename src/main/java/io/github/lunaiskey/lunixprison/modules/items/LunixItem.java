package io.github.lunaiskey.lunixprison.modules.items;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class LunixItem {

    private ItemID id;
    private String displayName;
    private List<String> description;
    private List<String> coloredDescription = new ArrayList<>();
    private Material mat;
    private Rarity rarity;

    public LunixItem(ItemID id, String displayName, List<String> description, Rarity rarity, Material mat) {
        this.id = id;
        this.displayName = displayName;
        if (description == null) {
            description = new ArrayList<>();
        }
        this.description = new ArrayList<>(description);
        for (String str : description) {
            coloredDescription.add(ChatColor.GRAY+StringUtil.color(str));
        }
        this.rarity = rarity;
        this.mat = mat;
    }

    public LunixItem(ItemID id, String displayName, List<String> description, Material mat) {
        this(id, displayName, description, Rarity.COMMON,mat);
    }

    public abstract List<String> getLore(LunixItemMeta meta);

    public ItemStack getItemStack(Player player) {
        List<String> lore = new ArrayList<>(getDescription());
        /*
        if (rarity != null) {
            if (lore.size() > 0) {
                lore.add("");
            }
            lore.add(StringUtil.color(rarity.getColorCode()+"&l"+rarity.name()));
        }
         */
        ItemStack item = ItemBuilder.createItem(getDisplayName(),getMaterial(),lore);
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        ItemManager.get().updateItemStack(item,player);
        return item;
    }

    public ItemStack getItemStack() {
        return getItemStack(null);
    }

    public abstract void onBlockBreak(BlockBreakEvent e);
    public abstract void onBlockPlace(BlockPlaceEvent e);
    public abstract void onInteract(PlayerInteractEvent e);



    public List<String> getLore() {
        return getLore(null);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColoredDisplayName() {
        return rarity.getChatColor()+getDisplayName();
    }

    public List<String> getDescription() {
        return getDescription(null);
    }

    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>(this.description);
        if (player != null) {
            description.replaceAll(s -> s.replaceAll("%%player_name%%", player.getName()));
        }
        return description;
    }

    public List<String> getColoredDescription() {
        return coloredDescription;
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
        this.coloredDescription = new ArrayList<>(StringUtil.color(description));
    }

    protected void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }
}
