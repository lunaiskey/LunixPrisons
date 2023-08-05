package io.github.lunaiskey.lunixprison.modules.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop {

    private final String shopID;
    private final String shopTitle;
    private final int size;
    private final ShopType type;
    private List<ShopItem> shopItems = new ArrayList<>();
    private Map<Integer,Map<Integer,ShopItem>> shopSlotedMap = new HashMap<>();

    private int maxListedPage;
    private int maxSlotedPage;

    public Shop(String shopID, String shopTitle, int size, ShopType type) {
        this.shopID = shopID.toUpperCase();
        this.shopTitle = shopTitle;
        this.size = size;
        this.type = type;
    }

    public String getShopID() {
        return shopID;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public int getSize() {
        return size;
    }

    public ShopType getType() {
        return type;
    }

    public int getMaxListedPage() {
        return maxListedPage;
    }

    public int getMaxSlotedPage() {
        return maxSlotedPage;
    }

    public ShopItem getListedItem(int index) {
        if (index >= shopItems.size()) {
            return null;
        }
        return shopItems.get(index);
    }

    public ShopItem getSlotedItem(int page, int slot) {
        Map<Integer,ShopItem> slotsFromPage = shopSlotedMap.get(page);
        if (slotsFromPage == null) {
            return null;
        }
        return slotsFromPage.get(slot);
    }

    public Shop addShopItem(ShopItem shopItem) {
        switch (type) {
            case LISTED -> {
                shopItems.add(shopItem);
                int size = shopItems.size()-1;
                maxListedPage = ((size - (size % 28))/28)+1;
            }
            case SLOTED -> {
                shopSlotedMap.putIfAbsent(shopItem.getPage(),new HashMap<>());
                Map<Integer,ShopItem> map = shopSlotedMap.get(shopItem.getPage());
                map.putIfAbsent(shopItem.getSlot(),shopItem);
                if (maxSlotedPage < shopItem.getPage()) {
                    maxSlotedPage = shopItem.getPage();
                }
            }
        }
        return this;
    }

    public int getMaxPages() {
        return switch(type) {
            case SLOTED -> getMaxSlotedPage();
            case LISTED -> getMaxListedPage();
        };
    }
}
