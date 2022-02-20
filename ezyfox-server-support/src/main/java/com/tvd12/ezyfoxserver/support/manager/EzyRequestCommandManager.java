package com.tvd12.ezyfoxserver.support.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.util.EzyDestroyable;

public class EzyRequestCommandManager implements EzyDestroyable {

    private final Set<String> commands;
    private final Set<String> managementCommands;
    private final Set<String> paymentCommands;

    public EzyRequestCommandManager() {
        this.commands = ConcurrentHashMap.newKeySet();
        this.managementCommands = ConcurrentHashMap.newKeySet();
        this.paymentCommands = ConcurrentHashMap.newKeySet();
    }
    
    public void addCommand(String uri) {
        this.commands.add(uri);
    }
    
    public boolean containsCommand(String uri) {
        return this.commands.contains(uri);
    }

    public void addManagementCommand(String uri) {
        this.managementCommands.add(uri);
    }

    public boolean isManagementCommand(String uri) {
        return managementCommands.contains(uri);
    }
    
    public void addPaymentCommand(String uri) {
        this.paymentCommands.add(uri);
    }

    public boolean isPaymentCommand(String uri) {
        return paymentCommands.contains(uri);
    }
    
    public List<String> getCommands() {
        return new ArrayList<>(commands);
    }
    
    public List<String> getManagementCommands() {
        return new ArrayList<>(managementCommands);
    }
    
    public List<String> getPaymentCommands() {
        return new ArrayList<>(paymentCommands);
    }

    @Override
    public void destroy() {
        this.commands.clear();
        this.managementCommands.clear();
        this.paymentCommands.clear();
    }
}
