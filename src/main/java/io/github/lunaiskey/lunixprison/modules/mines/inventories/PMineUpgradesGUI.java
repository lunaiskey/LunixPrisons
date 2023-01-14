package io.github.lunaiskey.lunixprison.modules.mines.inventories;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.mines.upgrades.upgrades.Size;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;
import io.github.lunaiskey.lunixprison.util.gui.LunixInventory;
import io.github.lunaiskey.lunixprison.modules.mines.upgrades.PMineUpgrade;
import io.github.lunaiskey.lunixprison.modules.mines.upgrades.PMineUpgradeType;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PMineUpgradesGUI implements LunixInventory {

    private final Player player;
    private final String name = "Upgrades";
    private final int size = 36;
    private final PMine mine;
    private final Map<PMineUpgradeType,PMineUpgrade> upgradeMap = PMineUpgradeType.getUpgradeMap();
    private final Map<PMineUpgradeType,Integer> upgradeLevelMap;
    private static Set<UUID> pendingSizeReset = new HashSet<>();

    private static final Map<Integer, PMineUpgradeType> typeMap = new HashMap<>();

    private Inventory inv = new LunixHolder(name,size, LunixInvType.PMINE_UPGRADES).getInventory();

    static {
        typeMap.put(20,PMineUpgradeType.SIZE);
        //typeMap.put(22,PMineUpgradeType.SELL_PRICE);
        //typeMap.put(24,PMineUpgradeType.MAX_PLAYERS);
    }

    public PMineUpgradesGUI(Player player) {
        this.player = player;
        this.mine = LunixPrison.getPlugin().getPMineManager().getPMine(player.getUniqueId());
        this.upgradeLevelMap = mine.getUpgradeMap();
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 13 -> inv.setItem(i,getPlayerSkull(player));
                case 20,22,24 -> {
                    if (typeMap.containsKey(i)) {
                        inv.setItem(i,getUpgradeIcon(typeMap.get(i)));
                    } else {
                        inv.setItem(i,getComingSoon());
                    }
                }
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ",Material.PURPLE_STAINED_GLASS_PANE,null));
                default -> inv.setItem(i, ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (pendingSizeReset.contains(p.getUniqueId())) {
            mine.genBedrock();
            mine.reset();
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(p.getUniqueId());
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getWhoClicked().getUniqueId());
        switch (slot) {
            case 20 -> {
                int level = upgradeLevelMap.get(PMineUpgradeType.SIZE);
                Size size = (Size) upgradeMap.get(PMineUpgradeType.SIZE);
                if (level < size.getMaxLevel()) {
                    if (lunixPlayer.getGems() >= size.getCost(level+1)) {
                        lunixPlayer.takeGems(size.getCost(level+1));
                        mine.setRadius(12 + size.getRadiusIncrease(level+1));
                        upgradeLevelMap.put(PMineUpgradeType.SIZE,level+1);
                        e.getClickedInventory().setItem(slot,getUpgradeIcon(typeMap.get(slot)));
                        pendingSizeReset.add(p.getUniqueId());
                    } else {
                        p.sendMessage(StringUtil.color("You can't afford this upgrade."));
                    }
                } else {
                    //p.sendMessage(StringUtil.color("You've already maxed out this upgrade."));
                }
            }
            //case 22,24 -> p.sendMessage(StringUtil.color("&cThis upgrade is currently unavailable."));
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    private ItemStack getComingSoon() {
        return ItemBuilder.createItem("&c&lCOMING SOON",Material.BEDROCK,null);
    }

    private ItemStack getUpgradeIcon(PMineUpgradeType type) {
        CurrencyType currencyType = CurrencyType.GEMS;
        PMineUpgrade upgrade = PMineUpgradeType.getUpgradeMap().get(type);
        int level = upgradeLevelMap.get(type);
        List<String> lore = new ArrayList<>();
        for (String desc : upgrade.getDescription()) {
            lore.add(StringUtil.color("&7"+desc));
            lore.add(" ");
        }
        if (level < upgrade.getMaxLevel()) {
            lore.add(StringUtil.color("&7Upgrade: &b"+upgrade.getUpgradeLore(level+1)));
            lore.add(StringUtil.color("&7Cost: &a"+currencyType.getUnicode()+"&f"+(upgrade.getCost(level+1))));
            lore.add(" ");
            lore.add(StringUtil.color("&eClick to upgrade!"));
        } else {
            lore.add(StringUtil.color("&7Current: &b"+upgrade.getUpgradeLore(level)));
            lore.add(" ");
            lore.add(StringUtil.color("&cYou've maxed this upgrade!"));
        }
        return ItemBuilder.createItem(type.getName(),type.getMaterial(),lore);
    }

    private ItemStack getPlayerSkull(Player p) {
        ItemStack item = ItemBuilder.getPlayerSkull(p);
        return item;
    }
}
