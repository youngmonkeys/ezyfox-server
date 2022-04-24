package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.io.EzyArraySerializable;
import com.tvd12.ezyfox.util.EzyReleasable;

import java.io.Serializable;

public interface EzyResponse extends EzyArraySerializable, EzyReleasable, Serializable {

    EzyConstant getCommand();
}
