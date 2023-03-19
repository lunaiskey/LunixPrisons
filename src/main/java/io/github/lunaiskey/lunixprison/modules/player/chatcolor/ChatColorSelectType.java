package io.github.lunaiskey.lunixprison.modules.player.chatcolor;

import io.github.lunaiskey.lunixprison.inventory.LunixInvType;

public enum ChatColorSelectType {
    NAME("Select Display Name Color", LunixInvType.CHAT_NAME_COLOR_SELECT),
    TEXT("Select Chat Text Color",LunixInvType.CHAT_TEXT_COLOR_SELECT),
    ;

    private LunixInvType type;
    private String title;

    ChatColorSelectType(String title, LunixInvType type) {
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public LunixInvType getType() {
        return type;
    }

    public String getName() {
        return switch (this) {
            case NAME -> "Name";
            case TEXT -> "Chat";
        };
    }

    public String getVoucherGetPerm() {
        return switch (this) {
            case NAME -> "lunix.chatcolor.name.voucher";
            case TEXT -> "lunix.chatcolor.text.voucher";
        };
    }
}
