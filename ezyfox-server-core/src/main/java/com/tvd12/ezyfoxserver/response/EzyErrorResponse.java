package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyIError;

public class EzyErrorResponse 
        extends EzyBaseResponse implements EzyResponse {

	protected int code;
	protected String message;
	
	protected EzyErrorResponse(Builder<?> builder) {
	    super(builder);
	    this.code = builder.code;
	    this.message = builder.message;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(code)
				.append(message)
				.build();
	}
	
    @Override
    public EzyConstant getCommand() {
        return EzyCommand.ERROR;
    }
    
    public static Builder<?> builder() {
        return new Builder<>();
    }
	
	public static class Builder<B extends Builder<B>>
	        extends EzyBaseResponse.Builder<Builder<B>> {
	    protected int code;
	    protected String message;
		
		@SuppressWarnings("unchecked")
        public B code(int code) {
			this.code = code;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
        public B message(String message) {
			this.message = message;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
        public B error(EzyIError error) {
		    this.code = error.getId();
		    this.message = error.getMessage();
		    return (B)this;
		}
		
		@Override
		public EzyResponse build() {
		    return new EzyErrorResponse(this);
		}
		
	}

}
