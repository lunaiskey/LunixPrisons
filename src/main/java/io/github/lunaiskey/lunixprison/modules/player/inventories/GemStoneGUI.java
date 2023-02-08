package io.github.lunaiskey.lunixprison.modules.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.items.items.GemStone;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GemStoneGUI implements LunixInventory {

    private static final Map<Integer, ItemID> gemStoneMap = new HashMap<>();

    static {
        gemStoneMap.put(11,ItemID.AMETHYST_GEMSTONE);
        gemStoneMap.put(12,ItemID.JASPER_GEMSTONE);
        gemStoneMap.put(13,ItemID.OPAL_GEMSTONE);
        gemStoneMap.put(14,ItemID.JADE_GEMSTONE);
        gemStoneMap.put(15,ItemID.TOPAZ_GEMSTONE);
        gemStoneMap.put(20,ItemID.AMBER_GEMSTONE);
        gemStoneMap.put(21,ItemID.SAPPHIRE_GEMSTONE);
        gemStoneMap.put(22,ItemID.EMERALD_GEMSTONE);
        gemStoneMap.put(23,ItemID.RUBY_GEMSTONE);
        gemStoneMap.put(24,ItemID.DIAMOND_GEMSTONE);
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixHolder("Gemstones",36, LunixInvType.GEMSTONES).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        for (int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 11,12,13,14,15,20,21,22,23,24 -> inv.setItem(i,getGemstones(i,p));
                default -> inv.setItem(i,ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        switch (e.getRawSlot()) {
            case 11,12,13,14,15,20,21,22,23,24 -> {
                GemStone gemStone = (GemStone) LunixPrison.getPlugin().getItemManager().getItemMap().get(gemStoneMap.get(e.getRawSlot()));
                if (lunixPlayer.getRank() >= getRankRequirement(gemStoneMap.get(e.getRawSlot()))) {
                    if (!lunixPlayer.getSelectedGemstone().equals(gemStoneMap.get(e.getRawSlot()))) {
                        lunixPlayer.setSelectedGemstone(gemStoneMap.get(e.getRawSlot()));
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(getInv(p)));
                    }
                } else {
                    p.sendMessage(StringUtil.color("&cYou don't have the required rank for this gemstone."));
                }
                /*
                if (p.getUniqueId().equals(UUID.fromString("cc94070d-11cb-4590-ac72-efbd84d0d282"))) {
                    ItemStack gem = gemStone.getItemStack();
                    switch (e.getClick()) {
                        case LEFT -> {
                            if (!p.getInventory().addItem(gem).isEmpty()) {
                                p.sendMessage(StringUtil.color("&cYour inventory is full!"));
                            }
                        }
                        case SHIFT_LEFT -> {
                            gem.setAmount(64);
                            if (!p.getInventory().addItem(gem).isEmpty()) {
                                p.sendMessage(StringUtil.color("&cYour inventory is full!"));
                            }
                        }
                        case RIGHT -> PyrexPrison.getPlugin().getPlayerManager().removePyrexItem(p,gemStone.getItemID(),1);
                        case SHIFT_RIGHT -> PyrexPrison.getPlugin().getPlayerManager().removePyrexItem(p,gemStone.getItemID(),64);
                    }
                }*/
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

    }

    private ItemStack getGemstones(int slot, Player player) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        ItemID gemstone = gemStoneMap.get(slot);
        ItemStack item = LunixPrison.getPlugin().getItemManager().getItemMap().get(gemstone).getItemStack();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.add(" ");
        lore.add(StringUtil.color("&7Rank Required: &f"+getRankRequirement(gemstone)));
        lore.add(" ");
        if (lunixPlayer.getRank() >= getRankRequirement(gemstone)) {
            if (gemstone.equals(lunixPlayer.getSelectedGemstone())) {
                lore.add(StringUtil.color("&aAlready selected."));
                meta.setDisplayName(StringUtil.color(meta.getDisplayName()+" &a✓"));
            } else {
                lore.add(StringUtil.color("&eClick to select!"));
                //meta.setDisplayName(StringUtil.color(meta.getDisplayName()+" &7(&c&lUNSELECTED&7)"));
            }
        } else {
            lore.add(StringUtil.color("&cCurrently Locked!"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private int getRankRequirement(ItemID gemstone) {
        return switch (gemstone) {
            case AMETHYST_GEMSTONE -> 0;
            case JASPER_GEMSTONE -> 25;
            case OPAL_GEMSTONE -> 50;
            case JADE_GEMSTONE -> 75;
            case TOPAZ_GEMSTONE -> 100;
            case AMBER_GEMSTONE -> 150;
            case SAPPHIRE_GEMSTONE -> 200;
            case EMERALD_GEMSTONE -> 250;
            case RUBY_GEMSTONE -> 300;
            case DIAMOND_GEMSTONE -> 400;
            default -> -1;
        };
    }
}
