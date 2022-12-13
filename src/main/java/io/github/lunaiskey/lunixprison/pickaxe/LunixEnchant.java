package io.github.lunaiskey.lunixprison.pickaxe;

import io.github.lunaiskey.lunixprison.player.CurrencyType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public abstract class LunixEnchant {

    private String name;
    private int maxLevel;
    private double chance;
    private boolean enabled;
    private List<String> description;
    private CurrencyType currencyType;

    public LunixEnchant(String name, List<String> description, int maxLevel, CurrencyType currencyType, boolean enabled) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.enabled = enabled;
        this.currencyType = currencyType;
    }

    public abstract void onBlockBreak(BlockBreakEvent e, int level);
    public abstract void onDrop(PlayerDropItemEvent e, int level);
    public abstract void onEquip(Player player, ItemStack pickaxe, int level);
    public abstract void onUnEquip(Player player, ItemStack pickaxe, int level);

    public abstract BigInteger getCost(int n);

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<String> getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public BigInteger getTotalCost(int level) {
        BigInteger sum = BigInteger.ZERO;
        for (int n = 0;n<level;n++) {
            sum = sum.add(getCost(n));
        }
        return sum;
    }

    public BigInteger getSingleLevelCost(int level) {
        return getCost(level);
    }

    public Pair<Integer,BigInteger> getMaxLevelFromAmount(int start, BigInteger amount) {
        BigInteger sum = BigInteger.ZERO;
        for (int n = start;n<getMaxLevel();n++) {
            //if (sum+getEquation(n) < amount) {
            if (sum.add(getCost(n)).compareTo(amount) < 0) {
                sum = sum.add(getCost(n));
            } else {
                return new ImmutablePair<>(n,sum);
            }
        }
        return new ImmutablePair<>(getMaxLevel(),sum);
    }

    public BigInteger getCostBetweenLevels(int start, int end) {
        BigInteger sum = BigInteger.ZERO;
        for (int n = start;n<end;n++) {
            sum = sum.add(getCost(n));
        }
        return sum;
    }
}
