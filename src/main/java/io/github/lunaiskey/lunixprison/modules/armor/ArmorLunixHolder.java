package io.github.lunaiskey.lunixprison.modules.armor;

import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;

public class ArmorLunixHolder extends LunixHolder {

    private ArmorSlot type;

    public ArmorLunixHolder(String name, int size, LunixInvType invType, ArmorSlot type) {
        super(name, size, invType);
        this.type = type;
    }

    public ArmorSlot getType() {
        return type;
    }
}
