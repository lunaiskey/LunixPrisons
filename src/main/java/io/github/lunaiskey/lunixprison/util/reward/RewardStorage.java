package io.github.lunaiskey.lunixprison.util.reward;

import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RewardStorage {

    private String rewardName;
    private double chance;
    private List<LunixReward> rewards;

    public RewardStorage(String rewardName, double chance, List<LunixReward> rewards) {
        this.rewardName = rewardName;
        this.chance = chance;
        this.rewards = Objects.requireNonNullElse(rewards,new ArrayList<>());
    }

    public String getRewardName() {
        return rewardName;
    }

    public double getChance() {
        return chance;
    }

    public void giveReward(Player player) {
        for (LunixReward reward : rewards) {
            reward.execute(player);
        }
        player.sendMessage(StringUtil.color("&aYou have been given reward "+rewardName+"&a."));
    }
}
