package com.tvd12.ezyfoxserver.setting;

public class EzyAppSettingBuilder extends EzyAbstractSettingBuilder<
    EzySimpleAppSetting, EzyAppSettingBuilder> {

    protected int maxUsers = 999999;

    public EzyAppSettingBuilder maxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
        return this;
    }

    @Override
    protected EzySimpleAppSetting newSetting() {
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        setting.setMaxUsers(maxUsers);
        return setting;
    }
}
