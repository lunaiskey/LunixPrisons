package io.github.lunaiskey.lunixprison.rankup.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.rankup.Rankup;
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
        if (sender instanceof Player) {
            Player p = (Player) sender;
            LunixPlayer player = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            Rankup rankup = new Rankup();
            CurrencyType type = CurrencyType.TOKENS;
            if (args.length == 0) {
                BigInteger cost = Rankup.getLevelCost(player.getRank()+1);
                if (player.getRank() < Rankup.getMaxRankup()) {
                    if (player.getTokens().compareTo(cost) >= 0) {
                        int newLevel = rankup.rankup(p);
                        int nextLevel = newLevel+1;
                        player.takeTokens(cost);
                        p.sendMessage(
                                StringUtil.color("&b&lYou have ranked up to &f&l"+newLevel+"&b&l!"),
                                StringUtil.color(" &3&l- &bNext Rankup: &f"+nextLevel),
                                StringUtil.color(" &3&l- &bCost: "+type.getColorCode()+type.getUnicode()+"&f"+ Numbers.formattedNumber(Rankup.getLevelCost(nextLevel)))
                        );
                    } else {
                        p.sendMessage(StringUtil.color("&7You still need "+Numbers.formattedNumber(cost.subtract(player.getTokens()))+" to rankup."));
                    }
                } else {
                    p.sendMessage(StringUtil.color("&cYou've already maxed your rank."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("max")) {
                if (player.getRank() < Rankup.getMaxRankup()) {
                    BigInteger cost = BigInteger.ZERO;
                    int rank = 0;
                    for (int i = player.getRank()+1;i<Rankup.getMaxRankup();i++) {
                        cost = cost.add(Rankup.getLevelCost(i));
                        if (player.getTokens().compareTo(cost) >= 0) {
                            rank++;
                        } else {
                            cost = cost.subtract(Rankup.getLevelCost(i));
                            break;
                        }
                    }
                    player.setRank(player.getRank()+rank);
                    player.takeTokens(cost);
                    if (player.getRank() < Rankup.getMaxRankup()) {
                        p.sendMessage(
                                StringUtil.color("&b&lYou have ranked up to &f&l"+player.getRank()+"&b&l!"),
                                StringUtil.color(" &3&l- &bNext Rankup: &f"+(player.getRank()+1)),
                                StringUtil.color(" &3&l- &bCost: "+type.getColorCode()+type.getUnicode()+"&f"+ Numbers.formattedNumber(Rankup.getLevelCost(player.getRank()+1)))
                        );
                    } else {
                        p.sendMessage(StringUtil.color("&aYou've have maxed out your rank! Congratulations."));
                    }
                } else {
                    p.sendMessage(StringUtil.color("&cYou've already maxed out your rank."));
                }
            }
        }
        return true;
    }
}
