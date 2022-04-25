package com.tvd12.ezyfoxserver.wrapper;

public class EzySimpleUserManager extends EzyAbstractUserManager {

    public EzySimpleUserManager(int maxUser) {
        super(maxUser);
    }

    public EzySimpleUserManager(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyAbstractUserManager.Builder<Builder> {

        @Override
        public EzyUserManager build() {
            return new EzySimpleUserManager(this);
        }
    }
}
