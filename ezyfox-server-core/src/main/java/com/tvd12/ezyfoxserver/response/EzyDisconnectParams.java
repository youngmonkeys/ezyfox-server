package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyDisconnectParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -2559755055442215708L;
    
    protected EzyConstant reason;
    
    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
                .append(reason.getId());
    }
	
}
