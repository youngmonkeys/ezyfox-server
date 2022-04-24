package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond;

public class EzySessionManagementSettingBuilder
    implements EzyBuilder<EzySimpleSessionManagementSetting> {

    protected long sessionMaxIdleTimeInSecond = 30;
    protected long sessionMaxWaitingTimeInSecond = 30;
    protected EzySimpleMaxRequestPerSecond sessionMaxRequestPerSecond = new EzySimpleMaxRequestPerSecond();

    public EzySessionManagementSettingBuilder sessionMaxIdleTimeInSecond(long sessionMaxIdleTimeInSecond) {
        this.sessionMaxIdleTimeInSecond = sessionMaxIdleTimeInSecond;
        return this;
    }

    public EzySessionManagementSettingBuilder sessionMaxWaitingTimeInSecond(long sessionMaxWaitingTimeInSecond) {
        this.sessionMaxWaitingTimeInSecond = sessionMaxWaitingTimeInSecond;
        return this;
    }

    public EzySessionManagementSettingBuilder sessionMaxRequestPerSecond(EzySimpleMaxRequestPerSecond sessionMaxRequestPerSecond) {
        this.sessionMaxRequestPerSecond = sessionMaxRequestPerSecond;
        return this;
    }

    @Override
    public EzySimpleSessionManagementSetting build() {
        EzySimpleSessionManagementSetting p = new EzySimpleSessionManagementSetting();
        p.setSessionMaxIdleTimeInSecond(sessionMaxIdleTimeInSecond);
        p.setSessionMaxWaitingTimeInSecond(sessionMaxWaitingTimeInSecond);
        p.setSessionMaxRequestPerSecond(sessionMaxRequestPerSecond);
        p.init();
        return p;
    }

    public static class EzyMaxRequestPerSecondBuilder
        implements EzyBuilder<EzySimpleMaxRequestPerSecond> {

        protected int value = 15;
        protected EzyMaxRequestPerSecondAction action = EzyMaxRequestPerSecondAction.DROP_REQUEST;

        public EzyMaxRequestPerSecondBuilder value(int value) {
            this.value = value;
            return this;
        }

        public EzyMaxRequestPerSecondBuilder action(EzyMaxRequestPerSecondAction action) {
            this.action = action;
            return this;
        }

        @Override
        public EzySimpleMaxRequestPerSecond build() {
            EzySimpleMaxRequestPerSecond p = new EzySimpleMaxRequestPerSecond();
            p.setValue(value);
            p.setAction(action);
            return p;
        }

    }

}
