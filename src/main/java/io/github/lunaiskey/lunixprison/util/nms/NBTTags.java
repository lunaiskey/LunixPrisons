package io.github.lunaiskey.lunixprison.util.nms;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.UUID;

public class NBTTags {

    public static ItemStack addLunixDataContainer(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag LunixDataTag = new CompoundTag();
        if (itemTag.getAllKeys().contains("LunixData")) {
            return item;
        }
        itemTag.put("LunixData",LunixDataTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static ItemStack addLunixData(ItemStack item, String identifier, Object value) {
        item = addLunixDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag LunixDataTag = itemTag.getCompound("LunixData");
        if (value instanceof Integer) {
            LunixDataTag.putInt(identifier,(int)value);
        }
        if (value instanceof Short) {
            LunixDataTag.putShort(identifier,(short)value);
        }
        if (value instanceof Long) {
            LunixDataTag.putLong(identifier,(long)value);
        }
        if (value instanceof Double) {
            LunixDataTag.putDouble(identifier,(double)value);
        }
        if (value instanceof String) {
            LunixDataTag.putString(identifier,(String) value);
        }
        if (value instanceof Boolean) {
            LunixDataTag.putBoolean(identifier,(boolean)value);
        }
        if (value instanceof Byte) {
            LunixDataTag.putByte(identifier,(byte) value);
        }
        if (value instanceof Float) {
            LunixDataTag.putFloat(identifier,(float) value);
        }
        if (value instanceof UUID) {
            LunixDataTag.putUUID(identifier,(UUID) value);
        }
        if (value instanceof Tag) {
            if (value instanceof CompoundTag) {
                LunixDataTag.put(identifier,(CompoundTag) value);
            } else {
                LunixDataTag.put(identifier, (Tag) value);
            }
        }
        itemTag.put("LunixData",LunixDataTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static CompoundTag getLunixDataTag(ItemStack item) {
        item = addLunixDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        if (itemTag.contains("LunixData")) {
            return itemTag.getCompound("LunixData");
        }
        return new CompoundTag();
    }

    public static ItemID getItemID(ItemStack item) {
        if (item == null) {
            return null;
        }
        try {
            return ItemID.valueOf(getLunixDataTag(item).getString("id"));
        } catch (Exception ignored) {
            return null;
        }
    }
}
