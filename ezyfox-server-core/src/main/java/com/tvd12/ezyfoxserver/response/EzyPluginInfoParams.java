package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyPluginInfoParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 712776912690368311L;

    protected EzyPluginSetting plugin;

    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
            .append(plugin.getId())
            .append(plugin.getName());
    }

    @Override
    public void release() {
        this.plugin = null;
    }
}
