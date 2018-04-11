package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;

import lombok.Getter;

@Getter
public class EzySimpleAccessAppParams
        extends EzySimpleRequestParams
        implements EzyAccessAppParams {
    private static final long serialVersionUID = 2608977146720735187L;
    
    protected int zoneId;
    protected String appName;
    protected EzyData data;
    
    @Override
    public void deserialize(EzyArray t) {
        this.zoneId = t.get(0, int.class);
        this.appName = t.get(1, String.class);
        this.data = t.get(2, EzyData.class);
    }
    
}
