package io.github.lunaiskey.lunixprison.pickaxe;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

    public static Set<EnchantType> getSortedSet() {
        Set<EnchantType> type = new LinkedHashSet<>();
        type.add(EFFICIENCY);
        type.add(FORTUNE);
        type.add(HASTE);
        type.add(SPEED);
        type.add(JUMP_BOOST);
        type.add(NIGHT_VISION);
        type.add(MINE_BOMB);
        type.add(JACK_HAMMER);
        type.add(STRIKE);
        type.add(EXPLOSIVE);
        //type.add(HORIZONTAL_BREAK);
        //type.add(VERTICAL_BREAK);
        type.add(NUKE);
        type.add(GEM_FINDER);
        type.add(KEY_FINDER);
        type.add(LOOT_FINDER);
        type.add(XP_BOOST);
        return type;
    }

    public static Map<EnchantType,Integer> getDefaultMap() {
        Map<EnchantType,Integer> map = new HashMap<>();
        map.put(EnchantType.EFFICIENCY, 100);
        map.put(EnchantType.HASTE,6);
        map.put(EnchantType.SPEED,3);
        map.put(EnchantType.JUMP_BOOST,3);
        map.putIfAbsent(EnchantType.NIGHT_VISION,1);
        map.putIfAbsent(EnchantType.FORTUNE,5);
        return map;
    }
}
