package io.github.lunaiskey.lunixprison.modules.items.commands;

import io.github.lunaiskey.lunixprison.modules.player.inventories.GemStoneGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandGemstones implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(new GemStoneGUI().getInv(p));
        } else {
            sender.sendMessage("This is a player only command.");
        }
        return true;
    }
}
