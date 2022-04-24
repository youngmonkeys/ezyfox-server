package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzySessionLoginDelegate {

    void onSessionLoggedIn(EzyUser user);

}
