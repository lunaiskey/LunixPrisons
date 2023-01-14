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

    public static CompoundTag getBaseTagContainer(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        return nmsStack.getOrCreateTag();
    }

    public static ItemStack addCustomTagContainer(ItemStack item, String containerName, CompoundTag tag) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        itemTag.put(containerName,tag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static boolean hasCustomTagContainer(ItemStack item, String containerName) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        if (!nmsStack.hasTag()) {
            return false;
        }
        CompoundTag itemTag = nmsStack.getTag();
        assert itemTag != null;
        return itemTag.contains(containerName);
    }

    public static ItemStack setCurrencyVoucherTags(ItemStack item, BigInteger amount, CurrencyType type) {
        CompoundTag voucherTag = new CompoundTag();
        voucherTag.putString("type",type.name());
        voucherTag.putString("amount",amount.toString());
        return addLunixData(item,"voucherData",voucherTag);
    }

    public static Pair<CurrencyType,BigInteger> getVoucherValue(ItemStack item) {
        CompoundTag itemTag = getLunixDataMap(item);
        CompoundTag voucherTag = itemTag.getCompound("voucherData");
        try {
            CurrencyType type = CurrencyType.valueOf(voucherTag.getString("type"));
            BigInteger amount = new BigInteger(voucherTag.getString("amount"));
            return new ImmutablePair<>(type,amount);
        } catch (Exception ignored) {

        }
        return null;
    }

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

    public static CompoundTag getLunixDataMap(ItemStack item) {
        item = addLunixDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        if (itemTag.contains("LunixData")) {
            return itemTag.getCompound("LunixData");
        }
        return new CompoundTag();
    }

    public static ItemID getItemID(ItemStack item) {
        try {
            return ItemID.valueOf(getLunixDataMap(item).getString("id"));
        } catch (Exception ignored) {
            return null;
        }
    }
}
