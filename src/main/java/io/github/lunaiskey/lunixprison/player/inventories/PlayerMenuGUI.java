package io.github.lunaiskey.lunixprison.player.inventories;

import io.github.lunaiskey.lunixprison.util.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.util.gui.LunixInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PlayerMenuGUI implements LunixInventory {

    private final String name = "Player Menu [WIP]";
    private final int size = 54;
    private final Inventory inv = new LunixHolder(name,size, LunixInvType.PLAYER_MENU).getInventory();

    @Override
    public void init() {

    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
