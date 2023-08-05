package io.github.lunaiskey.lunixprison.modules.shop.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.shop.Shop;
import io.github.lunaiskey.lunixprison.modules.shop.ShopGUIHolder;
import io.github.lunaiskey.lunixprison.modules.shop.ShopItem;
import io.github.lunaiskey.lunixprison.modules.shop.ShopManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ShopGUI implements LunixInventory {

    private Shop shop;

    public ShopGUI(Shop shop) {
        this.shop = shop;
    }

    public ShopGUI() {
        this(null);
    }

    @Override
    public Inventory getInv(Player p) {
        Inventory inv = new ShopGUIHolder(shop.getShopTitle(),shop.getSize(), LunixInvType.SHOP,shop.getShopID(), PlayerUtil.getInventoryContentsCounts(p)).getInventory();
        init(inv,p);
        return inv;
    }

    private void init(Inventory inv, Player p) {
        ShopGUIHolder holder = (ShopGUIHolder) inv.getHolder();
        Shop shop = ShopManager.get().getShop(holder.getShopID());
        for (int i = 0;i<54;i++) {
            switch (i) {
                case 0 -> inv.setItem(i,getPreviousPage(holder.getPage()-1));
                case 8 -> inv.setItem(i,getNextPage(holder.getPage()+1,shop.getMaxPages()));
                default -> {
                    switch(shop.getType()) {
                        case LISTED -> {
                            if (!inListedBounds(i)) {
                                inv.setItem(i, ItemBuilder.getDefaultFiller());
                                continue;
                            }
                            int index = getListedSlot(i);
                            ShopItem shopItem = shop.getListedItem(index);
                            if (shopItem == null) {
                                continue;
                            }
                            inv.setItem(i, shopItem.getShopItemPreview());
                        }
                        case SLOTED -> {
                            ShopItem shopItem = shop.getSlotedItem(holder.getPage(),i);
                            if (shopItem == null) {
                                inv.setItem(i,ItemBuilder.getDefaultFiller());
                                continue;
                            }
                            inv.setItem(i,shopItem.getShopItemPreview());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateInventory(Player player) {
        Inventory top = player.getOpenInventory().getTopInventory();
        init(top,player);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Player player = (Player) e.getWhoClicked();
        if (!(e.getInventory().getHolder() instanceof ShopGUIHolder)) {
            return;
        }
        ShopGUIHolder holder = (ShopGUIHolder) e.getInventory().getHolder();
        Shop shop = ShopManager.get().getShop(holder.getShopID());
        ShopItem shopItem = null;
        int maxPage = shop.getMaxPages();
        switch (shop.getType()) {
            case SLOTED -> {
                shopItem = shop.getSlotedItem(holder.getPage(),slot);
            }
            case LISTED -> {
                if (getListedSlot(slot) == -1) {
                    return;
                }
                int index = (28*(holder.getPage()-1))+getListedSlot(slot);
                shopItem = shop.getListedItem(index);
            }
        }
        switch (slot) {
            case 0 -> {
                if (holder.getPage() <= 1) {
                    return;
                }
                holder.setPage(holder.getPage()-1);
                updateInventory(player);
            }
            case 8 -> {
                if (holder.getPage() >= maxPage) {
                    return;
                }
                holder.setPage(holder.getPage()+1);
                updateInventory(player);
            }
        }
        if (shopItem != null) {
            shopItem.attemptPurchase(player);
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }

    private boolean inListedBounds(int slot) {
        int column = (slot % 9)+1;
        int row = ((slot - (slot % 9))/9)+1;
        return row >= 2 && row <= 5 && column >= 2 && column <= 8;
    }

    private int getListedSlot(int slot) {
        return switch (slot) {
            case 10,11,12,13,14,15,16 -> slot-10;
            case 19,20,21,22,23,24,25 -> slot-10-2;
            case 28,29,30,31,32,33,34 -> slot-10-2-2;
            case 37,38,39,40,41,42,43 -> slot-10-2-2-2;
            default -> -1;
        };
    }

    private ItemStack getPreviousPage(int page) {
        return page < 1 ? ItemBuilder.getDefaultFiller() : ItemBuilder.getPreviousPage(page);
    }

    private ItemStack getNextPage(int page, int totalPages) {
        if (page > totalPages) {
            return ItemBuilder.getDefaultFiller();
        } else {
            return ItemBuilder.getNextPage(page);
        }
    }
}
