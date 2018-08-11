package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimpleRequestAppParams
        extends EzySimpleRequestParams
        implements EzyRequestAppParams {
    private static final long serialVersionUID = 9158760365914559273L;
    
    protected int appId;
    protected EzyArray data;
    
    @Override
    public void deserialize(EzyArray t) {
        this.appId = t.get(0, int.class);
        this.data = t.get(1, EzyArray.class);
    }
    
    @Override
    public void release() {
        super.release();
        this.data = null;
    }
    
}
