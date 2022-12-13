package io.github.lunaiskey.lunixprison.player.rankup;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Rankup {

    private static final int maxRankup = 10000;

    public int rankup(Player p) {
        return rankup(p,1);
    }

    public int rankup(Player p, int amount) {
        LunixPlayer player = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        int newRank = player.getRank()+amount;
        player.setRank(newRank);
        return newRank;
    }

    public static int getMaxRankup() {
        return maxRankup;
    }

    public static BigInteger getLevelCost(int level) {
        if (level <= 500) {
            return BigDecimal.valueOf(25000).multiply(BigDecimal.valueOf(Math.pow(1.025,level-1))).toBigInteger();
        } else {
            return BigDecimal.valueOf(25000).multiply(BigDecimal.valueOf(Math.pow(1.01,level-1+740))).toBigInteger();
        }
    }

    public String getRankString(int level) {
        return String.valueOf(level);
    }



    public static double getRankUpPercentage(Player p) {
        LunixPlayer player = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        double percentage = new BigDecimal(player.getTokens()).divide(new BigDecimal(getLevelCost(player.getRank()+1)),4,RoundingMode.HALF_UP).doubleValue() * 100;
        return percentage > 100 ? 100 : percentage;
    }

    public static String getRankUpProgressBar(Player p) {
        double percentage = getRankUpPercentage(p);
        if (percentage >= 100) {
            return StringUtil.color("&a&l||||||||||");
        } else if (percentage >= 90) {
            return StringUtil.color("&a&l|||||||||&c&l|");
        } else if (percentage >= 80) {
            return StringUtil.color("&a&l||||||||&c&l||");
        } else if (percentage >= 70) {
            return StringUtil.color("&a&l|||||||&c&l|||");
        } else if (percentage >= 60) {
            return StringUtil.color("&a&l||||||&c&l||||");
        } else if (percentage >= 50) {
            return StringUtil.color("&a&l|||||&c&l|||||");
        } else if (percentage >= 40) {
            return StringUtil.color("&a&l||||&c&l||||||");
        } else if (percentage >= 30) {
            return StringUtil.color("&a&l|||&c&l|||||||");
        } else if (percentage >= 20) {
            return StringUtil.color("&a&l||&c&l||||||||");
        } else if (percentage >= 10) {
            return StringUtil.color("&a&l|&c&l|||||||||");
        } else {
            return StringUtil.color("&c&l||||||||||");
        }
    }
}
