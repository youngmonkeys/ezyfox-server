package com.tvd12.ezyfoxserver.response;

import java.io.Serializable;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.io.EzyArraySerializable;
import com.tvd12.ezyfox.util.EzyReleasable;

public interface EzyResponse extends EzyArraySerializable, EzyReleasable, Serializable {

    EzyConstant getCommand();
    
}
