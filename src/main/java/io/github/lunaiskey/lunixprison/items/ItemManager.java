package io.github.lunaiskey.lunixprison.items;

import io.github.lunaiskey.lunixprison.items.lunixitems.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {

    private Map<ItemID, LunixItem> itemMap = new LinkedHashMap<>();

    public void registerItems() {
        itemMap.put(ItemID.COMMON_GEODE,new Geode(ItemID.COMMON_GEODE));
        itemMap.put(ItemID.UNCOMMON_GEODE,new Geode(ItemID.UNCOMMON_GEODE));
        itemMap.put(ItemID.RARE_GEODE,new Geode(ItemID.RARE_GEODE));
        itemMap.put(ItemID.EPIC_GEODE,new Geode(ItemID.EPIC_GEODE));
        itemMap.put(ItemID.LEGENDARY_GEODE,new Geode(ItemID.LEGENDARY_GEODE));
        itemMap.put(ItemID.DIAMOND_GEMSTONE,new GemStone(ItemID.DIAMOND_GEMSTONE));
        itemMap.put(ItemID.RUBY_GEMSTONE,new GemStone(ItemID.RUBY_GEMSTONE));
        itemMap.put(ItemID.EMERALD_GEMSTONE,new GemStone(ItemID.EMERALD_GEMSTONE));
        itemMap.put(ItemID.SAPPHIRE_GEMSTONE,new GemStone(ItemID.SAPPHIRE_GEMSTONE));
        itemMap.put(ItemID.AMBER_GEMSTONE,new GemStone(ItemID.AMBER_GEMSTONE));
        itemMap.put(ItemID.TOPAZ_GEMSTONE,new GemStone(ItemID.TOPAZ_GEMSTONE));
        itemMap.put(ItemID.JADE_GEMSTONE,new GemStone(ItemID.JADE_GEMSTONE));
        itemMap.put(ItemID.OPAL_GEMSTONE,new GemStone(ItemID.OPAL_GEMSTONE));
        itemMap.put(ItemID.JASPER_GEMSTONE,new GemStone(ItemID.JASPER_GEMSTONE));
        itemMap.put(ItemID.AMETHYST_GEMSTONE,new GemStone(ItemID.AMETHYST_GEMSTONE));
        itemMap.put(ItemID.PLAYER_MENU,new PlayerMenu());

        itemMap.put(ItemID.COMMON_TOKEN_POUCH,new TokenPouch(ItemID.COMMON_TOKEN_POUCH));
        itemMap.put(ItemID.UNCOMMON_TOKEN_POUCH,new TokenPouch(ItemID.UNCOMMON_TOKEN_POUCH));
        itemMap.put(ItemID.RARE_TOKEN_POUCH,new TokenPouch(ItemID.RARE_TOKEN_POUCH));
        itemMap.put(ItemID.EPIC_TOKEN_POUCH,new TokenPouch(ItemID.EPIC_TOKEN_POUCH));
        itemMap.put(ItemID.LEGENDARY_TOKEN_POUCH,new TokenPouch(ItemID.LEGENDARY_TOKEN_POUCH));

        itemMap.put(ItemID.COMMON_GEM_POUCH,new GemPouch(ItemID.COMMON_GEM_POUCH));
        itemMap.put(ItemID.UNCOMMON_GEM_POUCH,new GemPouch(ItemID.UNCOMMON_GEM_POUCH));
        itemMap.put(ItemID.RARE_GEM_POUCH,new GemPouch(ItemID.RARE_GEM_POUCH));
        itemMap.put(ItemID.EPIC_GEM_POUCH,new GemPouch(ItemID.EPIC_GEM_POUCH));
        itemMap.put(ItemID.LEGENDARY_GEM_POUCH,new GemPouch(ItemID.LEGENDARY_GEM_POUCH));

        //itemMap.put(ItemID.SEX_ITEM,new SexItem());
    }

    public Map<ItemID, LunixItem> getItemMap() {
        return itemMap;
    }
}
