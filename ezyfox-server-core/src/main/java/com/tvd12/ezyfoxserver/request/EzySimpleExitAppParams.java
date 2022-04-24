package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;
import lombok.Getter;

@Getter
public class EzySimpleExitAppParams
    extends EzySimpleRequestParams
    implements EzyExitAppParams {
    private static final long serialVersionUID = -5086750569893577041L;

    private int appId;

    @Override
    public void deserialize(EzyArray t) {
        this.appId = t.get(0, int.class);
    }
}
