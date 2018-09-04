package com.tvd12.ezyfoxserver.support.command;

import java.util.Arrays;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

@SuppressWarnings("unchecked")
public abstract class EzyAbstractResponse<T extends EzyResponse<T>> 
		extends EzyEntityBuilders
		implements EzyResponse<T>, EzyDestroyable {

	protected Object data;
	
    protected com.tvd12.ezyfoxserver.command.EzyResponse response;
    
    protected EzyContext context;
    protected EzyMarshaller marshaller;
    
    public EzyAbstractResponse(EzyContext context, EzyMarshaller marshaller) {
        this.context = context;
        this.marshaller = marshaller;
        this.response = newResponse();
    }
    
	public T command(String command) {
		this.response.command(command);
        return (T) this;
    }

	@Override
    public T data(Object data) {
        this.data = data;
        return (T) this;
    }

    public T user(EzyUser user) {
        this.response.user(user);
        return (T) this;
    }
    
    public T users(EzyUser... users) {
        return users(Arrays.asList(users));
    }

    public T users(Iterable<EzyUser> users) {
        users.forEach(this::user);
        return (T) this;
    }
    
    @Override
    public T session(EzySession session) {
        this.response.session(session);
        return (T) this;
    }
    
    @Override
    public T sessions(EzySession... sessions) {
        return sessions(Arrays.asList(sessions));
    }
    
    @Override
    public T sessions(Iterable<EzySession> sessions) {
        sessions.forEach(this::session);
        return (T) this;
    }
    
    @Override
    public T username(String username) {
    		this.response.username(username);
    		return (T)this;
    }
    
    @Override
    public T usernames(String... usernames) {
    		return usernames(Arrays.asList(usernames));
    }
    
    @Override
    public T usernames(Iterable<String> usernames) {
    		usernames.forEach(this::username);
    		return (T)this;
    }
    
    public void execute() {
    		response(getResponseData());
    		destroy();
    }
    
    protected abstract EzyData getResponseData();
    
    protected void response(EzyData data) {
	    	response
	        	.params(data)
	        	.execute();
     }
    
    protected abstract com.tvd12.ezyfoxserver.command.EzyResponse newResponse();
    
    @Override
    public void destroy() {
    		this.data = null;
        this.response = null;
        this.context = null;
        this.marshaller = null;
    }
	
}
