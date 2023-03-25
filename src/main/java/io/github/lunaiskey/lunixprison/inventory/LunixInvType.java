package io.github.lunaiskey.lunixprison.inventory;

import io.github.lunaiskey.lunixprison.modules.armor.inventories.ArmorGUI;
import io.github.lunaiskey.lunixprison.modules.armor.inventories.ArmorUpgradeGUI;
import io.github.lunaiskey.lunixprison.modules.items.gui.RenameTagConfirmGUI;
import io.github.lunaiskey.lunixprison.modules.leaderboards.inventories.LeaderboardGUI;
import io.github.lunaiskey.lunixprison.modules.mines.inventories.*;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeEnchantAddLevelsGUI;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeEnchantGUI;
import io.github.lunaiskey.lunixprison.modules.pickaxe.inventories.PickaxeEnchantToggleGUI;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.inventories.*;
import io.github.lunaiskey.lunixprison.modules.shop.inventories.ShopGUI;
import org.bukkit.block.PistonMoveReaction;

public enum LunixInvType {
    PMINE_MAIN(new PMineGUI()),
    PMINE_BLOCKS(new PMineBlocksGUI()),
    PMINE_UPGRADES(new PMineUpgradesGUI()),
    PMINE_PUBLIC_MINES(new PMinePublicGUI()),
    PMINE_SETTINGS(new PMineSettingsGUI()),
    PICKAXE_ENCHANTS(new PickaxeEnchantGUI()),
    PICKAXE_ENCHANTS_ADD_LEVELS(new PickaxeEnchantAddLevelsGUI()),
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
    CHAT_NAME_COLOR_SELECT(new ChatNameTextColorGUI(ChatColorSelectType.NAME)),
    CHAT_TEXT_COLOR_SELECT(new ChatNameTextColorGUI(ChatColorSelectType.TEXT)),

    SHOP(new ShopGUI()),
    ;

    private final LunixInventory inventory;

    LunixInvType(LunixInventory inventory) {
        this.inventory = inventory;
    }

    public LunixInventory getInventory() {
        return inventory;
    }
}
