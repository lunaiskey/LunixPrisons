package io.github.lunaiskey.lunixprison.modules.items.meta;

import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class MetaCurrencyVoucher implements LunixItemMeta {

    private CurrencyType type;
    private BigInteger amount;

    public MetaCurrencyVoucher(ItemStack itemStack) {
        CompoundTag tag = NBTTags.getLunixDataTag(itemStack).getCompound("CurrencyVoucherData");
        try {
            this.type = CurrencyType.valueOf(tag.getString("type"));
        } catch (IllegalArgumentException ignored) {
            this.type = null;
        }
        try {
            this.amount = new BigInteger(tag.getString("amount"));
        } catch (NumberFormatException ignored) {
            this.amount = BigInteger.ZERO;
        }
    }

    public MetaCurrencyVoucher(CurrencyType type, BigInteger amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void applyMeta(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type",type.name());
        tag.putString("amount",amount.toString());
        NBTTags.addLunixData(item, "CurrencyVoucherData", tag);
    }

    public CurrencyType getType() {
        return type;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setType(CurrencyType type) {
        this.type = type;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
}
