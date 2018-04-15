package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyRequestPluginByNameResponseParams extends EzyRequestPluginResponseParams {
    private static final long serialVersionUID = 7032128358139872091L;

    protected String pluginName;
    
    @Override
    protected void firstSerialize(EzyArrayBuilder builder) {
        builder.append(pluginName);
    }
    
}
