package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyPluginInfoParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 712776912690368311L;

    protected int pluginId;
    protected String pluginName;
    
    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
                .append(pluginName)
                .append(pluginId);
    }
    
    @Override
    public void release() {
        this.pluginName = null;
    }

}
