package io.github.lunaiskey.pyrexprison.nms;

import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
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
        return addPyrexData(item,"voucherData",voucherTag);
    }

    public static Pair<CurrencyType,BigInteger> getVoucherValue(ItemStack item) {
        CompoundTag itemTag = getPyrexDataMap(item);
        CompoundTag voucherTag = itemTag.getCompound("voucherData");
        try {
            CurrencyType type = CurrencyType.valueOf(voucherTag.getString("type"));
            BigInteger amount = new BigInteger(voucherTag.getString("amount"));
            return new ImmutablePair<>(type,amount);
        } catch (Exception ignored) {

        }
        return null;
    }

    public static ItemStack addPyrexDataContainer(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag pyrexDataTag = new CompoundTag();
        if (itemTag.getAllKeys().contains("PyrexData")) {
            return item;
        }
        itemTag.put("PyrexData",pyrexDataTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static ItemStack addPyrexData(ItemStack item, String identifier, Object value) {
        item = addPyrexDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag pyrexDataTag = itemTag.getCompound("PyrexData");
        if (value instanceof Integer) {
            pyrexDataTag.putInt(identifier,(int)value);
        }
        if (value instanceof Short) {
            pyrexDataTag.putShort(identifier,(short)value);
        }
        if (value instanceof Long) {
            pyrexDataTag.putLong(identifier,(long)value);
        }
        if (value instanceof Double) {
            pyrexDataTag.putDouble(identifier,(double)value);
        }
        if (value instanceof String) {
            pyrexDataTag.putString(identifier,(String) value);
        }
        if (value instanceof Boolean) {
            pyrexDataTag.putBoolean(identifier,(boolean)value);
        }
        if (value instanceof Byte) {
            pyrexDataTag.putByte(identifier,(byte) value);
        }
        if (value instanceof Float) {
            pyrexDataTag.putFloat(identifier,(float) value);
        }
        if (value instanceof UUID) {
            pyrexDataTag.putUUID(identifier,(UUID) value);
        }
        if (value instanceof Tag) {
            if (value instanceof CompoundTag) {
                pyrexDataTag.put(identifier,(CompoundTag) value);
            } else {
                pyrexDataTag.put(identifier, (Tag) value);
            }
        }
        itemTag.put("PyrexData",pyrexDataTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static CompoundTag getPyrexDataMap(ItemStack item) {
        item = addPyrexDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        if (itemTag.contains("PyrexData")) {
            return itemTag.getCompound("PyrexData");
        }
        return new CompoundTag();
    }

    public static ItemID getItemID(ItemStack item) {
        try {
            return ItemID.valueOf(getPyrexDataMap(item).getString("id"));
        } catch (Exception ignored) {
            return null;
        }
    }
}
