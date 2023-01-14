package io.github.lunaiskey.lunixprison.modules.player;

import io.github.lunaiskey.lunixprison.LunixPrison;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class Currency {

    /**
     * Give a type of currency to a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to give currency to
     * @param type type of currency to give
     * @param amount amount of currency to take
     * @return amount that was given
     */
    public static void giveCurrency(UUID pUUID, CurrencyType type, long amount) {
        giveCurrency(pUUID, type, BigInteger.valueOf(amount));
    }

    /**
     * Give a type of currency to a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to give currency to
     * @param type type of currency to give
     * @param amount amount of currency to take
     * @return amount that was given
     */
    public static void giveCurrency(UUID pUUID, CurrencyType type, BigInteger amount) {
        if (Objects.equals(amount, BigInteger.ZERO)) {
            return;
        }
        LunixPlayer pPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> pPlayer.giveTokens(amount);
            case GEMS -> pPlayer.giveGems(amount.longValue());
            case LUNIX_POINTS -> pPlayer.giveLunixPoints(amount.longValue());
        }
    }

    /**
     * Takes a type of currency from a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to take currency from
     * @param type type of currency to take
     * @param amount amount of currency to take
     * @return amount that was taken
     */
    public static void takeCurrency(UUID pUUID, CurrencyType type,  long amount) {
        takeCurrency(pUUID, type, BigInteger.valueOf(amount));
    }

    /**
     * Takes a type of currency from a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to take currency from
     * @param type type of currency to take
     * @param amount amount of currency to take
     * @return amount that was taken
     */
    public static void takeCurrency(UUID pUUID, CurrencyType type,  BigInteger amount) {
        if (Objects.equals(amount, BigInteger.ZERO)) {
            return;
        }
        LunixPlayer pPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> {
                if (amount.compareTo(pPlayer.getTokens()) >= 0) {
                    pPlayer.setTokens(BigInteger.ZERO);
                    break;
                }
                pPlayer.takeTokens(amount);}
            case GEMS -> {
                if (amount.longValue() >= pPlayer.getGems()) {
                    pPlayer.setGems(0);
                    break;
                }
                pPlayer.takeGems(amount.longValue());}
            case LUNIX_POINTS -> {
                if (amount.longValue() >= pPlayer.getLunixPoints()) {
                    pPlayer.setLunixPoints(0);
                    break;
                }
                pPlayer.takeLunixPoints(amount.longValue());}
        }
    }
    public static void setCurrency(UUID pUUID, CurrencyType type, long amount) {
        setCurrency(pUUID, type, BigInteger.valueOf(amount));
    }

    public static void setCurrency(UUID pUUID, CurrencyType type, BigInteger amount) {
        BigInteger newAmount = BigInteger.ZERO;
        if (amount.compareTo(newAmount) > 0) {
            newAmount = amount;
        }
        LunixPlayer pPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> pPlayer.setTokens(newAmount);
            case GEMS -> pPlayer.setGems(newAmount.longValue());
            case LUNIX_POINTS -> pPlayer.setLunixPoints(newAmount.longValue());
        }
    }

    private static double getAcceptedAmount(double original) {
        if (original < 0) {
            return 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(original));
    }
}
