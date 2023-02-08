package io.github.lunaiskey.lunixprison.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    public static ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(StringUtil.color(name));
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack replaceSkullTexture(ItemStack head, String base64, UUID profileUUID) {
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(profileUUID, "");
        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getSkull(String base64, UUID profileUUID) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(profileUUID, "");
        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getSkull(String name, List<String> lore,String base64, UUID profileUUID) {
        ItemStack item = getSkull(base64,profileUUID);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(name));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlayerSkull(Player player) {
        return getPlayerSkull((OfflinePlayer) player);
    }

    public static ItemStack getPlayerSkull(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
        return item;
    }

    public static ItemStack getGoBack() {
        return ItemBuilder.createItem("&aGo Back",Material.ARROW,List.of(StringUtil.color("&eClick to return!")));
    }

    public static ItemStack getPreviousPage(int page) {
        return ItemBuilder.createItem(StringUtil.color("&aPrevious Page"),Material.ARROW,List.of(StringUtil.color("&7Page "+page)));
    }

    public static ItemStack getNextPage(int page) {
        return ItemBuilder.createItem(StringUtil.color("&aNext Page"),Material.ARROW,List.of(StringUtil.color("&7Page "+page)));
    }

    public static ItemStack getComingSoon() {
        return createItem("&c&lCOMING SOON", Material.BEDROCK, null);
    }

    public static ItemStack getComingLater() {
        return createItem("&c&lCOMING LATER", Material.BEDROCK, null);
    }

    /**
     * @return A black stained glass pane, with an empty name and no lore.
     */
    public static ItemStack getDefaultFiller() {
        return ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null);
    }

    public static ItemStack getDefaultEdgeFilder() {
        return ItemBuilder.createItem(" ",Material.PURPLE_STAINED_GLASS_PANE,null);
    }

}
