package io.github.lunaiskey.lunixprison.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.items.lunixitems.Voucher;
import io.github.lunaiskey.lunixprison.player.Currency;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import io.github.lunaiskey.lunixprison.player.PlayerManager;
import io.github.lunaiskey.lunixprison.player.LunixPlayer;
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
                sender.sendMessage(StringUtil.color(color()+"You have "+unicode + "&f" + Numbers.formattedNumber(currency) + color()+" "+name()+"."));
            }
            return true;
        }
        Player otherPlayer = Bukkit.getPlayer(args[0]);
        if (args[0].equalsIgnoreCase("give")) {
            if (sender.hasPermission(perm()+".give")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color(color()+"/"+label+" give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    sender.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(BigInteger.ZERO) < 0) {
                        sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    Currency.giveCurrency(givePlayer.getUniqueId(), type,amount);
                    sender.sendMessage(StringUtil.color(color()+"Given "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" to &f"+givePlayer.getName()+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've been given "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+"."));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            } else {
                sender.sendMessage(Messages.NO_PERMISSION.getText());
            }
        }
        if (args[0].equalsIgnoreCase("take")) {
            if (sender.hasPermission(perm()+".take")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color(color()+"/"+label+" take <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    sender.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(BigInteger.ZERO) < 0) {
                        sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    Currency.takeCurrency(givePlayer.getUniqueId(), type,amount);
                    sender.sendMessage(StringUtil.color(color()+"Taken "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" to &f"+givePlayer.getName()+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've had "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" taken from you."));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            } else {
                sender.sendMessage(Messages.NO_PERMISSION.getText());
            }
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (sender.hasPermission(perm()+".set")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color(color()+"/"+label+" give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    sender.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(BigInteger.ZERO) < 0) {
                        sender.sendMessage(StringUtil.color("&cAmount has to be positive."));
                        return true;
                    }
                    Currency.setCurrency(givePlayer.getUniqueId(), type,amount);
                    sender.sendMessage(StringUtil.color(color()+"Set &f"+givePlayer.getName()+"'s"+color()+" "+name()+" to &f"+Numbers.formattedNumber(amount)+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've had your "+name()+" set to &f"+Numbers.formattedNumber(amount)+color()+"."));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            } else {
                sender.sendMessage(Messages.NO_PERMISSION.getText());
            }
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("pay")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color(color()+"/"+label+" pay <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    p.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                if (givePlayer == p) {
                    p.sendMessage(StringUtil.color("&cYou can't pay yourself."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    BigInteger playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getTokens();
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(playerBalance) > 0) {
                        p.sendMessage(StringUtil.color("&cInsufficient "+name()+"."));
                        return true;
                    }
                    if (amount.compareTo(BigInteger.ZERO) <= 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    Currency.giveCurrency(givePlayer.getUniqueId(), type,amount);
                    Currency.takeCurrency(p.getUniqueId(), type,amount);
                    p.sendMessage(StringUtil.color(color()+"Paid "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" to &f"+givePlayer.getName()+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've been paid "+unicode+"&f"+Numbers.formattedNumber(amount)+" "+color()+name()+" by &f"+p.getName()+color()+"."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color(color()+"/"+label+" withdraw <amount>"));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[1]);
                    BigInteger playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getCurrency(type);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }

                    if (amount.compareTo(playerBalance) > 0 ) {
                        p.sendMessage(StringUtil.color("&cInsufficient "+name()+"."));
                        return true;
                    }
                    if (amount.compareTo(BigInteger.ZERO) <= 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    if (!p.getInventory().addItem(new Voucher(type,amount).getItemStack()).isEmpty()) {
                        p.sendMessage(StringUtil.color("&cNot enough inventory space."));
                        return true;
                    }
                    Currency.takeCurrency(p.getUniqueId(), type,amount);
                    p.sendMessage(StringUtil.color(color()+"Withdrawn "+unicode+"&f"+Numbers.formattedNumber(amount)+" "+color()+name()+"."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (otherPlayer != null) {
                LunixPlayer otherLunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                p.sendMessage(StringUtil.color(color()+otherPlayer.getName()+" has "+unicode+"&f"+Numbers.formattedNumber(otherLunixPlayer.getCurrency(type))+" "+color()+name()+"."));
            } else {
                p.sendMessage(StringUtil.color("&c"+args[0]+" isn't online."));
            }
        } else {
            sender.sendMessage(StringUtil.color("&cYou have to be a player to run this command."));
        }
        return true;
    }

    private String perm() {
        return switch (type) {
            case TOKENS -> "lunix.tokens";
            case GEMS -> "lunix.gems";
            case LUNIX_POINTS -> "lunix.points";
        };
    }

    private String color() {
        return switch (type) {
            case TOKENS -> "&e";
            case GEMS -> "&a";
            case LUNIX_POINTS -> "&d";
        };
    }

    private String name() {
        return switch (type) {
            case TOKENS -> "Tokens";
            case GEMS -> "Gems";
            case LUNIX_POINTS -> "Lunix Points";
        };
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
