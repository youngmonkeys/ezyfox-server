package com.tvd12.ezyfoxserver.constant;

import java.util.Set;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyCommand implements EzyConstant {

	ERROR(10, 10),
	HANDSHAKE(11, 0),
	PING(12, 10),
    PONG(13, 10),
    DISCONNECT(14, 10),
    PLUGIN_REQUEST(15, 10),
	LOGIN(20, 1),
	LOGIN_ERROR(21, 10),
	LOGOUT(22, 10),
	APP_ACCESS(30, 2),
	APP_REQUEST(31, 10),
	APP_JOINED(32, 10),
	APP_EXIT(33, 10),
	APP_ACCESS_ERROR(34, 10);
	
	@Getter
	private final int id;
	@Getter
	private final int priority;
	
	private static final Set<EzyCommand> SYSTEM_COMMANDS = systemCommands();
	
	private EzyCommand(int id) {
	    this(id, 10);
	}
	
	private EzyCommand(int id, int priority) {
		this.id = id;
		this.priority = priority;
	}
	
	public boolean isSystemCommand() {
	    return SYSTEM_COMMANDS.contains(this);
	}
	
	public int compareByPriority(EzyCommand other) {
	    return this.getPriority() - other.getPriority();
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyCommand valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
	
	private static final Set<EzyCommand> systemCommands() {
	    return Sets.newHashSet(HANDSHAKE, LOGIN, LOGOUT, APP_ACCESS, APP_EXIT, DISCONNECT);
	}
	
}
