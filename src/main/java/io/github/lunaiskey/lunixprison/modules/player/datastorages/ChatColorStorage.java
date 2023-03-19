package io.github.lunaiskey.lunixprison.modules.player.datastorages;

import io.github.lunaiskey.lunixprison.modules.player.LunixPlayer;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.ChatColorSelectType;
import io.github.lunaiskey.lunixprison.modules.player.chatcolor.LunixChatColor;
import io.github.lunaiskey.lunixprison.modules.player.inventories.ChatNameTextColorGUI;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

public class ChatColorStorage {
    //Name Color in chat.
    private LunixChatColor selectedNameColor;
    private Set<LunixChatColor> selectedNameFormats;
    private Set<LunixChatColor> unlockedNameColorAndFormats;

    //Text Color in chat.
    private LunixChatColor selectedTextColor;
    private Set<LunixChatColor> unlockedTextColors;

    public ChatColorStorage(LunixChatColor selectedNameColor, Set<LunixChatColor> selectedNameFormats, Set<LunixChatColor> unlockedNameColorAndFormats, LunixChatColor selectedTextColor, Set<LunixChatColor> unlockedTextColors) {
        this.selectedNameColor = selectedNameColor;

        this.selectedNameFormats = Objects.requireNonNullElseGet(selectedNameFormats, HashSet::new);
        this.unlockedNameColorAndFormats = Objects.requireNonNullElseGet(unlockedNameColorAndFormats, HashSet::new);
        this.selectedTextColor = selectedTextColor;
        this.unlockedTextColors = Objects.requireNonNullElseGet(unlockedTextColors, HashSet::new);
    }

    public ChatColorStorage() {
        this(null,null,null,null,null);
    }

    public LunixChatColor getSelectedNameColor() {
        return selectedNameColor;
    }

    public Set<LunixChatColor> getSelectedNameFormats() {
        return selectedNameFormats;
    }

    public Set<LunixChatColor> getUnlockedNameColorAndFormats() {
        return unlockedNameColorAndFormats;
    }

    public LunixChatColor getSelectedTextColor() {
        return selectedTextColor;
    }

    public Set<LunixChatColor> getUnlockedTextColors() {
        return unlockedTextColors;
    }

    public boolean isNameColorUnlocked(LunixChatColor lunixChatColor) {
        return unlockedNameColorAndFormats.contains(lunixChatColor);
    }

    public boolean isTextColorUnlocked(LunixChatColor lunixChatColor) {
        return unlockedTextColors.contains(lunixChatColor);
    }

    public void setSelectedNameColor(LunixChatColor selectedNameColor) {
        this.selectedNameColor = selectedNameColor;
    }

    public void setSelectedNameFormats(Set<LunixChatColor> selectedNameFormats) {
        this.selectedNameFormats = selectedNameFormats;
    }

    public void setUnlockedNameColorAndFormats(Set<LunixChatColor> unlockedNameColorAndFormats) {
        this.unlockedNameColorAndFormats = unlockedNameColorAndFormats;
    }

    public void setSelectedTextColor(LunixChatColor selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public void setUnlockedTextColors(Set<LunixChatColor> unlockedTextColors) {
        this.unlockedTextColors = unlockedTextColors;
    }

    public void addSelectedNameFormat(LunixChatColor color) {
        selectedNameFormats.add(color);
    }

    public void removeSelectedNameFormat(LunixChatColor color) {
        selectedNameFormats.remove(color);
    }

    public void addUnlockedColor(LunixChatColor color, ChatColorSelectType type) {
        switch (type) {
            case NAME -> unlockedNameColorAndFormats.add(color);
            case TEXT -> unlockedTextColors.add(color);
        }
    }

    public void removeUnlockedColor(LunixChatColor color, ChatColorSelectType type) {
        switch (type) {
            case NAME -> unlockedNameColorAndFormats.remove(color);
            case TEXT -> unlockedTextColors.remove(color);
        }
    }

    public String getNameColors() {
        StringBuilder builder = new StringBuilder();
        LunixChatColor color = getSelectedNameColor();
        if (color != null) {
            builder.append(color.getColor());
        }
        for (LunixChatColor formats : getSelectedNameFormats()) {
            builder.append(formats.getColor());
        }
        return builder.toString();
    }

    public String getNameColorsAndThis(LunixChatColor toAdd) {
        StringBuilder builder = new StringBuilder();
        LunixChatColor color = getSelectedNameColor();
        if (toAdd.isColor()) {
            builder.append(toAdd.getColor());
        } else {
            if (color != null) {
                builder.append(color.getColor());
            }
        }
        for (LunixChatColor formats : getSelectedNameFormats()) {
            builder.append(formats.getColor());
        }
        if (toAdd.isFormat() && !getSelectedNameFormats().contains(toAdd)) {
            builder.append(toAdd.getColor());
        }
        return builder.toString();
    }

    public String getTextColors() {
        LunixChatColor color = getSelectedTextColor() == null || !getSelectedNameColor().isColor() ? LunixChatColor.WHITE : getSelectedTextColor();
        return color.getColor()+"";
    }

}
