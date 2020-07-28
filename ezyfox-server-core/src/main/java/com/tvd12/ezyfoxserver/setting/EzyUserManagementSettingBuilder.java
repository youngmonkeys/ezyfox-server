package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyUserManagementSettingBuilder 
        implements EzyBuilder<EzySimpleUserManagementSetting> {
    
    protected long userMaxIdleTimeInSecond = 0;
    protected int maxSessionPerUser = 5;
    protected boolean allowGuestLogin = false;
    protected boolean allowChangeSession = true;
    protected String guestNamePrefix = "Guest#";
    protected String userNamePattern = "^[a-z0-9_.]{3,36}$";

    public EzyUserManagementSettingBuilder userMaxIdleTimeInSecond(long userMaxIdleTimeInSecond) {
        this.userMaxIdleTimeInSecond = userMaxIdleTimeInSecond;
        return this;
    }

    public EzyUserManagementSettingBuilder maxSessionPerUser(int maxSessionPerUser) {
        this.maxSessionPerUser = maxSessionPerUser;
        return this;
    }

    public EzyUserManagementSettingBuilder allowGuestLogin(boolean allowGuestLogin) {
        this.allowGuestLogin = allowGuestLogin;
        return this;
    }

    public EzyUserManagementSettingBuilder allowChangeSession(boolean allowChangeSession) {
        this.allowChangeSession = allowChangeSession;
        return this;
    }

    public EzyUserManagementSettingBuilder guestNamePrefix(String guestNamePrefix) {
        this.guestNamePrefix = guestNamePrefix;
        return this;
    }

    public EzyUserManagementSettingBuilder userNamePattern(String userNamePattern) {
        this.userNamePattern = userNamePattern;
        return this;
    }

    @Override
    public EzySimpleUserManagementSetting build() {
        EzySimpleUserManagementSetting p = new EzySimpleUserManagementSetting();
        p.setUserMaxIdleTimeInSecond(userMaxIdleTimeInSecond);
        p.setMaxSessionPerUser(maxSessionPerUser);
        p.setAllowGuestLogin(allowGuestLogin);
        p.setAllowChangeSession(allowChangeSession);
        p.setGuestNamePrefix(guestNamePrefix);
        p.setUserNamePattern(userNamePattern);
        return p;
    }

}
