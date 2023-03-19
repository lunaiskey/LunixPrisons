package io.github.lunaiskey.lunixprison.modules.items.meta;

import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

public class MetaChatColorVoucher implements LunixItemMeta {

    private ChatColorSelectType type;
    private LunixChatColor color;

    public MetaChatColorVoucher(ItemStack itemStack) {
        CompoundTag tag = NBTTags.getLunixDataTag(itemStack).getCompound("ChatColorVoucherData");
        try {
            this.type = ChatColorSelectType.valueOf(tag.getString("type"));
        } catch (IllegalArgumentException ignored) {
            this.type = null;
        }
        try {
            this.color = LunixChatColor.valueOf(tag.getString("color"));
        } catch (IllegalArgumentException ignored) {
            this.color = null;
        }
    }

    public MetaChatColorVoucher(ChatColorSelectType type, LunixChatColor color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public void applyMeta(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type.name());
        tag.putString("color",color.name());
        NBTTags.addLunixData(item,"ChatColorVoucherData",tag);
    }

    public ChatColorSelectType getType() {
        return type;
    }

    public LunixChatColor getColor() {
        return color;
    }

    public void setType(ChatColorSelectType type) {
        this.type = type;
    }

    public void setColor(LunixChatColor color) {
        this.color = color;
    }
}
