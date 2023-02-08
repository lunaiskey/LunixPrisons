package io.github.lunaiskey.lunixprison.hooks;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.CurrencyType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.rankup.Rankup;
import io.github.lunaiskey.lunixprison.util.Numbers;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlaceholderHook extends PlaceholderExpansion implements LunixHook {

    private final LunixPrison plugin;

    public PlaceholderHook(LunixPrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginIdentifier() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean registerHook() {
        if (Bukkit.getPluginManager().getPlugin(getPluginIdentifier()) != null) {
            this.register();
            return true;
        }
        plugin.getLogger().warning("PlaceholderAPI hooking failed, perhaps its missing?");
        return false;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lunix";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lunaiskey";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        LunixPlayer lunixPlayer = plugin.getPlayerManager().getPlayerMap().get(player.getUniqueId());
        if (lunixPlayer != null) {
            return switch(params.toLowerCase()) {
                case "tokens" -> Numbers.formattedNumber(lunixPlayer.getTokens());
                case "gems" -> Numbers.formattedNumber(lunixPlayer.getGems());
                case "points" -> Numbers.formattedNumber(lunixPlayer.getLunixPoints());
                case "tokens_raw" -> lunixPlayer.getTokens().toString();
                case "gems_raw" -> String.valueOf(lunixPlayer.getGems());
                case "points_raw" -> String.valueOf(lunixPlayer.getLunixPoints());
                case "tokens_unicode" -> CurrencyType.TOKENS.getUnicode();
                case "gems_unicode" -> CurrencyType.GEMS.getUnicode();
                case "points_unicode" -> CurrencyType.LUNIX_POINTS.getUnicode();
                case "tokens_color" -> CurrencyType.TOKENS.getColorCode()+"";
                case "gems_color" -> CurrencyType.GEMS.getColorCode()+"";
                case "points_color" -> CurrencyType.LUNIX_POINTS.getColorCode()+"";
                case "rank" -> String.valueOf(lunixPlayer.getRank());
                case "rank_next" -> String.valueOf(Math.min(lunixPlayer.getRank() + 1, Rankup.getMaxRankup()));
                case "rank_percentage" -> Numbers.formatDouble(Rankup.getRankUpPercentage(Objects.requireNonNull(player.getPlayer())));
                case "rank_progressbar" -> Rankup.getRankUpProgressBar(Objects.requireNonNull(player.getPlayer()));
                case "gemstone_progress" -> String.valueOf(lunixPlayer.getGemstoneCount());
                case "gemstone_max" -> String.valueOf(LunixPrison.getPlugin().getPlayerManager().getGemstoneCountMax((Player) player));
                default -> null;
            };
        } else {
            return null;
        }
    }
}
