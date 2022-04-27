package com.tvd12.ezyfoxserver.support.entry;

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
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.command.EzyAppSetup;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAppEventController;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.ext.EzyAbstractAppEntry;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.support.controller.EzyCommandsAware;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestAppSingletonController;
import com.tvd12.ezyfoxserver.support.factory.EzyAppResponseFactory;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import static com.tvd12.ezyfox.core.util.EzyEventHandlerLists.sortEventHandlersByPriority;
import static com.tvd12.ezyfoxserver.support.constant.EzySupportConstants.COMMANDS;
import static com.tvd12.ezyfoxserver.support.constant.EzySupportConstants.DEFAULT_PACKAGE_TO_SCAN;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EzySimpleAppEntry extends EzyAbstractAppEntry {

    @Override
    public final void config(EzyAppContext context) {
        preConfig(context);
        EzyBeanContext beanContext = createBeanContext(context);
        context.setProperty(EzyBeanContext.class, beanContext);
        addEventControllers(context, beanContext);
        setAppRequestController(context, beanContext);
        postConfig(context);
        postConfig(context, beanContext);
    }

    protected void preConfig(EzyAppContext context) {}

    protected void postConfig(EzyAppContext context) {}

    protected void postConfig(EzyAppContext context, EzyBeanContext beanContext) {}

    private void addEventControllers(EzyAppContext appContext, EzyBeanContext beanContext) {
        EzySetup setup = appContext.get(EzySetup.class);
        List<Object> eventControllers = beanContext.getSingletons(EzyEventHandler.class);
        sortEventHandlersByPriority(eventControllers);
        for (Object controller : eventControllers) {
            Class<?> controllerType = controller.getClass();
            EzyEventHandler annotation = controllerType.getAnnotation(EzyEventHandler.class);
            String eventName = EzyEventHandlerAnnotations.getEvent(annotation);
            setup.addEventController(EzyEventType.valueOf(eventName), (EzyEventController) controller);
            logger.info("add  event {} controller {}", eventName, controller);
        }
    }

    private void setAppRequestController(EzyAppContext appContext, EzyBeanContext beanContext) {
        EzyAppSetup setup = appContext.get(EzyAppSetup.class);
        EzyAppRequestController controller = newUserRequestController(beanContext);
        setup.setRequestController(controller);
        Set<String> commands = ((EzyCommandsAware) controller).getCommands();
        appContext.setProperty(COMMANDS, commands);
    }

    protected EzyAppRequestController newUserRequestController(EzyBeanContext beanContext) {
        return EzyUserRequestAppSingletonController.builder()
            .beanContext(beanContext)
            .build();
    }

    protected EzyBeanContext createBeanContext(EzyAppContext context) {
        EzyBindingContext bindingContext = createBindingContext();
        EzyMarshaller marshaller = bindingContext.newMarshaller();
        EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
        EzyResponseFactory appResponseFactory = createAppResponseFactory(context, marshaller);
        ScheduledExecutorService executorService = context.get(ScheduledExecutorService.class);
        EzyAppSetting appSetting = context.getApp().getSetting();
        EzyBeanContextBuilder beanContextBuilder = EzyBeanContext.builder()
            .addSingleton("appContext", context)
            .addSingleton("marshaller", marshaller)
            .addSingleton("unmarshaller", unmarshaller)
            .addSingleton("executorService", executorService)
            .addSingleton("zoneContext", context.getParent())
            .addSingleton("serverContext", context.getParent().getParent())
            .addSingleton("userManager", context.getApp().getUserManager())
            .addSingleton("appResponseFactory", appResponseFactory)
            .addSingleton("featureCommandManager", new EzyFeatureCommandManager())
            .addSingleton("requestCommandManager", new EzyRequestCommandManager())
            .activeProfiles(appSetting.getActiveProfiles());
        Class[] singletonClasses = getSingletonClasses();
        beanContextBuilder.addSingletonClasses(singletonClasses);
        Class[] prototypeClasses = getPrototypeClasses();
        beanContextBuilder.addPrototypeClasses(prototypeClasses);

        Set<String> scanablePackages = internalGetScanableBeanPackages();
        if (appSetting.getPackageName() != null) {
            scanablePackages.add(appSetting.getPackageName());
        }
        EzyReflection reflection = new EzyReflectionProxy(scanablePackages);
        beanContextBuilder.addSingletonClasses(
            (Set) reflection.getAnnotatedExtendsClasses(
                EzyEventHandler.class,
                EzyAppEventController.class));
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

    private EzyResponseFactory createAppResponseFactory(
        EzyAppContext appContext, EzyMarshaller marshaller) {
        EzyAppResponseFactory factory = new EzyAppResponseFactory();
        factory.setAppContext(appContext);
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
        Set<String> scanablePackages = new HashSet<>();
        scanablePackages.add(DEFAULT_PACKAGE_TO_SCAN);
        scanablePackages.addAll(Arrays.asList(getScanablePackages()));
        scanablePackages.addAll(Arrays.asList(getScanableBeanPackages()));
        return scanablePackages;
    }

    private Set<String> internalGetScanableBindingPackages() {
        Set<String> scanablePackages = new HashSet<>();
        scanablePackages.add(DEFAULT_PACKAGE_TO_SCAN);
        scanablePackages.addAll(Arrays.asList(getScanablePackages()));
        scanablePackages.addAll(Arrays.asList(getScanableBindingPackages()));
        return scanablePackages;
    }

    protected void setupBeanContext(EzyAppContext context, EzyBeanContextBuilder builder) {}

    @Override
    public void start() throws Exception {}

    @Override
    public void destroy() {}
}
