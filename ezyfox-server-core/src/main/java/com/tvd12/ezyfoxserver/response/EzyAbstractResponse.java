package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;

@Getter
public abstract class EzyAbstractResponse extends EzyFixedCommandResponse {

	protected int appId;
	protected EzyConstant command;
	
	protected EzyAbstractResponse(Builder<?> builder) {
	    super(builder);
	    this.appId = builder.appId;
	    this.command = builder.command;
	}
	
	@SuppressWarnings("unchecked")
	public static abstract class Builder<B extends Builder<B>> 
	        extends EzyFixedCommandResponse.Builder<Builder<B>> {
	    
	    protected int appId;
	    protected EzyConstant command;
	    
        public B appId(int appId) {
	        this.appId = appId;
	        return (B)this;
	    }
	    
	    public B command(EzyConstant command) {
	        this.command = command;
	        return (B)this;
	    }
	    
	    @Override
	    public abstract EzyAbstractResponse build();
	    
	}
	
}
