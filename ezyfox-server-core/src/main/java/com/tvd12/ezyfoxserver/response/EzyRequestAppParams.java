package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyRequestAppParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 712776912690368311L;
    
    protected int appId;
    protected EzyData data;
    
    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
                .append(appId)
                .append(data);
    }
    
    @Override
    public void release() {
        this.data = null;
    }

}
