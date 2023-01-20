package io.github.lunaiskey.lunixprison.modules.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.inventory.LunixInventory;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.*;

public class PMineBlocksGUI implements LunixInventory {

    private static Map<UUID,Material> editMap;
    private static Map<UUID,Integer> pageMap;

    static {
        editMap = new HashMap<>();
        pageMap = new HashMap<>();
    }

    @Override
    public Inventory getInv(Player p) {
        Inventory inv = new LunixHolder("Blocks",54, LunixInvType.PMINE_BLOCKS).getInventory();
        init(inv,p);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        if (mine != null) {
            List<Material> materialList = new ArrayList<>(mine.getComposition().keySet());
            int totalPages = (((materialList.size() - materialList.size()%45)/45)+1);
            for (int i = 0;i<inv.getSize();i++) {
                switch (i) {
                    case 1,2,3,5,6,7 -> inv.setItem(i,ItemBuilder.getDefaultFiller());
                    case 0 -> inv.setItem(i,getPreviousPage(0));
                    case 8 -> {
                        int page = 2;
                        if (page > totalPages) {
                            inv.setItem(i,ItemBuilder.getDefaultFiller());
                        } else {
                            inv.setItem(i,getNextPage(2, totalPages));
                        }
                    }
                    case 4 -> inv.setItem(i,getMiddleButton());
                    default -> {
                        if (getIndex(i) < materialList.size()) {
                            inv.setItem(i,getBlockItem(materialList.get(getIndex(i)),p));
                        }
                    }
                }
            }
        }
    }

    private void updateGUI(Player p) {
        Inventory inv = p.getOpenInventory().getTopInventory();
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        List<Material> materialList = new ArrayList<>(mine.getComposition().keySet());
        int totalPages = (((materialList.size() - materialList.size()%45)/45)+1);
        int page = pageMap.get(p.getUniqueId());
        int pageOffset = page*45;
        for (int i = 0;i<45;i++) {
            if (pageOffset+i < materialList.size()) {
                Material mat = materialList.get(pageOffset+i);
                inv.setItem(getSlot(i),getBlockItem(mat,p));
            } else {
                inv.setItem(getSlot(i),new ItemStack(Material.AIR));
            }
        }
        inv.setItem(0,getPreviousPage(page));
        inv.setItem(8,getNextPage(page+2,totalPages));
    }



    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Inventory inv = e.getClickedInventory();
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        List<Material> materialList = new ArrayList<>(mine.getComposition().keySet());
        Map<Material,Double> mineComposition = mine.getComposition();
        int slot = e.getRawSlot();
        int page = pageMap.get(p.getUniqueId());
        int totalPages = ((materialList.size() - materialList.size()%45)/45)+1;
        ClickType clickType = e.getClick();
        if (e.getClickedInventory() == e.getView().getTopInventory()) {
            switch (slot) {
                case 0 -> {
                    if (page > 0) {
                        pageMap.put(p.getUniqueId(),page-1);
                        updateGUI(p);
                    } else {
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PMineGUI().getInv(p)));
                    }
                }
                case 4 -> {
                    switch (clickType) {
                        case LEFT,SHIFT_LEFT -> {
                            if (!(mine.getDisabledBlocks().size() <= 0)) {
                                mine.getDisabledBlocks().clear();
                                updateGUI(p);
                            }
                        }
                        case RIGHT,SHIFT_RIGHT -> {
                            if (mine.getDisabledBlocks().size() != mineComposition.keySet().size()) {
                                mine.getDisabledBlocks().addAll(mineComposition.keySet());
                                updateGUI(p);
                            }
                        }
                    }
                }
                case 8 -> {
                    if (totalPages-1 > page) {
                        pageMap.put(p.getUniqueId(),page+1);
                        updateGUI(p);
                    }
                }
                default -> {
                    if (slot >= 9) {
                        if (item != null && item.getType() != Material.AIR) {
                            if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
                                editMap.put(p.getUniqueId(),item.getType());
                                Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),p::closeInventory);
                                e.getWhoClicked().sendMessage("Type a number into chat to set the percentage.");
                            } else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
                                if (mine.getDisabledBlocks().contains(item.getType())) {
                                    mine.getDisabledBlocks().remove(item.getType());
                                    inv.setItem(slot,getBlockItem(item.getType(),p));
                                } else {
                                    mine.getDisabledBlocks().add(item.getType());
                                    inv.setItem(slot,getBlockItem(item.getType(),p));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }

    public void onOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        pageMap.put(player.getUniqueId(),0);
    }

    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        pageMap.remove(player.getUniqueId());
    }

    private ItemStack getBlockItem(Material material, Player p) {
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        double chance = mine.getComposition().get(material);
        List<String> lore = new ArrayList<>();
        String status = !mine.getDisabledBlocks().contains(material) ? "&aEnabled" : "&cDisabled";
        lore.add(StringUtil.color("&7Chance: &d" + chance*100 + "%"));
        lore.add(StringUtil.color("&7Status: "+status));
        lore.add(" ");
        lore.add(StringUtil.color("&eL-Click to modify!"));
        if (!mine.getDisabledBlocks().contains(material)) {
            lore.add(StringUtil.color("&aR-Click to disable!"));
        } else {
            lore.add(StringUtil.color("&bR-Click to enable!"));
        }
        return ItemBuilder.createItem(null,material,lore);
    }

    private ItemStack getMiddleButton() {
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&eL-Click to enable all"));
        lore.add(StringUtil.color("&aR-Click to disable all"));
        return ItemBuilder.createItem("&fToggle all blocks",Material.OBSIDIAN,lore);
    }

    public static Map<UUID, Material> getEditMap() {
        return editMap;
    }

    private int getSlot(int index) {
        return index+9;
    }
    private int getIndex(int slot) {
        return slot-9;
    }

    private ItemStack getPreviousPage(int slot) {
        return slot <= 0 ? ItemBuilder.getGoBack() : ItemBuilder.getPreviousPage(slot);
    }

    private ItemStack getNextPage(int slot, int totalPages) {
        if (slot > totalPages) {
            return ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null);
        } else {
            return ItemBuilder.getNextPage(slot);
        }
    }
}
