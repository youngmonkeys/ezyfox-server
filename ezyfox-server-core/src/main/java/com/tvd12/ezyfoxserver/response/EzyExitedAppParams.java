package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyExitedAppParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -2355811939162393678L;

    protected EzyAppSetting app;
    protected EzyConstant reason;

    @Override
    protected EzyArrayBuilder doSerialize() {
        return newArrayBuilder()
            .append(app.getId())
            .append(reason.getId());
    }

    @Override
    public void release() {
        super.release();
        this.app = null;
        this.reason = null;
    }
}
