package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.entity.EzyData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyRequestPluginResponseParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -851367467100512738L;
    
    protected int pluginId;
    protected EzyData data;
    
    @Override
    public EzyArrayBuilder serialize0() {
        EzyArrayBuilder builder = newArrayBuilder()
                .append(pluginId)
                .append(data);
        return builder;
    }
    
    @Override
    public void release() {
        this.data = null;
    }
    
}
