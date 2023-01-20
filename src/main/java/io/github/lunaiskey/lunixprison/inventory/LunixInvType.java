package io.github.lunaiskey.lunixprison.inventory;

import io.github.lunaiskey.lunixprison.modules.items.gui.RenameTagConfirmGUI;
import io.github.lunaiskey.lunixprison.modules.leaderboards.LeaderboardGUI;
import io.github.lunaiskey.lunixprison.modules.mines.inventories.*;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeAddLevelsGUI;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeEnchantGUI;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeEnchantToggleGUI;
import io.github.lunaiskey.lunixprison.modules.player.inventories.*;

public enum LunixInvType {
    PMINE_MAIN(new PMineGUI()),
    PMINE_BLOCKS(new PMineBlocksGUI()),
    PMINE_UPGRADES(new PMineUpgradesGUI()),
    PMINE_PUBLIC_MINES(new PMinePublicGUI()),
    PMINE_SETTINGS(new PMineSettingsGUI()),
    PICKAXE_ENCHANTS(new PickaxeEnchantGUI()),
    PICKAXE_ENCHANTS_ADD_LEVELS(new PickaxeAddLevelsGUI()),
    PICKAXE_ENCHANTS_TOGGLE(new PickaxeEnchantToggleGUI()),
    PERSONAL_BOOSTER(new PersonalBoosterGUI()),
    ARMOR(new ArmorGUI()),
    ARMOR_UPGRADES(new ArmorUpgradeGUI()),
    GEMSTONES(new GemStoneGUI()),
    VIEW_PLAYER(new ViewPlayerGUI()),
    PLAYER_MENU(new PlayerMenuGUI()),
    LEADERBOARDS(new LeaderboardGUI()),
    RENAME_TAG_CONFIRM(new RenameTagConfirmGUI()),
    CASHBACK(new CashbackGUI()),
    ;

    private final LunixInventory inventory;

    LunixInvType(LunixInventory inventory) {
        this.inventory = inventory;
    }

    public LunixInventory getInventory() {
        return inventory;
    }
}