package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tvd12.ezyfox.util.EzyInitable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "user-management")
public class EzySimpleUserManagementSetting implements EzyUserManagementSetting, EzyInitable {

    protected long userMaxIdleTime = 0;
    
    @XmlElement(name = "user-max-idle-time")
    protected long userMaxIdleTimeInSecond = 0;
    
    @XmlElement(name = "max-session-per-user")
    protected int maxSessionPerUser = 5;
    
    @XmlElement(name = "allow-guest-login")
    protected boolean allowGuestLogin = true;
    
    @XmlElement(name = "allow-change-session")
    protected boolean allowChangeSession = true;
    
    @XmlElement(name = "guest-name-prefix")
    protected String guestNamePrefix = "Guest#";
    
    @XmlElement(name = "user-name-pattern")
    protected String userNamePattern = "^[a-z0-9_.]{3,36}$";
    
    @Override
    public void init() {
        this.userMaxIdleTime = userMaxIdleTimeInSecond * 1000;
    }
    
}
