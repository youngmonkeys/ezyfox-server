package com.tvd12.ezyfoxserver.command;

import java.util.function.Supplier;

public interface EzyAddCommand {

    @SuppressWarnings("rawtypes")
    void add(Class commandType, Supplier commandSupplier);

}
