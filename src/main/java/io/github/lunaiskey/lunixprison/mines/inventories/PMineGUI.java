package io.github.lunaiskey.lunixprison.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.util.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.mines.PMine;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PMineGUI {

    private String name = "Personal Mine";
    private int size = 27;
    private Player player;
    private PMine mine;

    private final Inventory inv = new LunixHolder(name,size, LunixInvType.PMINE_MAIN).getInventory();

    public PMineGUI(Player player) {
        this.player = player;
        this.mine = LunixPrison.getPlugin().getPmineManager().getPMine(player.getUniqueId());
    }

    private void init() {
        for (int i = 0; i < size;i++) {
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

    public Inventory getInv() {
        init();
        return inv;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();
        if (mine != null) {
            switch (slot) {
                case 10 -> {mine.teleportToCenter(p,false,false);Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), p::closeInventory);}
                case 11 -> {mine.reset();Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), p::closeInventory);}
                case 14 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineSettingsGUI(p).getInv()));
                case 15 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineBlocksGUI(p).getInv()));
                case 16 -> Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMineUpgradesGUI(p).getInv()));
                case 12 -> {
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> p.openInventory(new PMinePublicGUI().getInv()));
                }
            }
        } else {
            p.sendMessage(ChatColor.RED+"Your Mine hasn't loaded correctly, Please contact an administrator.");
        }
        //p.sendMessage(ChatColor.LIGHT_PURPLE + "This feature is currently a work in progress, Please try again later.");
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
