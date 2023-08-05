package io.github.lunaiskey.lunixprison.modules.gangs;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Gang {

    private final UUID uuid;
    private UUID owner;
    private String name;
    private Map<UUID,GangMember> members;
    private final Set<UUID> pendingInvites = new HashSet<>();
    private long trophies;

    private final int maxMembers = 10;

    public Gang(UUID uuid, UUID owner, String name, Map<UUID,GangMember> members, long trophies){
        this.uuid = uuid;
        this.owner = owner;
        this.name = name;
        this.members = Objects.requireNonNullElseGet(members, HashMap::new);;
        this.trophies = trophies;
        save();
    }
    public Gang(UUID uuid, UUID owner, String name){
        this(uuid,owner,name,null,0);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public Map<UUID, GangMember> getMembers() {
        return members;
    }

    public void removeMember(UUID uuid) {
        if (owner != uuid) {
            getMembers().remove(uuid);
            GangManager.get().getPlayerGangMap().remove(uuid);
        }
    }

    public void addMember(UUID uuid) {
        if (!getMembers().containsKey(uuid)) {
            getMembers().put(uuid,new GangMember(uuid, PlayerManager.get().getPlayerMap().get(uuid).getName(),GangRankType.MEMBER));
            GangManager.get().getPlayerGangMap().put(uuid,this.uuid);
            pendingInvites.remove(uuid);
        }
    }

    public void setName(String name) {
        GangManager.get().getGangNameMap().remove(this.name.toUpperCase());
        this.name = name.replace(" ","");
        GangManager.get().getGangNameMap().put(this.name.toUpperCase(),this.uuid);
    }

    public void setOwner(UUID owner) {
        if (members.containsKey(owner)) {
            members.get(this.owner).setType(GangRankType.MOD);
            members.get(owner).setType(GangRankType.OWNER);
            this.owner = owner;
        }
    }

    public boolean isFull() {
        return getMembers().size() >= maxMembers;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public long getTrophies() {
        return trophies;
    }

    public Set<UUID> getPendingInvites() {
        return pendingInvites;
    }

    public void save() {
        File file = new File(LunixPrison.getPlugin().getDataFolder() + "/"+ GangManager.GANG_FILE_NAME+"/" + uuid + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> gangMap = new LinkedHashMap<>();
        gangMap.put("owner", owner.toString());
        gangMap.put("name", name);
        Map<String, Object> memberMap = new LinkedHashMap<>();
        for (UUID pUUID : members.keySet()) {
            memberMap.put(pUUID.toString(),members.get(pUUID).getType().name());
        }
        gangMap.put("trophies", trophies);
        data.createSection("gangData", gangMap);
        data.createSection("members",memberMap);
        try {
            data.save(file);
        } catch (IOException e) {
            LunixPrison.getPlugin().getLogger().severe("Failed to save the " + name + " gang.");
            e.printStackTrace();
        }
    }


}
