package io.github.lunaiskey.lunixprison.modules.shop;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ShopManager {

    private Map<String,Shop> shopMap = new HashMap<>();
    private List<String> sortedShopIDs = new ArrayList<>();

    public void registerShops() {
        registerHardCodedShops();
        Bukkit.getScheduler().runTaskAsynchronously(LunixPrison.getPlugin(), this::sortShopIDs);
    }
    public void registerHardCodedShops() {
        addShop(new Shop("SCIENTIST","The Scientist",54,ShopType.LISTED)
            .addShopItem(new ShopItem(22,2)
                .setItemStack(LunixPrison.getPlugin().getItemManager().getLunixItem(ItemID.LEGENDARY_GEODE).getItemStack())
                .addCostItemStack(ItemID.BLAN_NOES,32)
            )
            .addShopItem(new ShopItem(0,0)
                .setItemStack(new ItemStack(Material.OAK_DOOR))
                .addCostItemStack(Material.OAK_PLANKS.name(),6)
            )
        );
    }

    private void sortShopIDs() {
        List<String> shopIDs = new ArrayList<>(shopMap.keySet());
        Collections.sort(shopIDs);
        sortedShopIDs = shopIDs;
    }

    public Shop getShop(String shopID) {
        return shopMap.get(shopID.toUpperCase());
    }

    public List<String> getAlphabeticallySortedShopIDs() {
        return sortedShopIDs;
    }

    public void addShop(Shop shop) {
        shopMap.put(shop.getShopID(),shop);
    }
}
