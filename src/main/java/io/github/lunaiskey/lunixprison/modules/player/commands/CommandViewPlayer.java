package io.github.lunaiskey.lunixprison.modules.player.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.inventories.ViewPlayerGUI;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandViewPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(StringUtil.color("&cUsage: /viewplayer <player>"));
            return true;
        }
        Player toView = Bukkit.getPlayer(args[0]);
        if (toView == null) {
            p.sendMessage(StringUtil.color("&cPlayer \""+args[0]+"\" is offline."));
            return true;
        }
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(toView.getUniqueId());
        if (lunixPlayer == null) {
            p.sendMessage(ChatColor.RED+"Unable to view "+toView.getName()+".");
            return true;
        }
        p.openInventory(new ViewPlayerGUI(toView).getInv(p));
        return true;
    }
}
