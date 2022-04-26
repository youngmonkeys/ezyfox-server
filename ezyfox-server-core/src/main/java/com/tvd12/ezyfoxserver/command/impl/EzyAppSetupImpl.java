package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyServerChild;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.command.EzyAbstractSetup;
import com.tvd12.ezyfoxserver.command.EzyAppSetup;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public class EzyAppSetupImpl
    extends EzyAbstractSetup
    implements EzyAppSetup {

    private final EzySimpleApplication app;

    public EzyAppSetupImpl(EzyServerChild app) {
        super(app);
        this.app = (EzySimpleApplication) app;
    }

    @Override
    public EzyAppSetup setRequestController(EzyAppRequestController controller) {
        app.setRequestController(controller);
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public EzyAppSetup addEventController(
        EzyConstant eventType,
        EzyEventController controller
    ) {
        doAddEventController(eventType, controller);
        return this;
    }
}
