package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "logger")
public class EzySimpleLoggerSetting implements EzyLoggerSetting {

    @XmlElement(name = "ignored-commands")
    protected EzySimpleIgnoredCommandsSetting ignoredCommands 
            = new EzySimpleIgnoredCommandsSetting();
    
    @Getter
    @ToString
    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement(name = "ignored-commands")
    public static class EzySimpleIgnoredCommandsSetting implements EzyIgnoredCommandsSetting {
        
        protected Set<EzyConstant> commands = 
                Sets.newHashSet(EzyCommand.PING, EzyCommand.PONG);
        
        @XmlElement(name="command")
        public void setCommand(String string) {
            commands.add(EzyCommand.valueOf(string));
        }
    }
    
}
