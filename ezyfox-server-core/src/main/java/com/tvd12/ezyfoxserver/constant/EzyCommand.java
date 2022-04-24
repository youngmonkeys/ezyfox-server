package com.tvd12.ezyfoxserver.constant;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.constant.EzyConstant;

import lombok.Getter;

public enum EzyCommand implements EzyConstant {

    ERROR(10, 10),
    HANDSHAKE(11, 0),
    PING(12, 10),
    PONG(13, 10),
    DISCONNECT(14, 10),
    LOGIN(20, 1),
    LOGIN_ERROR(21, 10),
    APP_ACCESS(30, 2),
    APP_REQUEST(31, 10),
    APP_EXIT(33, 10),
    APP_ACCESS_ERROR(34, 10),
    APP_REQUEST_ERROR(35, 10),
    PLUGIN_INFO(40, 10),
    PLUGIN_REQUEST(41, 10),
    UDP_HANDSHAKE(50, 10);

    @Getter
    private final int id;
    @Getter
    private final int priority;

    private static final Set<EzyCommand> SYSTEM_COMMANDS = systemCommands();
    private static final Map<Integer, EzyCommand> COMMANDS_BY_ID = commandsById();

    private EzyCommand(int id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    public boolean isSystemCommand() {
        return SYSTEM_COMMANDS.contains(this);
    }

    @Override
    public String getName() {
        return toString();
    }

    public static EzyCommand valueOf(int id) {
        EzyCommand cmd = COMMANDS_BY_ID.get(id);
        return cmd;
    }

    private static final Set<EzyCommand> systemCommands() {
        return Sets.newHashSet(HANDSHAKE, LOGIN, APP_ACCESS, APP_EXIT, PLUGIN_INFO, DISCONNECT);
    }

    private static final Map<Integer, EzyCommand> commandsById() {
        Map<Integer, EzyCommand> map = new ConcurrentHashMap<>();
        for(EzyCommand cmd : values())
            map.put(cmd.getId(), cmd);
        return map;
    }

}
