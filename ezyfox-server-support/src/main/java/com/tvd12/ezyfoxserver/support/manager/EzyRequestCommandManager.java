package com.tvd12.ezyfoxserver.support.manager;

import com.tvd12.ezyfox.util.EzyDestroyable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EzyRequestCommandManager implements EzyDestroyable {

    private final Set<String> commands;
    private final Set<String> managementCommands;
    private final Set<String> paymentCommands;

    public EzyRequestCommandManager() {
        this.commands = ConcurrentHashMap.newKeySet();
        this.managementCommands = ConcurrentHashMap.newKeySet();
        this.paymentCommands = ConcurrentHashMap.newKeySet();
    }

    public void addCommand(String command) {
        this.commands.add(command);
    }

    public boolean containsCommand(String command) {
        return this.commands.contains(command);
    }

    public void addManagementCommand(String command) {
        this.managementCommands.add(command);
    }

    public boolean isManagementCommand(String command) {
        return managementCommands.contains(command);
    }

    public void addPaymentCommand(String command) {
        this.paymentCommands.add(command);
    }

    public boolean isPaymentCommand(String command) {
        return paymentCommands.contains(command);
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
