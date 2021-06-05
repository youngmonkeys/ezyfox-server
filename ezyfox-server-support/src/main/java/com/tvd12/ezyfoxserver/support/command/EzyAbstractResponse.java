package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
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
    
    public T encrypted() {
    	this.response.encrypted();
    	return (T)this;
    }
    
    public T encrypted(boolean value) {
    	this.response.encrypted(value);
    	return (T)this;
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
	
    public T user(EzyUser user, boolean exclude) {
        this.response.user(user, exclude);
        return (T) this;
    }
    
    public T users(EzyUser[] users, boolean exclude) {
    		this.response.users(users, exclude);
        return (T) this;
    }

    public T users(Iterable<EzyUser> users, boolean exclude) {
        this.response.users(users, exclude);
        return (T) this;
    }
    
    @Override
    public T session(EzySession session, boolean exclude) {
        this.response.session(session, exclude);
        return (T) this;
    }
    
    @Override
    public T sessions(EzySession[] sessions, boolean exclude) {
        this.response.sessions(sessions, exclude);
        return (T)this;
    }
    
    @Override
    public T sessions(Iterable<EzySession> sessions, boolean exclude) {
        this.response.sessions(sessions, exclude);
        return (T) this;
    }
    
    @Override
    public T username(String username, boolean exclude) {
    		this.response.username(username, exclude);
    		return (T)this;
    }
    
    @Override
    public T usernames(String[] usernames, boolean exclude) {
    		this.response.usernames(usernames, exclude);
    		return (T)this;
    }
    
    @Override
    public T usernames(Iterable<String> usernames, boolean exclude) {
    		this.response.usernames(usernames, exclude);
		return (T)this;
    }
    
    @Override
    public T transportType(EzyTransportType transportType) {
    	this.response.transportType(transportType);
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
