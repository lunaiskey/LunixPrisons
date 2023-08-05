package io.github.lunaiskey.lunixprison.modules.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.modules.player.datastorages.ChatColorStorage;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ChatNameTextColorGUI implements LunixInventory {

    public ChatColorSelectType type;

    public ChatNameTextColorGUI(ChatColorSelectType type) {
        this.type = type;
    }

    @Override
    public Inventory getInv(Player player) {
        int size = type == ChatColorSelectType.NAME ? 54 : 45;
        Inventory inv = new LunixChatColorHolder(type.getTitle(),size, type.getType(),type).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        LunixPlayer lunixPlayer = PlayerManager.get().getLunixPlayer(p.getUniqueId());
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack s;
            switch (i) {
                case 11,12,13,14,15,20,21,22,23,24,29,30,31,32,33 -> s = getChatColorStack(LunixChatColor.getColors().get(invSlotToColorIndex(i)),p,lunixPlayer);
                case 38,39,40,41 -> {
                    if (type == ChatColorSelectType.TEXT) {
                        s = ItemBuilder.getDefaultFiller();
                        break;
                    }
                    s = getChatColorStack(LunixChatColor.getFormats().get(invSlotToFormatIndex(i)),p,lunixPlayer);
                }
                case 0,9,18,27,36,45,8,17,26,35,44,53 -> s = ItemBuilder.getDefaultEdgeFiller();
                default -> s = ItemBuilder.getDefaultFiller();
            }
            inv.setItem(i,s);
        }
    }

    private int invSlotToColorIndex(int slot) {
        return switch (slot) {
            case 11,12,13,14,15 -> slot-9-2;
            case 20,21,22,23,24 -> slot-9-2-4;
            case 29,30,31,32,33 -> slot-9-2-4-4;
            default -> -1;
        };
    }

    private int invSlotToFormatIndex(int slot) {
        return switch (slot) {
            case 38,39,40,41 -> slot-38;
            default -> -1;
        };
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        LunixPlayer lunixPlayer = PlayerManager.get().getLunixPlayer(player.getUniqueId());
        ChatColorSelectType selectType = ((LunixChatColorHolder) e.getInventory().getHolder()).getSelectType();
        ChatColorStorage storage = lunixPlayer.getChatColorStorage();

        switch (e.getRawSlot()) {
            case 11,12,13,14,15,20,21,22,23,24,29,30,31,32,33 -> {
                int index = invSlotToColorIndex(e.getRawSlot());
                LunixChatColor color = LunixChatColor.getColors().get(index);
                switch (selectType) {
                    case NAME -> {
                        if (storage.getUnlockedNameColorAndFormats().contains(color)) {
                            storage.setSelectedNameColor(color);
                        } else {
                            player.sendMessage(ChatColor.RED+"You haven't unlocked this Color.");
                        }
                    }
                    case TEXT -> {
                        if (storage.getUnlockedTextColors().contains(color)) {
                            storage.setSelectedTextColor(color);
                        } else {
                            player.sendMessage(ChatColor.RED+"You haven't unlocked this Color.");
                        }
                    }
                }
            }
            case 38,39,40,41 -> {
                if (selectType == ChatColorSelectType.NAME) {
                    int index = invSlotToFormatIndex(e.getRawSlot());
                    LunixChatColor color = LunixChatColor.getFormats().get(index);
                    if (storage.getUnlockedNameColorAndFormats().contains(color)) {
                        storage.addSelectedNameFormat(color);
                    } else {
                        player.sendMessage(ChatColor.RED+"You haven't unlocked this Format.");
                    }
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }

    private ItemStack getChatColorStack(LunixChatColor wrapper, Player player, LunixPlayer lunixPlayer) {
        ChatColorStorage s = lunixPlayer.getChatColorStorage();
        String applyText = ChatColor.YELLOW+"Click to Apply!";
        String alreadySelectedText = ChatColor.AQUA+"Click to Remove!";
        String textColorPreview = type == ChatColorSelectType.TEXT ? wrapper.getColor()+"" : s.getTextColors();
        String nameColorPreview = type == ChatColorSelectType.NAME ? s.getNameColorsAndThis(wrapper) : s.getNameColors();
        String previewText = ChatColor.WHITE+nameColorPreview+player.getName()+ChatColor.GRAY+": "+textColorPreview+"Sample!";
        boolean isSelected = false;
        boolean unlocked = false;
        if (type == ChatColorSelectType.NAME) {
            if (wrapper.isFormat()) {
                isSelected = s.getSelectedNameFormats().contains(wrapper);
            } else if (wrapper.isColor()) {
                isSelected = s.getSelectedNameColor() == wrapper;
            }
            if (s.isNameColorUnlocked(wrapper)) {
                unlocked = true;
            }
        } else if (type == ChatColorSelectType.TEXT){
            isSelected = s.getSelectedTextColor() == wrapper;
            if (s.isTextColorUnlocked(wrapper)) {
                unlocked = true;
            }
        }
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY+"Preview:");
        lore.add(ChatColor.GRAY+""+ChatColor.BOLD+"| "+ChatColor.RESET+previewText);
        lore.add("");
        if (unlocked) {
            if (isSelected) {
                lore.add(alreadySelectedText);
            } else {
                lore.add(applyText);
            }
        } else {
            lore.add(ChatColor.RED+"Currently Locked!");
        }

        ItemStack item = new ItemStack(wrapper.getPlayerHead());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+""+wrapper.getColor()+wrapper.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public class LunixChatColorHolder extends LunixHolder {
        public ChatColorSelectType selectType;
        public LunixChatColorHolder(String name, int size, LunixInvType invType, ChatColorSelectType selectType) {
            super(name, size, invType);
            this.selectType = selectType;
        }

        public ChatColorSelectType getSelectType() {
            return selectType;
        }
    }
}
