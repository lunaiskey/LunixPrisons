package io.github.lunaiskey.lunixprison.util.reward.rewards;

import io.github.lunaiskey.lunixprison.util.reward.LunixReward;
import org.bukkit.entity.Player;

public class XPReward implements LunixReward {

    private int amount;

    public XPReward(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player player) {
        player.giveExpLevels(amount);
    }
}
