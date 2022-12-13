package io.github.lunaiskey.lunixprison.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface LunixInventory {

    void init();

    Inventory getInv();

    void onClick(InventoryClickEvent e);
}
