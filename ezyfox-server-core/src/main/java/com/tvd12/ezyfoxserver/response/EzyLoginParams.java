package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyLoginParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 3437241102772473580L;
    
    protected long userId;
	protected Object data;
	protected String username;
	protected EzyArray joinedApps;
	
	@Override
	protected EzyArrayBuilder serialize0() {
	    return newArrayBuilder()
                .append(userId)
                .append(username)
                .append(joinedApps)
                .append(data);
	}
	
	@Override
	public void release() {
	    super.release();
	    this.data = null;
	    this.joinedApps = null;
	}

}
