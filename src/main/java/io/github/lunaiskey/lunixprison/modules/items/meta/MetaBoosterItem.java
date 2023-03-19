package io.github.lunaiskey.lunixprison.modules.items.meta;

import io.github.lunaiskey.lunixprison.modules.boosters.BoosterType;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

public class MetaBoosterItem implements LunixItemMeta {

    private BoosterType boosterType;
    private int lengthSeconds;
    private double multiplier;

    public MetaBoosterItem(CompoundTag boosterData) {
        try {
            boosterType = BoosterType.valueOf(boosterData.getString("type"));
        } catch (Exception ignored){
            boosterType = null;
        }
        lengthSeconds = boosterData.getInt("length");
        multiplier = boosterData.getDouble("multiplier");
    }

    public MetaBoosterItem(ItemStack itemStack) {
        this(NBTTags.getLunixDataTag(itemStack).getCompound("boosterData"));
    }

    @Override
    public void applyMeta(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", boosterType.name());
        tag.putInt("length",lengthSeconds);
        tag.putDouble("multiplier",multiplier);
        NBTTags.addLunixData(item,"boosterData",tag);
    }

    public BoosterType getBoosterType() {
        return boosterType;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setBoosterType(BoosterType boosterType) {
        this.boosterType = boosterType;
    }

    public void setLengthSeconds(int lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
