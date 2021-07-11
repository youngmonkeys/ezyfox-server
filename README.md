
# ezyfox-server <img src="https://github.com/youngmonkeys/ezyfox-server/blob/master/logo.png" width="64" />

[![Build Status](https://travis-ci.org/youngmonkeys/ezyfox-server.svg?branch=master)](https://travis-ci.org/youngmonkeys/ezyfox-server)

* [Features](#features)
* [Benchmark](#benchmark)
* [Introduction](#introduction)
* [Get started](#get-started)
* [Documentation](#documentation)
* [Code Example](#code-example)
* [Demos](#demos)
* [Tutorials](#tutorials)
* [Tests](#tests)
* [Contributors](#contributors)

# Features
* **Core container & dependency injection**: bean manipulation, auto binding, auto implementation, etc.
* **Multiple Communication Protocols**: supports TCP, UDP, WebSocket, HTTP.
* **Traffic Encryption**: traffic between clients and servers can be encrypted using SSL.
* **Multiple Client SDKs**: Android, IOS, Unity, ReactJS, C++.

# Benchmark

Ezyfox Server's benchmark with 1000 CCU broadcast messages in 1 hour on one VPS ram 512MB, CPU 1 core

<img src="https://github.com/youngmonkeys/ezyfox-server/blob/master/images/ezyfox_1h.png" width="747" height="320" />

# Introduction
To rapidly develop online games, developers often have to use a game server engine like SmartFoxServer or Photon, but unfortunately, they come with really high price, especially when more and more users are engaging in our products. Therefore, we develop EzyFox ecosystem aiming to make everything free and open for everyone who are keen on building multi-players games and applications.

EzyFox ecosystem supports a wide range of most important components to develop an enterprise product including TCP, UDP, WebSocket protocols with SSL encryption, HTTP RESTful API, Remote procedure call RPC protocol, Database interaction, Memory caching, Message Queue. 

With EzyFox ecosystem, we can stay away from the headache of choosing which technologies to use to manage and scale up an application, so we can just focus on implementing business logics.

# Get Started
```
https://youngmonkeys.org/get-started/
```

# Documentation

[https://youngmonkeys.org/ezyfox-sever/](https://youngmonkeys.org/project/ezyfox-sever/)

# Code Example

**1. Create an app entry**

```java
public static class HelloAppEntry extends EzySimpleAppEntry {

    @Override
    protected String[] getScanableBeanPackages() {
        return new String[] {
                "com.tvd12.ezyfoxserver.embedded.test" // replace by your package
        };
    }

    @Override
    protected String[] getScanableBindingPackages() {
        return new String[] {
                "com.tvd12.ezyfoxserver.embedded.test" // replace by your package
        };
    }
    
    @Override
    protected EzyAppRequestController newUserRequestController(EzyBeanContext beanContext) {
        return EzyUserRequestAppSingletonController.builder()
                .beanContext(beanContext)
                .build();
    }
    
}
```

**2. Create a plugin entry**

```java
public static class HelloPluginEntry extends EzySimplePluginEntry {

    @Override
    protected String[] getScanableBeanPackages() {
        return new String[] {
                "com.tvd12.ezyfoxserver.embedded.test" // replace by your package
        };
    }

}
```

**3. Setup**

```java
EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
        .name("hello")
        .addListenEvent(EzyEventType.USER_LOGIN)
        .entryLoader(HelloPluginEntryLoader.class);

EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
        .name("hello")
        .entryLoader(HelloAppEntryLoader.class);

EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
        .name("hello")
        .application(appSettingBuilder.build())
        .plugin(pluginSettingBuilder.build());

EzySimpleSettings settings = new EzySettingsBuilder()
        .zone(zoneSettingBuilder.build())
        .build();
```

**4. Create and start a server**

```java
EzyEmbeddedServer server = EzyEmbeddedServer.builder()
        .settings(settings)
        .build();
server.start();
```

You can find full example [here](https://youngmonkeys.org/use-embedded-server/)

# Demos
1. [Space Game](https://youngmonkeys.org/asset/space-game/)
2. [Free Chat](https://youngmonkeys.org/asset/freechat/)

# Tutorials:
1. EzyChat: https://youtube.com/playlist?list=PLlZavoxtKE1IfKY7ohkLLyv6YkHMkvH6G

# Tests
Navigate to the source folder and run:
```
mvn test
```

# Contributors

 - [DungTV](mailto:itprono3@gmail.com)

# License

- Apache License, Version 2.0
