package io.github.lunaiskey.lunixprison.modules.player.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.util.gui.LunixPagedHolder;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.boosters.Booster;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.util.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PersonalBoosterGUI implements LunixInventory {

    private final String name = "Personal Boosters";
    private final int size = 36;
    private Inventory inv = new LunixPagedHolder(name,size, LunixInvType.PERSONAL_BOOSTER).getInventory();
    private Player player;

    public PersonalBoosterGUI(Player player) {
        this.player = player;
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 10,11,12,13,14,15,16,19,20,21,22,23,24,25 -> {
                    inv.setItem(i,getBoosterItem(getIndex(i)));
                }
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
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

    public void onOpen(InventoryOpenEvent e) {

    }

    public void onClose(InventoryCloseEvent e) {

    }

    private ItemStack getBoosterItem(int index) {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        List<Booster> boosters = lunixPlayer.getBoosters();
        if (index < boosters.size()) {
            Booster booster = boosters.get(index);
            Material material = Material.BEACON;
            String name = booster.getType().getName();
            List<String> lore = new ArrayList<>();
            lore.add(StringUtil.color("&7Multiplier: &f"+booster.getMultiplier()+"x"));
            lore.add(StringUtil.color("&7Ending in: &f"+TimeUtil.countdown(booster.getEndTime())));
            return ItemBuilder.createItem(name,material,lore);
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    public void updateGUI(Player p) {
        Inventory inv = p.getOpenInventory().getTopInventory();
        if (inv.getHolder() instanceof LunixPagedHolder) {
            LunixPagedHolder holder = (LunixPagedHolder) inv.getHolder();
            int page = holder.getPage();
            if (holder.getInvType() == LunixInvType.PERSONAL_BOOSTER) {
                for (int i = 0;i<14;i++) {
                    int slot = getSlot(i);
                    int index = (page*18)+i;
                    inv.setItem(slot,getBoosterItem(index));
                }
            }
        }
    }

    private int getIndex(int slot) {
        return switch(slot) {
            case 10,11,12,13,14,15,16 -> slot-10;
            case 19,20,21,22,23,24,25 -> slot-12;
            default -> -1;
        };
    }

    private int getSlot(int index) {
        return switch(index) {
            case 0,1,2,3,4,5,6 -> index+10;
            case 7,8,9,10,11,12,13 -> index+12;
            default -> -1;
        };
    }
}
