package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimpleRequestPluginParams 
        extends EzySimpleRequestParams
        implements EzyRequestPluginParams {
    private static final long serialVersionUID = 1875560863565659154L;
    
    protected EzyArray data;
    
    @Override
    public void deserialize(EzyArray t) {
        this.data = t.get(1, EzyArray.class);
    }
    
    @Override
    public void release() {
        super.release();
        this.data = null;
    }
    
}
