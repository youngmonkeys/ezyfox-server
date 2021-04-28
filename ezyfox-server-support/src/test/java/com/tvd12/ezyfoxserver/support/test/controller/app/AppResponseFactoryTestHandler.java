package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

import lombok.Setter;

@Setter
@EzySingleton
@EzyRequestListener("responseFactoryTest")
public class AppResponseFactoryTestHandler
		implements EzyUserRequestHandler<EzyAppContext, Hello> {

	@EzyAutoBind("appResponseFactory")
	protected EzyResponseFactory responseFactory;
	
	@Override
	public void handle(EzyAppContext context, EzyUserSessionEvent event, Hello data) {
		responseFactory.newArrayResponse()
			.command("hello")
			.data(EzyEntityFactory.newArrayBuilder())
			.param(1)
			.params(2, 3)
			.params(Lists.newArrayList(4, 5))
			.user(event.getUser())
			.users(event.getUser())
			.users(Lists.newArrayList(event.getUser()))
			.session(event.getSession())
			.sessions(event.getSession())
			.sessions(Lists.newArrayList(event.getSession()))
			.username(event.getUser().getName())
			.usernames(event.getUser().getName())
			.usernames(Lists.newArrayList(event.getUser().getName()))
			.execute();
		
		responseFactory.newArrayResponse()
			.command("hello")
			.usernames(Lists.newArrayList(event.getUser().getName()))
			.execute();
		
		responseFactory.newObjectResponse()
			.command("hello")
			.data(EzyEntityFactory.newObjectBuilder())
			.param("1", 1)
			.param("2", 2)
			.exclude("2")
			.user(event.getUser())
			.users(event.getUser())
			.users(Lists.newArrayList(event.getUser()))
			.session(event.getSession())
			.sessions(event.getSession())
			.sessions(Lists.newArrayList(event.getSession()))
			.username(event.getUser().getName())
			.usernames(event.getUser().getName())
			.usernames(Lists.newArrayList(event.getUser().getName()))
			.execute();
		
		responseFactory.newObjectResponse()
			.command("hello")
			.usernames(Lists.newArrayList(event.getUser().getName()))
			.execute();
	}
	
}
