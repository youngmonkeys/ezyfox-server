package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketUserRemoval;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemoval;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalQueue;
import lombok.Setter;

@Setter
public class EzySimpleUserDelegate
    extends EzyLoggable
    implements EzyUserDelegate {

    protected final EzyServerContext serverContext;
    protected final EzySocketUserRemovalQueue userRemovalQueue;

    public EzySimpleUserDelegate(
        EzyServerContext serverContext,
        EzySocketUserRemovalQueue userRemovalQueue) {
        this.serverContext = serverContext;
        this.userRemovalQueue = userRemovalQueue;
    }

    @Override
    public void onUserRemoved(EzyUser user, EzyConstant reason) {
        EzyZoneContext zoneContext
            = serverContext.getZoneContext(user.getZoneId());
        EzySocketUserRemoval removal
            = new EzySimpleSocketUserRemoval(zoneContext, user, reason);
        userRemovalQueue.add(removal);
    }
}
