package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandRank implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            if (args.length == 0) {
                p.sendMessage(StringUtil.color("&bYour rank is: &f"+ lunixPlayer.getRank()));
                return true;
            }
            Player otherPlayer = Bukkit.getPlayer(args[0]);
            if (args.length == 1) {
                if (otherPlayer != null) {
                    LunixPlayer otherLunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                    p.sendMessage(StringUtil.color("&b"+otherPlayer.getName()+"'s rank is: &f"+ otherLunixPlayer.getRank()));
                } else {
                    p.sendMessage(StringUtil.color("&cPlayer "+args[0]+" isn't online."));
                }
                return true;
            }
            if (otherPlayer != null) {
                if (p.hasPermission("lunix.rank.set")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        if (args.length >= 3) {
                            try {
                                int rank = Integer.parseInt(args[2]);
                                if (rank < 0) {
                                    p.sendMessage(StringUtil.color("&cValue cannot be smaller then 0."));
                                    return true;
                                }
                                LunixPlayer otherLunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                                otherLunixPlayer.setRank(rank);
                                p.sendMessage(StringUtil.color("&aSuccessfully set "+otherPlayer.getName()+"'s rank to "+rank+"."));
                            } catch (Exception ignored) {
                                p.sendMessage(StringUtil.color("&cInvalid number."));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
