package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;

import java.math.BigInteger;
import java.util.Map;

public class EnchantLunixHolder extends LunixHolder {

    private final EnchantType type;
    private Map<Integer, BigInteger> costMap;

    public EnchantLunixHolder(String name, int size, LunixInvType invType, EnchantType type, Map<Integer, BigInteger> costMap) {
        super(name, size, invType);
        this.type = type;
        this.costMap = costMap;
    }

    public EnchantType getType() {
        return type;
    }

    public Map<Integer, BigInteger> getCostMap() {
        return costMap;
    }
}
