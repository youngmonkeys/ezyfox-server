package com.tvd12.ezyfoxserver.support.command;

public interface EzyObjectResponse extends EzyResponse<EzyObjectResponse> {

    /**
     * Add a key value pair to the response data.
     *
     * @param key the key.
     * @param value the value.
     * @return this pointer.
     */
    EzyObjectResponse param(Object key, Object value);

    /**
     * Exclude a ky from the response data.
     *
     * @param key the key.
     * @return this pointer.
     */
    EzyObjectResponse exclude(Object key);
}
