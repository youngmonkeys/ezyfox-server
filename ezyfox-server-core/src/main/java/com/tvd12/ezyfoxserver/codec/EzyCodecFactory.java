package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfox.constant.EzyConstant;

public interface EzyCodecFactory {

    Object newEncoder(EzyConstant type);

    Object newDecoder(EzyConstant type);
}
