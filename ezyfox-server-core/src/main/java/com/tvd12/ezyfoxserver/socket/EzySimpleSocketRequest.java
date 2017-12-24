package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimpleSocketRequest implements EzySocketRequest {

    private EzyArray data;
    private long timestamp;
    private EzyCommand command;
    private boolean systemRequest;
    @Setter
    private EzySocketDataHandler handler;
    
    public EzySimpleSocketRequest(EzyArray data) {
        this.data = data;
        int cmdId = data.get(0, int.class);
        this.command = EzyCommand.valueOf(cmdId);
        this.timestamp = System.currentTimeMillis();
        this.systemRequest = command.isSystemCommand();
    }
    
    @Override
    public void release() {
        this.command = null;
        this.handler = null;
    }
    
}
