package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaChatColorVoucher;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.modules.player.datastorages.ChatColorStorage;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ChatColorVoucher extends LunixItem {
    public static final String VOUCHER_DATA_ID = "ChatColorVoucherData";

    private LunixChatColor color;
    private ChatColorSelectType type;

    public ChatColorVoucher(LunixChatColor color, ChatColorSelectType type) {
        super(ItemID.CHATCOLOR_VOUCHER, null, null, null, Material.PAPER);
        this.color = color;
        this.type = type;
    }

    public ChatColorVoucher(MetaChatColorVoucher meta) {
        this(meta.getColor(),meta.getType());
    }

    @Override
    public List<String> getLore(LunixItemMeta meta) {
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = super.getItemStack();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color.getColor()+color.getName()+" "+ChatColor.WHITE+type.getName()+ChatColor.GRAY+"| "+ ChatColor.YELLOW+"RIGHT-CLICK");
        item.setItemMeta(meta);
        new MetaChatColorVoucher(type,color).applyMeta(item);
        return item;
    }

    public ChatColorVoucher() {
        this(null,null);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ChatColorStorage s = PlayerManager.get().getLunixPlayer(p.getUniqueId()).getChatColorStorage();
        ItemStack item = e.getItem();
        MetaChatColorVoucher meta = new MetaChatColorVoucher(item);
        ChatColorSelectType selectType = meta.getType();
        LunixChatColor lunixChatColor = meta.getColor();
        if (selectType == null || lunixChatColor == null) {
            p.sendMessage(ChatColor.RED+"Invalid voucher.");
            return;
        }
        boolean shouldConsume = false;
        switch (selectType) {
            case NAME ->{
                if (s.isNameColorUnlocked(lunixChatColor)) {
                    if (lunixChatColor.isFormat()) {
                        p.sendMessage(ChatColor.RED+"You've already unlocked this Format.");
                    } else {
                        p.sendMessage(ChatColor.RED+"You've already unlocked this Color.");
                    }
                } else {
                    s.addUnlockedColor(lunixChatColor,selectType);
                    shouldConsume = true;
                }
            }
            case TEXT ->{
                if (s.isTextColorUnlocked(lunixChatColor)) {
                    p.sendMessage(ChatColor.RED+"You've already unlocked this Color.");
                } else {
                    s.addUnlockedColor(lunixChatColor,selectType);
                    shouldConsume = true;
                }
            }
        }
        if (!shouldConsume) {
            return;
        }
        item.setAmount(item.getAmount()-1);
        p.getInventory().setItem(e.getHand(),item);
    }
}
