package io.github.lunaiskey.lunixprison.modules.shop.commands;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.shop.Shop;
import io.github.lunaiskey.lunixprison.modules.shop.ShopManager;
import io.github.lunaiskey.lunixprison.modules.shop.inventories.ShopGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ListIterator;

public class CommandShop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }
        switch(args[0].toLowerCase()) {
            case "open" -> openSubCommand(player,label,args);
            case "list" -> listSubCommand(player,label,args);
            default -> sendHelpMessage(player);
        }
        //player.openInventory(new ShopGUI(LunixPrison.getPlugin().getShopManager().getShop("scientist")).getInv(player));
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(
            ChatColor.GREEN+""+ChatColor.BOLD+"Shop:",
            ChatColor.GREEN+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/shop open <ShopID>",
            ChatColor.GREEN+""+ChatColor.BOLD+"| "+ChatColor.WHITE+"/shop list"
        );
    }

    private void openSubCommand(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            player.sendMessage(ChatColor.RED+"Usage: /shop open <ShopID>");
            return;
        }
        Shop shop = ShopManager.get().getShop(args[1]);
        if (shop == null) {
            player.sendMessage(ChatColor.RED+"Invalid ShopID.");
            return;
        }
        player.openInventory(new ShopGUI(shop).getInv(player));
        player.sendMessage("Opening shop "+shop.getShopID()+".");
    }

    private void listSubCommand(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        StringBuilder stringBuilder = new StringBuilder(ChatColor.GREEN+"Available Shops: ");
        List<String> shopIDList = ShopManager.get().getAlphabeticallySortedShopIDs();
        for (ListIterator<String> i = shopIDList.listIterator(); i.hasNext();) {
            stringBuilder.append(ChatColor.WHITE).append(i.next()).append(ChatColor.GREEN).append(!i.hasNext() ? ".":",");
        }
        player.sendMessage(stringBuilder.toString());
    }
}
