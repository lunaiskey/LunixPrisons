package io.github.lunaiskey.lunixprison.modules.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixPagedHolder;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
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

    private static Map<UUID,Material> editMap = new HashMap<>();

    private static final int blocksPerPageSize = 45;

    @Override
    public Inventory getInv(Player p) {
        Inventory inv = new LunixPagedHolder("Blocks",54, LunixInvType.PMINE_BLOCKS).getInventory();
        init(inv,p);
        return inv;
    }

    public void init(Inventory inv, Player p) {
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        LunixPagedHolder holder = (LunixPagedHolder) inv.getHolder();
        if (mine == null) {
            return;
        }
        List<Material> materialList = new ArrayList<>(mine.getComposition().keySet());
        for (int i = 0;i<inv.getSize();i++) {
            switch (i) {
                case 1,2,3,5,6,7 -> inv.setItem(i,ItemBuilder.getDefaultFiller());
                case 0 -> inv.setItem(i,getPreviousPage(0));
                case 8 -> {
                    int page = holder.getPage();
                    int totalPages = (((materialList.size()-materialList.size()% blocksPerPageSize)/blocksPerPageSize)+1);
                    inv.setItem(i,getNextPage(page+1, totalPages));
                }
                case 4 -> inv.setItem(i, getToggleAllButton());
                default -> {
                    if (getIndex(i) >= materialList.size()) {
                        return;
                    }
                    inv.setItem(i,getBlockItem(materialList.get(getIndex(i)),p));
                }
            }
        }
    }

    private void updateGUI(Player p) {
        Inventory inv = p.getOpenInventory().getTopInventory();
        if (!(inv.getHolder() instanceof LunixPagedHolder)) {
            return;
        }
        LunixPagedHolder holder = (LunixPagedHolder) inv.getHolder();
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        List<Material> materialList = new ArrayList<>(mine.getComposition().keySet());
        int totalPages = (((materialList.size()-materialList.size()% blocksPerPageSize)/blocksPerPageSize)+1);
        int page = holder.getPage();
        int pageOffset = page*blocksPerPageSize;
        for (int i = 0;i<blocksPerPageSize;i++) {
            if (pageOffset+i < materialList.size()) {
                Material mat = materialList.get(pageOffset+i);
                inv.setItem(getSlot(i),getBlockItem(mat,p));
            } else {
                inv.setItem(getSlot(i),new ItemStack(Material.AIR));
            }
        }
        inv.setItem(0,getPreviousPage(page-1));
        inv.setItem(8,getNextPage(page+1,totalPages));
    }



    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!(e.getInventory().getHolder() instanceof LunixPagedHolder)) {
            return;
        }
        LunixPagedHolder holder = (LunixPagedHolder) e.getInventory().getHolder();
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Inventory inv = e.getClickedInventory();
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        List<Material> materialList = new ArrayList<>(mine.getComposition().keySet());
        Map<Material,Double> mineComposition = mine.getComposition();
        int slot = e.getRawSlot();
        int page = holder.getPage();
        int totalPages = ((materialList.size() - materialList.size()%45)/45)+1;
        ClickType clickType = e.getClick();
        if (e.getClickedInventory() == e.getView().getTopInventory()) {
            switch (slot) {
                case 0 -> {
                    if (page > 1) {
                        holder.setPage(page-1);
                        updateGUI(p);
                        return;
                    }
                    Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PMineGUI().getInv(p)));
                }
                case 4 -> {
                    switch (clickType) {
                        case LEFT,SHIFT_LEFT -> {
                            if (!(mine.getDisabledBlocks().size() == 0)) {
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
                        holder.setPage(page+1);
                        updateGUI(p);
                    }
                }
                case 1,2,3,5,6,7 -> {}
                default -> {
                    if (item == null || item.getType() == Material.AIR) {
                        return;
                    }
                    switch (clickType) {
                        case LEFT,SHIFT_LEFT ->{
                            Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),p::closeInventory);
                            editMap.put(p.getUniqueId(),item.getType());
                            e.getWhoClicked().sendMessage("Type a number into chat to set the percentage.");
                        }
                        case RIGHT,SHIFT_RIGHT -> {
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

    @Override
    public void onDrag(InventoryDragEvent e) {

    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        editMap.remove(e.getPlayer().getUniqueId());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {

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

    private ItemStack getToggleAllButton() {
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&eL-Click to enable all"));
        lore.add(StringUtil.color("&aR-Click to disable all"));
        return ItemBuilder.createItem("&fToggle all blocks",Material.OBSIDIAN,lore);
    }

    private int getSlot(int index) {
        return index+9;
    }
    private int getIndex(int slot) {
        return slot-9;
    }

    private ItemStack getPreviousPage(int page) {
        return page <= 0 ? ItemBuilder.getGoBack() : ItemBuilder.getPreviousPage(page);
    }

    private ItemStack getNextPage(int page, int totalPages) {
        if (page > totalPages) {
            return ItemBuilder.getDefaultFiller();
        } else {
            return ItemBuilder.getNextPage(page);
        }
    }

    public static Map<UUID, Material> getEditMap() {
        return editMap;
    }
}
