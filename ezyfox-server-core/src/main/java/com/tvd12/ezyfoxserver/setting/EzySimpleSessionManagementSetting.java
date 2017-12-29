package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.ezyfoxserver.util.EzyInitable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "session-management")
public class EzySimpleSessionManagementSetting implements EzySessionManagementSetting, EzyInitable {

    protected long sessionMaxIdleTime = 30 * 1000;

    @XmlElement(name = "session-max-idle-time")
    protected long sessionMaxIdleTimeInSecond = 30;
    
    protected long sessionMaxWaitingTime = 30 * 1000;
    
    @XmlElement(name = "session-max-waiting-time")
    protected long sessionMaxWaitingTimeInSecond = 30;
    
    @XmlElement(name = "session-allow-reconnect")
    protected boolean sessionAllowReconnect = true;
    
    @XmlElement(name = "session-max-request-per-second")
    protected EzySimpleEzyMaxRequestPerSecond sessionMaxRequestPerSecond = new EzySimpleEzyMaxRequestPerSecond();
    
    @Override
    public void init() {
        this.sessionMaxIdleTime = sessionMaxIdleTimeInSecond * 1000;
        this.sessionMaxWaitingTime = sessionMaxWaitingTimeInSecond * 1000;
    }
    
    @Getter
    @ToString
    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement(name = "session-max-request-per-second")
    public static class EzySimpleEzyMaxRequestPerSecond implements EzyMaxRequestPerSecond {
        
        @XmlElement(name = "value")
        protected int value = 15;
        
        @XmlElement(name = "action")
        protected EzyMaxRequestPerSecondAction action = EzyMaxRequestPerSecondAction.DROP_REQUEST;
    }
    
}
