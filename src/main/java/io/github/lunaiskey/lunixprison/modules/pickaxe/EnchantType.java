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
}
