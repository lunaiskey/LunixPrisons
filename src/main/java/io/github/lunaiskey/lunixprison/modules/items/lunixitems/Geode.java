package io.github.lunaiskey.lunixprison.modules.items.lunixitems;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.modules.items.LunixPlayerHeadItem;
import io.github.lunaiskey.lunixprison.util.nms.NBTTags;
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

public class Geode extends LunixPlayerHeadItem {

    private List<Reward> rewardList = new ArrayList<>();

    public Geode(GeodeRarity rarity) {
        super(rarity.getItemID(), rarity.getDisplayName(), null,null);
        switch (rarity) {
            case COMMON -> rewardList.add(new Reward("5,000 Tokens, 50 Gems",1,null,List.of("tokens give %player% 5000","gems give %player% 50")));
            case UNCOMMON -> rewardList.add(new Reward("10,000 Tokens, 75 Gems",1,null,List.of("tokens give %player% 10000","gems give %player% 75")));
            case RARE -> rewardList.add(new Reward("15,000 Tokens, 100 Gems",1,null,List.of("tokens give %player% 15000","gems give %player% 100")));
            case EPIC -> rewardList.add(new Reward("20,000 Tokens, 125 Gems",1,null,List.of("tokens give %player% 20000","gems give %player% 125")));
            case LEGENDARY -> rewardList.add(new Reward("30,000 Tokens, 150 Gems",1,null,List.of("tokens give %player% 30000","gems give %player% 150")));
        }
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
        //boolean success = false;
        rewardList.get(0).giveReward(p);
        e.getItem().setAmount(e.getItem().getAmount()-1);
    }

    @Override
    public UUID getHeadUUID() {
        return UUID.fromString("a2f2daf5-0ffd-4ae5-acf8-4e7193e7228d");
    }

    @Override
    public String getHeadBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NmQ3YjJjZjlhMjE3ODRmMzZmNDU5MjE1MWRmYzU1YWE3YzZlZjlhZjRhNDMwYWJkODZkZTBmMDhiMjI0ZCJ9fX0=";
    }

    public enum GeodeRarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY,
        ;

        public ItemID getItemID() {
            return switch (this) {
                case COMMON -> ItemID.COMMON_GEODE;
                case UNCOMMON -> ItemID.UNCOMMON_GEODE;
                case RARE -> ItemID.RARE_GEODE;
                case EPIC -> ItemID.EPIC_GEODE;
                case LEGENDARY -> ItemID.LEGENDARY_GEODE;
            };
        }

        public String getDisplayName() {
            String displayName = "";
            switch (this) {
                case COMMON -> displayName = "&fCommon ";
                case UNCOMMON -> displayName = "&aUncommon ";
                case RARE -> displayName = "&9Rare ";
                case EPIC -> displayName = "&5Epic ";
                case LEGENDARY -> displayName = "&6Legendary ";
            }
            return StringUtil.color(displayName.concat("&7Geode"));
        }
    }
}
