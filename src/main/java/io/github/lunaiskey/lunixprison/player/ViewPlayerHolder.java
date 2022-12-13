package io.github.lunaiskey.lunixprison.player;

import io.github.lunaiskey.lunixprison.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.gui.LunixInvType;
import org.bukkit.entity.Player;

public class ViewPlayerHolder extends LunixHolder {

    private Player player;

    public ViewPlayerHolder(String name, int size, LunixInvType invType, Player player) {
        super(name, size, invType);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
