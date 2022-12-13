package io.github.lunaiskey.lunixprison.items;

import java.util.ArrayList;
import java.util.List;

public enum ItemID {

    VOUCHER(false),
    PYREX_PICKAXE,
    PLAYER_MENU,

    COMMON_GEODE,
    UNCOMMON_GEODE,
    RARE_GEODE,
    EPIC_GEODE,
    LEGENDARY_GEODE,

    DIAMOND_GEMSTONE,
    RUBY_GEMSTONE,
    EMERALD_GEMSTONE,
    SAPPHIRE_GEMSTONE,
    AMBER_GEMSTONE,
    TOPAZ_GEMSTONE,
    JADE_GEMSTONE,
    OPAL_GEMSTONE,
    JASPER_GEMSTONE,
    AMETHYST_GEMSTONE,

    ROCKY_COMPONENT(false),
    NATURE_COMPONENT(false),
    FERROUS_COMPONENT(false),
    CARBON_COMPONENT(false),

    COMMON_TOKEN_POUCH,
    UNCOMMON_TOKEN_POUCH,
    RARE_TOKEN_POUCH,
    EPIC_TOKEN_POUCH,
    LEGENDARY_TOKEN_POUCH,

    COMMON_GEM_POUCH,
    UNCOMMON_GEM_POUCH,
    RARE_GEM_POUCH,
    EPIC_GEM_POUCH,
    LEGENDARY_GEM_POUCH,

    KEY_SACK(false),
    RENAME_TAG(false),
    SEX_ITEM,
    BOOSTER,
    ;

    ItemID() {
        this.visible = true;
    }

    ItemID(boolean visible) {
        this.visible = visible;
    }

    private final boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public static List<ItemID> getVisibleIDs() {
        List<ItemID> list = new ArrayList<>();
        for (ItemID id : ItemID.values()) {
            if (id.isVisible()) {
                list.add(id);
            }
        }
        return list;
    }
}
