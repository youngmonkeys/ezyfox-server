package com.tvd12.ezyfoxserver.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.constant.EzyConstant;

import lombok.Getter;

public enum EzyDisconnectReason implements EzyConstant {

	UNKNOWN(0),
	IDLE(1),
	NOT_LOGGED_IN(2),
	ANOTHER_SESSION_LOGIN(3),
	ADMIN_BAN(4),
    ADMIN_KICK(5),
    MAX_REQUEST_PER_SECOND(6),
    MAX_REQUEST_SIZE(7),
    SERVER_ERROR(8);
    
    private static final Map<Integer, EzyDisconnectReason> REASONS_BY_ID = reasonsById();
	
	@Getter
	private final int id;
	
	private EzyDisconnectReason(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyDisconnectReason valueOf(int id) {
		EzyDisconnectReason reason = REASONS_BY_ID.get(id);
		return reason;
	}
	
	private static final Map<Integer, EzyDisconnectReason> reasonsById() {
        Map<Integer, EzyDisconnectReason> map = new ConcurrentHashMap<>();
        for(EzyDisconnectReason reason : values())
            map.put(reason.getId(), reason);
        return map;
    }
	
}
