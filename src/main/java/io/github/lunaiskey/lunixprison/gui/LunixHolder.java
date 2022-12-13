package io.github.lunaiskey.lunixprison.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class LunixHolder implements InventoryHolder {

    private final Inventory inventory;
    private final LunixInvType invType;

    public LunixHolder(String name, int size, LunixInvType invType) {
        this.inventory = Bukkit.createInventory(this,size,name);
        this.invType = invType;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public LunixInvType getInvType() { return invType; }


}
