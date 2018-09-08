package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.command.EzyVoidCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyResponse<T extends EzyResponse<T>> extends EzyVoidCommand {

	T data(Object data);
	
	T command(String command);
    
    T user(EzyUser user);
    
    T users(EzyUser... users);
    
    T users(Iterable<EzyUser> users);
    
    T session(EzySession session);
    
    T sessions(EzySession... sessions);
    
    T sessions(Iterable<EzySession> sessions);
    
    T username(String username);
    
    T usernames(String... usernames);
    
    T usernames(Iterable<String> usernames);
    
    default T data(EzyBuilder<?> builder) {
        return data(builder.build());
    }
	
}
