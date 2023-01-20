package io.github.lunaiskey.lunixprison.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

public interface LunixInventory {

    Inventory getInv(Player player);

    void updateInventory(Player player);

    void onClick(InventoryClickEvent e);

    void onDrag(InventoryDragEvent e);

    void onOpen(InventoryOpenEvent e);

    void onClose(InventoryCloseEvent e);
}
