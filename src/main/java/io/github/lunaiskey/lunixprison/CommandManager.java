package io.github.lunaiskey.lunixprison;

import io.github.lunaiskey.lunixprison.commands.CommandLunixDebug;
import io.github.lunaiskey.lunixprison.modules.armor.commands.CommandArmor;
import io.github.lunaiskey.lunixprison.modules.gangs.commands.CommandGang;
import io.github.lunaiskey.lunixprison.modules.items.commands.CommandGemstones;
import io.github.lunaiskey.lunixprison.modules.items.commands.CommandLItem;
import io.github.lunaiskey.lunixprison.modules.leaderboards.commands.CommandLeaderboard;
import io.github.lunaiskey.lunixprison.modules.mines.commands.CommandPMine;
import io.github.lunaiskey.lunixprison.modules.pickaxe.commands.CommandEnchant;
import io.github.lunaiskey.lunixprison.modules.pickaxe.commands.CommandGetPickaxe;
import io.github.lunaiskey.lunixprison.modules.pickaxe.commands.CommandResetPickaxe;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.commands.*;
import io.github.lunaiskey.lunixprison.modules.rankup.commands.CommandRank;
import io.github.lunaiskey.lunixprison.modules.rankup.commands.CommandRankup;
import io.github.lunaiskey.lunixprison.modules.rankup.commands.CommandRankupMax;
import io.github.lunaiskey.lunixprison.modules.shop.commands.CommandShop;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandManager {
    private final LunixPrison plug;

    public CommandManager(LunixPrison plugin) {
        plug = plugin;
        registerCommands();
    }
    private void registerCommands() {
        registerCommand("pmine",new CommandPMine(plug));

        registerCommand("tokens",new CommandCurrency(plug, CurrencyType.TOKENS));
        registerCommand("gems",new CommandCurrency(plug, CurrencyType.GEMS));
        registerCommand("lunixpoints",new CommandCurrency(plug, CurrencyType.LUNIX_POINTS));

        registerCommand("enchants",new CommandEnchant());
        registerCommand("armor",new CommandArmor());
        registerCommand("getpickaxe",new CommandGetPickaxe());
        registerCommand("multiplier",new CommandMultiplier(plug));
        registerCommand("gemstones",new CommandGemstones());
        registerCommand("booster",new CommandBooster());
        registerCommand("rank",new CommandRank());
        registerCommand("rankup",new CommandRankup());
        registerCommand("rankupmax",new CommandRankupMax());

        registerCommand("litem",new CommandLItem());

        registerCommand("viewplayer",new CommandViewPlayer());

        registerCommand("resetpickaxe",new CommandResetPickaxe());
        registerCommand("leaderboard",new CommandLeaderboard());

        registerCommand("gang",new CommandGang(plug));
        registerCommand("cashback",new CommandCashback());
        registerCommand("settings", new CommandSettings());

        registerCommand("namecolor", new CommandChatNameTextColor(ChatColorSelectType.NAME));
        registerCommand("chatcolor", new CommandChatNameTextColor(ChatColorSelectType.TEXT));


        registerCommand("lunixdebug", new CommandLunixDebug());
        registerCommand("shop", new CommandShop());
    }

    private static void registerCommand(String command, CommandExecutor executor) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command);
        if (pluginCommand == null) return;
        if (executor == null) return;
        pluginCommand.setExecutor(executor);
        if (!(executor instanceof TabCompleter)) return;
        pluginCommand.setTabCompleter((TabCompleter) executor);
    }

}
