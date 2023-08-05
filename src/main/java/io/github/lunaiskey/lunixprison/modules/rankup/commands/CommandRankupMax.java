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

public class CommandRankupMax implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        LunixPlayer player = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
        CurrencyType type = CurrencyType.TOKENS;
        if (player.getRank() >= Rankup.getMaxRankup()) {
            p.sendMessage(StringUtil.color("&cYou've already maxed out your rank."));
            return true;
        }
        BigInteger cost = BigInteger.ZERO;
        int rank = player.getRank();
        for (int i = player.getRank()+1;i<Rankup.getMaxRankup();i++) {
            cost = cost.add(Rankup.getLevelCost(i));
            if (player.getTokens().compareTo(cost) < 0) {
                cost = cost.subtract(Rankup.getLevelCost(i));
                break;
            }
            rank++;
        }
        player.setRank(rank);
        player.takeTokens(cost,false);
        p.sendMessage(
                StringUtil.color("&b&lYou have ranked up to &f&l"+rank+"&b&l!")
        );
        if (rank < Rankup.getMaxRankup()) {
            p.sendMessage(
                    StringUtil.color(" &3&l- &bNext Rankup: &f"+(rank+1)),
                    StringUtil.color(" &3&l- &bCost: "+type.getColorCode()+type.getUnicode()+"&f"+ Numbers.formattedNumber(Rankup.getLevelCost((rank+1))))
            );
            return true;
        }
        p.sendMessage(
                StringUtil.color("&aYou have maxed out your rank, congratulation!")
        );


        return true;
    }
}
