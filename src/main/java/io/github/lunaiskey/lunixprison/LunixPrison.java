package io.github.lunaiskey.lunixprison;

import io.github.lunaiskey.lunixprison.inventory.InventoryManager;
import io.github.lunaiskey.lunixprison.modules.gangs.Gang;
import io.github.lunaiskey.lunixprison.modules.gangs.GangManager;
import io.github.lunaiskey.lunixprison.hooks.PlaceholderHook;
import io.github.lunaiskey.lunixprison.modules.mines.PMine;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.modules.items.ItemManager;
import io.github.lunaiskey.lunixprison.modules.leaderboards.LeaderboardManager;
import io.github.lunaiskey.lunixprison.listeners.PlayerEvents;
import io.github.lunaiskey.lunixprison.modules.mines.PMineManager;
import io.github.lunaiskey.lunixprison.modules.mines.generator.PMineWorld;
import io.github.lunaiskey.lunixprison.modules.pickaxe.PickaxeManager;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.boosters.Boosters;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.*;

public final class LunixPrison extends JavaPlugin {

    private static LunixPrison plugin;
    private PMineManager playerMineManager;
    private PlayerManager playerManager;
    private PickaxeManager pickaxeManager;
    private ItemManager itemManager;
    private GangManager gangManager;
    private LeaderboardManager leaderboardManager;
    private InventoryManager inventoryManager;

    private Boosters boosters;
    private Random rand = new Random();
    private final Set<UUID> savePending = new HashSet<>();

    private int saveTaskID = -1;

    @Override
    public void onEnable() {
        plugin = this;
        if (!setupConfig()) {
            this.getLogger().severe("Directories failed to setup correctly, exiting...");
            return;
        }

        new PMineWorld().generateWorld();
        registerManagers();

        playerManager.loadPlayers();
        playerMineManager.loadPMines();
        gangManager.loadGangs();
        itemManager.registerItems();
        new CommandManager(plugin);
        boosters = new Boosters();

        playerManager.checkPlayerData();
        leaderboardManager.startTasks();
        bufferSaveTask();

        boolean isPlaceholderHookRegistered = new PlaceholderHook(this).registerHook();

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this),this);
        getLogger().info("version " + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(saveTaskID);
        closeAllLunixInvs();
        saveAll();
        getLogger().info("Saved "+savePending.size()+" Players.");
    }

    private void registerManagers() {
        playerMineManager = new PMineManager();
        playerManager = new PlayerManager();
        pickaxeManager = new PickaxeManager();
        itemManager = new ItemManager();
        gangManager = new GangManager();
        inventoryManager = new InventoryManager();
        leaderboardManager = new LeaderboardManager();
    }

    public static LunixPrison getPlugin() {
        return plugin;
    }

    private void closeAllLunixInvs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof LunixHolder) {
                player.closeInventory();
            }
        }
    }

    public void bufferSaveTask() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        if (saveTaskID != -1) {
            //Cancel old task
            scheduler.cancelTask(saveTaskID);
        }
        //Schedule save
        saveTaskID = scheduler.scheduleSyncRepeatingTask(LunixPrison.getPlugin(), this::saveAll, 5 * 60 * 20L,5 * 60 * 20L);
        this.getLogger().info("Buffered a save task to happen in 5 minutes.");
    }

    private void savePlayer(UUID player, boolean slient) {
        PMine mine = LunixPrison.getPlugin().getPMineManager().getPMine(player);
        if (mine != null) {
            mine.save();
        }
        LunixPlayer lunixPlayer = playerManager.getPlayerMap().get(player);
        if (lunixPlayer != null) {
            lunixPlayer.save();
        }
    }

    private void saveAll() {
        for (UUID uuid : savePending) {
            savePlayer(uuid,true);
            savePending.removeIf(n -> (Bukkit.getPlayer(n) == null));
        }
        for (Gang gang : gangManager.getGangMap().values()) {
            gang.save();
        }
        this.getLogger().info("Saving Player, Mine and Gang data...");
    }

    private boolean setupConfig() {
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists() && !pluginFolder.mkdir()) {
            this.getLogger().severe("Could not make plugin folder.");
            return false;
        }
        File pmineFolder = new File(getDataFolder(), "pmines");
        if (!pmineFolder.exists() && !pmineFolder.mkdir()) {
            this.getLogger().severe("Could not make PMine folder.");
            return false;
        }
        File playerFolder = new File(getDataFolder(), "playerdata");
        if (!playerFolder.exists() && !playerFolder.mkdir()) {
            this.getLogger().severe("Could not make PlayerData folder.");
            return false;
        }
        File gangFolder = new File(getDataFolder(), "gangdata");
        if (!gangFolder.exists() && !gangFolder.mkdir()) {
            this.getLogger().severe("Could not make GangData folder.");
            return false;
        }
        return true;
    }


    public PMineManager getPMineManager() {
        return playerMineManager;
    }

    public GangManager getGangManager() { return gangManager;}

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PickaxeManager getPickaxeHandler() {
        return pickaxeManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public Random getRand() {
        return rand;
    }

    public Set<UUID> getSavePending() {
        return savePending;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }
}
