package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyAppUserRemoveDelegate {

    void onUserRemoved(EzyUser user, EzyConstant reason);
}
