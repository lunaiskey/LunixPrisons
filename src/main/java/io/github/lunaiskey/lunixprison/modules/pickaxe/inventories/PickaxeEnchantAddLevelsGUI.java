package io.github.lunaiskey.lunixprison.modules.pickaxe.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantLunixHolder;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PickaxeEnchantAddLevelsGUI implements LunixInventory {

    private EnchantType type;

    private static final int[] amountLoc = {1,5,10,25,50,100,200,500,1000};

    public PickaxeEnchantAddLevelsGUI(EnchantType type) {
        this.type = type;
    }

    public PickaxeEnchantAddLevelsGUI() {
        this(null);
    }

    @Override
    public Inventory getInv(Player player) {
        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        PickaxeStorage pickaxe = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId()).getPickaxeStorage();
        Inventory inv = new EnchantLunixHolder("Add "+enchant.getName()+" Levels",27, LunixInvType.PICKAXE_ENCHANTS_ADD_LEVELS, type,enchant.getCostAmountFromLevelArray(pickaxe.getEnchants().getOrDefault(type,0),amountLoc)).getInventory();
        init(inv,player);
        return inv;
    }

    private void init(Inventory inv, Player p) {
        LunixPlayer lunixPlayer =  LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        PickaxeStorage pickaxe = lunixPlayer.getPickaxeStorage();
        EnchantLunixHolder holder = (EnchantLunixHolder) inv.getHolder();
        for (int i = 0; i<inv.getSize();i++) {
            switch(i) {
                case 0 -> inv.setItem(i,ItemBuilder.getGoBack());
                case 4 -> inv.setItem(i,pickaxe.getItemStack());
                case 9,10,11,12,13,14,15,16,17 -> inv.setItem(i,getAddLevelButton(p,getAmount(i),holder.getCostMap()));
                default -> inv.setItem(i,ItemBuilder.getDefaultFiller());
            }
        }
    }

    @Override
    public void updateInventory(Player player) {

    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        EnchantLunixHolder holder = (EnchantLunixHolder) e.getInventory().getHolder();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        PickaxeStorage pickaxe = lunixPlayer.getPickaxeStorage();
        EnchantType enchantType = holder.getType();
        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(enchantType);
        CurrencyType currencyType = enchant.getCurrencyType();
        switch (e.getRawSlot()) {
            case 0 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantGUI().getInv(p)));
            case 9,10,11,12,13,14,15,16,17 -> {
                int level = getAmount(e.getRawSlot());
                int start = pickaxe.getEnchants().getOrDefault(enchantType, 0);
                int end = Math.min(start + level, enchant.getMaxLevel());
                /*
                if (level == Integer.MAX_VALUE) {
                    Pair<Integer, BigInteger> pair = enchant.getMaxLevelFromAmount(start, lunixPlayer.getTokens());
                    // afford or not
                    if (lunixPlayer.getCurrency(currencyType).compareTo(pair.getRight()) >= 0) {
                        // pickaxe level is max or not
                        if (start < enchant.getMaxLevel()) {
                            if (start != pair.getLeft()) {
                                lunixPlayer.takeCurrency(currencyType,pair.getRight(),true);
                                pickaxe.getEnchants().put(enchantType, pair.getLeft());
                                LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                                p.sendMessage("Purchased " + (pair.getLeft() - start) + " Levels of " + enchant.getName() + ".");
                                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), () -> p.openInventory(getInv(p)));
                            }
                        } else {
                            p.sendMessage(StringUtil.color("&cYou have the max level of this enchantment."));
                        }
                    //pair.getRight(); cost
                    //pair.getLeft(); level
                    } else {
                        p.sendMessage(StringUtil.color("&cYou cannot afford any more levels of this enchantment."));
                    }

                 */
                //p.sendMessage(holder.getCostMap().size()+"");
                /*
                p.sendMessage(level+"");
                StringBuilder stringBuilder = new StringBuilder();
                for (int index : holder.getCostMap().keySet()) {
                    stringBuilder.append(index).append(", ");
                }
                p.sendMessage(stringBuilder.toString());

                 */
                BigInteger cost = holder.getCostMap().get(level);
                if (start < enchant.getMaxLevel()) {
                    if (lunixPlayer.getCurrency(currencyType).compareTo(cost) >= 0) {
                        lunixPlayer.takeCurrency(currencyType,cost,true);
                        pickaxe.getEnchants().put(enchantType, end);
                        p.sendMessage("Purchased " + (end - start) + " Levels of " + enchant.getName() + ".");
                        LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), () -> p.openInventory(new PickaxeEnchantAddLevelsGUI(holder.getType()).getInv(p)));
                    } else {
                        p.sendMessage(StringUtil.color("&cYou cannot afford this level of enchantment."));
                    }
                } else {
                    p.sendMessage(StringUtil.color("&cYou have the max level of this enchantment."));
                }
                //enchant.getCostBetweenLevels(start,end); cost
                //end; level

            }
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
        /*
        Player player = (Player) e.getPlayer();
        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->{
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof EnchantLunixHolder)) {
                player.openInventory(new PickaxeEnchantGUI().getInv(player));
            }
        });

         */
    }

    private ItemStack getAddLevelButton(Player p,int level, Map<Integer,BigInteger> amountMap) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        PickaxeStorage pickaxe = lunixPlayer.getPickaxeStorage();
        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        CurrencyType currencyType = enchant.getCurrencyType();
        String name = StringUtil.color("&a&l+"+level);
        int start = pickaxe.getEnchants().getOrDefault(type,0);
        int end = Math.min(start + level, enchant.getMaxLevel());
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&a&l| &7Cost: "+currencyType.getColorCode()+currencyType.getUnicode()+"&f"+ Numbers.formattedNumber(amountMap.get(level))));
        lore.add(StringUtil.color("&a&l| &7New Level: &e"+end));
        lore.add("");
        //lore.add(ChatColor.YELLOW+"Click to Apply!");
        Material mat;
        if (lunixPlayer.getTokens().compareTo(amountMap.get(level)) >= 0) {
            mat = Material.LIME_STAINED_GLASS_PANE;
            lore.add(ChatColor.YELLOW+"Click to Apply!");
        } else {
            mat = Material.RED_STAINED_GLASS_PANE;
            lore.add(ChatColor.RED+"Not Enough "+currencyType.getName()+".");
        }
        ItemStack item = ItemBuilder.createItem(name,mat,lore);
        item.setAmount(level);
        return item;
    }

    private int getAmount(int slot) {
        if (slot < 0) {
            return -1;
        }
        int offset = 9;
        if (slot-offset < amountLoc.length) {
            return amountLoc[slot-offset];
        }
        return -1;
    }
}
