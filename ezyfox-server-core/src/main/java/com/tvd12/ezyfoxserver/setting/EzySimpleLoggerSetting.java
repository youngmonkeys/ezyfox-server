package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "logger")
public class EzySimpleLoggerSetting implements EzyLoggerSetting {

    @XmlElement(name = "ignored-commands")
    protected EzySimpleIgnoredCommandsSetting ignoredCommands
        = new EzySimpleIgnoredCommandsSetting();

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("ignoredCommands", ignoredCommands.getCommands());
        return map;
    }

    @Getter
    @ToString
    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement(name = "ignored-commands")
    public static class EzySimpleIgnoredCommandsSetting implements EzyIgnoredCommandsSetting {

        protected Set<EzyConstant> commands =
            Sets.newHashSet(EzyCommand.PING, EzyCommand.PONG);

        @XmlElement(name = "command")
        public void setCommand(String string) {
            commands.add(EzyCommand.valueOf(string));
        }
    }
}
