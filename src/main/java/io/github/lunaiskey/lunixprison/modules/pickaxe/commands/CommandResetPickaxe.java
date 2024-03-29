package io.github.lunaiskey.lunixprison.modules.pickaxe.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeManager;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeStorage;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandResetPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
        lunixPlayer.setPickaxeStorage(new PickaxeStorage(p.getUniqueId()));
        p.sendMessage(ChatColor.GREEN+"Your pickaxe has been reset to its default state!");
        PickaxeManager.get().updateInventoryPickaxe(p);
        return true;
    }
}
