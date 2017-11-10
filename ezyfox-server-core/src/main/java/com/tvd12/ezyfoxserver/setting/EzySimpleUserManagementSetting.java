package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "user-management")
public class EzySimpleUserManagementSetting implements EzyUserManagementSetting {

    @XmlElement(name = "user-max-idle-time")
    protected long userMaxIdleTime = 30 * 1000;
    
    @XmlElement(name = "max-session-per-user")
    protected int maxSessionPerUser = 5;
    
    @XmlElement(name = "allow-guest-login")
    protected boolean allowGuestLogin = true;
    
    @XmlElement(name = "guest-name-prefix")
    protected String guestNamePrefix = "Guest#";
    
    @XmlElement(name = "user-name-pattern")
    protected String userNamePattern = "^[a-z0-9_.]{3,36}$";
    
}
