package io.github.lunaiskey.lunixprison.items.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.items.ItemID;
import io.github.lunaiskey.lunixprison.items.LunixItem;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CommandLItem implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("lunix.litem")) {
            if (args.length == 0) {
                sender.sendMessage(
                        StringUtil.color("&b&lPItem:"),
                        StringUtil.color("&b&l| &f/litem <id> [amount]"),
                        StringUtil.color("&b&l| &f/litem give <player> <id> [amount]")
                );
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color("Usage: /litem give <player> <id> [amount]"));
                    return true;
                }
                try {
                    Player otherPlayer = Bukkit.getPlayer(args[1]);
                    if (otherPlayer != null) {
                        ItemID itemID = ItemID.valueOf(args[2].toUpperCase());
                        Map<ItemID, LunixItem> itemMap = LunixPrison.getPlugin().getItemManager().getItemMap();
                        if (itemMap.containsKey(itemID)) {
                            int amount = 1;
                            if (args.length == 3) {
                                amount = giveItem(otherPlayer,itemID,amount);
                                sender.sendMessage(StringUtil.color("&aSuccessfully gave " + otherPlayer.getName() + " &f" + amount + " &aof&f " + itemID.name() + "&a."));
                                return true;
                            }
                            try {
                                amount = Integer.parseInt(args[3]);
                                if (amount <= 0) {
                                    sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                                } else {
                                    amount = giveItem(otherPlayer,itemID,amount);
                                    sender.sendMessage(StringUtil.color("&aSuccessfully gave " + otherPlayer.getName() + " &f" + amount + " &aof&f " + itemID.name() + "&a."));
                                }
                            } catch (NumberFormatException e) {
                                sender.sendMessage(StringUtil.color("&cInvalid Amount."));
                            }
                            return true;
                        } else {
                            sender.sendMessage(StringUtil.color("&cItemID \""+itemID.name()+"\" doesn't have an item assigned to it."));
                        }
                    } else {
                        sender.sendMessage(StringUtil.color("&cPlayer "+args[1]+" is offline."));
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(StringUtil.color("&cInvalid ItemID."));
                }
                return true;
            }
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            try {
                ItemID itemID = ItemID.valueOf(args[0].toUpperCase());
                Map<ItemID, LunixItem> itemMap = LunixPrison.getPlugin().getItemManager().getItemMap();
                if (itemMap.containsKey(itemID)) {
                    int amount = 1;
                    if (args.length == 1) {
                        amount = giveItem(p,itemID,amount);
                        p.sendMessage(StringUtil.color("&aSuccessfully gave " + p.getName() + " &f" + amount + " &aof&f " + itemID.name() + "&a."));
                        return true;
                    } else {
                        try {
                            amount = Integer.parseInt(args[1]);
                            if (amount <= 0) {
                                p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                                return true;
                            } else {
                                amount = giveItem(p,itemID,amount);
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(StringUtil.color("&cInvalid Amount."));
                            return true;
                        }
                    }
                    p.sendMessage(StringUtil.color("&aSuccessfully gave " + p.getName() + " &f" + amount + " &aof&f " + itemID.name() + "&a."));
                } else {
                    p.sendMessage(StringUtil.color("&cItemID \""+itemID.name()+"\" doesn't have an item assigned to it."));
                }
                return true;
            } catch (IllegalArgumentException ignored) {
                p.sendMessage(StringUtil.color("&cInvalid ItemID."));
                return true;
            }
        } else {
            sender.sendMessage(StringUtil.color("&cInvalid Arguments."));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    private int giveItem(Player p, ItemID itemID, int amount) {
        ItemStack item = LunixPrison.getPlugin().getItemManager().getItemMap().get(itemID).getItemStack();
        int counter = amount;
        while (counter > 0) {
            if (counter > 64) {
                item.setAmount(64);
                Map<Integer, ItemStack> leftOver = p.getInventory().addItem(item);
                if (leftOver.isEmpty()) {
                    counter -= 64;
                } else {
                    counter -= (64 - leftOver.get(0).getAmount());
                    break;
                }
            } else {
                item.setAmount(counter);
                Map<Integer, ItemStack> leftOver = p.getInventory().addItem(item);
                if (leftOver.isEmpty()) {
                    counter = 0;
                } else {
                    counter = leftOver.get(0).getAmount();
                    break;
                }
            }
        }
        amount -= counter;
        return amount;
    }
}
