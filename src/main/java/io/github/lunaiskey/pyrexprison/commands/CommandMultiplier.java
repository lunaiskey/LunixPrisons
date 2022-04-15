package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.Rankup;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandMultiplier implements CommandExecutor {

    PyrexPrison plugin;

    public CommandMultiplier(PyrexPrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PyrexPlayer pyrexPlayer = plugin.getPlayerManager().getPlayerMap().get(p.getUniqueId());
            double baseMulti = pyrexPlayer.getBaseMultiplier();
            double rankMultiplier = pyrexPlayer.getRankMultiplier();
            p.sendMessage(
                    StringUtil.color("&f&lMultipliers:"),
                    " ",
                    StringUtil.color("&7- &eBase:&f "+(1+baseMulti)),
                    StringUtil.color("&7- &bRank:&f "+rankMultiplier),
                    StringUtil.color("&7- &cArmor:&f "+"N/A"),
                    StringUtil.color("&7- &dBooster:&f "+"N/A"),
                    StringUtil.color("&7- &fTotal:&f "+(baseMulti+rankMultiplier+1))
            );
        } else {
            sender.sendMessage("You need to be a player to run this command.");
        }
        return true;
    }
}