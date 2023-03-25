package io.github.lunaiskey.lunixprison.util.reward.rewards;

import io.github.lunaiskey.lunixprison.util.reward.LunixReward;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemStackReward implements LunixReward {

    private ItemStack itemStack;

    public ItemStackReward(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    @Override
    public void execute(Player player) {
        Location playerLoc = player.getLocation();
        Map<Integer,ItemStack> leftOver = player.getInventory().addItem(itemStack);
        if (playerLoc.getWorld() != null) {
            for (ItemStack leftOverItem : leftOver.values()) {
                playerLoc.getWorld().dropItem(playerLoc,leftOverItem);
            }
        }

    }
}
