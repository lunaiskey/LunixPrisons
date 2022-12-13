package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.player.inventories.ViewPlayerGUI;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandViewPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(StringUtil.color("&cUsage: /viewplayer <player>"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                Player toView = Bukkit.getPlayer(args[0]);
                p.openInventory(new ViewPlayerGUI(toView).getInv());
                return true;
            } else {
                p.sendMessage(StringUtil.color("&cPlayer '"+args+"' is offline."));
            }
        }
        return true;
    }
}
