package io.github.lunaiskey.lunixprison.util.reward.rewards;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.reward.LunixReward;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class CurrencyReward implements LunixReward {

    private CurrencyType currencyType;
    private BigInteger amount;

    public CurrencyReward(CurrencyType currencyType, BigInteger amount) {
        this.currencyType = currencyType;
        this.amount = amount;
    }

    @Override
    public void execute(Player player) {
        LunixPrison.getPlugin().getPlayerManager().getLunixPlayer(player.getUniqueId()).giveCurrency(currencyType,amount);
    }
}
