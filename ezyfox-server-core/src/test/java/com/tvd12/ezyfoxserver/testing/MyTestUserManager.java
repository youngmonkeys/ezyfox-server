package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;

public class MyTestUserManager extends EzyZoneUserManagerImpl {

    protected MyTestUserManager(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyZoneUserManagerImpl.Builder {
        @Override
        public MyTestUserManager build() {
            return new MyTestUserManager(this);
        }
    }

}
