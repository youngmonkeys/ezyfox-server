package com.tvd12.ezyfoxserver.support.entry;

import static com.tvd12.ezyfox.core.util.EzyEventHandlerLists.sortEventHandlersByPriority;
import static com.tvd12.ezyfoxserver.support.constant.EzySupportConstants.COMMANDS;
import static com.tvd12.ezyfoxserver.support.constant.EzySupportConstants.DEFAULT_PACKAGE_TO_SCAN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestInterceptor;
import com.tvd12.ezyfox.core.util.EzyEventHandlerAnnotations;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.controller.EzyPluginEventController;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntry;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.support.annotation.EzyDisallowRequest;
import com.tvd12.ezyfoxserver.support.controller.EzyCommandsAware;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPluginSingletonController;
import com.tvd12.ezyfoxserver.support.factory.EzyPluginResponseFactory;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EzySimplePluginEntry extends EzyAbstractPluginEntry {

    @Override
    public void config(EzyPluginContext context) {
        preConfig(context);
        EzyBeanContext beanContext = createBeanContext(context);
        context.setProperty(EzyBeanContext.class, beanContext);
        addEventControllers(context, beanContext);
        setPluginRequestController(context, beanContext);
        postConfig(context);
        postConfig(context, beanContext);
    }

    protected void preConfig(EzyPluginContext ctx) {}

    protected void postConfig(EzyPluginContext ctx) {}

    protected void postConfig(EzyPluginContext ctx, EzyBeanContext beanContext) {}

    private void addEventControllers(EzyPluginContext context, EzyBeanContext beanContext) {
        EzySetup setup = context.get(EzySetup.class);
        List<Object> eventControllers = beanContext.getSingletons(EzyEventHandler.class);
        sortEventHandlersByPriority(eventControllers);
        for (Object controller : eventControllers) {
            Class<?> handlerType = controller.getClass();
            EzyEventHandler annotation = handlerType.getAnnotation(EzyEventHandler.class);
            String eventName = EzyEventHandlerAnnotations.getEvent(annotation);
            setup.addEventController(EzyEventType.valueOf(eventName), (EzyEventController) controller);
            logger.info("add  event {} controller {}", eventName, controller);
        }
    }

    private void setPluginRequestController(EzyPluginContext pluginContext, EzyBeanContext beanContext) {
        if(!allowRequest() || getClass().isAnnotationPresent(EzyDisallowRequest.class)) {
            return;
        }
        EzyPluginSetup setup = pluginContext.get(EzyPluginSetup.class);
        EzyPluginRequestController controller = newUserRequestController(beanContext);
        setup.setRequestController(controller);
        Set<String> commands = ((EzyCommandsAware)controller).getCommands();
        pluginContext.setProperty(COMMANDS, commands);
    }

    protected boolean allowRequest() {
        return true;
    }

    protected EzyPluginRequestController newUserRequestController(EzyBeanContext beanContext) {
        return EzyUserRequestPluginSingletonController.builder()
                .beanContext(beanContext)
                .build();
    }

    private EzyBeanContext createBeanContext(EzyPluginContext context) {
        EzyBindingContext bindingContext = createBindingContext();
        EzyMarshaller marshaller = bindingContext.newMarshaller();
        EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
        EzyResponseFactory pluginResponseFactory = createPluginResponseFactory(context, marshaller);
        ScheduledExecutorService executorService = context.get(ScheduledExecutorService.class);
        EzyPluginSetting pluginSetting = context.getPlugin().getSetting();
        EzyBeanContextBuilder beanContextBuilder = EzyBeanContext.builder()
                .addSingleton("pluginContext", context)
                .addSingleton("marshaller", marshaller)
                .addSingleton("unmarshaller", unmarshaller)
                .addSingleton("executorService", executorService)
                .addSingleton("zoneContext", context.getParent())
                .addSingleton("serverContext", context.getParent().getParent())
                .addSingleton("pluginResponseFactory", pluginResponseFactory)
                .addSingleton("featureCommandManager", new EzyFeatureCommandManager())
                .addSingleton("requestCommandManager", new EzyRequestCommandManager())
                .activeProfiles(pluginSetting.getActiveProfiles());
        Class[] singletonClasses = getSingletonClasses();
        beanContextBuilder.addSingletonClasses(singletonClasses);
        Class[] prototypeClasses = getPrototypeClasses();
        beanContextBuilder.addPrototypeClasses(prototypeClasses);

        Set<String> scanablePackages = internalGetScanableBeanPackages();
        if(pluginSetting.getPackageName() != null) {
            scanablePackages.add(pluginSetting.getPackageName());
        }
        EzyReflection reflection = new EzyReflectionProxy(scanablePackages);
        beanContextBuilder.addSingletonClasses(
                (Set) reflection.getAnnotatedExtendsClasses(
                        EzyEventHandler.class,
                        EzyPluginEventController.class));
        beanContextBuilder.addSingletonClasses(
                (Set) reflection.getAnnotatedClasses(EzyRequestController.class));
        beanContextBuilder.addSingletonClasses(
                (Set) reflection.getAnnotatedClasses(EzyExceptionHandler.class));
        beanContextBuilder.addSingletonClasses(
                (Set) reflection.getAnnotatedClasses(EzyRequestInterceptor.class));
        beanContextBuilder.scan(scanablePackages);
        setupBeanContext(context, beanContextBuilder);
        return beanContextBuilder.build();
    }

    protected EzyBindingContext createBindingContext() {
        return EzyBindingContext.builder()
            .scan(internalGetScanableBindingPackages())
            .build();
    }

    private EzyResponseFactory createPluginResponseFactory(
            EzyPluginContext pluginContext, EzyMarshaller marshaller) {
        EzyPluginResponseFactory factory = new EzyPluginResponseFactory();
        factory.setPluginContext(pluginContext);
        factory.setMarshaller(marshaller);
        return factory;
    }

    protected Class[] getSingletonClasses() {
        return new Class[0];
    }

    protected Class[] getPrototypeClasses() {
        return new Class[0];
    }

    protected String[] getScanablePackages() {
        return new String[0];
    }

    protected String[] getScanableBeanPackages() {
        return new String[0];
    }

    protected String[] getScanableBindingPackages() {
        return new String[0];
    }

    private Set<String> internalGetScanableBeanPackages() {
        Set<String> scanablePackages = new HashSet<String>();
        scanablePackages.add(DEFAULT_PACKAGE_TO_SCAN);
        scanablePackages.addAll(Arrays.asList(getScanablePackages()));
        scanablePackages.addAll(Arrays.asList(getScanableBeanPackages()));
        return scanablePackages;
    }

    private Set<String> internalGetScanableBindingPackages() {
        Set<String> scanablePackages = new HashSet<String>();
        scanablePackages.add(DEFAULT_PACKAGE_TO_SCAN);
        scanablePackages.addAll(Arrays.asList(getScanablePackages()));
        scanablePackages.addAll(Arrays.asList(getScanableBindingPackages()));
        return scanablePackages;
    }

    protected void setupBeanContext(EzyPluginContext context, EzyBeanContextBuilder builder) {
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void destroy() {
    }
}
