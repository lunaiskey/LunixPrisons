package io.github.lunaiskey.pyrexprison.mines;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PMineManager {

    private static Map<Pair<Integer,Integer>, PMine> pMines = new HashMap<>();
    private static Map<UUID, Pair<Integer,Integer>> ownerPMines = new HashMap<>();
    private final int gridIslandSize = 225;
    private ImmutablePair<Integer,Integer> last = null;

    public void loadPMines() {
        File[] pmineFiles = new File(PyrexPrison.getPlugin().getDataFolder(), "pmines").listFiles(new IsPMineFile());
        assert pmineFiles != null;
        for (File file : pmineFiles) {
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            Map<String,Object> map = fileConf.getConfigurationSection("mine").getValues(false);
            UUID owner = UUID.fromString(file.getName().replace(".yml",""));
            int chunkX = (int) map.get("chunkX");
            int chunkZ = (int) map.get("chunkZ");
            Map<Material,Double> blocksMap = new LinkedHashMap<>();
            if (fileConf.getConfigurationSection("blocks") != null) {
                Map<String,Object> blocksMapRaw = fileConf.getConfigurationSection("blocks").getValues(false);
                for (String str : blocksMapRaw.keySet()) {
                    blocksMap.put(Material.getMaterial(str),(double) blocksMapRaw.get(str));
                }
            }
            PMineManager.newPMine(owner,chunkX,chunkZ,blocksMap);
            if (Bukkit.getPlayer(owner) != null) {
                PMineManager.getPMine(owner).reset();
            }
        }
    }

    public static class IsPMineFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".yml");
        }
    }

    public Location getMinCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(PMineWorld.getWorldName()),-112+(gridIslandSize *chunkX),0,-112+(gridIslandSize *chunkZ));
    }

    public Location getMaxCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(PMineWorld.getWorldName()),112+(gridIslandSize *chunkX),256,112+(gridIslandSize *chunkZ));
    }

    public Pair<Integer,Integer> getGridLocation(Location loc) {
        double x = Math.floor((112D + loc.getBlockX()) / gridIslandSize);
        double z = Math.floor((112D + loc.getBlockZ()) / gridIslandSize);
        return new ImmutablePair<>((int) x,(int) z);
    }

    public static void newPMine(UUID owner, int chunkX, int chunkZ, Map<Material,Double> composition) {
        PMine mine = new PMine(owner, chunkX, chunkZ,12,composition);
        Pair<Integer,Integer> pair = new ImmutablePair<>(chunkX,chunkZ);
        pMines.put(pair,mine);
        ownerPMines.put(owner,pair);
        pMines.get(pair).save();
    }

    public static void newPMine(UUID owner, int chunkX, int chunkZ) {
        newPMine(owner,chunkX,chunkZ,new HashMap<>());
    }

    public void newPMine(UUID owner) {
        if (getPMine(owner) == null) {
            Pair<Integer,Integer> newGridLoc = getNextIsland();
            newPMine(owner,newGridLoc.getLeft(),newGridLoc.getRight());
        }
    }

    public static PMine getPMine(int chunkX,int chunkZ) {
        return pMines.get(new ImmutablePair<>(chunkX,chunkZ));
    }

    public static PMine getPMine(UUID owner) {
        Pair<Integer,Integer> pair = ownerPMines.get(owner);
        if (pair != null) {
            return getPMine(pair.getLeft(),pair.getRight());
        }
        return null;
    }

    public static Map<Pair<Integer, Integer>, PMine> getPMinesMap() {
        return pMines;
    }

    public boolean isChunkOccupied(int chunkX,int chunkZ) {
        return getPMinesMap().containsKey(new ImmutablePair<>(chunkX,chunkZ));
    }

    public static Map<UUID, Pair<Integer, Integer>> getOwnerPMines() {
        return ownerPMines;
    }

    private Pair<Integer,Integer> getNextIsland() {
        // Find the next free spot
        if (last == null) {
            last = new ImmutablePair<>(0,0);
        }
        Pair<Integer,Integer> next = new ImmutablePair<>(last.getLeft(),last.getRight());

        while (getPMinesMap().containsKey(next)) {
            next = nextGridLocation(next);
        }
        // Make the last next, last
        last = new ImmutablePair<>(next.getLeft(),next.getRight());
        return next;
    }

    private Pair<Integer,Integer> nextGridLocation(Pair<Integer,Integer> lastIsland) {
        final int x = lastIsland.getLeft();
        final int z = lastIsland.getRight();
        final MutablePair<Integer,Integer> nextPos = new MutablePair<>(lastIsland.getLeft(),lastIsland.getRight());
        if (x < z) {
            if (-1 * x < z) {
                nextPos.setLeft(nextPos.getLeft() + 1);
                return nextPos;
            }
            nextPos.setRight(nextPos.getRight() + 1);
            return nextPos;
        }
        if (x > z) {
            if (-1 * x >= z) {
                nextPos.setLeft(nextPos.getLeft() - 1);
                return nextPos;
            }
            nextPos.setRight(nextPos.getRight() - 1);
            return nextPos;
        }
        if (x <= 0) {
            nextPos.setRight(nextPos.getRight() + 1);
            return nextPos;
        }
        nextPos.setRight(nextPos.getRight() - 1);
        return nextPos;
    }


}