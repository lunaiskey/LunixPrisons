package io.github.lunaiskey.lunixprison.modules.gangs;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class GangManager {

    private static GangManager instance;

    private Map<UUID, Gang> gangMap = new HashMap<>();
    private Map<String, UUID> gangNameMap = new HashMap<>();

    private Map<UUID, UUID> playerGangMap = new HashMap<>();
    public static final String GANG_FILE_NAME = "gangdata";

    private GangManager() {

    }

    public static GangManager get() {
        if (instance == null) {
            instance = new GangManager();
        }
        return instance;
    }


    public void loadGangs(){
        File[] gangFiles = new File(LunixPrison.getPlugin().getDataFolder(), GANG_FILE_NAME).listFiles(new IsGangFile());
        assert gangFiles != null;
        for (File file : gangFiles){
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            Map<String,Object> gangMap = fileConf.getConfigurationSection("gangData").getValues(false);
            Map<String,Object> rawMemberMap = fileConf.getConfigurationSection("members").getValues(false);
            Map<UUID,GangMember> memberMap = new LinkedHashMap<>();

            UUID gangUUID = UUID.fromString(file.getName().replace(".yml",""));
            UUID owner = UUID.fromString((String) gangMap.get("owner"));
            String name = (String) gangMap.get("name");

            for (String str : rawMemberMap.keySet()) {
                try {
                    UUID memberUUID = UUID.fromString(str);
                    GangRankType rankType = GangRankType.valueOf((String) rawMemberMap.get(str));
                    memberMap.put(memberUUID,new GangMember(memberUUID, PlayerManager.get().getPlayerMap().get(memberUUID).getName(),rankType));
                } catch (Exception ignored) {}
            }

            long trophies = ((Number) gangMap.get("trophies")).longValue();
            addGang(gangUUID, owner, name, memberMap, trophies);
        }
    }

    public Map<UUID, Gang> getGangMap() {
        return gangMap;
    }

    public Map<String, UUID> getGangNameMap() {
        return gangNameMap;
    }

    public Map<UUID, UUID> getPlayerGangMap() {
        return playerGangMap;
    }

    public boolean isInGang(UUID player) {
        return playerGangMap.containsKey(player);
    }

    @Nullable
    public Gang getGang(@NotNull String gangName) {
        if (gangExists(gangName)) {
            return gangMap.get(gangNameMap.get(gangName.toUpperCase()));
        } else {
            return null;
        }
    }

    public boolean gangExists(UUID gangUUID) {
        return gangMap.containsKey(gangUUID);
    }

    public boolean gangExists(@NotNull String gangName) {
        gangName = gangName.toUpperCase();
        if (gangNameMap.containsKey(gangName)) {
            return gangExists(gangNameMap.get(gangName));
        } else {
            return false;
        }
    }

    @Nullable
    public Gang getGang(@NotNull UUID player) {
        if (!isInGang(player)) {
            return null;
        }
        return gangMap.get(getPlayerGangMap().get(player));
    }

    public void addGang(UUID uuid, UUID owner, String name, Map<UUID,GangMember> members, long trophies) {
        if (members == null) {
            members = new LinkedHashMap<>();
        }
        members.put(owner,new GangMember(owner, PlayerManager.get().getPlayerMap().get(owner).getName(),GangRankType.OWNER) );
        for (UUID member : members.keySet()) {
            playerGangMap.put(member,uuid);
        }
        gangMap.put(uuid,new Gang(uuid,owner,name,members,trophies));
        gangNameMap.put(name.toUpperCase(),uuid);
    }

    public void addGang(UUID uuid, UUID owner, String name) {
        addGang(uuid,owner,name,new LinkedHashMap<>(),0);
    }

    public void createGang(UUID owner, String name) {
        UUID newUUID;
        do {
            newUUID = UUID.randomUUID();
        } while (gangMap.containsKey(newUUID));
        addGang(newUUID,owner,name);
    }

    public boolean removeGang(UUID gangUUID) {
        if (gangMap.containsKey(gangUUID)) {
            Gang gang = gangMap.get(gangUUID);
            String name = gang.getName();
            for (UUID member : gang.getMembers().keySet()) {
                Player player = Bukkit.getPlayer(member);
                if (player != null) {
                    player.sendMessage(StringUtil.color("&cYour gang &f"+gang.getName()+"&c has been disbanded."));
                }
                playerGangMap.remove(member);
            }
            gangMap.remove(gangUUID);
            gangNameMap.remove(name.toUpperCase());
            File gangFile = new File(LunixPrison.getPlugin().getDataFolder() + "/"+GANG_FILE_NAME+"/" + gangUUID + ".yml");
            if (gangFile.exists()) {
                gangFile.delete();
            }
            return true;
        } else {
            return false;
        }
    }
    public static class IsGangFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".yml");
        }
    }

}
