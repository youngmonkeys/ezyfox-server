package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyThreadPoolSizeSetting extends EzyToMap {

    int getCodec();
    
    int getStatistics();
    
    int getStreamHandler();
    
    int getSystemRequestHandler();
    
    int getExtensionRequestHandler();
    
    int getSocketDisconnectionHandler();
    
    int getSocketUserRemovalHandler();
    
}
