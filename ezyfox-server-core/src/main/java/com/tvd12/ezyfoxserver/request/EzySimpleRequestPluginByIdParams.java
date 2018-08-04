package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimpleRequestPluginByIdParams 
        extends EzySimpleRequestPluginParams
        implements EzyRequestPluginByIdParams {
    private static final long serialVersionUID = -5400022051215082036L;
    
    protected int pluginId;
    
    @Override
    public void deserialize(EzyArray t) {
        this.pluginId = t.get(0, int.class);
    }
    
}
