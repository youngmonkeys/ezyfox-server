package com.tvd12.ezyfoxserver.creator;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

public interface EzySessionCreator {

    <S extends EzySession> S create(EzyChannel channel);
}
