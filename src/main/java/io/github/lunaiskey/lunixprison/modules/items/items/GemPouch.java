package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.LunixPlayerHeadItem;
import io.github.lunaiskey.lunixprison.modules.items.PouchRarity;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.UUID;

public class GemPouch extends LunixPlayerHeadItem {

    private PouchRarity pouchRarity;

    public GemPouch(PouchRarity rarity) {
        super(rarity.getGemItemID(), rarity.getRarityString()+" &7Gem Pouch", List.of(StringUtil.color("&eR-Click to open!")), null);
        pouchRarity = rarity;
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
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {

    }

    @Override
    public UUID getHeadUUID() {
        return UUID.fromString("2ce42c30-dbf7-4e0d-afcb-c54eaefbf937");
    }

    @Override
    public String getHeadBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY2MjRhN2JkZWU2MjQwZGRkYmVkODI2ODA5MGUyMzRkMGJhNDcwZWE4OTZlODkyOWY0ZWZiMjEzZjIyNjk0NCJ9fX0=";
    }
}
