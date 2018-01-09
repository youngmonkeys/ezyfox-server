package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyRequestPluginParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -851367467100512738L;
    
    protected EzyData data;
    protected String pluginName;
    
    @Override
    public EzyArrayBuilder serialize0() {
        return newArrayBuilder()
                .append(pluginName)
                .append(data);
    }
    
    @Override
    public void release() {
        this.data = null;
    }
    
}
