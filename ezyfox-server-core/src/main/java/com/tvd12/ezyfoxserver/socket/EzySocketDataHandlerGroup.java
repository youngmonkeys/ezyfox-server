package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;

public interface EzySocketDataHandlerGroup {

    void fireChannelRead(EzyCommand cmd, EzyArray msg) throws Exception;

    void fireChannelInactive(EzyConstant reason);

    void fireStreamBytesReceived(byte[] bytes) throws Exception;

}
