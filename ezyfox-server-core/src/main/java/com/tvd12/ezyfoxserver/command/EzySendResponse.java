package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.entity.EzySender;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzySendResponse extends EzyCommand<Boolean> {

    EzySendResponse sender(EzySender sender);
    
    EzySendResponse response(EzyResponse response);
    
}
