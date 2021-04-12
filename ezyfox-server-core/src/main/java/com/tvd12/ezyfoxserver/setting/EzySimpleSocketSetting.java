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
@XmlRootElement(name = "socket")
public class EzySimpleSocketSetting extends EzyAbstractSocketSetting implements EzySocketSetting {
    
    @XmlElement(name = "max-request-size")
    protected int maxRequestSize;
    
    @XmlElement(name = "tcp-no-delay")
    protected boolean tcpNoDelay;
    
    @XmlElement(name = "writer-thread-pool-size")
    protected int writerThreadPoolSize;
    
    public EzySimpleSocketSetting() {
        super();
        setPort(3005);
        setMaxRequestSize(4096);
        setWriterThreadPoolSize(8);
        setCodecCreator("com.tvd12.ezyfox.codec.MsgPackCodecCreator");
    }
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = super.toMap();
        map.put("tcpNoDelay", tcpNoDelay);
        map.put("maxRequestSize", maxRequestSize);
        map.put("writerThreadPoolSize", writerThreadPoolSize);
        return map;
    }
}
