package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimpleRequestPluginByNameParams 
        extends EzySimpleRequestPluginParams
        implements EzyRequestPluginByNameParams {
    private static final long serialVersionUID = -5400022051215082036L;
    
    protected String pluginName;
    
    @Override
    public void deserialize(EzyArray t) {
        this.pluginName = t.get(0, String.class);
    }
    
}
