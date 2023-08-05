package io.github.lunaiskey.lunixprison.modules.leaderboards;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.gangs.Gang;
import io.github.lunaiskey.lunixprison.modules.gangs.GangManager;
import io.github.lunaiskey.lunixprison.modules.gangs.GangMember;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import org.bukkit.Bukkit;

import java.util.*;

public class LeaderboardManager {

    private static LeaderboardManager instance;
    private boolean tasksStarted = false;

    private Map<UUID, LunixPlayer> playerMap = PlayerManager.get().getPlayerMap();
    private Map<UUID, Gang> gangMap = GangManager.get().getGangMap();
    private LinkedHashMap<UUID,BigIntegerEntry> tokenTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> gemsTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> rankTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> gangTopCache = new LinkedHashMap<>();

    private LeaderboardManager() {}

    public static LeaderboardManager get() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }


    public void startTasks() {
        if (tasksStarted) {
            return;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateTokensTop,0L,10*60*20L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateGemsTop,0L,10*60*20L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateRankTop,0L,10*60*20L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(LunixPrison.getPlugin(), this::calculateGangTop,0L,10*60*20L);
        tasksStarted = true;
    }

    public void calculateTokensTop() {
        final List<BigIntegerEntry> entries = new ArrayList<>();
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
        final List<LongEntry> entries = new ArrayList<>();
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
        final List<LongEntry> entries = new ArrayList<>();
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

    public void calculateGangTop() {
        final List<LongEntry> entries = new ArrayList<>();
        for (UUID uuid : gangMap.keySet()) {
            Gang gang = gangMap.get(uuid);
            entries.add(new LongEntry(uuid, gang.getName(), gang.getTrophies()));
        }
        final LinkedHashMap<UUID,LongEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> Long.compare(entry2.getValue(),entry1.getValue()));
        for (LongEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        gangTopCache = sortedMap;
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

    public LinkedHashMap<UUID, LongEntry> getGangTopCache() {
        return gangTopCache;
    }
}

