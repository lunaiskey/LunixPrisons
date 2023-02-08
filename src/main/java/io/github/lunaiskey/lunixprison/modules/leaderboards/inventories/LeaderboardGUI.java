package io.github.lunaiskey.lunixprison.modules.leaderboards.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.leaderboards.BigIntegerEntry;
import io.github.lunaiskey.lunixprison.modules.leaderboards.LongEntry;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
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

public class LeaderboardGUI implements LunixInventory {

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixHolder("Leaderboards",27, LunixInvType.LEADERBOARDS).getInventory();
        init(inv,player);
        return inv;
    }


    public void init(Inventory inv, Player p) {
        for (int i = 0;i< inv.getSize();i++) {
            switch (i) {
                case 12 -> inv.setItem(i,getTokenTop());
                case 13 -> inv.setItem(i,getGemsTop());
                case 14 -> inv.setItem(i,getRankTop());
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
            }
        }
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
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

    public ItemStack getTokenTop() {
        ArrayList<BigIntegerEntry> tokenTop = new ArrayList<>(LunixPrison.getPlugin().getLeaderboardManager().getTokenTopCache().values());
        List<String> lore = new ArrayList<>();
        for (int i = 0;i<5;i++) {
            BigIntegerEntry entry;
            if (i < tokenTop.size()) {
                entry = tokenTop.get(i);
            } else {
                entry = new BigIntegerEntry(null,"Empty", BigInteger.ZERO);
            }
            lore.add(StringUtil.color("&7"+(i+1)+". &f"+entry.getName()+"&7 - &f"+ Numbers.formattedNumber(entry.getValue())));
        }
        return ItemBuilder.createItem("&eToken Top", Material.SUNFLOWER,lore);
    }

    public ItemStack getGemsTop() {
        ArrayList<LongEntry> gemsTop = new ArrayList<>(LunixPrison.getPlugin().getLeaderboardManager().getGemsTopCache().values());
        List<String> lore = new ArrayList<>();
        for (int i = 0;i<5;i++) {
            LongEntry entry;
            if (i < gemsTop.size()) {
                entry = gemsTop.get(i);
            } else {
                entry = new LongEntry(null,"Empty", 0);
            }
            lore.add(StringUtil.color("&7"+(i+1)+". &f"+entry.getName()+"&7 - &f"+ Numbers.formattedNumber(entry.getValue())));
        }
        return ItemBuilder.createItem("&aGems Top", Material.EMERALD,lore);
    }

    public ItemStack getRankTop() {
        ArrayList<LongEntry> rankTop = new ArrayList<>(LunixPrison.getPlugin().getLeaderboardManager().getRankTopCache().values());
        List<String> lore = new ArrayList<>();
        for (int i = 0;i<5;i++) {
            LongEntry entry;
            if (i < rankTop.size()) {
                entry = rankTop.get(i);
            } else {
                entry = new LongEntry(null,"Empty", 0);
            }
            lore.add(StringUtil.color("&7"+(i+1)+". &f"+entry.getName()+"&7 - &f"+ Numbers.formattedNumber(entry.getValue())));
        }
        return ItemBuilder.createItem("&bRank Top", Material.PAPER,lore);
    }

}
