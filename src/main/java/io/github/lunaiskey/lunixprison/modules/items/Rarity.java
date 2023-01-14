package io.github.lunaiskey.lunixprison.modules.items;

public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    ;

    public String getColorCode() {
        return switch (this){
            case COMMON -> "&f";
            case UNCOMMON -> "&a";
            case RARE -> "&9";
            case EPIC -> "&5";
            case LEGENDARY -> "&6";
            case MYTHIC -> "&d";
        };
    }
}
