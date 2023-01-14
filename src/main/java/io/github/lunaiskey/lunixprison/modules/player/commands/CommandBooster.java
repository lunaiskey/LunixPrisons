package io.github.lunaiskey.lunixprison.modules.player.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.items.lunixitems.BoosterItem;
import io.github.lunaiskey.lunixprison.modules.boosters.BoosterType;
import io.github.lunaiskey.lunixprison.modules.player.inventories.PersonalBoosterGUI;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandBooster implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.openInventory(new PersonalBoosterGUI(p).getInv());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("lunix.booster.give")) {
                sender.sendMessage(Messages.NO_PERMISSION.getText());
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage(StringUtil.color("&cUsage: /booster give <player> <type> <multiplier> <length>"));
                return true;
            }
            Player otherPlayer = Bukkit.getPlayer(args[1]);
            if (otherPlayer == null) {
                sender.sendMessage(StringUtil.color("Player \""+args[1]+"\" isn't online."));
                return true;
            }
            if (!(args.length >= 5)) {
                sender.sendMessage("&cNot Enough Arguments.");
                return true;
            }
            BoosterType boosterType;
            try {
                boosterType = BoosterType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException ignored){
                sender.sendMessage(StringUtil.color("&cInvalid Booster Type."));
                return true;
            }
            double multiplier;
            try {
                multiplier = Double.parseDouble(args[3]);
            } catch (NumberFormatException ignored) {
                sender.sendMessage(StringUtil.color("&cMultiplier has to be a valid number."));
                return true;
            }
            if (multiplier < 1) {
                sender.sendMessage(StringUtil.color("&cMultiplier has to be 1 or higher."));
                return true;
            }
            int length;
            try {
                length = Integer.parseInt(args[4]);
            } catch (NumberFormatException ignored) {
                sender.sendMessage(StringUtil.color("Length has to be a valid number."));
                return true;
            }
            if (length < 0) {
                sender.sendMessage(StringUtil.color("&cLength has to be positive."));
                return true;
            }
            otherPlayer.getInventory().addItem(new BoosterItem(boosterType,length,multiplier).getItemStack());
        }
        return true;
    }
}
