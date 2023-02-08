package io.github.lunaiskey.lunixprison.modules.pickaxe.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.LunixPickaxe;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CommandEnchant implements CommandExecutor {

    public String[] getHelpMessage() {
        return StringUtil.color(
                "&bEnchant Commands:",
                "&b|&f /enchant set <player> <id> <level> ",
                "&b|&f /enchant set <id> <level>"
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (!(p.hasPermission("lunix.enchant"))) {
            p.sendMessage(Messages.NO_PERMISSION.getText());
        }
        if (args.length == 0) {
            p.sendMessage(getHelpMessage());
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "set" -> setCommand(p, label, args);
        }
        return true;
    }

    public void setCommand(CommandSender sender,String label,String[] args) {
        Player p = (Player) sender;
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED+"Not Enough Arguments.");
            return;
        }
        String toEnchantStr = args.length == 3 ? null : args[1];
        String enchantTypeStr = args.length == 3 ? args[1] : args[2];
        String levelStr = args.length == 3 ? args[2] : args[3];
        Player toEnchant = toEnchantStr == null ? p : Bukkit.getPlayer(toEnchantStr);
        if (toEnchant == null) {
            p.sendMessage(ChatColor.RED+"Player \""+toEnchantStr+"\" has to be online to do this.");
            return;
        }
        EnchantType enchantType;
        try {
            enchantType = EnchantType.valueOf(enchantTypeStr.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            p.sendMessage(ChatColor.RED + "Invalid Enchant type.");
            return;
        }
        LunixEnchant enchant = LunixPrison.getPlugin().getPickaxeHandler().getEnchantments().get(enchantType);
        int level;
        try {
            level = Integer.parseInt(levelStr);
        } catch (NumberFormatException ignored) {
            p.sendMessage(ChatColor.RED + "Invalid Enchant level.");
            return;
        }
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(toEnchant.getUniqueId());
        LunixPickaxe pickaxe = lunixPlayer.getPickaxe();
        if (level > 0) {
            pickaxe.getEnchants().put(enchantType,level);
            p.sendMessage(ChatColor.GREEN+"Set "+enchant.getName()+" to level "+level+" on pickaxe.");
        } else {
            pickaxe.getEnchants().remove(enchantType);
            p.sendMessage(ChatColor.GREEN+"Removed "+enchant.getName()+" from pickaxe.");
        }
        LunixPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(toEnchant);
    }
}
