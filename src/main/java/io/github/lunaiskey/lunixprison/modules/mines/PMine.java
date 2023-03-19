package io.github.lunaiskey.lunixprison.modules.mines;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.modules.mines.upgrades.PMineUpgradeType;
import io.github.lunaiskey.lunixprison.util.nms.NMSBlockChange;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.util.nms.NMSWorldBorder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PMine {

    private final int chunkX;
    private final int chunkZ;
    private final UUID owner;
    //mine coordinatess.
    private final World world;
    private Location center;
    private Location min;
    private Location max;
    private int mineRadius;
    private long maxMineBlocks;
    private long blocksBroken = 0;
    private BukkitTask resetTask = null;
    private int resetTime = 10; // in minutes
    private float resetPercentage = 0.70F;
    private Map<Material,Double> composition;
    private Set<Material> disabledBlocks;
    private Map<PMineUpgradeType,Integer> upgradeMap;

    private boolean isPublic;
    private double mineTax;
    private Set<UUID> playersAllowSet;

    public PMine(UUID owner, int chunkX, int chunkZ, int mineRadius, boolean isPublic, double mineTax,Set<Material> disabledBlocks, Map<Material,Double> comp, Set<UUID> playerSet, Map<PMineUpgradeType,Integer> upgradeMap) {
        this.owner = owner;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = Bukkit.getWorld(PMineWorld.getWorldName());
        this.center = new Location(world, (225 * chunkX), 100, (225 * chunkZ));
        this.upgradeMap = Objects.requireNonNullElseGet(upgradeMap, HashMap::new);
        for (PMineUpgradeType upgradeType : PMineUpgradeType.values()) {
            this.upgradeMap.putIfAbsent(upgradeType, 0);
        }
        this.mineRadius = mineRadius+this.upgradeMap.get(PMineUpgradeType.SIZE);
        this.min = new Location(center.getWorld(), center.getBlockX() - this.mineRadius, 51, center.getBlockZ() - this.mineRadius);
        this.max = new Location(center.getWorld(), center.getBlockX() + this.mineRadius, 100, center.getBlockZ() + this.mineRadius);
        this.maxMineBlocks = (this.mineRadius * 2L +1)*(this.mineRadius * 2L +1)*(max.getBlockY()-min.getBlockY()+1);
        Player p = Bukkit.getPlayer(owner);
        if (p != null) {
            if (new Location(center.getWorld(),center.getBlockX()+ this.mineRadius +1,max.getBlockY(),center.getBlockZ()+ this.mineRadius +1).getBlock().getType() != Material.BEDROCK) {
                genBedrock();
                reset();
            }
        }
        this.disabledBlocks = Objects.requireNonNullElseGet(disabledBlocks, HashSet::new);
        this.composition = Objects.requireNonNullElseGet(comp, LinkedHashMap::new);
        Map<Material,Integer> sortedBlocks = PMineManager.getBlockRankMap();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(owner);
        if (lunixPlayer != null) {
            for(Material mat : sortedBlocks.keySet()) {
                if (lunixPlayer.getRank() >= sortedBlocks.get(mat)) {
                    this.composition.putIfAbsent(mat,1D);
                } else {
                    this.composition.remove(mat);
                }
            }
        }
        this.playersAllowSet = Objects.requireNonNullElseGet(playerSet, HashSet::new);
        this.isPublic = isPublic;
        this.mineTax = mineTax;

        checkBlocksBroken();
    }

    public PMine(UUID owner, int chunkX, int chunkZ) {
        this(owner,chunkX,chunkZ,12,false,10F,null,null,null,null);
    }

    public void checkMineBlocks() {
        Map<Material,Integer> sortedBlocks = PMineManager.getBlockRankMap();
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(owner);
        Map<Material,Double> replaceMap = new LinkedHashMap<>();
        for (Material mat : sortedBlocks.keySet()) {
            if (lunixPlayer.getRank() >= sortedBlocks.get(mat)) {
                replaceMap.put(mat, composition.getOrDefault(mat, 1D));
            } else {
                break;
            }
        }
        this.composition = replaceMap;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public Location getCenter() {
        return center.clone();
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public Map<Material, Double> getComposition() {
        return composition;
    }

    public Map<PMineUpgradeType, Integer> getUpgradeMap() {
        return upgradeMap;
    }

    public double getMineTax() {
        return mineTax;
    }

    public double getSellPrice() {
        LunixPlayer lunixPlayer = LunixPrison.getPlugin().getPlayerManager().getPlayerMap().get(owner);
        return 1+(lunixPlayer.getRank()*0.005);
    }

    public Set<Material> getDisabledBlocks() {
        return disabledBlocks;
    }

    public void setMineTax(double mineTax) {
        this.mineTax = mineTax;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        if (isPublic) {
            LunixPrison.getPlugin().getPMineManager().getPublicMines().add(owner);
        } else {
            LunixPrison.getPlugin().getPMineManager().getPublicMines().remove(owner);
        }
    }

    public void setComposition(Map<Material, Double> composition) {
        this.composition = composition;
    }

    public void setDisabledBlocks(Set<Material> disabledBlocks) {
        this.disabledBlocks = disabledBlocks;
    }

    public void save() {
        File file = new File(LunixPrison.getPlugin().getDataFolder() + "/pmines/" + owner + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        data.set("chunkX", chunkX);
        data.set("chunkZ", chunkZ);
        data.set("isPublic",isPublic);
        data.set("tax",mineTax);
        Map<String,Object> upgradeMap = new LinkedHashMap<>();
        for (PMineUpgradeType upgradeType : this.upgradeMap.keySet()) {
            upgradeMap.put(upgradeType.name(),this.upgradeMap.get(upgradeType));
        }
        data.createSection("upgrades",upgradeMap);
        if (composition != null) {
            data.createSection("blocks",composition);
        }
        List<String> disabledBlocksList = new ArrayList<>();
        for (Material mat : disabledBlocks) {
            disabledBlocksList.add(mat.name());
        }
        data.set("disabledBlocks",disabledBlocksList);
        try {
            data.save(file);
        } catch (IOException e) {
            LunixPrison.getPlugin().getLogger().severe("Failed to save " + owner + "'s Pmine.");
            e.printStackTrace();
        }
    }

    private boolean resetting = false;

    public boolean reset() {
        if (resetting) {
            return false;
        } else {
            resetting = true;
        }

        Random rand = new Random();
        Player p = Bukkit.getPlayer(owner);
        teleportToCenter(true,true);
        NMSBlockChange NMSBlockChange = new NMSBlockChange(world, ((CraftWorld) world).getHandle());
        List<CompositionEntry> probabilityMap = mapComposition(composition,disabledBlocks);
        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    double r = rand.nextDouble();
                    for (CompositionEntry ce : probabilityMap) {
                        if (r <= ce.getChance()) {
                            NMSBlockChange.setBlock(x,y,z,ce.getBlock());
                            break;
                        }
                    }
                }
            }
        }
        NMSBlockChange.update();
        resetting = false;
        blocksBroken = 0;
        scheduleReset();
        return true;
    }


    public void genBedrock() {
        Location wallMin = this.min.clone().subtract(1, 1, 1);
        Location walkMax = this.max.clone().add(1, 0, 1);
        for (int x = wallMin.getBlockX(); x <= walkMax.getBlockX(); ++x) {
            for (int y = wallMin.getBlockY(); y <= walkMax.getBlockY(); ++y) {
                for (int z = wallMin.getBlockZ(); z <= walkMax.getBlockZ(); ++z) {
                    Block b = world.getBlockAt(x, y, z);
                    boolean inX = true;
                    boolean inZ = true;
                    if (x < min.getBlockX() || x > max.getBlockX()) {
                        inX = false;
                    }
                    if (z < min.getBlockZ() || z > max.getBlockZ()) {
                        inZ = false;
                    }
                    if (inX && inZ) {
                        if (y != wallMin.getBlockY()) {
                            continue;
                        }
                    }
                    if (b.getType() != Material.BEDROCK) {
                        b.setType(Material.BEDROCK,false);
                    }
                }
            }
        }
    }

    public void teleportToCenter(boolean keepLook,boolean silent) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isInMineRegion(p)) {
                teleportToCenter(p,keepLook,silent);
            }
        }
    }

    public void teleportToCenter(Player p,boolean keepLook,boolean silent) {
        Location oldLoc = p.getLocation();
        Location newLoc = getCenter().add(0.5,1,0.5);
        if (keepLook) {
            newLoc.setDirection(oldLoc.getDirection());
        }
        p.teleport(newLoc,PlayerTeleportEvent.TeleportCause.PLUGIN);
        if (!silent) {
            p.sendMessage("Teleporting to mine...");
        }
    }

    public boolean isInMineRegion(Location loc) {
        return (loc.getBlockY() >= min.getBlockY() && loc.getBlockY() <= max.getBlockY() && loc.getBlockX() >= min.getBlockX() && loc.getBlockX() <= max.getBlockX() && loc.getBlockZ() >= min.getBlockZ() && loc.getBlockZ() <= max.getBlockZ() && loc.getWorld().getName().equals(PMineWorld.getWorldName()));
    }

    public boolean isInMineRegion(Player p) {
        Location loc = p.getEyeLocation();
        return isInMineRegion(loc);
    }

    public boolean isInMineIsland(Player p) {
        return isInMineIsland(p.getLocation());
    }


    public boolean isInMineIsland(Location loc) {
        if (!(loc.getWorld().getName().equals(PMineWorld.getWorldName()))) {
            return false;
        }
        Pair<Integer,Integer> pair = LunixPrison.getPlugin().getPMineManager().getGridLocation(loc);
        return pair.getLeft() == chunkX && pair.getRight() == chunkZ;
    }

    public void increaseRadius(int amount) {
        if (amount <= 0) {
            return;
        }
        setRadius(getSize()+amount);
    }

    public void decreaseRadius(int amount) {
        if (amount <= 0) {
            return;
        }
        setRadius(getSize()-amount);
    }

    public void setRadius(int amount) {
        if (amount <= 0) {
            mineRadius = 0;
        } else {
            mineRadius = amount;
        }
        updateSize();
    }

    public void updateSize() {
        this.min = new Location(center.getWorld(), center.getBlockX() - mineRadius, 51, center.getBlockZ() - mineRadius);
        this.max = new Location(center.getWorld(), center.getBlockX() + mineRadius, 100, center.getBlockZ() + mineRadius);
        this.maxMineBlocks = (mineRadius * 2L +1)*(mineRadius * 2L +1)*(max.getBlockY()-min.getBlockY()+1);
    }

    public int getSize() {
        return mineRadius;
    }

    public long getBlocksBroken() {
        return blocksBroken;
    }

    public void addMineBlocks(long amount) {
        if (amount <= 0) {
            return;
        }
        blocksBroken += amount;
        if (blocksBroken >= maxMineBlocks*resetPercentage) {
            reset();
        }
    }

    public void scheduleReset() {
        if (resetTask != null && !resetTask.isCancelled()) {
            resetTask.cancel();
        }
        resetTask = Bukkit.getScheduler().runTaskLater(LunixPrison.getPlugin(), () -> {
            Player p = Bukkit.getPlayer(owner);
            if (p != null) {
                if (blocksBroken > 0) {
                    reset();
                }
                return;
            }
            resetTask = null;
        },20L*60L*resetTime);
    }

    public void checkBlocksBroken() {
        long blocks = 0;
        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    Block b = world.getBlockAt(x, y, z);
                    if (b.getType() == Material.AIR) {
                        blocks += 1;
                    }
                }
            }
        }
        blocksBroken = blocks;
    }

    public static class CompositionEntry {
        private Material block;
        private double chance;

        public CompositionEntry(Material block, double chance) {
            this.block = block;
            this.chance = chance;
        }

        public Material getBlock() {
            return block;
        }

        double getChance() {
            return chance;
        }
    }

    private static List<CompositionEntry> mapComposition(Map<Material, Double> compositionIn, Set<Material> disabledBlocks) {
        List<CompositionEntry> probabilityMap = new ArrayList<>();
        Map<Material, Double> comp = new HashMap<>();
        if (compositionIn != null && !compositionIn.isEmpty()) {
            for (Material mat : compositionIn.keySet()) {
                if (!disabledBlocks.contains(mat)) {
                    comp.put(mat,compositionIn.get(mat));
                }
            }
        }
        double max = 0;
        for (Map.Entry<Material, Double> entry : comp.entrySet()) {
            max += entry.getValue();
        }
        if (max <= 0) {
            comp.put(Material.COBBLESTONE, 1-max);
            max = 1;
        }
        double i = 0;
        for (Map.Entry<Material, Double> entry : comp.entrySet()) {
            double v = entry.getValue() / max;
            i += v;
            probabilityMap.add(new CompositionEntry(entry.getKey(), i));
        }
        return probabilityMap;
    }

    public long getArea() {
        return ((long) max.getBlockX()-min.getBlockX()+1) * ((long) max.getBlockZ()-min.getBlockZ()+1) * ((long) max.getBlockY()-min.getBlockY()+1);
    }

    public void sendBorder(Player player) {
        if (!player.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
            return;
        }
        new NMSWorldBorder().sendBorder(player,225+1,getCenter().getBlockX(),getCenter().getBlockZ());
    }
}