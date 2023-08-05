package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class LunixChanceEnchant extends LunixEnchant {
    public LunixChanceEnchant(String name,EnchantType enchantID, List<String> description, int maxLevel, CurrencyType currencyType, boolean enabled) {
        super(name,enchantID, description, maxLevel, currencyType, enabled);
    }

    public abstract double getChance(int level, Player player);

    @Override
    protected void onActivationEnd(Player player, LunixPlayer lunixPlayer, PMine pMine, long blocksBroken) {
        if (!lunixPlayer.getPickaxeStorage().getDisabledMessages().contains(getEnchantID())) {
            sendActivationMessage(player);
        }
        super.onActivationEnd(player, lunixPlayer,pMine, blocksBroken);
    }

    protected void onActivationEnd(Player player, LunixPlayer lunixPlayer) {
        if (!lunixPlayer.getPickaxeStorage().getDisabledMessages().contains(getEnchantID())) {
            sendActivationMessage(player);
        }
    }

    protected void sendActivationMessage(Player player) {
        player.sendMessage(ChatColor.WHITE+"Enchant "+ChatColor.AQUA+getName()+ChatColor.WHITE+" has just activated.");
    }
}
