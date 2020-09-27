package com.tvd12.ezyfoxserver.support.test.controller;

import static org.mockito.Mockito.spy;

import java.util.concurrent.ScheduledExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.concurrent.EzyErrorScheduledExecutorService;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;
import com.tvd12.ezyfoxserver.setting.EzyEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestAppSingletonController;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.test.base.BaseTest;

public class EzyUserRequestAppSingletonControllerTest extends BaseTest {

	@Test
	public void test() throws Exception {
		EzySimpleSettings settings = new EzySimpleSettings();
		EzySimpleServer server = new EzySimpleServer();
		server.setSettings(settings);
		EzySimpleServerContext serverContext = new EzySimpleServerContext();
		serverContext.setServer(server);
		serverContext.init();
		
		EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
		EzySimpleZone zone = new EzySimpleZone();
		zone.setSetting(zoneSetting);
		EzySimpleZoneContext zoneContext = new EzySimpleZoneContext();
		zoneContext.setZone(zone);
		zoneContext.init();
		zoneContext.setParent(serverContext);
		
		EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
		appSetting.setName("test");
		
		EzyAppUserManager appUserManager = EzyAppUserManagerImpl.builder()
				.build();

		EzyEventControllersSetting eventControllersSetting = new EzySimpleEventControllersSetting();
		EzyEventControllers eventControllers = EzyEventControllersImpl.create(eventControllersSetting);
		EzySimpleApplication application = new EzySimpleApplication();
		application.setSetting(appSetting);
		application.setUserManager(appUserManager);
		application.setEventControllers(eventControllers);
		
		ScheduledExecutorService appScheduledExecutorService = new EzyErrorScheduledExecutorService("not implement");
		EzySimpleAppContext appContext = new EzySimpleAppContext();
		appContext.setApp(application);
		appContext.setParent(zoneContext);
		appContext.setExecutorService(appScheduledExecutorService);
		appContext.init();
		
		EzySimpleAppEntry entry = new EzyAppEntryEx();
		entry.config(appContext);
		entry.start();
		handleClientRequest(appContext);
		entry.destroy();
	}
	
	private void handleClientRequest(EzyAppContext context) {
		EzySimpleApplication app = (EzySimpleApplication) context.getApp();
		EzyAppRequestController requestController = app.getRequestController();
		
		EzyAbstractSession session = spy(EzyAbstractSession.class);
		EzySimpleUser user = new EzySimpleUser();
		EzyArray data = EzyEntityFactory.newArrayBuilder()
				.append("hello")
				.append(EzyEntityFactory.newObjectBuilder()
						.append("who", "Mr.Young Monkey!"))
				.build();
		EzyUserRequestAppEvent event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		data = EzyEntityFactory.newArrayBuilder()
				.append("responseFactoryTest")
				.append(EzyEntityFactory.newObjectBuilder()
						.append("who", "Mr.Young Monkey!"))
				.build();
		event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		data = EzyEntityFactory.newArrayBuilder()
				.append("no command")
				.build();
		event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		data = EzyEntityFactory.newArrayBuilder()
				.append("hello2")
				.append(EzyEntityFactory.newObjectBuilder()
						.append("who", "Mr.Young Monkey!"))
				.build();
		event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		data = EzyEntityFactory.newArrayBuilder()
				.append("hello6")
				.append(EzyEntityFactory.newObjectBuilder()
						.append("who", "Mr.Young Monkey!"))
				.build();
		event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		data = EzyEntityFactory.newArrayBuilder()
				.append("badRequestSend")
				.build();
		event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		data = EzyEntityFactory.newArrayBuilder()
				.append("badRequestNotSend")
				.build();
		event = new EzySimpleUserRequestAppEvent(user, session, data);
		requestController.handle(context, event);
		
		try {
			data = EzyEntityFactory.newArrayBuilder()
					.append("exception")
					.build();
			event = new EzySimpleUserRequestAppEvent(user, session, data);
			requestController.handle(context, event);
		}
		catch (Exception e) {
			assert e instanceof IllegalStateException;
		}
	}
	
	public static class EzyAppEntryEx extends EzySimpleAppEntry {

		@Override
		protected String[] getScanableBeanPackages() {
			return new String[] {
					"com.tvd12.ezyfoxserver.support.test.controller.app"
			};
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		protected Class[] getPrototypeClasses() {
			return new Class[] {
			};
		}

		@Override
		protected String[] getScanableBindingPackages() {
			return new String[] {
					"com.tvd12.ezyfoxserver.support.test.controller"
			};
		}

		@Override
		protected EzyAppRequestController newUserRequestController(EzyBeanContext beanContext) {
			return EzyUserRequestAppSingletonController.builder()
					.beanContext(beanContext)
					.build();
		}
		
	}
	
}
