package io.github.lunaiskey.lunixprison.modules.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.ViewPlayerHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayerGUI implements LunixInventory {

    private Player otherPlayer;

    public ViewPlayerGUI(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public ViewPlayerGUI() {

    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new ViewPlayerHolder(otherPlayer.getName()+"'s Profile",45, LunixInvType.VIEW_PLAYER,otherPlayer).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
        for(int i = 0;i<inv.getSize();i++) {
            switch(i) {
                case 10 -> inv.setItem(i, lunixPlayer.getHelmet().getItemStack());
                case 12 -> inv.setItem(i, lunixPlayer.getChestplate().getItemStack());
                case 14 -> inv.setItem(i, lunixPlayer.getLeggings().getItemStack());
                case 16 -> inv.setItem(i, lunixPlayer.getBoots().getItemStack());
                case 30 -> inv.setItem(i, lunixPlayer.getPickaxe().getItemStack());
                case 32 -> inv.setItem(i,getPlayerStats(p));
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

    private ItemStack getPlayerStats(Player player) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&ePlayer Stats"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&eTokens: &f"+ Numbers.formattedNumber(lunixPlayer.getTokens())));
        lore.add(StringUtil.color("&aGems: &f"+ Numbers.formattedNumber(lunixPlayer.getGems())));
        lore.add(StringUtil.color("&3LunixPoints: &f"+ Numbers.formattedNumber(lunixPlayer.getLunixPoints())));
        lore.add(StringUtil.color("&bRank: &f"+ lunixPlayer.getRank()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
