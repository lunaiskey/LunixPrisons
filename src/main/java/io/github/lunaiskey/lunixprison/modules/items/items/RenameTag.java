package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.modules.player.ChatReplyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class RenameTag extends LunixItem {
    public RenameTag() {
        super(ItemID.RENAME_TAG, "&6Rename Tag &7(Right Click)",List.of("Lets your rename your pickaxe.") , null, Material.NAME_TAG);
    }

    @Override
    public List<String> getLore(LunixItemMeta meta) {
        return null;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        e.setUseItemInHand(Event.Result.DENY);
        e.setUseInteractedBlock(Event.Result.DENY);
        Player p = e.getPlayer();
        LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
        InventoryType type = p.getOpenInventory().getType();
        if (lunixPlayer.getChatReplyType() != ChatReplyType.RENAME_TAG) {
            if (type == InventoryType.CRAFTING || type == InventoryType.CREATIVE) {
                p.sendMessage(ChatColor.GREEN+"Type what you want your pickaxe to be renamed to, color codes are accepted.");
                lunixPlayer.setChatReplyType(ChatReplyType.RENAME_TAG);
            }
        }
    }
}
