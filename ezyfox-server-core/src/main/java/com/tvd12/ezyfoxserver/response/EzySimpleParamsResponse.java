package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;

@Getter
public class EzySimpleParamsResponse<P extends EzyResponseParams>
        extends EzySimpleResponse
        implements EzyParamsResponse<P> {
    private static final long serialVersionUID = 5869682333882930052L;
    
    protected P params;
    
    public EzySimpleParamsResponse(EzyConstant command, P params) {
        super(command);
        this.params = params;
    }
    
    @Override
    protected final void serialize(EzyArrayBuilder arrayBuilder) {
        arrayBuilder.append(params.serialize());
    }
    
    @Override
    public void release() {
        super.release();
        this.params = null;
    }
    
}
