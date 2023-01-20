package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.player.ChatReplyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.StringUtil;
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
        super(ItemID.RENAME_TAG, "&6Rename Tag",List.of("&7Right click to rename pickaxe!") , null, Material.NAME_TAG);
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
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        if (lunixPlayer.getChatReplyType() != ChatReplyType.RENAME_TAG) {
            if (p.getOpenInventory().getType() == InventoryType.CRAFTING) {
                p.sendMessage(StringUtil.color("&bType in chat what you want to rename your pickaxe too."));
                lunixPlayer.setChatReplyType(ChatReplyType.RENAME_TAG);
            }
        }
    }
}
