package io.github.lunaiskey.lunixprison.modules.player.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.items.items.Voucher;
import io.github.lunaiskey.lunixprison.modules.player.Currency;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.Numbers;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CommandCurrency implements CommandExecutor, TabCompleter {

    private LunixPrison plugin;
    private PlayerManager playerManager;
    private final CurrencyType type;
    private final String unicode;

    public CommandCurrency(LunixPrison plugin, CurrencyType type) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.type = type;
        this.unicode = type.getUnicode();
    }

    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                BigInteger currency = playerManager.getPlayerMap().get(p.getUniqueId()).getCurrency(type);
                sender.sendMessage(StringUtil.color(type.getColorCode()+"You have "+unicode + "&f" + Numbers.formattedNumber(currency) + type.getColorCode()+" "+name()+"."));
            }
            return true;
        }
        Player otherPlayer = Bukkit.getPlayer(args[0]);
        switch (args[0].toLowerCase()) {
            case "give" -> {giveCommand(sender,label,args); return true;}
            case "take" -> {takeCommand(sender,label, args); return true;}
            case "set" -> {setCommand(sender,label, args); return true;}
            case "pay" -> {payCommand(sender,label, args); return true;}
            case "withdraw" -> {withdrawCommand(sender,label, args); return true;}
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (otherPlayer != null) {
                LunixPlayer otherLunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                p.sendMessage(StringUtil.color(type.getColorCode()+otherPlayer.getName()+" has "+unicode+"&f"+Numbers.formattedNumber(otherLunixPlayer.getCurrency(type))+" "+type.getColorCode()+name()+"."));
            } else {
                p.sendMessage(StringUtil.color("&c"+args[0]+" isn't online."));
            }
        } else {
            sender.sendMessage(StringUtil.color("&cYou have to be a player to run this command."));
        }
        return true;
    }

    private void giveCommand(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(perm()+".give")) {
            sender.sendMessage(Messages.NO_PERMISSION.getText());
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(StringUtil.color(type.getColorCode()+"/"+label+" give <player> <amount>"));
            return;
        }
        Player givePlayer = Bukkit.getPlayer(args[1]);
        if (givePlayer == null) {
            sender.sendMessage(StringUtil.color("&cPlayer is not online."));
            return;
        }
        if (args.length == 2) {
            sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
            return;
        }
        BigInteger amount;
        try {
            amount = new BigInteger(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
            return;
        }
        if (type != CurrencyType.TOKENS) {
            amount = BigInteger.valueOf(amount.longValue());
        }
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
            return;
        }
        Currency.giveCurrency(givePlayer.getUniqueId(), type,amount);
        sender.sendMessage(StringUtil.color(type.getColorCode()+"Given "+unicode+"&f"+Numbers.formattedNumber(amount)+type.getColorCode()+" "+name()+" to &f"+givePlayer.getName()+type.getColorCode()+"."));
        givePlayer.sendMessage(StringUtil.color(type.getColorCode()+"You've been given "+unicode+"&f"+Numbers.formattedNumber(amount)+type.getColorCode()+" "+name()+"."));
    }
    private void takeCommand(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(perm()+".take")) {
            sender.sendMessage(Messages.NO_PERMISSION.getText());
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(StringUtil.color(type.getColorCode()+"/"+label+" take <player> <amount>"));
            return;
        }
        Player givePlayer = Bukkit.getPlayer(args[1]);
        if (givePlayer == null) {
            sender.sendMessage(StringUtil.color("&cPlayer is not online."));
            return;
        }
        if (args.length == 2) {
            sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
            return;
        }
        BigInteger amount;
        try {
            amount = new BigInteger(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
            return;
        }
        if (type != CurrencyType.TOKENS) {
            amount = BigInteger.valueOf(amount.longValue());
        }
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
            return;
        }
        Currency.takeCurrency(givePlayer.getUniqueId(), type,amount);
        sender.sendMessage(StringUtil.color(type.getColorCode()+"Taken "+unicode+"&f"+Numbers.formattedNumber(amount)+type.getColorCode()+" "+name()+" to &f"+givePlayer.getName()+type.getColorCode()+"."));
        givePlayer.sendMessage(StringUtil.color(type.getColorCode()+"You've had "+unicode+"&f"+Numbers.formattedNumber(amount)+type.getColorCode()+" "+name()+" taken from you."));
    }
    private void setCommand(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(perm()+".set")) {
            sender.sendMessage(Messages.NO_PERMISSION.getText());
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(StringUtil.color(type.getColorCode()+"/"+label+" give <player> <amount>"));
            return;
        }
        Player givePlayer = Bukkit.getPlayer(args[1]);
        if (givePlayer == null) {
            sender.sendMessage(StringUtil.color("&cPlayer is not online."));
            return;
        }
        if (args.length == 2) {
            sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
            return;
        }
        BigInteger amount;
        try {
            amount = new BigInteger(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
            return;
        }
        if (type != CurrencyType.TOKENS) {
            amount = BigInteger.valueOf(amount.longValue());
        }
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            sender.sendMessage(StringUtil.color("&cAmount has to be positive."));
            return;
        }
        Currency.setCurrency(givePlayer.getUniqueId(), type,amount);
        sender.sendMessage(StringUtil.color(type.getColorCode()+"Set &f"+givePlayer.getName()+"'s"+type.getColorCode()+" "+name()+" to &f"+Numbers.formattedNumber(amount)+type.getColorCode()+"."));
        givePlayer.sendMessage(StringUtil.color(type.getColorCode()+"You've had your "+name()+" set to &f"+Numbers.formattedNumber(amount)+type.getColorCode()+"."));
    }

    private void payCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StringUtil.color("&cYou have to be a player to run this command."));
            return;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            p.sendMessage(StringUtil.color(type.getColorCode()+"/"+label+" pay <player> <amount>"));
            return;
        }
        Player givePlayer = Bukkit.getPlayer(args[1]);
        if (givePlayer == null) {
            p.sendMessage(StringUtil.color("&cPlayer is not online."));
            return;
        }
        if (args.length == 2) {
            p.sendMessage(StringUtil.color("&cAmount can't be empty."));
            return;
        }
        if (givePlayer == p) {
            p.sendMessage(StringUtil.color("&cYou can't pay yourself."));
            return;
        }
        BigInteger amount;
        try {
            amount  = new BigInteger(args[2]);
        } catch (NumberFormatException ex) {
            p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
            return;
        }
        BigInteger playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getTokens();
        if (type != CurrencyType.TOKENS) {
            amount = BigInteger.valueOf(amount.longValue());
        }
        if (amount.compareTo(playerBalance) > 0) {
            p.sendMessage(StringUtil.color("&cInsufficient "+name()+"."));
            return;
        }
        if (amount.compareTo(BigInteger.ZERO) <= 0) {
            p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
            return;
        }
        Currency.giveCurrency(givePlayer.getUniqueId(), type,amount);
        Currency.takeCurrency(p.getUniqueId(), type,amount);
        p.sendMessage(StringUtil.color(type.getColorCode()+"Paid "+unicode+"&f"+Numbers.formattedNumber(amount)+type.getColorCode()+" "+name()+" to &f"+givePlayer.getName()+type.getColorCode()+"."));
        givePlayer.sendMessage(StringUtil.color(type.getColorCode()+"You've been paid "+unicode+"&f"+Numbers.formattedNumber(amount)+" "+type.getColorCode()+name()+" by &f"+p.getName()+type.getColorCode()+"."));
    }

    private void withdrawCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StringUtil.color("&cYou have to be a player to run this command."));
            return;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            p.sendMessage(StringUtil.color(type.getColorCode()+"/"+label+" withdraw <amount>"));
            return;
        }
        BigInteger amount;
        try {
            amount = new BigInteger(args[1]);
        } catch (NumberFormatException ex) {
            p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
            return;
        }
        BigInteger playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getCurrency(type);
        if (type != CurrencyType.TOKENS) {
            amount = BigInteger.valueOf(amount.longValue());
        }
        if (amount.compareTo(playerBalance) > 0 ) {
            p.sendMessage(StringUtil.color("&cInsufficient "+name()+"."));
            return;
        }
        if (amount.compareTo(BigInteger.ZERO) <= 0) {
            p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
            return;
        }
        if (!p.getInventory().addItem(new Voucher(type,amount).getItemStack()).isEmpty()) {
            p.sendMessage(StringUtil.color("&cNot enough inventory space."));
            return;
        }
        Currency.takeCurrency(p.getUniqueId(), type,amount);
        p.sendMessage(StringUtil.color(type.getColorCode()+"Withdrawn "+unicode+"&f"+Numbers.formattedNumber(amount)+" "+type.getColorCode()+name()+"."));



    }

    private String perm() {
        return switch (type) {
            case TOKENS -> "lunix.tokens";
            case GEMS -> "lunix.gems";
            case LUNIX_POINTS -> "lunix.points";
        };
    }

    private String name() {
        return type.getName();
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        if (args.length == 1) {
            completion.add("pay");
            completion.add("withdraw");
            if (sender.hasPermission(perm()+".give")) {
                completion.add("give");
            }
            if (sender.hasPermission(perm()+".take")) {
                completion.add("take");
            }
            if (sender.hasPermission(perm()+".set")) {
                completion.add("set");
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                completion.add(player.getName());
            }
        }
        if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("set")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completion.add(player.getName());
            }
        }
        List<String> finalCompletions = new ArrayList<>();
        org.bukkit.util.StringUtil.copyPartialMatches(args[args.length-1],completion,finalCompletions);
        return finalCompletions;
    }
}
