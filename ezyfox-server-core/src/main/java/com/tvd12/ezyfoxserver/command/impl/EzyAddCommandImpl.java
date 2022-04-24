package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyAddCommand;
import com.tvd12.ezyfoxserver.context.EzyAbstractContext;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor
public class EzyAddCommandImpl implements EzyAddCommand {

    private final EzyAbstractContext context;

    @SuppressWarnings("rawtypes")
    @Override
    public void add(Class commandType, Supplier commandSupplier) {
        context.addCommand(commandType, commandSupplier);
    }
}
