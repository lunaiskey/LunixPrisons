package io.github.lunaiskey.lunixprison.modules.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PMineGUI implements LunixInventory {

    @Override
    public Inventory getInv(Player p) {
        Inventory inv = new LunixHolder("Personal Mine",27, LunixInvType.PMINE_MAIN).getInventory();
        init(inv,p);
        return inv;
    }

    private void init(Inventory inv, Player p) {
        for (int i = 0; i < inv.getSize();i++) {
            switch(i) {
                case 0,9,18,8,17,26 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 10 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aTeleport to Mine"),Material.COMPASS, List.of(ChatColor.YELLOW+"Click to teleport!")));
                case 11 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aReset Mine"),Material.CLOCK, List.of(ChatColor.YELLOW+"Click to reset!")));
                case 14 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aMine Settings"),Material.WHITE_WOOL,List.of(ChatColor.YELLOW+"Click to manage!")));
                case 15 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aChange Mine Blocks"),Material.GRASS_BLOCK, List.of(ChatColor.YELLOW+"Click to view menu!")));
                case 16 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aMine Upgrades"),Material.COMMAND_BLOCK,List.of(StringUtil.color("&eClick to view!"))));
                case 12 -> inv.setItem(i,viewPublic());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public void updateInventory(Player player) {

    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        if (mine != null) {
            switch (slot) {
                case 10 -> {mine.teleportToCenter(p,false,false);Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), p::closeInventory);}
                case 11 -> {mine.reset();Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), p::closeInventory);}
                case 14 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI().getInv(p)));
                case 15 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineBlocksGUI().getInv(p)));
                case 16 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineUpgradesGUI().getInv(p)));
                case 12 -> {
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMinePublicGUI().getInv(p)));
                }
            }
        } else {
            p.sendMessage(ChatColor.RED+"Your Mine hasn't loaded correctly, Please contact an administrator.");
        }
        //p.sendMessage(ChatColor.LIGHT_PURPLE + "This feature is currently a work in progress, Please try again later.");
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

    private ItemStack viewPublic() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aView Public"));
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&eClick to view!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
