package io.github.lunaiskey.lunixprison.modules.leaderboards;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import org.bukkit.Bukkit;

import java.util.*;

public class LeaderboardManager {

    private Map<UUID, LunixPlayer> playerMap = LunixPrison.getPlugin().getPlayerManager().getPlayerMap();
    private LinkedHashMap<UUID,BigIntegerEntry> tokenTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> gemsTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> rankTopCache = new LinkedHashMap<>();


    public void startTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateTokensTop,0L,20*60*10L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateGemsTop,0L,20*60*10L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateRankTop,0L,20*60*10L);
    }

    public void calculateTokensTop() {
        final List<BigIntegerEntry> entries = new LinkedList<>();
        for (UUID uuid : playerMap.keySet()) {
            LunixPlayer lunixPlayer = playerMap.get(uuid);
            entries.add(new BigIntegerEntry(uuid, lunixPlayer.getName(), lunixPlayer.getTokens()));
        }
        final LinkedHashMap<UUID,BigIntegerEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        for (BigIntegerEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        tokenTopCache = sortedMap;
    }

    public void calculateGemsTop() {
        final List<LongEntry> entries = new LinkedList<>();
        for (UUID uuid : playerMap.keySet()) {
            LunixPlayer lunixPlayer = playerMap.get(uuid);
            entries.add(new LongEntry(uuid, lunixPlayer.getName(), lunixPlayer.getGems()));
        }
        final LinkedHashMap<UUID,LongEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> Long.compare(entry2.getValue(),entry1.getValue()));
        for (LongEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        gemsTopCache = sortedMap;
    }

    public void calculateRankTop() {
        final List<LongEntry> entries = new LinkedList<>();
        for (UUID uuid : playerMap.keySet()) {
            LunixPlayer lunixPlayer = playerMap.get(uuid);
            entries.add(new LongEntry(uuid, lunixPlayer.getName(), lunixPlayer.getRank()));
        }
        final LinkedHashMap<UUID,LongEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> Long.compare(entry2.getValue(),entry1.getValue()));
        for (LongEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        rankTopCache = sortedMap;
    }

    public LinkedHashMap<UUID, BigIntegerEntry> getTokenTopCache() {
        return tokenTopCache;
    }

    public LinkedHashMap<UUID, LongEntry> getGemsTopCache() {
        return gemsTopCache;
    }

    public LinkedHashMap<UUID, LongEntry> getRankTopCache() {
        return rankTopCache;
    }


}

