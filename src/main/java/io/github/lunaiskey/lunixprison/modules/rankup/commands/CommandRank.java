package io.github.lunaiskey.lunixprison.modules.rankup.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandRank implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        if (args.length == 0) {
            p.sendMessage(StringUtil.color("&bYour rank is: &f"+ lunixPlayer.getRank()));
            return true;
        }
        String arg0 = args[0].toLowerCase();
        switch (arg0) {
            case "set" -> {setCommand(sender, label, args);return true;}
        }
        whenPlayerCommand(sender, label, args);
        return true;
    }

    private void setCommand(CommandSender sender, String label, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("lunix.rank.set")) {
            p.sendMessage(Messages.NO_PERMISSION.getText());
            return;
        }
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED+"Usage: /rank set <player> <value>");
            return;
        }
        Player otherPlayer = Bukkit.getPlayer(args[1]);
        if (otherPlayer == null) {
            p.sendMessage(ChatColor.RED+"Player \"playername\" has to be online to do this!");
            return;
        }
        int rank;
        try {
            rank = Integer.parseInt(args[2]);
        } catch (Exception ignored) {
            p.sendMessage(ChatColor.RED+"&cInvalid number!");
            return;
        }
        if (rank < 0) {
            p.sendMessage(ChatColor.RED+"&cValue cannot be smaller then 0!");
            return;
        }
        LunixPlayer otherLunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
        otherLunixPlayer.setRank(rank);
        p.sendMessage(ChatColor.GREEN+"Successfully set "+otherPlayer.getName()+"'s rank to "+rank+"!");
    }

    private void whenPlayerCommand(CommandSender sender, String label, String[] args) {
        Player p = (Player) sender;
        String arg0 = args[0];
        Player otherPlayer = Bukkit.getPlayer(arg0);
        if (otherPlayer == null) {
            p.sendMessage(StringUtil.color("&cPlayer "+args[0]+" isn't online."));
        }
        LunixPlayer otherLunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
        p.sendMessage(StringUtil.color("&b"+otherPlayer.getName()+"'s rank is: &f"+ otherLunixPlayer.getRank()));
    }
}
