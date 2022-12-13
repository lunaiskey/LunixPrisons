package io.github.lunaiskey.lunixprison.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.player.ViewPlayerHolder;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayerGUI implements LunixInventory {

    private String name;
    private int size = 45;
    private Inventory inv;
    private Player viewing;
    private LunixPlayer lunixPlayer;

    public ViewPlayerGUI(Player toView) {
        this.viewing = toView;
        this.name = viewing.getName()+"'s Profile";
        this.inv = new ViewPlayerHolder(name,size, LunixInvType.VIEW_PLAYER,viewing).getInventory();
        this.lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(viewing.getUniqueId());
    }

    @Override
    public void init() {
        for(int i = 0;i<size;i++) {
            switch(i) {
                case 10 -> inv.setItem(i, lunixPlayer.getHelmet().getItemStack());
                case 12 -> inv.setItem(i, lunixPlayer.getChestplate().getItemStack());
                case 14 -> inv.setItem(i, lunixPlayer.getLeggings().getItemStack());
                case 16 -> inv.setItem(i, lunixPlayer.getBoots().getItemStack());
                case 30 -> inv.setItem(i, lunixPlayer.getPickaxe().getItemStack());
                case 32 -> inv.setItem(i,getPlayerStats());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    private ItemStack getPlayerStats() {
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
