package io.github.lunaiskey.lunixprison.modules.pickaxe;

import java.util.*;

public enum EnchantType {
    EFFICIENCY,
    FORTUNE,
    HASTE,
    SPEED,
    NIGHT_VISION,
    JUMP_BOOST,
    MINE_BOMB,
    JACK_HAMMER,
    GEM_FINDER,
    KEY_FINDER,
    LOOT_FINDER,
    STRIKE,
    EXPLOSIVE,
    XP_BOOST,
    NUKE,
    //HORIZONTAL_BREAK,
    //VERTICAL_BREAK,
    ;

    private static final Set<EnchantType> SORTED_ENCHANTS = new LinkedHashSet<>();
    private static final Map<EnchantType,Integer> DEFAULT_ENCHANTS = new HashMap<>();

    static {
        SORTED_ENCHANTS.addAll(List.of(
                EFFICIENCY,FORTUNE,HASTE,SPEED,JUMP_BOOST,NIGHT_VISION,
                MINE_BOMB,JACK_HAMMER,STRIKE,EXPLOSIVE,NUKE,
                GEM_FINDER,KEY_FINDER,LOOT_FINDER,XP_BOOST
        ));
        DEFAULT_ENCHANTS.put(EnchantType.EFFICIENCY, 100);
        DEFAULT_ENCHANTS.put(EnchantType.HASTE,6);
        DEFAULT_ENCHANTS.put(EnchantType.SPEED,3);
        DEFAULT_ENCHANTS.put(EnchantType.JUMP_BOOST,3);
        DEFAULT_ENCHANTS.put(EnchantType.NIGHT_VISION,1);
        DEFAULT_ENCHANTS.put(EnchantType.FORTUNE,5);
    }


    public static Set<EnchantType> getSortedEnchants() {
        return SORTED_ENCHANTS;
    }

    public static Map<EnchantType,Integer> getDefaultEnchants() {
        return DEFAULT_ENCHANTS;
    }

    public static EnchantType getEnchantFromSlot(int slot) {
        return switch (slot) {
            case 11 -> EFFICIENCY;
            case 12 -> HASTE;
            case 13 -> SPEED;
            case 14 -> JUMP_BOOST;
            case 15 -> NIGHT_VISION;
            case 20 -> FORTUNE;
            case 21 -> JACK_HAMMER;
            case 22 -> STRIKE;
            case 23 -> EXPLOSIVE;
            case 24 -> MINE_BOMB;
            case 29 -> NUKE;
            case 30 -> GEM_FINDER;
            case 31 -> KEY_FINDER;
            case 32 -> LOOT_FINDER;
            //case 33 -> XP_BOOST;
            default -> null;
        };
    }
}
