package com.tvd12.ezyfoxserver.database.query;

import java.util.concurrent.TimeUnit;

public interface EzyFindAndModifyOptions {

	/**
     * Returns the remove
     *
     * @return the remove
     */
    boolean isRemove();

    /**
     * Indicates whether to remove the elements matching the query or not
     *
     * @param remove true if the matching elements should be deleted
     * @return this
     */
    EzyFindAndModifyOptions remove(boolean remove);;

    /**
     * Returns the upsert
     *
     * @return the upsert
     */
    boolean isUpsert();

    /**
     * Indicates that an upsert should be performed
     *
     * @param upsert the upsert
     * @return this
     */
    EzyFindAndModifyOptions upsert(boolean upsert);

    /**
     * Returns the returnNew
     *
     * @return the returnNew
     */
    boolean isReturnNew();
    /**
     * Sets the returnNew
     *
     * @param returnNew the returnNew
     * @return this
     */
    EzyFindAndModifyOptions returnNew(boolean returnNew);

    /**
     * Gets the maximum execution time on the server for this operation.  The default is 0, which places no limit on the execution time.
     *
     * @param timeUnit the time unit to return the result in
     * @return the maximum execution time in the given time unit
     */
    long getMaxTime(TimeUnit timeUnit);

    /**
     * Sets the maximum execution time on the server for this operation.
     *
     * @param maxTime  the max time
     * @param timeUnit the time unit, which may not be null
     * @return this
     */
    EzyFindAndModifyOptions maxTime(long maxTime, TimeUnit timeUnit);

}
