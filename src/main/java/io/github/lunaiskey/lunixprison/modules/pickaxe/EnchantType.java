package io.github.lunaiskey.lunixprison.modules.pickaxe;

import io.github.lunaiskey.lunixprison.modules.pickaxe.enchants.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public enum EnchantType {
    EFFICIENCY(Efficiency.class,11,100),
    FORTUNE(Fortune.class,20,5),
    HASTE(Haste.class,12,6),
    SPEED(Speed.class,13,3),
    JUMP_BOOST(JumpBoost.class,14,3),
    NIGHT_VISION(NightVision.class,15,1),
    MINE_BOMB(MineBomb.class,24),
    JACK_HAMMER(JackHammer.class,21),
    STRIKE(Strike.class,22),
    EXPLOSIVE(Explosive.class,23),
    NUKE(Nuke.class,29),
    GEM_FINDER(GemFinder.class,30),
    KEY_FINDER(KeyFinder.class,31),
    LOOT_FINDER(LootFinder.class,32),
    XP_BOOST(XPBoost.class,33),
    //HORIZONTAL_BREAK,
    //VERTICAL_BREAK,
    ;

    private final int enchantGUISlot;
    private final int minLevel;
    private final Class<?> lunixEnchantClass;
    private LunixEnchant lunixEnchant;
    private static final Map<EnchantType,Integer> DEFAULT_ENCHANTS = new HashMap<>();
    private static final Map<Integer,EnchantType> INTEGER_ENCHANT_TYPE_MAP = new HashMap<>();

    EnchantType(Class<?> lunixEnchantClass, int enchantGUISlot, int minLevel) {
        this.lunixEnchantClass = lunixEnchantClass;
        this.enchantGUISlot = enchantGUISlot;
        this.minLevel = minLevel;
    }

    EnchantType(Class<?> lunixEnchantClass, int enchantGUISlot) {
        this(lunixEnchantClass, enchantGUISlot, 0);
    }



    public LunixEnchant getLunixEnchant() {
        return lunixEnchant;
    }

    static {
        for (EnchantType type : values()) {
            try {
                //Fixes LunixEnchant objects having EnchantType reference being null when object created, is a weird fix might change.
                type.lunixEnchant = (LunixEnchant) type.lunixEnchantClass.getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {
            }
            if (type.minLevel > 0) {
                DEFAULT_ENCHANTS.put(type,type.minLevel);
            }
            INTEGER_ENCHANT_TYPE_MAP.put(type.enchantGUISlot,type);
        }
    }



    public static Map<EnchantType,Integer> getDefaultEnchants() {
        return DEFAULT_ENCHANTS;
    }

    public static EnchantType getEnchantFromSlot(int slot) {
        return INTEGER_ENCHANT_TYPE_MAP.get(slot);
    }
}
