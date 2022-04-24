package com.tvd12.ezyfoxserver.support.controller;

import static com.tvd12.ezyfox.io.EzyStrings.isNotBlank;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPrototypeFactory;
import com.tvd12.ezyfox.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.core.util.EzyRequestListenerAnnotations;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;
import com.tvd12.ezyfoxserver.entity.EzyUserAware;
import com.tvd12.ezyfoxserver.event.EzyUserRequestEvent;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;

public abstract class EzyUserRequestPrototypeController<
        C extends EzyZoneChildContext,
        E extends EzyUserRequestEvent>
        extends EzyAbstractUserRequestController {

    protected final EzyBeanContext beanContext;
    protected final EzyUnmarshaller unmarshaller;
    protected final Map<String, EzyPrototypeSupplier> handlers;

    protected EzyUserRequestPrototypeController(Builder<?> builder) {
        this.beanContext = builder.beanContext;
        this.unmarshaller = builder.unmarshaller;
        this.handlers = new HashMap<>(builder.extractHandlers());
    }

    public void handle(C context, E event) {
        EzyArray data = event.getData();
        String cmd = data.get(0, String.class);
        EzyPrototypeSupplier supplier = handlers.get(cmd);
        if(supplier == null) {
            logger.warn("has no handler with command: {} from session: {}", cmd, event.getSession().getName());
            return;
        }
        EzyHandler handler = (EzyHandler)supplier.supply(beanContext);
        if(handler instanceof EzyUserAware)
            ((EzyUserAware)handler).setUser(event.getUser());
        if(handler instanceof EzySessionAware)
            ((EzySessionAware)handler).setSession(event.getSession());
        if(handler instanceof EzyDataBinding) {
            EzyData params = data.get(1, EzyData.class, null);
            if(params != null)
                unmarshaller.unwrap(params, handler);
        }
        try {
            preHandle(context, event, cmd, handler);
            handler.handle();
            postHandle(context, event, cmd, handler);
        }
        catch(EzyBadRequestException e) {
            if(e.isSendToClient()) {
                EzyData errorData = newErrorData(e);
                responseError(context, event, errorData);
            }
            logger.debug("request cmd: {} by session: {} with data: {} error", cmd, event.getSession().getName(), data, e);
            postHandle(context, event, cmd, handler, e);
        }
        catch(Exception e) {
            postHandle(context, event, cmd, handler, e);
            throw e;
        }
    }

    protected abstract void preHandle(C context, E event, String cmd, EzyHandler handler);
    protected void postHandle(C context, E event, String cmd, EzyHandler handler) {}
    protected void postHandle(C context, E event, String cmd, EzyHandler handler, Exception e) {}

    protected abstract void responseError(C context, E event, EzyData errorData);

    @Override
    public Set<String> getCommands() {
        return new HashSet<>(handlers.keySet());
    }

    @SuppressWarnings("rawtypes")
    public abstract static class Builder<B extends Builder>
            extends EzyLoggable
            implements EzyBuilder<EzyUserRequestPrototypeController> {

        protected EzyBeanContext beanContext;
        protected EzyPrototypeFactory prototypeFactory;
        protected EzyUnmarshaller unmarshaller;

        @SuppressWarnings("unchecked")
        public B beanContext(EzyBeanContext beanContext) {
            this.beanContext = beanContext;
            this.prototypeFactory = beanContext.getPrototypeFactory();
            this.unmarshaller = beanContext.getSingleton("unmarshaller", EzyUnmarshaller.class);
            return (B)this;
        }

        private Map<String, EzyPrototypeSupplier> extractHandlers() {
            Map<String, EzyPrototypeSupplier> handlers = getHandlers();
            extractRequestCommands(handlers);
            return handlers;
        }

        private Map<String, EzyPrototypeSupplier> getHandlers() {
            List<EzyPrototypeSupplier> suppliers =
                prototypeFactory.getSuppliers(EzyRequestListener.class);
            Map<String, EzyPrototypeSupplier> handlers = new HashMap<>();
            for(EzyPrototypeSupplier supplier : suppliers) {
                Class<?> handleType = supplier.getObjectType();
                EzyRequestListener annotation = handleType.getAnnotation(EzyRequestListener.class);
                String command = EzyRequestListenerAnnotations.getCommand(annotation);
                handlers.put(command, supplier);
                logger.debug("add command {} and request handler supplier {}", command, supplier);
            }
            extractRequestCommands(handlers);
            return handlers;
        }

        private void extractRequestCommands(Map<String, EzyPrototypeSupplier> handlers) {
            EzyFeatureCommandManager featureCommandManager =
                beanContext.getSingleton(EzyFeatureCommandManager.class);
            EzyRequestCommandManager requestCommandManager =
                beanContext.getSingleton(EzyRequestCommandManager.class);
            for(String command : handlers.keySet()) {
                EzyPrototypeSupplier handler = handlers.get(command);
                Class<?> handleType = handler.getObjectType();
                requestCommandManager.addCommand(command);
                if (handleType.isAnnotationPresent(EzyManagement.class)) {
                    requestCommandManager.addManagementCommand(command);
                }
                if (handleType.isAnnotationPresent(EzyPayment.class)) {
                    requestCommandManager.addPaymentCommand(command);
                }
                EzyFeature featureAnno = handleType.getAnnotation(EzyFeature.class);
                if (featureAnno != null && isNotBlank(featureAnno.value())) {
                    featureCommandManager.addFeatureCommand(featureAnno.value(), command);
                }
            }
        }
    }
}
