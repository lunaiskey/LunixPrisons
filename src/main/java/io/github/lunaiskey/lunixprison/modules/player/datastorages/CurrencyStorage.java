package io.github.lunaiskey.lunixprison.modules.player.datastorages;

import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.UUID;

public class CurrencyStorage {
    private BigInteger tokens;
    private long gems;
    private long lunixPoints;

    private BigInteger cashback;

    public CurrencyStorage(BigInteger tokens, long gems, long lunixPoints, BigInteger cashback) {
        this.tokens = tokens;
        this.gems = gems;
        this.lunixPoints = lunixPoints;
        this.cashback = cashback;
    }

    public CurrencyStorage() {
        this(BigInteger.ZERO,0,0,BigInteger.ZERO);
    }

    public void setTokens(BigInteger tokens) {
        this.tokens = tokens;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }

    public void setLunixPoints(long lunixPoints) {
        this.lunixPoints = lunixPoints;
    }

    public void setCashback(BigInteger cashback) {
        this.cashback = cashback;
    }

    public BigInteger getTokens() {
        return tokens;
    }

    public long getGems() {
        return gems;
    }

    public long getLunixPoints() {
        return lunixPoints;
    }

    public BigInteger getCashback() {
        return cashback;
    }

    public void giveTokens(BigInteger tokens) {this.tokens = this.tokens.add(tokens);}
    public void giveTokens(long tokens) {this.tokens = this.tokens.add(BigInteger.valueOf(tokens));}
    public void giveGems(long gems) {
        this.gems += gems;
    }
    public void giveLunixPoints(long lunixPoints) {
        this.lunixPoints += lunixPoints;
    }

    public void giveCurrency(CurrencyType type, long amount) {
        giveCurrency(type, BigInteger.valueOf(amount));
    }

    public void giveCurrency(CurrencyType type, BigInteger amount) {
        switch (type) {
            case GEMS -> giveGems(amount.longValue());
            case LUNIX_POINTS -> giveLunixPoints(amount.longValue());
            case TOKENS -> giveTokens(amount);
        }
    }

    public void takeTokens(BigInteger tokens, boolean giveCashBack, UUID pUUID) {
        if (giveCashBack) {
            cashback = cashback.add(tokens.divide(new BigInteger("20")));
            Player player = Bukkit.getPlayer(pUUID);
            if (player != null) {
                player.sendMessage(StringUtil.color("&aYou gained some cashback, check /cashback"));
            }
        }
        this.tokens = this.tokens.subtract(tokens);
    }
    public void takeGems(long gems) {
        this.gems -= gems;
    }
    public void takeLunixPoints(long lunixPoints) {
        this.lunixPoints -= lunixPoints;
    }

    public void takeCurrency(CurrencyType type, BigInteger amount, boolean giveCashBack, UUID pUUID) {
        switch (type) {
            case GEMS -> takeGems(amount.longValue());
            case LUNIX_POINTS -> takeLunixPoints(amount.longValue());
            case TOKENS -> takeTokens(amount,giveCashBack,pUUID);
        }
    }
}
