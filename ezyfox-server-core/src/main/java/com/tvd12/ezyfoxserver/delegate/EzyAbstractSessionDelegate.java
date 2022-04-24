package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public abstract class EzyAbstractSessionDelegate
    extends EzyLoggable
    implements EzySessionDelegate {

    @Override
    public void onSessionLoggedIn(EzyUser user) {}
}
