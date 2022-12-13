package io.github.lunaiskey.lunixprison.items.lunixitems;

import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.items.LunixItem;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class SexItem extends LunixItem {
    public SexItem() {
        super(ItemID.SEX_ITEM, "&dSex.com", List.of(ChatColor.GRAY+"Free sex at Sex.com!","",ChatColor.DARK_GREEN+""+ChatColor.BOLD+"MISC ITEM"), Material.BAMBOO);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        e.getPlayer().sendMessage(StringUtil.color("&2What you talking bout willis?"));
    }
}
