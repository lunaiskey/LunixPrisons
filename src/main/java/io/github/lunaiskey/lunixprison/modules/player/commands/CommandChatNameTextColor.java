package io.github.lunaiskey.lunixprison.modules.player.commands;

import io.github.lunaiskey.lunixprison.Messages;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.modules.items.items.ChatColorVoucher;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.modules.player.inventories.ChatNameTextColorGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandChatNameTextColor implements CommandExecutor {

    private ChatColorSelectType type;

    public CommandChatNameTextColor(ChatColorSelectType type) {
        this.type = type;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY.getText());
            return true;
        }
        Player player = (Player) sender;
        if (type == null) {
            return true;
        }
        if (args.length == 0) {
            player.openInventory(new ChatNameTextColorGUI(type).getInv(player));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "voucher" -> {
                if (!player.hasPermission(type.getVoucherGetPerm())) {
                    player.sendMessage(Messages.NO_PERMISSION.getText());
                    return true;
                }
                String hasFormat = type == ChatColorSelectType.NAME ? "/Format" : "";
                if (args.length == 2) {
                    player.sendMessage(ChatColor.RED+"Usage: /"+label+" voucher <Color"+hasFormat+">");
                    return true;
                }
                LunixChatColor color;
                try {
                    color = LunixChatColor.valueOf(args[2].toUpperCase());
                } catch (Exception ignored) {
                    player.sendMessage(ChatColor.RED+"Invalid Color");
                    return true;
                }
                if (type == ChatColorSelectType.TEXT && color.isFormat()) {
                    player.sendMessage(ChatColor.RED+"Chat Text doesn't support format codes currently, try a color instead.");
                    return true;
                }
                player.getInventory().addItem(new ChatColorVoucher(color,type).getItemStack());
                return true;
            }
        }
        player.openInventory(new ChatNameTextColorGUI(type).getInv(player));
        return true;
    }
}
