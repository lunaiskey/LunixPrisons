package io.github.lunaiskey.lunixprison.modules.shop;

import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixPagedHolder;

import java.util.Map;

public class ShopGUIHolder extends LunixPagedHolder {

    private String shopID;
    private Map<String,Integer> playerInvItemStackCount;
    public ShopGUIHolder(String name, int size, LunixInvType invType, String shopID, Map<String,Integer> playerInvItemStackCount) {
        super(name, size, invType);
        this.shopID = shopID;
        this.playerInvItemStackCount = playerInvItemStackCount;
    }

    public String getShopID() {
        return shopID;
    }

    public Map<String, Integer> getPlayerInvItemStackCount() {
        return playerInvItemStackCount;
    }
}
