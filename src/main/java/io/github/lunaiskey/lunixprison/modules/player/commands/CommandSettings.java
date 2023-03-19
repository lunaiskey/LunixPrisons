package io.github.lunaiskey.lunixprison.modules.player.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandSettings implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StringUtil.color(Messages.PLAYER_ONLY.getText()));
            return true;
        }
        Player player = (Player) sender;
        player.sendMessage(StringUtil.color("&cCommand Currently Unavailable."));
        return true;
    }
}
