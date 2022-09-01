package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfoxserver.support.command.EzyArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;

/**
 * A factory class to create response command.
 */
public interface EzyResponseFactory {

    /**
     * Create a response command.
     * This command will respond data to client in array.
     *
     * @return the array response command.
     */
    EzyArrayResponse newArrayResponse();

    /**
     * Create a response command.
     * This command will respond to client in object (key - value).
     *
     * @return the array response command.
     */
    EzyObjectResponse newObjectResponse();
}
