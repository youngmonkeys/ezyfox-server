package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.constant.EzyConstant;

public enum EzyCustomEventType implements EzyConstant {

    CUSTOM_EVENT(100);

    private final int id;

    EzyCustomEventType(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return toString();
    }
}
