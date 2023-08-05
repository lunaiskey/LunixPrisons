package io.github.lunaiskey.lunixprison.modules.items;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.items.*;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaBoosterItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaChatColorVoucher;
import io.github.lunaiskey.lunixprison.modules.items.meta.MetaCurrencyVoucher;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeManager;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemManager {

    private static ItemManager instance;

    private ItemManager() {

    }

    public static ItemManager get() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

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
        addLunixItem(new BlanNoes());
        //itemMap.put(ItemID.SEX_ITEM,new SexItem());
    }

    private void addLunixItem(LunixItem item) {
        itemMap.putIfAbsent(item.getItemID(),item);
    }

    public LunixItem getLunixItem(ItemID itemID) {
        return itemMap.get(itemID);
    }

    public LunixItem getLunixItem(ItemStack itemStack) {
        ItemID itemID = NBTTags.getItemID(itemStack);
        if (itemID == null) {
            return null;
        }
        LunixItem lunixItem = getLunixItem(itemID);
        if (lunixItem != null) {
            return lunixItem;
        }
        switch (itemID) {
            case BOOSTER -> new BoosterItem(new MetaBoosterItem(itemStack));
            case CURRENCY_VOUCHER -> new CurrencyVoucher(new MetaCurrencyVoucher(itemStack));
            case CHATCOLOR_VOUCHER -> new ChatColorVoucher(new MetaChatColorVoucher(itemStack));
        }
        return null;
    }

    public void updateItemStack(ItemStack itemStack, Player player) {
        ItemID itemID = NBTTags.getItemID(itemStack);
        if (itemID != null) {
            boolean hasCustomUpdate = true;
            switch (itemID) {
                case LUNIX_PICKAXE -> PickaxeManager.get().updatePickaxe(itemStack,player.getUniqueId());
                default -> hasCustomUpdate = false;
            }
            if (hasCustomUpdate) {
                return;
            }
        }
        LunixItem lunixItem = getLunixItem(itemStack);
        if (lunixItem == null) {
            return;
        }
        updateMaterial(itemStack,lunixItem);
        ItemMeta meta = itemStack.getItemMeta();
        updateItemName(itemStack,lunixItem,meta);
        updateItemLore(itemStack,lunixItem,meta);
        itemStack.setItemMeta(meta);
    }

    private void updateItemName(ItemStack itemStack,LunixItem lunixItem, ItemMeta meta) {
        String name = StringUtil.color(lunixItem.getDisplayName());
        if (lunixItem.getRarity() != null) {
            name = lunixItem.getRarity().getChatColor()+name;
        }
        meta.setDisplayName(name);
    }

    private void updateItemLore(ItemStack itemStack, LunixItem lunixItem, ItemMeta meta) {
        List<String> lore = new ArrayList<>();
        lore.addAll(lunixItem.getColoredDescription());
        if (lunixItem.getRarity() != null) {
            if (lore.size() != 0) {
                lore.add(" ");
            }
            lore.add(lunixItem.getRarity().getChatColor()+""+ChatColor.BOLD+lunixItem.getRarity().name());
        }
        meta.setLore(lore);
    }

    private void updateMaterial(ItemStack itemStack, LunixItem lunixItem) {
        if (!itemStack.getType().equals(lunixItem.getMaterial())) {
            itemStack.setType(lunixItem.getMaterial());
        }
    }

    public void updateInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (ItemStack content : contents) {
            updateItemStack(content,player);
        }
        player.getInventory().setContents(contents);
    }

    public void updateEquipmentSlot(Player player, EquipmentSlot slot) {
        ItemStack stack = player.getInventory().getItem(slot);
        updateItemStack(stack,player);
        player.getInventory().setItem(slot,stack);
    }

    public Set<ItemID> getLunixItems() {
        return itemMap.keySet();
    }

    public List<String> getLunixItemsList() {
        List<String> items = new ArrayList<>();
        for (ItemID itemID : getLunixItems()) {
            items.add(itemID.name());
        }
        return items;
    }
}
