package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.constant.EzyConstant;

import lombok.Getter;

@Getter
public class EzySimpleSessionRemovedEvent 
		extends EzySimpleUserSessionEvent 
		implements EzySessionRemovedEvent {

    protected EzyConstant reason;
    
    protected EzySimpleSessionRemovedEvent(Builder builder) {
        super(builder);
        this.reason = builder.reason;
    }
    
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
		
	    protected EzyConstant reason;
	    
	    public Builder reason(EzyConstant reason) {
	        this.reason = reason;
	        return this;
	    }
	    
	    @Override
	    public EzySessionRemovedEvent build() {
	        return new EzySimpleSessionRemovedEvent(this);
	    }
	    
	}
	
}
