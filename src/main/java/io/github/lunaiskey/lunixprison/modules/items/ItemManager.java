package io.github.lunaiskey.lunixprison.modules.items;

import io.github.lunaiskey.lunixprison.modules.items.items.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {

    private Map<ItemID, LunixItem> itemMap = new LinkedHashMap<>();

    public void registerItems() {
        addLunixItem(new Geode(Geode.GeodeRarity.COMMON));
        addLunixItem(new Geode(Geode.GeodeRarity.UNCOMMON));
        addLunixItem(new Geode(Geode.GeodeRarity.RARE));
        addLunixItem(new Geode(Geode.GeodeRarity.EPIC));
        addLunixItem(new Geode(Geode.GeodeRarity.LEGENDARY));

        addLunixItem(new GemStone(GemStone.GemstoneType.DIAMOND));
        addLunixItem(new GemStone(GemStone.GemstoneType.RUBY));
        addLunixItem(new GemStone(GemStone.GemstoneType.EMERALD));
        addLunixItem(new GemStone(GemStone.GemstoneType.SAPPHIRE));
        addLunixItem(new GemStone(GemStone.GemstoneType.AMBER));
        addLunixItem(new GemStone(GemStone.GemstoneType.TOPAZ));
        addLunixItem(new GemStone(GemStone.GemstoneType.JADE));
        addLunixItem(new GemStone(GemStone.GemstoneType.OPAL));
        addLunixItem(new GemStone(GemStone.GemstoneType.JASPER));
        addLunixItem(new GemStone(GemStone.GemstoneType.AMETHYST));

        addLunixItem(new TokenPouch(PouchRarity.COMMON));
        addLunixItem(new TokenPouch(PouchRarity.UNCOMMON));
        addLunixItem(new TokenPouch(PouchRarity.RARE));
        addLunixItem(new TokenPouch(PouchRarity.EPIC));
        addLunixItem(new TokenPouch(PouchRarity.LEGENDARY));

        addLunixItem(new GemPouch(PouchRarity.COMMON));
        addLunixItem(new GemPouch(PouchRarity.UNCOMMON));
        addLunixItem(new GemPouch(PouchRarity.RARE));
        addLunixItem(new GemPouch(PouchRarity.EPIC));
        addLunixItem(new GemPouch(PouchRarity.LEGENDARY));

        addLunixItem(new PlayerMenu());
        addLunixItem(new RenameTag());

        //itemMap.put(ItemID.SEX_ITEM,new SexItem());
    }

    public void addLunixItem(LunixItem item) {
        itemMap.putIfAbsent(item.getItemID(),item);
    }

    public Map<ItemID, LunixItem> getItemMap() {
        return itemMap;
    }
}
