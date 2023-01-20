package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import io.github.lunaiskey.lunixprison.modules.player.inventories.PlayerMenuGUI;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerMenu extends LunixItem {

    public PlayerMenu() {
        super(ItemID.PLAYER_MENU, "&aPlayer Menu", null, Material.NETHER_STAR);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = ItemBuilder.createItem(getDisplayName(),getMaterial(), getDescription());
        item = NBTTags.addLunixData(item,"id",getItemID().name());
        return item;
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
        p.openInventory(new PlayerMenuGUI().getInv(p));
    }

    @Override
    public List<String> getDescription() {
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use this item to view the player menu!"));
        lore.add(StringUtil.color(" "));
        lore.add(StringUtil.color("&eClick to open!"));
        return lore;
    }
}
