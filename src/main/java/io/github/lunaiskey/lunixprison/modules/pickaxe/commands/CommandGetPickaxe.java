package io.github.lunaiskey.lunixprison.modules.pickaxe.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandGetPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (LunixPrison.getPlugin().getPickaxeHandler().hasOrGivePickaxe(p)) {
            p.sendMessage(StringUtil.color("&cYou already have a pickaxe."));
            return true;
        }
        p.sendMessage(ChatColor.GREEN+"You're pickaxe has been returned to you, try not to loose it again :P");
        return true;
    }
}
