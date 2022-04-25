package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Getter;

@Getter
public class EzySimpleSocketRequest implements EzySocketRequest {

    private EzyArray data;
    private EzyCommand command;
    private EzySession session;
    private final boolean systemRequest;

    public EzySimpleSocketRequest(EzySession session, EzyArray data) {
        this.data = data;
        this.session = session;
        int cmdId = data.get(0, int.class);
        this.command = EzyCommand.valueOf(cmdId);
        this.systemRequest = command.isSystemCommand();
    }

    @Override
    public void release() {
        this.data = null;
        this.command = null;
        this.session = null;
    }
}
