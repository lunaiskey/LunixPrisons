package io.github.lunaiskey.lunixprison.player.armor;

import io.github.lunaiskey.lunixprison.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;

public class ArmorLunixHolder extends LunixHolder {

    private ArmorType type;

    public ArmorLunixHolder(String name, int size, LunixInvType invType, ArmorType type) {
        super(name, size, invType);
        this.type = type;
    }

    public ArmorType getType() {
        return type;
    }
}
