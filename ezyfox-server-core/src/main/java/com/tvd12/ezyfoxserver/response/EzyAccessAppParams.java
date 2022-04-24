package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyAccessAppParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -2355811939162393678L;
    
    protected EzyData data;
    protected EzyAppSetting app;

    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
                .append(app.getId())
                .append(app.getName())
                .append(data);
    }

    @Override
    public void release() {
        super.release();
        this.data = null;
        this.app = null;
    }

}
