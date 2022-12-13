package io.github.lunaiskey.lunixprison;

import io.github.lunaiskey.lunixprison.util.StringUtil;

public enum Messages {
    NO_PERMISSION("&cNo Permission."),
    ;


    private final String text;
    Messages(String text) {
        this.text = text;
    }

    public String getText() {
        return StringUtil.color(text);
    }

    public String getRawText() {
        return text;
    }
}
