package io.github.lunaiskey.lunixprison.modules.items;

import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class LunixPlayerHeadItem extends LunixItem {
    public LunixPlayerHeadItem(ItemID id, String name, List<String> lore, Rarity rarity) {
        super(id, name, lore, rarity, Material.PLAYER_HEAD);
    }

    public LunixPlayerHeadItem(ItemID id, String name, List<String> lore) {
        this(id, name, lore, Rarity.COMMON);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = super.getItemStack();
        item = ItemBuilder.replaceSkullTexture(item,getHeadBase64(),getHeadUUID());
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

    }

    public abstract UUID getHeadUUID();

    public abstract String getHeadBase64();
}
