package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class EzySimpleRequest<P extends EzyRequestParams> implements
    EzyUserRequest<P>,
    EzySessionAware,
    EzyUserFetcher,
    EzyUserAware,
    EzyRequestParamsDeserializable {
    private static final long serialVersionUID = -6768423001481501599L;

    protected P params;
    @Setter
    protected EzyUser user;
    @Setter
    protected EzySession session;

    @Override
    public final void deserializeParams(EzyArray array) {
        this.params = newParams();
        this.params.deserialize(array);
    }

    protected abstract P newParams();

    @Override
    public void release() {
        this.params.release();
        this.user = null;
        this.session = null;
        this.params = null;
    }

}
