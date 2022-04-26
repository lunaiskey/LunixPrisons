package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.player.inventories.ArmorGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandArmor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(new ArmorGUI(p).getInv());
        }
        return true;
    }
}
