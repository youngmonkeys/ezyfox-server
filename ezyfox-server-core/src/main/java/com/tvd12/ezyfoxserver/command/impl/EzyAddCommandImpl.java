package com.tvd12.ezyfoxserver.command.impl;

import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.command.EzyAddCommand;
import com.tvd12.ezyfoxserver.context.EzyAbstractContext;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyAddCommandImpl implements EzyAddCommand {

    private final EzyAbstractContext context;
    
    @SuppressWarnings("rawtypes")
    @Override
    public void add(Class commandType, Supplier commandSupplier) {
        context.addCommand(commandType, commandSupplier);
    }
    
}
