package io.github.lunaiskey.lunixprison.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class LunixHolder implements InventoryHolder {

    private String name;
    private int size;
    private final LunixInvType invType;

    public LunixHolder(String name, int size, LunixInvType invType) {
        this.name = name;
        this.size = size;
        this.invType = invType;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Bukkit.createInventory(this,size,name);
    }

    public LunixInvType getInvType() { return invType; }

}
