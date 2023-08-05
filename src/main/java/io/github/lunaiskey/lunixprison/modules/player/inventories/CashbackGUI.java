package io.github.lunaiskey.lunixprison.modules.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import org.bukkit.Bukkit;
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
import java.util.UUID;

public class CashbackGUI implements LunixInventory {

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixHolder("Cashback",27,LunixInvType.CASHBACK).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        for (int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 13 -> inv.setItem(i,getCashBackButton(p));
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
            }
        }
    }

    private ItemStack getCashBackButton(Player player) {
        BigInteger cashback = PlayerManager.get().getPlayerMap().get(player.getUniqueId()).getCashback();
        CurrencyType type = CurrencyType.TOKENS;
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7When spending &eTokens &7on some"));
        lore.add(StringUtil.color("&7upgrades gain &b5%&7 back."));
        lore.add("");
        lore.add(StringUtil.color("&fBalance: "+type.getColorCode()+type.getUnicode()+"&f"+Numbers.formattedNumber(cashback)));
        lore.add("");
        lore.add(StringUtil.color("&eClick to claim!"));
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=";
        UUID profileUUID = UUID.fromString("1eef9526-98d7-430a-b56d-d7995c648040");
        ItemStack item = ItemBuilder.getSkull(StringUtil.color("&b&lCashback"),lore, base64,profileUUID);
        item = ItemBuilder.replaceSkullTexture(item,base64,profileUUID);
        return item;
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(player.getUniqueId());
        int slot = e.getRawSlot();
        if (slot != 13) {
            return;
        }
        BigInteger cashback = lunixPlayer.getCashback();
        CurrencyType type = CurrencyType.TOKENS;
        if (cashback.compareTo(BigInteger.ZERO) > 0) {
            lunixPlayer.giveTokens(cashback);
            lunixPlayer.setCashback(BigInteger.ZERO);
            player.sendMessage(StringUtil.color("&aSuccessfully claimed &e"+type.getColorCode()+type.getUnicode()+"&f"+Numbers.formattedNumber(cashback)+"&a."));
        } else {
            player.sendMessage(StringUtil.color("&cYou have nothing to claim."));
        }
        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), ()->player.closeInventory());
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
}
