package com.tvd12.ezyfoxserver.creator;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzySimpleSessionCreator implements EzySessionCreator {

    @SuppressWarnings("rawtypes")
    protected final EzySessionManager sessionManager;
    protected final EzySessionManagementSetting sessionSetting;

    public EzySimpleSessionCreator(Builder builder) {
        this.sessionManager = builder.sessionManager;
        this.sessionSetting = builder.sessionSetting;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends EzySession> S create(EzyChannel channel) {
        S session = (S) newSession(channel);
        session.setActivated(true);
        session.setMaxIdleTime(sessionSetting.getSessionMaxIdleTime());
        session.setMaxWaitingTime(sessionSetting.getSessionMaxWaitingTime());
        return session;
    }

    protected EzySession newSession(EzyChannel channel) {
        return sessionManager.provideSession(channel);
    }

    public static class Builder implements EzyBuilder<EzySessionCreator> {

        @SuppressWarnings("rawtypes")
        private EzySessionManager sessionManager;
        private EzySessionManagementSetting sessionSetting;

        @SuppressWarnings("rawtypes")
        public Builder sessionManager(EzySessionManager sessionManager) {
            this.sessionManager = sessionManager;
            return this;
        }

        public Builder sessionSetting(EzySessionManagementSetting sessionSetting) {
            this.sessionSetting = sessionSetting;
            return this;
        }

        @Override
        public EzySessionCreator build() {
            return new EzySimpleSessionCreator(this);
        }
    }
}
