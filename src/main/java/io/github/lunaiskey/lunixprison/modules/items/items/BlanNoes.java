package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.Rarity;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class BlanNoes extends LunixItem {
    public BlanNoes() {
        super(ItemID.BLAN_NOES, "Blan Noes", List.of("A Strange substance that seems to","infest the mines. Perhaps the Scientist","would exchange it for something more useful."), Rarity.UNCOMMON, Material.CHARCOAL);
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

    }
}
