package com.tvd12.ezyfoxserver.support.command;

public interface EzyArrayResponse extends EzyResponse<EzyArrayResponse> {

    /**
     * Add a value to response data.
     *
     * @param value the value.
     * @return this pointer.
     */
    EzyArrayResponse param(Object value);

    /**
     * Add an array of values to response data.
     *
     * @param values the array of values.
     * @return this pointer.
     */
    EzyArrayResponse params(Object... values);

    /**
     * Add a collection of values to response data.
     *
     * @param values the collection of values.
     * @return this pointer.
     */
    EzyArrayResponse params(Iterable<?> values);
}
