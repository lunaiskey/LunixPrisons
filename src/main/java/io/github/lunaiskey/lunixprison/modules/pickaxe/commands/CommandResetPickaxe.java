package io.github.lunaiskey.lunixprison.modules.pickaxe.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixPickaxe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandResetPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            lunixPlayer.setPickaxe(new LunixPickaxe(p.getUniqueId()));
            p.sendMessage("Reset your pickaxe.");
            LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
        }
        return true;
    }
}
