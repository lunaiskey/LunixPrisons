package io.github.lunaiskey.lunixprison;

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
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.boosters.Boosters;
import io.github.lunaiskey.lunixprison.modules.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public final class LunixPrison extends JavaPlugin {

    private static LunixPrison plugin;
    private final Random rand = new Random();
    private Boosters boosters;
    private final Set<UUID> savePending = new HashSet<>();

    private BukkitTask saveTask;

    @Override
    public void onEnable() {
        plugin = this;
        if (!setupConfig()) {
            this.getLogger().severe("Directories failed to setup correctly, exiting...");
            return;
        }
        new PMineWorld().generateWorld();
        PlayerManager.get().loadPlayers();
        PMineManager.get().loadPMines();
        GangManager.get().loadGangs();
        ItemManager.get().registerItems();
        new CommandManager(plugin);
        boosters = new Boosters();

        PlayerManager.get().checkPlayerData();
        LeaderboardManager.get().startTasks();
        ShopManager.get().registerShops();
        startSaveTask();

        boolean isPlaceholderHookRegistered = new PlaceholderHook(this).registerHook();

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this),this);
        //getLogger().info("version " + getDescription().getVersion() + " enabled!");
        Bukkit.getBossBars().hasNext()
    }

    @Override
    public void onDisable() {
        saveTask.cancel();
        closeAllLunixInvs();
        saveAll();
        getLogger().info("Saved "+savePending.size()+" Players.");
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

    public void startSaveTask() {
        if (saveTask != null) {
            //Cancel old task
            saveTask.cancel();
        }
        //Schedule save
        saveTask = Bukkit.getScheduler().runTaskTimer(this, this::saveAll, 5*60*20L,5*60*20L);
        this.getLogger().info("Buffered a save task to happen in 5 minutes.");
    }

    private void savePlayer(UUID player, boolean slient) {
        PMine mine = PMineManager.get().getPMine(player);
        if (mine != null) {
            mine.save();
        }
        LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(player);
        if (lunixPlayer != null) {
            lunixPlayer.save();
        }
    }

    private void saveAll() {
        for (UUID uuid : savePending) {
            savePlayer(uuid,true);
            savePending.removeIf(n -> (Bukkit.getPlayer(n) == null));
        }
        for (Gang gang : GangManager.get().getGangMap().values()) {
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

    public Random getRand() {
        return rand;
    }

    public Set<UUID> getSavePending() {
        return savePending;
    }
}
