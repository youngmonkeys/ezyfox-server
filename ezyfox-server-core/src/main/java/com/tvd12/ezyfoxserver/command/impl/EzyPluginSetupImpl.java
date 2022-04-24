package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyServerChild;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.command.EzyAbstractSetup;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;

public class EzyPluginSetupImpl
    extends EzyAbstractSetup
    implements EzyPluginSetup {

    private final EzySimplePlugin plugin;

    public EzyPluginSetupImpl(EzyServerChild plugin) {
        super(plugin);
        this.plugin = (EzySimplePlugin) plugin;
    }

    @Override
    public EzyPluginSetup setRequestController(EzyPluginRequestController controller) {
        plugin.setRequestController(controller);
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public EzyPluginSetup addEventController(EzyConstant eventType, EzyEventController controller) {
        addEventController0(eventType, controller);
        return this;
    }

}
