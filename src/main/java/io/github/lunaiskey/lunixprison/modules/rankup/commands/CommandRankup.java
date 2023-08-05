package io.github.lunaiskey.lunixprison.modules.rankup.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.rankup.Rankup;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class CommandRankup implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        LunixPlayer player = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
        Rankup rankup = new Rankup();
        CurrencyType type = CurrencyType.TOKENS;
        BigInteger cost = Rankup.getLevelCost(player.getRank()+1);
        if (player.getRank() >= Rankup.getMaxRankup()) {
            p.sendMessage(StringUtil.color("&cYou've already maxed your rank."));
            return true;
        }
        if (player.getTokens().compareTo(cost) < 0) {
            p.sendMessage(StringUtil.color("&7You still need "+Numbers.formattedNumber(cost.subtract(player.getTokens()))+" to rankup."));
            return true;
        }
        int newLevel = rankup.rankup(p);
        int nextLevel = newLevel+1;
        player.takeTokens(cost,false);
        p.sendMessage(
                StringUtil.color("&b&lYou have ranked up to &f&l"+newLevel+"&b&l!")
        );
        if (newLevel < Rankup.getMaxRankup()) {
            p.sendMessage(
                    StringUtil.color(" &3&l- &bNext Rankup: &f"+nextLevel),
                    StringUtil.color(" &3&l- &bCost: "+type.getColorCode()+type.getUnicode()+"&f"+ Numbers.formattedNumber(Rankup.getLevelCost(nextLevel)))
            );
            return true;
        }
        p.sendMessage(
                StringUtil.color("&aYou have maxed out your rank, congratulation!")
        );
        return true;
    }

}
