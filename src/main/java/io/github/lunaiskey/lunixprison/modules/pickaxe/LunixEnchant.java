package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.*;

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

    public Map<Integer,BigInteger> getCostAmountFromLevelArray(int start, int[] amountArray) {
        Map<Integer,BigInteger> amountMap = new HashMap<>();
        BigInteger sum = BigInteger.ZERO;
        int currentArrayIndex = 0;
        int endAmount = Math.min(start+amountArray[amountArray.length - 1], getMaxLevel());
        for (int i = start;i<endAmount;i++) {
            //if (sum+getEquation(n) < amount) {
            sum = sum.add(getCost(i));
            if (i-start+1 == amountArray[currentArrayIndex]) {
                amountMap.put(i-start+1,sum);
                currentArrayIndex++;
            }
        }
        for (int i = currentArrayIndex;i<amountArray.length;i++) {
            int amount = amountArray[i];
            if (!amountMap.containsKey(amount)) {
                amountMap.put(amount,sum);
            }
        }
        return amountMap;
    }

    public BigInteger getCostBetweenLevels(int start, int end) {
        BigInteger sum = BigInteger.ZERO;
        for (int n = start;n<end;n++) {
            sum = sum.add(getCost(n));
        }
        return sum;
    }
}
