package io.github.lunaiskey.lunixprison.util.reward.rewards;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
import io.github.lunaiskey.lunixprison.util.reward.LunixReward;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class LunixItemReward implements LunixReward {

    private ItemID itemID;

    public LunixItemReward(ItemID itemID) {
        this.itemID = itemID;
    }
    @Override
    public void execute(Player player) {
        LunixItem lunixItem = LunixPrison.getPlugin().getItemManager().getLunixItem(itemID);
        Location playerLoc = player.getLocation();
        Map<Integer, ItemStack> leftOver = player.getInventory().addItem(lunixItem.getItemStack());
        if (playerLoc.getWorld() != null) {
            for (ItemStack leftOverItem : leftOver.values()) {
                playerLoc.getWorld().dropItem(playerLoc,leftOverItem);
            }
        }
    }
}
