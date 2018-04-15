package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimplePluginInfoParams 
        extends EzySimpleRequestParams
        implements EzyPluginInfoParams {
    private static final long serialVersionUID = 1875560863565659154L;
    
    protected String pluginName;
    
    @Override
    public void deserialize(EzyArray t) {
        this.pluginName = t.get(0, String.class);
    }
    
}
