package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyThreadPoolSizeSetting extends EzyToMap {

    int getStatistics();

    int getStreamHandler();

    int getSocketDataReceiver();

    int getSystemRequestHandler();

    int getExtensionRequestHandler();

    int getSocketDisconnectionHandler();

    int getSocketUserRemovalHandler();
}
