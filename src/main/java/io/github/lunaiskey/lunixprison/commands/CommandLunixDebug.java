package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandLunixDebug implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("lunix.debug")) {
            player.sendMessage(Messages.NO_PERMISSION.getText());
            return true;
        }
        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }
        ItemManager itemManager = LunixPrison.getPlugin().getItemManager();
        switch (args[0].toLowerCase()) {
            case "updatemainhand" -> {
                itemManager.updateEquipmentSlot(player, EquipmentSlot.HAND);
                player.sendMessage("Attempted to update Main Hand.");
            }
            case "updateoffhand" -> {
                itemManager.updateEquipmentSlot(player,EquipmentSlot.OFF_HAND);
                player.sendMessage("Attempted to update Off Hand.");
            }
            case "updateinventory" -> {
                itemManager.updateInventory(player);
                player.sendMessage("Attempted to update Inventory.");
            }
            default -> sendHelpMessage(player);
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(
                "DEBUG:",
                "| /lunixdebug updatemainhand",
                "| /lunixdebug updateoffhand",
                "| /lunixdebug updateinventory"
        );
    }


}
