# ezyfox-server

# Synopsis

Ezyfox server is a socket server. It supports both tcp socket and websocket. It also provides manipulation beans,
auto binding, auto implementation and more utilities.

# Code Example

**1. Create an app entry**

```java
public class EzyChatEntryLoader extends EzyAbstractAppEntryLoader {

	@Override
	public EzyAppEntry load() throws Exception {
		return new EzyChatEntry();
	}
	
}
```

**2. Create bean context**

```java
private EzyBeanContext createBeanContext(
			EzyPluginContext context, EzyApply<EzyBeanContextBuilder> applier) {
    	EzyBindingContext bindingContext = createBindingContext();
    	EzyMarshaller marshaller = bindingContext.newMarshaller();
    	EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
    	EzyBeanContextBuilder beanContextBuilder = EzyBeanContext.builder()
    			.addSingleton("pluginContext", context)
    			.addSingleton("marshaller", marshaller)
    			.addSingleton("unmarshaller", unmarshaller)
    			.scan("vn.team.freechat.plugin")
    			.scan("vn.team.freechat.common.repo")
    			.scan("vn.team.freechat.common.service");
    	
    	applier.apply(beanContextBuilder);

		return beanContextBuilder.build();
}
```

**3. Auto implementation**

```java
@EzyAutoImpl
public interface EzyChatMessageRepo 
	extends EzyMongoRepository<ObjectId, EzyChatMessage> {

}
```

**4. Auto binding**

```java
@EzyPrototype(properties = { @EzyKeyValue(key = "type", value = "REQUEST_HANDLER"),
		@EzyKeyValue(key = "cmd", value = "2") })
@EzyArrayBinding(indexes = { "message", "receiver" })
@Setter
public class EzyChatUserRequestHandler extends EzyClientRequestHandler implements EzyDataBinding {

	private String message;
	private String receiver;

	@EzyAutoBind
	private EzyChatMessageRepo ezyChatMessageRepo;

	@EzyAutoBind
	private EzyResponseFactory responseFactory;
}
```

**5. Auto serialize**

```java
@Getter
@Setter
@EzyObjectBinding(read = false)
public class EzyChatError {

	private final int code;
	private final String message;
	
}
```

# Motivation

We are game server developers, We have a lot of online games and We must buy game server engine like SmarfoxServer
or Photon but they are really expensive. We need make a change to help us and everyone. 
Free is good idea and We make Ezyfox server

# API Reference

# Tests

mvn test

# Contributors

- Project development
  - [DungTV](mailto:dungtv192@gmail.com)
- Project admin tools development
  - [SonPH](hoaison95@gmail.com)

# License

- Apache License, Version 2.0
