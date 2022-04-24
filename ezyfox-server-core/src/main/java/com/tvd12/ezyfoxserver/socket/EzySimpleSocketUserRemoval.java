package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzySimpleSocketUserRemoval implements EzySocketUserRemoval {

    protected EzyZoneContext zoneContext;
    protected EzyUser user;
    protected EzyConstant reason;

    @Override
    public void release() {
        this.user = null;
        this.reason = null;
        this.zoneContext = null;
    }

}
