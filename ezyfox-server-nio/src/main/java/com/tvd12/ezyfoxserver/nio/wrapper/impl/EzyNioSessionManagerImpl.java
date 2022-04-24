package com.tvd12.ezyfoxserver.nio.wrapper.impl;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyNioSessionFactory;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import static com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting.EzyMaxRequestPerSecond;

public class EzyNioSessionManagerImpl 
        extends EzySimpleSessionManager<EzyNioSession>
        implements EzyNioSessionManager {

    protected EzyNioSessionManagerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzySimpleSessionManager.Builder<EzyNioSession> {

        protected EzyMaxRequestPerSecond maxRequestPerSecond;

        public Builder maxRequestPerSecond(EzyMaxRequestPerSecond maxRequestPerSecond) {
            this.maxRequestPerSecond = maxRequestPerSecond;
            return this;
        }

        @Override
        public EzyNioSessionManagerImpl build() {
            return new EzyNioSessionManagerImpl(this);
        }

        @Override
        protected EzyObjectFactory<EzyNioSession> newObjectFactory() {
            EzyNioSessionFactory factory = new EzyNioSessionFactory();
            factory.setMaxRequestPerSecond(maxRequestPerSecond);
            return factory;
        }
    }


}
