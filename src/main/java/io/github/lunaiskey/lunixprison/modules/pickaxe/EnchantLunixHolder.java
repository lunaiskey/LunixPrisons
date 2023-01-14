package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.util.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;

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
