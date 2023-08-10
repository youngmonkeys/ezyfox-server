package com.tvd12.ezyfoxserver.socket;

public interface EzySecureChannel {

    byte[] pack(byte[] bytes) throws Exception;

    Object getPackingLock();
}
