package io.github.lunaiskey.lunixprison.modules.items.items;

import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixPlayerHeadItem;
import io.github.lunaiskey.lunixprison.modules.items.meta.LunixItemMeta;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import io.github.lunaiskey.lunixprison.util.reward.RewardStorage;
import io.github.lunaiskey.lunixprison.util.reward.rewards.CurrencyReward;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Geode extends LunixPlayerHeadItem {

    private List<RewardStorage> rewardStorageList = new ArrayList<>();

    public Geode(GeodeRarity rarity) {
        super(rarity.getItemID(), rarity.getDisplayName(), null,null);
        switch (rarity) {
            case COMMON -> rewardStorageList.add(new RewardStorage("5,000 Tokens, 50 Gems",1,List.of(
                    new CurrencyReward(CurrencyType.TOKENS, BigInteger.valueOf(5000)),
                    new CurrencyReward(CurrencyType.GEMS,BigInteger.valueOf(50))
                    )));
            case UNCOMMON -> rewardStorageList.add(new RewardStorage("10,000 Tokens, 75 Gems",1,List.of(
                    new CurrencyReward(CurrencyType.TOKENS, BigInteger.valueOf(10000)),
                    new CurrencyReward(CurrencyType.GEMS,BigInteger.valueOf(75))
            )));
            case RARE -> rewardStorageList.add(new RewardStorage("15,000 Tokens, 100 Gems",1,List.of(
                    new CurrencyReward(CurrencyType.TOKENS, BigInteger.valueOf(15000)),
                    new CurrencyReward(CurrencyType.GEMS,BigInteger.valueOf(100))
            )));
            case EPIC -> rewardStorageList.add(new RewardStorage("20,000 Tokens, 125 Gems",1,List.of(
                    new CurrencyReward(CurrencyType.TOKENS, BigInteger.valueOf(20000)),
                    new CurrencyReward(CurrencyType.GEMS,BigInteger.valueOf(125))
            )));
            case LEGENDARY -> rewardStorageList.add(new RewardStorage("30,000 Tokens, 150 Gems",1,List.of(
                    new CurrencyReward(CurrencyType.TOKENS, BigInteger.valueOf(30000)),
                    new CurrencyReward(CurrencyType.GEMS,BigInteger.valueOf(150))
            )));
        }
    }

    @Override
    public List<String> getLore(LunixItemMeta meta) {
        return null;
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
        rewardStorageList.get(0).giveReward(p);
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
