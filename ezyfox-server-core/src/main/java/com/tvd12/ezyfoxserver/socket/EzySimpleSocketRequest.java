package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimpleSocketRequest implements EzySocketRequest {

    private EzyArray data;
    private long timestamp;
    private EzyCommand command;
    private boolean systemRequest;
    @Setter
    private EzySession session;
    
    public EzySimpleSocketRequest(EzyArray data) {
        this.data = data;
        int cmdId = data.get(0, int.class);
        this.command = EzyCommand.valueOf(cmdId);
        this.timestamp = System.currentTimeMillis();
        this.systemRequest = command.isSystemCommand();
    }
    
    @Override
    public void release() {
        this.data = null;
        this.command = null;
        this.session = null;
    }
    
}
