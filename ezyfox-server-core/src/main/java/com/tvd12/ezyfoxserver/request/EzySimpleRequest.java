package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;
import com.tvd12.ezyfoxserver.io.EzyDataDeserializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EzySimpleRequest<P extends EzyRequestParams> implements 
        EzyRequest<P>,
        EzySessionAware,
        EzyRequestParamsDeserializable {
    private static final long serialVersionUID = -6768423001481501599L;
    
    protected P params;
    
    @Setter
    protected EzySession session;
    
    @Override
    public final void deserializeParams(EzyArray array) {
        ((EzyDataDeserializable)(params = newParams())).deserialize(array);
    }
    
    protected abstract P newParams();
    
}
