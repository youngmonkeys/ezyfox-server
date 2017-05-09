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
@XmlRootElement(name = "administrator")
public class EzySimpleAdminSetting implements EzyAdminSetting {

    @XmlElement(name = "username")
    protected String username;
    
    @XmlElement(name = "password")
    protected String password;
    
    @XmlElement(name = "api-access-token")
    protected String apiAccessToken;
    
}
