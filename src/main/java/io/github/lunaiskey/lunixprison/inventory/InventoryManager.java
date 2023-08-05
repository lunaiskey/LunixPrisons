package io.github.lunaiskey.lunixprison.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    private static InventoryManager instance;

    private InventoryManager() {

    }

    public static InventoryManager get() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }



    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof LunixHolder)) {
            return;
        }
        LunixHolder lunixHolder = (LunixHolder) holder;
        lunixHolder.getInvType().getInventory().onClick(e);
    }

    public void onDrag(InventoryDragEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof LunixHolder)) {
            return;
        }
        LunixHolder lunixHolder = (LunixHolder) holder;
        lunixHolder.getInvType().getInventory().onDrag(e);
    }

    public void onOpen(InventoryOpenEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof LunixHolder)) {
            return;
        }
        LunixHolder lunixHolder = (LunixHolder) holder;
        lunixHolder.getInvType().getInventory().onOpen(e);
    }

    public void onClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof LunixHolder)) {
            return;
        }
        LunixHolder lunixHolder = (LunixHolder) holder;
        lunixHolder.getInvType().getInventory().onClose(e);
    }
}
