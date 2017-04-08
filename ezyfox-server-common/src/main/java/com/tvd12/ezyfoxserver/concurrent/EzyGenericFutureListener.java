package com.tvd12.ezyfoxserver.concurrent;

import java.util.EventListener;

/**
 * Listens to the result of a {@link Future}.  The result of the asynchronous operation is notified once this listener
 * is added by calling {@link Future#addListener(GenericFutureListener)}.
 */
public interface EzyGenericFutureListener<F extends EzyFuture<?>> extends EventListener {

    /**
     * Invoked when the operation associated with the {@link Future} has been completed.
     *
     * @param future  the source {@link Future} which called this callback
     */
    void operationComplete(F future) throws Exception;
}
