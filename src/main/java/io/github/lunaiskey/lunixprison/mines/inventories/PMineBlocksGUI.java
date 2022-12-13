package io.github.lunaiskey.lunixprison.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.mines.PMine;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.*;

public class PMineBlocksGUI implements LunixInventory {

    private static Map<UUID,Material> editMap;
    private static Map<UUID,Integer> pageMap;

    private String name = "Blocks";
    private int size = 54;
    private Player p;
    private PMine mine;
    private List<Material> materialList;
    private Map<Material,Double> mineComposition;
    private int totalPages;

    static {
        editMap = new HashMap<>();
        pageMap = new HashMap<>();
    }


    public PMineBlocksGUI(Player p) {
        this.p = p;
        this.mine = LunixPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId());
        materialList = new ArrayList<>(mine.getComposition().keySet());
        mineComposition = mine.getComposition();
        totalPages = (((materialList.size() - materialList.size()%45)/45)+1);
    }

    private final Inventory inv = new LunixHolder(name,size, LunixInvType.PMINE_BLOCKS).getInventory();

    @Override
    public void init() {
        if (mine != null) {
            for (int i = 0;i<size;i++) {
                switch (i) {
                    case 1,2,3,5,6,7 -> inv.setItem(i,ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
                    case 0 -> inv.setItem(i,getPreviousPage(0));
                    case 8 -> {
                        int page = 2;
                        if (page > totalPages) {
                            inv.setItem(i,ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
                        } else {
                            inv.setItem(i,getNextPage(2));
                        }
                    }
                    case 4 -> inv.setItem(i,getMiddleButton());
                    default -> {
                        if (getIndex(i) < materialList.size()) {
                            inv.setItem(i,getBlockItem(materialList.get(getIndex(i))));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    private void updateGUI(Player p) {
        Inventory inv = p.getOpenInventory().getTopInventory();
        int page = pageMap.get(p.getUniqueId());
        int pageOffset = page*45;
        for (int i = 0;i<45;i++) {
            if (pageOffset+i < materialList.size()) {
                Material mat = materialList.get(pageOffset+i);
                inv.setItem(getSlot(i),getBlockItem(mat));
            } else {
                inv.setItem(getSlot(i),new ItemStack(Material.AIR));
            }
        }
        inv.setItem(0,getPreviousPage(page));
        inv.setItem(8,getNextPage(page+2));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        Inventory inv = e.getClickedInventory();
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
                        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),()->p.openInventory(new PMineGUI(p).getInv()));
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
                                    inv.setItem(slot,getBlockItem(item.getType()));
                                } else {
                                    mine.getDisabledBlocks().add(item.getType());
                                    inv.setItem(slot,getBlockItem(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void onOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        pageMap.put(player.getUniqueId(),0);
    }

    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        pageMap.remove(player.getUniqueId());
    }

    private ItemStack getBlockItem(Material material) {
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
        return ItemBuilder.createItem("&fMass toggle blocks",Material.OBSIDIAN,lore);
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

    private ItemStack getNextPage(int slot) {
        if (slot > totalPages) {
            return ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null);
        } else {
            return ItemBuilder.getNextPage(slot);
        }
    }
}
