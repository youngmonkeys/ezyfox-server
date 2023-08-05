package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.constant.SslType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "socket")
public class EzySimpleSocketSetting
    extends EzyAbstractSocketSetting
    implements EzySocketSetting {

    @XmlElement(name = "ssl-type")
    protected SslType sslType;

    @XmlElement(name = "ssl-handshake-timeout")
    protected int sslHandshakeTimeout;

    @XmlElement(name = "max-request-size")
    protected int maxRequestSize;

    @XmlElement(name = "tcp-no-delay")
    protected boolean tcpNoDelay;

    @XmlElement(name = "connection-acceptor-thread-pool-size")
    protected int connectionAcceptorThreadPoolSize;

    @XmlElement(name = "ssl-connection-acceptor-thread-pool-size")
    protected int sslConnectionAcceptorThreadPoolSize;

    @XmlElement(name = "writer-thread-pool-size")
    protected int writerThreadPoolSize;

    public EzySimpleSocketSetting() {
        super();
        setPort(3005);
        setSslType(SslType.CUSTOMIZATION);
        setSslHandshakeTimeout(350);
        setMaxRequestSize(4096);
        setConnectionAcceptorThreadPoolSize(1);
        setSslConnectionAcceptorThreadPoolSize(8);
        setWriterThreadPoolSize(8);
        setCodecCreator("com.tvd12.ezyfox.codec.MsgPackCodecCreator");
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = super.toMap();
        map.put("sslActive", sslActive);
        map.put("sslType", sslType);
        map.put("sslHandshakeTimeout", sslHandshakeTimeout);
        map.put("tcpNoDelay", tcpNoDelay);
        map.put("maxRequestSize", maxRequestSize);
        map.put("writerThreadPoolSize", writerThreadPoolSize);
        map.put("connectionAcceptorThreadPoolSize", connectionAcceptorThreadPoolSize);
        map.put("sslConnectionAcceptorThreadPoolSize", sslConnectionAcceptorThreadPoolSize);
        return map;
    }
}
