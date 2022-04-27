package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;

public class EzyUserRequestPluginSingletonController
    extends EzyUserRequestSingletonController<EzyPluginContext, EzyUserRequestPluginEvent>
    implements EzyPluginRequestController {

    protected EzyUserRequestPluginSingletonController(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void responseError(
        EzyPluginContext context,
        EzyUserRequestPluginEvent event, EzyData errorData) {
        context.send(errorData, event.getSession(), false);
    }

    public static class Builder extends EzyUserRequestSingletonController.Builder<Builder> {

        @Override
        public EzyUserRequestPluginSingletonController build() {
            return new EzyUserRequestPluginSingletonController(this);
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected EzyUserRequestPrototypeController getPrototypeController() {
            return EzyUserRequestPluginPrototypeController.builder()
                .beanContext(beanContext)
                .build();
        }

    }
}
