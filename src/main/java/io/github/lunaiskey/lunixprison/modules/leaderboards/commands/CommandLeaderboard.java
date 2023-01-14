package io.github.lunaiskey.lunixprison.modules.leaderboards.commands;

import io.github.lunaiskey.lunixprison.modules.leaderboards.LeaderboardGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandLeaderboard implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(new LeaderboardGUI().getInv());
        }
        return true;
    }
}
