package io.github.lunaiskey.lunixprison.modules.items;

public enum PouchRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    ;

    public String getRarityString() {
        return switch (this){
            case COMMON -> "&fCommon";
            case UNCOMMON -> "&aUncommon";
            case RARE -> "&9Rare";
            case EPIC -> "&5Epic";
            case LEGENDARY -> "&6Legendary";
        };
    }

    public ItemID getGemItemID() {
        return switch(this) {
            case COMMON -> ItemID.COMMON_GEM_POUCH;
            case UNCOMMON -> ItemID.UNCOMMON_GEM_POUCH;
            case RARE -> ItemID.RARE_GEM_POUCH;
            case EPIC -> ItemID.EPIC_GEM_POUCH;
            case LEGENDARY -> ItemID.LEGENDARY_GEM_POUCH;
        };
    }

    public ItemID getTokenItemID() {
        return switch(this) {
            case COMMON -> ItemID.COMMON_TOKEN_POUCH;
            case UNCOMMON -> ItemID.UNCOMMON_TOKEN_POUCH;
            case RARE -> ItemID.RARE_TOKEN_POUCH;
            case EPIC -> ItemID.EPIC_TOKEN_POUCH;
            case LEGENDARY -> ItemID.LEGENDARY_TOKEN_POUCH;
        };
    }
}
