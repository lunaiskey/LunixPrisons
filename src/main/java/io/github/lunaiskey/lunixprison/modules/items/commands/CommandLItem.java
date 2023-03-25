package io.github.lunaiskey.lunixprison.modules.items.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.modules.items.ItemID;
import io.github.lunaiskey.lunixprison.modules.items.LunixItem;
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
        if (!sender.hasPermission("lunix.admin.litem")) {
            sender.sendMessage(Messages.NO_PERMISSION.getText());
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(StringUtil.color(
                    "&b&lLItem:",
                    "&b&l| &f/litem <id> [amount]",
                    "&b&l| &f/litem give <player> <id> [amount]"
            ));
            return true;
        }
        String arg0 = args[0].toLowerCase();
        switch (arg0) {
            case "give" -> {giveCommand(sender, label, args,true);return true;}
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        giveCommand(sender,label,args,false);
        return true;
    }

    private void giveCommand(CommandSender sender, String label, String[] args, boolean isGiveSubCommand) {
        int argOffSet = isGiveSubCommand ? 2 : 0;
        if (isGiveSubCommand && args.length == 1) {
            sender.sendMessage(StringUtil.color("&cUsage: /litem give <player> <id> [amount]"));
            return;
        }
        Player toGive = isGiveSubCommand ? Bukkit.getPlayer(args[1]) : (Player) sender;
        if (toGive == null) {
            sender.sendMessage(StringUtil.color("&cPlayer "+args[1]+" is offline."));
        }
        ItemID itemID;
        try {
            itemID = ItemID.valueOf(args[argOffSet]);
        } catch (IllegalArgumentException ignored) {
            sender.sendMessage(StringUtil.color("&cInvalid ItemID."));
            return;
        }
        LunixItem lunixItem = LunixPrison.getPlugin().getItemManager().getLunixItem(itemID);
        if (lunixItem == null) {
            sender.sendMessage(StringUtil.color("&cItemID \""+itemID.name()+"\" doesn't have an item assigned to it."));
            return;
        }
        int amount = 1;
        try {
            amount = Integer.parseInt(args[1+argOffSet]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage(StringUtil.color("&cInvalid Amount."));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
            return;
        }
        giveItem(sender,toGive,lunixItem,amount);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    private void giveItem(CommandSender giver, Player toGive, LunixItem lunixItem, int amount) {
        ItemStack item = lunixItem.getItemStack();
        int counter = amount;
        while (counter > 0) {
            if (counter > 64) {
                item.setAmount(64);
                Map<Integer, ItemStack> leftOver = toGive.getInventory().addItem(item);
                if (leftOver.isEmpty()) {
                    counter -= 64;
                } else {
                    counter -= (64 - leftOver.get(0).getAmount());
                    break;
                }
            } else {
                item.setAmount(counter);
                Map<Integer, ItemStack> leftOver = toGive.getInventory().addItem(item);
                if (leftOver.isEmpty()) {
                    counter = 0;
                } else {
                    counter = leftOver.get(0).getAmount();
                    break;
                }
            }
        }
        amount -= counter;
        //Successfully gave TOGIVE ITEMID xAMOUNT.
        if (giver instanceof Player) {
            Player giverPlayer = (Player) giver;
            if (giverPlayer.getUniqueId().equals(toGive.getUniqueId())) {
                giver.sendMessage(StringUtil.color("&aSuccessfully given yourself "+lunixItem.getDisplayName()+" x"+amount+"."));
                return;
            }
        }
        //You've been given ITEMID xAMOUNT by GIVER.
        giver.sendMessage(StringUtil.color("&aSuccessfully given "+toGive.getName()+" "+lunixItem.getDisplayName()+" x"+amount+"."));
        toGive.sendMessage(StringUtil.color("&aYou've been given "+lunixItem.getDisplayName()+" x"+amount+" by "+giver.getName()+"."));
    }
}
