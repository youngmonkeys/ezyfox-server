package com.tvd12.ezyfoxserver.factory;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionFactory<S extends EzySession>
    extends EzyObjectFactory<S> {
}
