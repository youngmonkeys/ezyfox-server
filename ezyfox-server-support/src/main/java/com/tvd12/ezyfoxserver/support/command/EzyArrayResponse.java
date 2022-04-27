package com.tvd12.ezyfoxserver.support.command;

public interface EzyArrayResponse extends EzyResponse<EzyArrayResponse> {

    EzyArrayResponse param(Object value);

    EzyArrayResponse params(Object... values);

    EzyArrayResponse params(Iterable<?> values);
}
