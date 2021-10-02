package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyUserManagementSettingBuilder 
        implements EzyBuilder<EzySimpleUserManagementSetting> {
    
    protected long userMaxIdleTimeInSecond = 0;
    protected int maxSessionPerUser = 5;
    protected boolean allowGuestLogin = false;
    protected boolean allowChangeSession = true;
    protected String guestNamePrefix = "Guest#";
    protected String userNamePattern = "^[a-zA-Z0-9_.#]{3,64}$";

    /**
     * Set live time to keep an user when this user has no connected sessions
     * 
     * @param userMaxIdleTimeInSecond the max idle time of user in second
     * @return 
     */
    public EzyUserManagementSettingBuilder userMaxIdleTimeInSecond(long userMaxIdleTimeInSecond) {
        this.userMaxIdleTimeInSecond = userMaxIdleTimeInSecond;
        return this;
    }

    /**
     * Set maximum number of sessions per user, 
     * If the maximum = 1, we need care about {@code allowChangeSession}
     * 
     * @param maxSessionPerUser the maximum number of sessions per user
     * @return this pointer for chaining
     */
    public EzyUserManagementSettingBuilder maxSessionPerUser(int maxSessionPerUser) {
        this.maxSessionPerUser = maxSessionPerUser;
        return this;
    }

    /**
     * When login error, allowGuestLogin = true allow server create a guest user
     * 
     * @param allowGuestLogin allow guest login or not
     * @return this pointer for chaining
     */
    public EzyUserManagementSettingBuilder allowGuestLogin(boolean allowGuestLogin) {
        this.allowGuestLogin = allowGuestLogin;
        return this;
    }

    /**
     * Allow change session of an user when max sessions per user is 1
     * If allowChangeSession = true, replaced session will be disconnected
     * If allowChangeSession = false, incoming session will get login error: MAXIMUM_SESSION 
     * 
     * @param allowChangeSession allow change session or not
     * @return this pointer for chaining
     */
    public EzyUserManagementSettingBuilder allowChangeSession(boolean allowChangeSession) {
        this.allowChangeSession = allowChangeSession;
        return this;
    }

    /**
     * Set prefix for guest user when allowGuestLogin = true
     * 
     * @param guestNamePrefix prefix name for guest
     * @return this pointer for chaining
     */
    public EzyUserManagementSettingBuilder guestNamePrefix(String guestNamePrefix) {
        this.guestNamePrefix = guestNamePrefix;
        return this;
    }

    /**
     * Set username pattern for login pre-checking
     * 
     * @param userNamePattern the username pattern
     * @return this pointer for chaining
     */
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
