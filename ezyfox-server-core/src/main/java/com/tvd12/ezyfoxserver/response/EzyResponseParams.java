package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.io.EzyArraySerializable;
import com.tvd12.ezyfox.util.EzyReleasable;

import java.io.Serializable;

public interface EzyResponseParams
    extends EzyArraySerializable, EzyReleasable, Serializable {

    @Override
    void release();
}
