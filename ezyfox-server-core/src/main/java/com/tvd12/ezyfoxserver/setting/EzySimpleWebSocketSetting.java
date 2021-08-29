package com.tvd12.ezyfoxserver.setting;

import java.util.Map;

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
@XmlRootElement(name = "web-socket")
public class EzySimpleWebSocketSetting extends EzyAbstractSocketSetting implements EzyWebSocketSetting {
    
    @XmlElement(name = "ssl-port")
    protected int sslPort;
    
    @XmlElement(name = "max-frame-size")
    protected int maxFrameSize;
    
    @XmlElement(name = "writer-thread-pool-size")
    protected int writerThreadPoolSize;
    
    @XmlElement(name = "ssl-config")
    protected EzySimpleSslConfigSetting sslConfig;
    
    @XmlElement(name = "management-enable")
    protected boolean managementEnable;
    
    public EzySimpleWebSocketSetting() {
        super();
        setPort(2208);
        setSslPort(2812);
        setMaxFrameSize(4096);
        setWriterThreadPoolSize(8);
        setSslConfig(new EzySimpleSslConfigSetting());
        setCodecCreator("com.tvd12.ezyfox.codec.JacksonCodecCreator");
    }
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = super.toMap();
        map.put("maxFrameSize", maxFrameSize);
        map.put("sslPort", sslPort);
        map.put("sslActive", sslActive);
        map.put("sslConfig", sslConfig.toMap());
        map.put("managementEnable", managementEnable);
        map.put("writerThreadPoolSize", writerThreadPoolSize);
        return map;
    }
}
