package io.github.lunaiskey.lunixprison.pickaxe;

import io.github.lunaiskey.lunixprison.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;

public class EnchantLunixHolder extends LunixHolder {

    private final EnchantType type;

    public EnchantLunixHolder(String name, int size, LunixInvType invType, EnchantType type) {
        super(name, size, invType);
        this.type = type;
    }

    public EnchantType getType() {
        return type;
    }
}
