package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyAdminSettingBuilder implements EzyBuilder<EzySimpleAdminSetting> {

    protected String username;
    protected String password;
    protected String accessToken;

    public EzyAdminSettingBuilder username(String username) {
        this.username = username;
        return this;
    }

    public EzyAdminSettingBuilder password(String password) {
        this.password = password;
        return this;
    }

    public EzyAdminSettingBuilder accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    @Override
    public EzySimpleAdminSetting build() {
        EzySimpleAdminSetting p = new EzySimpleAdminSetting();
        p.setUsername(username);
        p.setPassword(password);
        p.setAccessToken(accessToken);
        return p;
    }
}

