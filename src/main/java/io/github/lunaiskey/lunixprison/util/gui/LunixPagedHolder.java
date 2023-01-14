package io.github.lunaiskey.lunixprison.util.gui;

import io.github.lunaiskey.lunixprison.util.gui.LunixHolder;
import io.github.lunaiskey.lunixprison.util.gui.LunixInvType;

public class LunixPagedHolder extends LunixHolder {

    private int page = 1;

    public LunixPagedHolder(String name, int size, LunixInvType invType) {
        super(name, size, invType);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
