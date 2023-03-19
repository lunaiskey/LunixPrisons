package io.github.lunaiskey.lunixprison.modules.mines;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.modules.mines.inventories.PMinePublicGUI;
import io.github.lunaiskey.lunixprison.modules.mines.upgrades.PMineUpgradeType;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PMineManager {

    public static final int DEFAULT_RADIUS = 12;
    private final int GRID_ISLAND_DIAMETER;
    private final int GRID_ISLAND_RADIUS;
    private final Map<UUID, PMine> pMines = new HashMap<>();
    private final Map<Pair<Integer,Integer>, UUID> gridPosUUIDMap = new HashMap<>();
    private ImmutablePair<Integer,Integer> lastChunkChecked = new ImmutablePair<>(0,0);
    private Set<UUID> publicMines = new HashSet<>();
    private List<UUID> sortedList = new ArrayList<>();

    public PMineManager() {
        GRID_ISLAND_DIAMETER = 225; //ALWAYS BE ODD
        GRID_ISLAND_RADIUS = (GRID_ISLAND_DIAMETER - (GRID_ISLAND_DIAMETER % 2))/2;
    }

    public void loadPMines() {
        File[] pmineFiles = new File(LunixPrison.getPlugin().getDataFolder(), "pmines").listFiles(new IsPMineFile());
        assert pmineFiles != null;
        for (File file : pmineFiles) {
            loadPMine(file);
        }
        sortedList = getSortedPublicByRank();
        ScheduleSortPublic();
    }

    public void loadPMine(File file) {
        FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
        Map<String,Object> map = fileConf.getValues(true);
        UUID owner = UUID.fromString(file.getName().replace(".yml",""));
        int chunkX = ((Number) map.get("chunkX")).intValue();
        int chunkZ = ((Number) map.get("chunkZ")).intValue();
        PMine mine = new PMine(owner,chunkX,chunkZ);
        loadRootData(mine,map);
        loadUpgradeData(mine,map);
        loadBlocksData(mine,map);
        newPMine(mine);
        if (Bukkit.getPlayer(owner) != null) {
            getPMine(owner).reset();
        }
    }

    private void loadRootData(PMine mine, Map<String, Object> map) {
        boolean isPublic = (boolean) map.getOrDefault("isPublic",false);
        double tax = ((Number) map.getOrDefault("tax",10D)).doubleValue();
        mine.setPublic(isPublic);
        mine.setMineTax(tax);
    }

    private void loadUpgradeData(PMine mine, Map<String, Object> map) {
        Map<String,Object> section = ((MemorySection) map.get("upgrades")).getValues(true);
        Map<PMineUpgradeType,Integer> upgradesMap = new HashMap<>();
        for (String str : section.keySet()) {
            upgradesMap.put(PMineUpgradeType.valueOf(str), ((Number) section.get(str)).intValue());
        }
    }

    private void loadBlocksData(PMine mine, Map<String, Object> map) {
        Map<String,Object> section = ((MemorySection) map.get("blocks")).getValues(true);
        Map<Material,Double> blocksMap = new LinkedHashMap<>();
        for (String str : section.keySet()) {
            blocksMap.put(Material.getMaterial(str),((Number) section.get(str)).doubleValue());
        }
        Set<Material> disabledBlocks = new HashSet<>();
        List<String> disabledBlocksRaw = (List<String>) map.get("disabledBlocks");
        for (String str : disabledBlocksRaw) {
            try {
                disabledBlocks.add(Material.valueOf(str)); //material is valid
            } catch (Exception ignored) {}
        }
        mine.setComposition(blocksMap);
        mine.setDisabledBlocks(disabledBlocks);
    }

    public void ScheduleSortPublic() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(LunixPrison.getPlugin(),()-> {
            boolean anyone = false;
            for (UUID uuid : PMinePublicGUI.getPageMap().keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    if (player.getOpenInventory().getTopInventory().getHolder() instanceof LunixHolder) {
                        LunixHolder holder = (LunixHolder) player.getOpenInventory().getTopInventory().getHolder();
                        if (holder.getInvType() == LunixInvType.PMINE_PUBLIC_MINES) {
                            anyone = true;
                            break;
                        }
                    }
                }
            }
            if (anyone) {
                sortedList = getSortedPublicByRank();
                for (UUID uuid : PMinePublicGUI.getPageMap().keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        if (player.getOpenInventory().getTopInventory().getHolder() instanceof LunixHolder) {
                            LunixHolder holder = (LunixHolder) player.getOpenInventory().getTopInventory().getHolder();
                            if (holder.getInvType() == LunixInvType.PMINE_PUBLIC_MINES) {
                                new PMinePublicGUI().updateGUI(player);
                            }
                        }
                    }
                }
            }
        },0,20*5);
    }

    private List<UUID> getSortedPublicByRank() {
        Map<UUID, LunixPlayer> playerMap = LunixPrison.getPlugin().getPlayerManager().getPlayerMap();
        List<UUID> sortedList = new ArrayList<>(publicMines);
        sortedList.sort(Collections.reverseOrder(Comparator.comparingInt(o -> playerMap.get(o).getRank())));
        return sortedList;
    }

    public static class IsPMineFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".yml");
        }
    }

    public Location getMinCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(PMineWorld.getWorldName()),-GRID_ISLAND_RADIUS+(GRID_ISLAND_DIAMETER *chunkX),0,-GRID_ISLAND_RADIUS+(GRID_ISLAND_DIAMETER *chunkZ));
    }

    public Location getMaxCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(PMineWorld.getWorldName()),GRID_ISLAND_RADIUS+(GRID_ISLAND_DIAMETER *chunkX),256,GRID_ISLAND_RADIUS+(GRID_ISLAND_DIAMETER *chunkZ));
    }

    public Pair<Integer,Integer> getGridLocation(Location loc) {
        double x = Math.floor((((double)GRID_ISLAND_RADIUS) + loc.getBlockX()) / GRID_ISLAND_DIAMETER);
        double z = Math.floor((((double)GRID_ISLAND_RADIUS) + loc.getBlockZ()) / GRID_ISLAND_DIAMETER);
        return new ImmutablePair<>((int) x,(int) z);
    }

    public void newPMine(UUID owner, int chunkX, int chunkZ,boolean isPublic,double tax,Set<Material> disabledBlocks,Map<Material,Double> composition,Map<PMineUpgradeType,Integer> upgradeMap) {
        PMine mine = new PMine(owner, chunkX, chunkZ,12,isPublic,tax,disabledBlocks,composition,null,upgradeMap);
        newPMine(mine);
    }

    public void newPMine(UUID owner, int chunkX, int chunkZ) {
        newPMine(owner,chunkX,chunkZ,false,10,null,null,null);
    }

    public void newPMine(UUID owner) {
        if (getPMine(owner) == null) {
            Pair<Integer,Integer> newGridLoc = getNextIsland();
            newPMine(owner,newGridLoc.getLeft(),newGridLoc.getRight());
        }
    }

    public void newPMine(PMine mine) {
        Pair<Integer,Integer> pair = new ImmutablePair<>(mine.getChunkX(),mine.getChunkZ());
        pMines.put(mine.getOwner(),mine);
        gridPosUUIDMap.put(pair,mine.getOwner());
        mine.save();
        if (mine.isPublic()) {
            publicMines.add(mine.getOwner());
        }
    }

    public PMine getPMine(int chunkX,int chunkZ) {
        return getPMine(gridPosUUIDMap.get(new ImmutablePair<>(chunkX,chunkZ)));
    }

    public PMine getPMine(UUID owner) {
        return pMines.get(owner);
    }
    public PMine getPMineAtLocation(Location location) {
        return getPMine(gridPosUUIDMap.get(getGridLocation(location.clone())));
    }

    public PMine getPMineAtPlayer(Player player) {
        return getPMineAtLocation(player.getLocation().clone());
    }

    public Map<Pair<Integer, Integer>, UUID> getGridPosToUUIDMap() {
        return gridPosUUIDMap;
    }

    public boolean isChunkOccupied(int chunkX,int chunkZ) {
        return getGridPosToUUIDMap().containsKey(new ImmutablePair<>(chunkX,chunkZ));
    }

    public Map<UUID, PMine> getPMineMap() {
        return pMines;
    }

    private Pair<Integer,Integer> getNextIsland() {
        // Find the next free spot
        Pair<Integer,Integer> next = new ImmutablePair<>(lastChunkChecked.getLeft(), lastChunkChecked.getRight());

        while (getGridPosToUUIDMap().containsKey(next)) {
            next = nextGridLocation(next);
        }
        // Make the last next, last
        lastChunkChecked = new ImmutablePair<>(next.getLeft(),next.getRight());
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

    public Set<UUID> getPublicMines() {
        return publicMines;
    }

    public List<UUID> getPublicSortedByRankList() {
        return sortedList;
    }

    public static Map<Material,Integer> getBlockRankMap() {
        Map<Material,Integer> map = new LinkedHashMap<>();
        map.put(Material.COBBLESTONE,0);
        map.put(Material.STONE,5);
        map.put(Material.GRANITE,10);
        map.put(Material.POLISHED_GRANITE,15);
        map.put(Material.DIORITE,20);
        map.put(Material.POLISHED_DIORITE,25);
        map.put(Material.ANDESITE,30);
        map.put(Material.POLISHED_ANDESITE,35);
        map.put(Material.COBBLED_DEEPSLATE,40);
        map.put(Material.DEEPSLATE,50);
        map.put(Material.POLISHED_DEEPSLATE,60);
        map.put(Material.COAL_ORE,70);
        map.put(Material.DEEPSLATE_COAL_ORE,80);
        map.put(Material.COAL_BLOCK,90);
        map.put(Material.COPPER_ORE,100);
        map.put(Material.DEEPSLATE_COPPER_ORE,110);
        map.put(Material.RAW_COPPER_BLOCK,120);
        map.put(Material.COPPER_BLOCK,130);
        map.put(Material.IRON_ORE,140);
        map.put(Material.DEEPSLATE_IRON_ORE,150);
        map.put(Material.RAW_IRON_BLOCK,160);
        map.put(Material.IRON_BLOCK,170);
        map.put(Material.GOLD_ORE,180);
        map.put(Material.DEEPSLATE_GOLD_ORE,190);
        map.put(Material.RAW_GOLD_BLOCK,200);
        map.put(Material.GOLD_BLOCK,210);
        map.put(Material.DIAMOND_ORE,220);
        map.put(Material.DEEPSLATE_DIAMOND_ORE,230);
        map.put(Material.DIAMOND_BLOCK,240);
        map.put(Material.EMERALD_ORE,250);
        map.put(Material.DEEPSLATE_EMERALD_ORE,260);
        map.put(Material.EMERALD_BLOCK,270);
        map.put(Material.AMETHYST_BLOCK,280);
        map.put(Material.NETHERRACK,290);
        map.put(Material.NETHER_BRICKS,300);
        map.put(Material.RED_NETHER_BRICKS,310);
        map.put(Material.QUARTZ_BLOCK,320);
        map.put(Material.SMOOTH_QUARTZ,330);
        map.put(Material.CHISELED_QUARTZ_BLOCK,340);
        map.put(Material.WHITE_TERRACOTTA,350);
        map.put(Material.ORANGE_TERRACOTTA,360);
        map.put(Material.MAGENTA_TERRACOTTA,370);
        map.put(Material.LIGHT_BLUE_TERRACOTTA,380);
        map.put(Material.YELLOW_TERRACOTTA,390);
        map.put(Material.LIME_TERRACOTTA,400);
        map.put(Material.PINK_TERRACOTTA,410);
        map.put(Material.GRAY_TERRACOTTA,420);
        map.put(Material.LIGHT_GRAY_TERRACOTTA,430);
        map.put(Material.CYAN_TERRACOTTA,440);
        map.put(Material.PURPLE_TERRACOTTA,450);
        map.put(Material.BLUE_TERRACOTTA,460);
        map.put(Material.BROWN_TERRACOTTA,470);
        map.put(Material.GREEN_TERRACOTTA,480);
        map.put(Material.RED_TERRACOTTA,490);
        map.put(Material.BLACK_TERRACOTTA,500);
        map.put(Material.TERRACOTTA,525);
        return map;
    }
}
