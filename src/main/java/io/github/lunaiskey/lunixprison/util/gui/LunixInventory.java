package io.github.lunaiskey.lunixprison.util.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public interface LunixInventory {

    void init();

    Inventory getInv();

    void onClick(InventoryClickEvent e);

    void onOpen(InventoryOpenEvent e);

    void onClose(InventoryCloseEvent e);
}
