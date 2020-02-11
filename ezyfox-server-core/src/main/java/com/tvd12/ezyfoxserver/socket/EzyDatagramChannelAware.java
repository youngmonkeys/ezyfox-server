package com.tvd12.ezyfoxserver.socket;

import java.nio.channels.DatagramChannel;

public interface EzyDatagramChannelAware {

    void setDatagramChannel(DatagramChannel channel);
    
}
