package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyLoginParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 3437241102772473580L;

    protected int zoneId;
    protected String zoneName;
    protected long userId;
    protected Object data;
    protected String username;

    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
            .append(zoneId)
            .append(zoneName)
            .append(userId)
            .append(username)
            .append(data);
    }

    @Override
    public void release() {
        super.release();
        this.data = null;
    }
}
