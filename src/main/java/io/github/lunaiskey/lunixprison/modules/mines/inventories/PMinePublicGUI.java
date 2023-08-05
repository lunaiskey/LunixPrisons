package io.github.lunaiskey.lunixprison.modules.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PMinePublicGUI implements LunixInventory {
    private final List<UUID> sortedList = PMineManager.get().getPublicSortedByRankList();
    private final int totalPages = (((sortedList.size() - sortedList.size()%28)/28)+1);

    private static final Map<UUID,Integer> pageMap = new HashMap<>();

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = new LunixHolder("Public P-Mines",54, LunixInvType.PMINE_PUBLIC_MINES).getInventory();
        init(inv,player);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        for(int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 1,2,3,4,5,6,7,45,46,47,48,49,50,51,52,53,9,18,27,36,17,26,35,44 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
                case 0 -> inv.setItem(i,getPreviousPage(0));
                case 8 -> {
                    int page = 2;
                    if (page > totalPages) {
                        inv.setItem(i,ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
                    } else {
                        inv.setItem(i,getNextPage(2));
                    }
                }
                default ->  {
                    int place = getPlace(i);
                    if (place <= sortedList.size()-1) {
                        UUID uuid = sortedList.get(place);
                        inv.setItem(i,getMineIcon(uuid));
                    }
                }
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
        UUID uuid = p.getUniqueId();
        int page = pageMap.get(p.getUniqueId());
        int slot = e.getRawSlot();
        Inventory inv = e.getClickedInventory();
        switch (slot) {
            case 10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43 -> {
                int place = getPlace(slot) + (28 * pageMap.get(p.getUniqueId()));
                if (place < sortedList.size()) {
                    PMine mine = PMineManager.get().getPMine(sortedList.get(place));
                    mine.teleportToCenter(p,false,false);
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(), ()->p.closeInventory());
                }
            }
            case 0 -> {
                if (page > 0) {
                    pageMap.put(uuid,page-1);
                    updateGUI(p);
                    inv.setItem(0,getPreviousPage(page));
                    inv.setItem(8,getNextPage(page+2));
                } else {
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PMineGUI().getInv(p)));
                }
            }
            case 8 -> {
                if (totalPages-1 > page) {
                    pageMap.put(uuid,page+1);
                    updateGUI(p);
                    inv.setItem(0,getPreviousPage(page));
                    inv.setItem(8,getNextPage(page+2));
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }

    public void onOpen(InventoryOpenEvent e) {
        pageMap.put(e.getPlayer().getUniqueId(), 0);
    }

    public void onClose(InventoryCloseEvent e) {
        pageMap.remove(e.getPlayer().getUniqueId());
    }

    public static Map<UUID, Integer> getPageMap() {
        return Collections.unmodifiableMap(pageMap);
    }

    public void updateGUI(Player p) {
        Inventory inv = p.getOpenInventory().getTopInventory();
        int page = pageMap.get(p.getUniqueId());
        for (int i = 0;i<28;i++) {
            int slot = getSlot(i);
            if (i + page*28 <= sortedList.size()-1) {
                inv.setItem(slot, getMineIcon(sortedList.get(i + (page*28))));
            } else {
                inv.setItem(slot,new ItemStack(Material.AIR));
            }
        }
    }

    private ItemStack getMineIcon(UUID puuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(puuid);
        LunixPlayer iconPlayer = PlayerManager.get().getPlayerMap().get(puuid);
        PMine minePlayer = PMineManager.get().getPMine(puuid);
        ItemStack item = ItemBuilder.getPlayerSkull(offlinePlayer);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&d"+offlinePlayer.getName()+"'s PMine"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&d&lStats:"));
        lore.add(StringUtil.color("&d&l| &fRank: &d"+iconPlayer.getRank()));
        lore.add(StringUtil.color("&d&l| &fTax: &d"+minePlayer.getMineTax()+"%"));
        lore.add(StringUtil.color("&d&l| &fSell Price: &d"+minePlayer.getSellPrice()));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to teleport!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPreviousPage(int slot) {
        return slot <= 0 ? ItemBuilder.getGoBack() : ItemBuilder.getPreviousPage(slot);
    }

    private ItemStack getNextPage(int slot) {
        if (slot > totalPages) {
            return ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null);
        } else {
            return ItemBuilder.getNextPage(slot);
        }
    }

    private int getPlace(int slot) {
        return switch (slot) {
            case 10,11,12,13,14,15,16 -> slot - 10;
            case 19,20,21,22,23,24,25 -> slot - 12;
            case 28,29,30,31,32,33,34 -> slot - 14;
            case 37,38,39,40,41,42,43 -> slot - 16;
            default -> -1;
        };
    }

    private int getSlot(int place) {
        int[] array = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
        if (place >= 0 && place < 28) {
            return array[place];
        }
        return -1;
    }
}
