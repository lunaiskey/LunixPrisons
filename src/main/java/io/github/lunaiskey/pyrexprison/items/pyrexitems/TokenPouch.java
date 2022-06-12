package io.github.lunaiskey.pyrexprison.items.pyrexitems;

import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class TokenPouch extends PyrexItem {

    public TokenPouch(ItemID id) {
        super(id, "&7Token Pouch", List.of(StringUtil.color("&eR-Click to open!")), Material.PLAYER_HEAD);
    }

    @Override
    public ItemStack getItemStack() {
        String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk2Y2UxM2ZmNjE1NWZkZjMyMzVkOGQyMjE3NGM1ZGU0YmY1NTEyZjFhZGVkYTFhZmEzZmMyODE4MGYzZjcifX19";
        UUID skulluuid = UUID.fromString("a12e0e3c-25d6-418c-9b8b-685e3010c1d8");
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
            case COMMON_TOKEN_POUCH -> "&fCommon";
            case UNCOMMON_TOKEN_POUCH -> "&aUncommon";
            case RARE_TOKEN_POUCH -> "&9Rare";
            case EPIC_TOKEN_POUCH -> "&5Epic";
            case LEGENDARY_TOKEN_POUCH -> "&6Legendary";
            default -> "";
        };
    }
}
