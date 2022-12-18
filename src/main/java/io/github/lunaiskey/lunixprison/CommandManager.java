package io.github.lunaiskey.lunixprison;

import io.github.lunaiskey.lunixprison.gangs.commands.CommandGang;
import io.github.lunaiskey.lunixprison.items.commands.CommandGemstones;
import io.github.lunaiskey.lunixprison.items.commands.CommandLItem;
import io.github.lunaiskey.lunixprison.leaderboards.commands.CommandLeaderboard;
import io.github.lunaiskey.lunixprison.mines.commands.CommandPMine;
import io.github.lunaiskey.lunixprison.pickaxe.commands.CommandEnchant;
import io.github.lunaiskey.lunixprison.pickaxe.commands.CommandGetPickaxe;
import io.github.lunaiskey.lunixprison.pickaxe.commands.CommandResetPickaxe;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
import io.github.lunaiskey.lunixprison.player.commands.*;
import io.github.lunaiskey.lunixprison.rankup.commands.CommandRank;
import io.github.lunaiskey.lunixprison.rankup.commands.CommandRankup;
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
        registerCommand("rankup",new CommandRankup());
        registerCommand("armor",new CommandArmor());
        registerCommand("getpickaxe",new CommandGetPickaxe());
        registerCommand("multiplier",new CommandMultiplier(plug));
        registerCommand("gemstones",new CommandGemstones());
        registerCommand("booster",new CommandBooster());

        registerCommand("litem",new CommandLItem());

        registerCommand("viewplayer",new CommandViewPlayer());
        registerCommand("rank",new CommandRank());
        registerCommand("resetpickaxe",new CommandResetPickaxe());
        registerCommand("leaderboard",new CommandLeaderboard());

        registerCommand("gang",new CommandGang(plug));
    }

    private void registerCommand(String command, CommandExecutor executor) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command);
        if (pluginCommand == null) return;
        if (executor == null) return;
        pluginCommand.setExecutor(executor);
        if (executor instanceof TabCompleter) {
            pluginCommand.setTabCompleter((TabCompleter) executor);
        }

    }

}
