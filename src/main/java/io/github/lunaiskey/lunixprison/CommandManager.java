package io.github.lunaiskey.lunixprison;

import io.github.lunaiskey.lunixprison.commands.*;
import io.github.lunaiskey.lunixprison.player.CurrencyType;
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

        registerCommand("litem",new CommandPItem());

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