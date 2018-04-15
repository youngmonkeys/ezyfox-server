package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EzyRequestPluginResponseParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -851367467100512738L;
    
    protected EzyData data;
    
    @Override
    public EzyArrayBuilder serialize0() {
        EzyArrayBuilder builder = newArrayBuilder();
        firstSerialize(builder);
        builder.append(data);
        return builder;
    }
    
    protected abstract void firstSerialize(EzyArrayBuilder builder);
    
    @Override
    public void release() {
        this.data = null;
    }
    
}
