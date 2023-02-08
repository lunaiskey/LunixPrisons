package io.github.lunaiskey.lunixprison.modules.player.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandMultiplier implements CommandExecutor {

    LunixPrison plugin;

    public CommandMultiplier(LunixPrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        Player p = (Player) sender;
        LunixPlayer lunixPlayer = plugin.getPlayerManager().getPlayerMap().get(p.getUniqueId());
        double baseMulti = lunixPlayer.getBaseMultiplier();
        double rankMultiplier = lunixPlayer.getRankMultiplier();
        double armorMultiplier = lunixPlayer.getArmorMultiplier();
        double boosterMultiplier = lunixPlayer.getBoosterMultiplier();
        double totalMultiplier = lunixPlayer.getTotalMultiplier();
        p.sendMessage(
                " ",
                StringUtil.color("&f&lMultipliers:"),
                StringUtil.color("&7- &eBase:&f "+(1+baseMulti)),
                StringUtil.color("&7- &bRank:&f "+ Numbers.formatDouble(rankMultiplier)),
                StringUtil.color("&7- &cArmor:&f "+Numbers.formatDouble(armorMultiplier)),
                StringUtil.color("&7- &dBooster:&f "+ Numbers.formatDouble(boosterMultiplier)),
                StringUtil.color("&7- &fTotal:&f "+Numbers.formatDouble(totalMultiplier+1))
        );
        return true;
    }
}
