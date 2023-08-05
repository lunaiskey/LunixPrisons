package io.github.lunaiskey.lunixprison.modules.shop;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.PlayerUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.*;

public class ShopItem {

    private ItemStack itemStack;
    private List<String> commands;
    private boolean giveItemStack;
    private int slot;
    private int page;
    private Map<ShopItemCostCurrencyType, BigInteger> costCurrency;
    private Map<String, Integer> costItemStack;

    public ShopItem(ItemStack itemStack, List<String> commands, boolean giveItemStack, int slot, int page, Map<ShopItemCostCurrencyType, BigInteger> costCurrency, Map<String, Integer> costItemStack) {
        this.page = page;
        this.slot = slot;
        this.itemStack = itemStack != null ? itemStack.clone() : null;
        this.commands = Objects.requireNonNullElse(commands,new ArrayList<>());
        this.giveItemStack = giveItemStack;
        this.costCurrency = Objects.requireNonNullElse(costCurrency,new LinkedHashMap<>());
        this.costItemStack = Objects.requireNonNullElse(costItemStack,new LinkedHashMap<>());
    }

    public ShopItem(int slot, int page) {
        this(null,null,true,slot,page,null,null);
    }

    public ShopItem setItemStack(ItemStack itemStack, boolean giveItemStack) {
        this.itemStack = itemStack.clone();
        this.giveItemStack = giveItemStack;
        return this;
    }

    public ShopItem setItemStack(ItemStack itemStack) {
        return setItemStack(itemStack,true);
    }

    public ShopItem setCommands(List<String> commands) {
        this.commands = new ArrayList<>(commands);
        return this;
    }

    public ShopItem addCommand(String command) {
        this.commands.add(command);
        return this;
    }

    public ShopItem setGiveItemStack(boolean value) {
        this.giveItemStack = value;
        return this;
    }

    public ShopItem addCostCurrency(ShopItemCostCurrencyType costCurrency, BigInteger amount) {
        this.costCurrency.put(costCurrency,amount);
        return this;
    }

    public ShopItem addCostItemStack(String itemID, int amount) {
        this.costItemStack.put(itemID.toUpperCase(),amount);
        return this;
    }

    public ShopItem addCostItemStack(ItemID itemID, int amount) {
        return addCostItemStack(itemID.name(),amount);
    }

    public int getPage() {
        return page;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean hasCurrencyCosts() {
        return costCurrency.size() != 0;
    }

    public boolean hasItemStackCosts() {
        return costItemStack.size() != 0;
    }

    public boolean hasAnyCosts() {
        return hasCurrencyCosts() || hasItemStackCosts();
    }

    public boolean isFree() {
        return !hasAnyCosts();
    }

    public boolean isValidItemStack() {
        return itemStack != null && itemStack.getType().isItem() && !itemStack.getType().isAir();
    }

    public ItemStack getShopItemPreview() {
        boolean isValid = isValidItemStack();
        ItemStack item = isValid ? itemStack.clone() : ItemBuilder.createItem("&cItemStack is Null, Air or Not an item.",Material.BARRIER,null).clone();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY+"Cost:");
        if (hasAnyCosts()) {
            for (Map.Entry<ShopItemCostCurrencyType,BigInteger> entry : costCurrency.entrySet()) {
                ShopItemCostCurrencyType type = entry.getKey();
                lore.add(" "+type.getPrefix()+" "+Numbers.formattedNumber(entry.getValue())+" "+type.getSuffix());
            }
            for (Map.Entry<String, Integer> entry : costItemStack.entrySet()) {
                String itemDisplayName;
                String invalidItem = ChatColor.RED+"Invalid ("+entry.getKey()+")";
                boolean invalidLunixItem;
                boolean invalidVanillaItem;
                try {
                    ItemID itemID = ItemID.valueOf(entry.getKey());
                    LunixItem lunixItem = ItemManager.get().getLunixItem(itemID);
                    invalidLunixItem = lunixItem == null;
                } catch (IllegalArgumentException ignored) {
                    invalidLunixItem = true;
                }
                try {
                    Material material = Material.valueOf(entry.getKey());
                    invalidVanillaItem = !material.isItem() || material.isAir();
                } catch (IllegalArgumentException ignored) {
                    invalidVanillaItem = true;
                }
                if (!invalidLunixItem) {
                    LunixItem lunixItem = ItemManager.get().getLunixItem(ItemID.valueOf(entry.getKey()));
                    itemDisplayName = lunixItem.getColoredDisplayName();
                } else if (!invalidVanillaItem) {
                    itemDisplayName = WordUtils.capitalizeFully(entry.getKey().replace("_"," "));
                } else {
                    itemDisplayName = invalidItem;
                }
                lore.add(ChatColor.WHITE+itemDisplayName+ChatColor.DARK_GRAY+" x"+entry.getValue());
            }
        } else {
            lore.add(" "+ChatColor.GREEN+"Free");
        }
        lore.add("");
        lore.add(ChatColor.YELLOW+"Click to Purchase!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void attemptPurchase(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        Inventory playerInventory = player.getInventory();
        if (!(inventory.getHolder() instanceof ShopGUIHolder)) {
            return;
        }
        ShopGUIHolder holder = (ShopGUIHolder) inventory.getHolder();
        if (playerInventory.firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED+"Insufficient inventory space.");
            return;
        }
        Map<String,Integer> map = PlayerUtil.getInventoryContentsCounts(player);
        LunixPlayer lunixPlayer = PlayerManager.get().getLunixPlayer(player.getUniqueId());
        boolean playerHasCurrency = true;
        ShopItemCostCurrencyType notEnough = null;
        for (Map.Entry<ShopItemCostCurrencyType,BigInteger> entry : costCurrency.entrySet()) {
            switch (entry.getKey()) {
                case TOKEN -> {
                    if (lunixPlayer.getCurrency(CurrencyType.TOKENS).compareTo(entry.getValue()) < 0) {
                        playerHasCurrency = false;
                    }
                }
                case GEM -> {
                    if (lunixPlayer.getCurrency(CurrencyType.GEMS).compareTo(entry.getValue()) < 0) {
                        playerHasCurrency = false;
                    }
                }
                case LUNIX_POINT -> {
                    if (lunixPlayer.getCurrency(CurrencyType.LUNIX_POINTS).compareTo(entry.getValue()) < 0) {
                        playerHasCurrency = false;
                    }
                }
                case XP -> {
                    if (player.getLevel() < entry.getValue().intValue()) {
                        playerHasCurrency = false;
                    }
                }
            }
            if (!playerHasCurrency) {
                notEnough = entry.getKey();
                break;
            }
        }
        if (!playerHasCurrency) {
            player.sendMessage(ChatColor.RED+"Insufficient "+notEnough.name()+".");
            return;
        }
        boolean playerHasItems = true;
        String notEnoughItem = null;
        for (Map.Entry<String,Integer> entry : costItemStack.entrySet()) {
            Integer amount = map.get(entry.getKey());
            if (amount == null || amount < entry.getValue()) {
                playerHasItems = false;
                notEnoughItem = entry.getKey();
                break;
            }
        }
        if (!playerHasItems) {
            player.sendMessage(ChatColor.RED+"Insufficient "+notEnoughItem+".");
            return;
        }
        for (Map.Entry<ShopItemCostCurrencyType,BigInteger> entry : costCurrency.entrySet()) {
            switch (entry.getKey()) {
                case TOKEN -> lunixPlayer.takeCurrency(CurrencyType.TOKENS,entry.getValue(),false);
                case GEM -> lunixPlayer.takeCurrency(CurrencyType.GEMS,entry.getValue(),false);
                case LUNIX_POINT -> lunixPlayer.takeCurrency(CurrencyType.LUNIX_POINTS,entry.getValue(),false);
                case XP -> player.setLevel(player.getLevel()-entry.getValue().intValue());
            }
        }
        for (Map.Entry<String,Integer> entry : costItemStack.entrySet()) {
            PlayerUtil.removeLunixItemOrMaterialIfNull(player,entry.getKey(),entry.getValue(),map);
        }
        ItemStack clone = itemStack.clone();
        playerInventory.addItem(clone);
        String itemName = clone.getItemMeta().hasDisplayName() ? clone.getItemMeta().getDisplayName() : WordUtils.capitalizeFully(clone.getType().name().replace("_"," "));
        player.sendMessage(ChatColor.GREEN+"Purchased "+itemName+".");
    }
}
