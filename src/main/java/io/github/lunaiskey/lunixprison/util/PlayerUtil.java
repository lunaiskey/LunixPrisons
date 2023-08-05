package io.github.lunaiskey.lunixprison.util;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeManager;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerUtil {

    public static Map<String, Integer> getInventoryContentsCounts(Player player) {
        ItemStack[] inventory = player.getInventory().getStorageContents();
        Map<String,Integer> map = new HashMap<>();
        for (ItemStack itemStack : inventory) {
            if (itemStack == null || itemStack.getType().isAir()) {
                continue;
            }
            ItemID itemID = NBTTags.getItemID(itemStack);
            if (itemID != null) {
                map.put(itemID.name(), map.getOrDefault(itemID.name(),0)+itemStack.getAmount());
                continue;
            }
            map.put(itemStack.getType().name(),map.getOrDefault(itemStack.getType().name(),0)+itemStack.getAmount());
        }
        return map;
    }

    public static void removeLunixItemOrMaterialIfNull(Player player, String itemID, int amount, Map<String,Integer> mapToUpdate) {
        ItemStack[] inventory = player.getInventory().getStorageContents();
        int currentAmount = mapToUpdate.getOrDefault(itemID,0);
        if (currentAmount > amount) {
            mapToUpdate.put(itemID,currentAmount-amount);
        } else {
            mapToUpdate.remove(itemID);
        }
        Material material;
        try {
            material = Material.valueOf(itemID);
        } catch (IllegalArgumentException ignored) {
            material = null;
        }
        for (int i = 0;i < inventory.length ;i++) {
            if (amount <= 0) {
                break;
            }
            ItemStack itemStack = inventory[i];
            if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() <= 0) {
                continue;
            }
            LunixItem lunixItem = ItemManager.get().getLunixItem(itemStack);
            if (lunixItem != null) {
                if (!lunixItem.getItemID().name().equals(itemID)) {
                    continue;
                }
            } else {
                if (material == null || itemStack.getType() != material) {
                    continue;
                }
            }
            if (amount >= itemStack.getAmount()) {
                amount -= itemStack.getAmount();
                inventory[i] = null;
            } else {
                itemStack.setAmount(itemStack.getAmount()-amount);
                inventory[i] = itemStack;
                amount = 0;
            }
        }
        player.getInventory().setStorageContents(inventory);
    }

    public static void removeLunixItemOrMaterialIfNull(Player player, String itemID, int amount) {
        removeLunixItemOrMaterialIfNull(player, itemID, amount,null);
    }
}
