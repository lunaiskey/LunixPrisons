package io.github.lunaiskey.lunixprison.items.lunixitems;

import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.items.LunixItem;
import io.github.lunaiskey.lunixprison.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class GemPouch extends LunixItem {

    public GemPouch(ItemID id) {
        super(id, "&7Gem Pouch", List.of(StringUtil.color("&eR-Click to open!")), Material.PLAYER_HEAD);
    }

    @Override
    public ItemStack getItemStack() {
        String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY2MjRhN2JkZWU2MjQwZGRkYmVkODI2ODA5MGUyMzRkMGJhNDcwZWE4OTZlODkyOWY0ZWZiMjEzZjIyNjk0NCJ9fX0=";
        UUID skulluuid = UUID.fromString("2ce42c30-dbf7-4e0d-afcb-c54eaefbf937");
        ItemStack item = ItemBuilder.getSkull(getRarityString()+" "+getName(), getLore(),texture,skulluuid);
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        return item;
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

    }

    private String getRarityString() {
        return switch (getItemID()){
            case COMMON_GEM_POUCH -> "&fCommon";
            case UNCOMMON_GEM_POUCH -> "&aUncommon";
            case RARE_GEM_POUCH -> "&9Rare";
            case EPIC_GEM_POUCH -> "&5Epic";
            case LEGENDARY_GEM_POUCH -> "&6Legendary";
            default -> "";
        };
    }
}
