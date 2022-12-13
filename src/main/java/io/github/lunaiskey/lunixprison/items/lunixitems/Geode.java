package io.github.lunaiskey.lunixprison.items.lunixitems;

import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.items.LunixItem;
import io.github.lunaiskey.lunixprison.nms.NBTTags;
import io.github.lunaiskey.lunixprison.util.ItemBuilder;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.reward.Reward;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Geode extends LunixItem {

    private static List<Reward> common = new ArrayList<>();
    private static List<Reward> uncommon = new ArrayList<>();
    private static List<Reward> rare = new ArrayList<>();
    private static List<Reward> epic = new ArrayList<>();
    private static List<Reward> legendary = new ArrayList<>();
    public Geode(ItemID id) {
        super(id, "&7Geode", null, Material.PLAYER_HEAD);
    }

    @Override
    public ItemStack getItemStack() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NmQ3YjJjZjlhMjE3ODRmMzZmNDU5MjE1MWRmYzU1YWE3YzZlZjlhZjRhNDMwYWJkODZkZTBmMDhiMjI0ZCJ9fX0=";
        UUID profileUUID = UUID.fromString("a2f2daf5-0ffd-4ae5-acf8-4e7193e7228d");
        String displayName = "";
        switch (getItemID()) {
            case COMMON_GEODE -> displayName = StringUtil.color("&fCommon "+getName());
            case UNCOMMON_GEODE -> displayName = StringUtil.color("&aUncommon "+getName());
            case RARE_GEODE -> displayName = StringUtil.color("&9Rare "+getName());
            case EPIC_GEODE -> displayName = StringUtil.color("&5Epic "+getName());
            case LEGENDARY_GEODE -> displayName = StringUtil.color("&6Legendary "+getName());
        }
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7A small rock found while mining."));
        lore.add("");
        lore.add(StringUtil.color("&eR-Click to crack open!"));
        ItemStack item = ItemBuilder.getSkull(displayName,lore,base64,profileUUID);
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        return item;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack geode = e.getItem();
        if (geode == null) {
            return;
        }
        //Random rand = PyrexPrison.getPlugin().getRand();
        //PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId());
        boolean success = false;
        switch (getItemID()) {
            case COMMON_GEODE -> {
                common.get(0).giveReward(p);
            }
            case UNCOMMON_GEODE -> {
                uncommon.get(0).giveReward(p);
            }
            case RARE_GEODE -> {
                rare.get(0).giveReward(p);
            }
            case EPIC_GEODE -> {
                epic.get(0).giveReward(p);
            }
            case LEGENDARY_GEODE -> {
                legendary.get(0).giveReward(p);
            }
        }
        e.getItem().setAmount(e.getItem().getAmount()-1);
    }

    static {
        common.add(new Reward("5,000 Tokens, 50 Gems",1,null,List.of("tokens give %player% 5000","gems give %player% 50")));
        uncommon.add(new Reward("10,000 Tokens, 75 Gems",1,null,List.of("tokens give %player% 10000","gems give %player% 75")));
        rare.add(new Reward("15,000 Tokens, 100 Gems",1,null,List.of("tokens give %player% 15000","gems give %player% 100")));
        epic.add(new Reward("20,000 Tokens, 125 Gems",1,null,List.of("tokens give %player% 20000","gems give %player% 125")));
        legendary.add(new Reward("30,000 Tokens, 150 Gems",1,null,List.of("tokens give %player% 30000","gems give %player% 150")));
    }
}
