package io.github.lunaiskey.lunixprison.modules.boosters;

import io.github.lunaiskey.lunixprison.LunixPrison;
import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.PlayerManager;
import io.github.lunaiskey.lunixprison.modules.player.inventories.PersonalBoosterGUI;
import io.github.lunaiskey.lunixprison.inventory.LunixHolder;
import io.github.lunaiskey.lunixprison.inventory.LunixInvType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class Boosters {

    public Boosters() {
        scheduleTask();
    }

    private void scheduleTask() {
        Bukkit.getScheduler().runTaskTimer(LunixPrison.getPlugin(),() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                LunixPlayer lunixPlayer = PlayerManager.get().getPlayerMap().get(p.getUniqueId());
                if (lunixPlayer != null) {
                    List<Booster> boosters = lunixPlayer.getBoosters();
                    if (boosters.size() > 0) {
                        for (int i = boosters.size()-1; i>=0;i--) {
                            Booster booster = boosters.get(i);
                            if (!booster.isActive()) {
                                continue;
                            }
                            if (System.currentTimeMillis() > booster.getStartTime()+(booster.getLength()*1000L)) {
                                boosters.remove(i);
                            }
                        }
                    }
                    Inventory inv = p.getOpenInventory().getTopInventory();
                    if (inv.getHolder() instanceof LunixHolder) {
                        LunixHolder holder = (LunixHolder) inv.getHolder();
                        if (holder.getInvType() == LunixInvType.PERSONAL_BOOSTER) {
                            new PersonalBoosterGUI().updateGUI(p);
                        }
                    }
                }
            }

        },0L,20L);
    }
}
