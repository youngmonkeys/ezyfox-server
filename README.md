
# ezyfox-server <img src="https://github.com/youngmonkeys/ezyfox-server/blob/master/logo.png" width="64" />

* [Features](#features)
* [Benchmark](#benchmark)
* [Introduction](#introduction)
* [Get started](#get-started)
* [Documentation](#documentation)
* [Latest Version](#latest-version)
* [Code Example](#code-example)
* [Client SDKs](#client-sdks)
* [Products](#products)
* [Demos](#demos)
* [Tutorials](#tutorials)
* [Tests](#tests)
* [Contact us](#contact-us)

# Required

* Java version: 1.8

# Features

* **Core Container & Dependency Injection**: Includes bean manipulation, auto-binding, auto-implementation, and more.
* **Multiple Communication Protocols**: Supports TCP, UDP, WebSocket, and HTTP.
* **Traffic Encryption**: Allows for traffic between clients and servers to be encrypted using SSL.
* **Multiple Client SDKs**: Includes Android, iOS, Unity, React, C++, Flutter, and more.

# Benchmark

Ezyfox Server's benchmark involved broadcasting messages to 1000 concurrent users in one hour on a VPS with 512MB of RAM and 1 CPU core. You can [watch this video](https://youtu.be/TiSLOWIid5o) to see how we conducted the test.

<img src="https://github.com/youngmonkeys/ezyfox-server/blob/master/images/ezyfox_1h.png" width="747" height="320" />

# Introduction

A Free and Open Solution for Multiplayer Game Development

To develop online games rapidly, developers often need to use a game server engine like SmartFoxServer or Photon. Unfortunately, these engines come with a high price tag, especially as more and more users engage with our products. That's why we developed the EzyFox ecosystem, which aims to be free and open for anyone interested in building multiplayer games and applications.

The EzyFox ecosystem supports a wide range of essential components for enterprise product development, including TCP, UDP, WebSocket protocols with SSL encryption, HTTP RESTful API, Remote Procedure Call (RPC) protocol, Database Interaction, Memory Caching, and Message Queue.

With the EzyFox ecosystem, developers can foster creativity, collaboration, and innovation in multiplayer game development, irrespective of their budget constraints. By providing a comprehensive, free, and open solution, we aim to empower developers to bring their gaming visions to life and create memorable experiences for players worldwide.

# Getting Started

[https://youngmonkeys.org/get-started/](https://youngmonkeys.org/get-started/)

# Documentation

[https://youngmonkeys.org/ezyfox-sever/](https://youngmonkeys.org/project/ezyfox-sever/)

# Latest version

You can [download it here](https://resources.tvd12.com/)

# Code Example

**1. Create an app entry**

```java
public static class HelloAppEntry extends EzySimpleAppEntry {

    @Override
    protected String[] getScanablePackages() {
        return new String[] {
                "com.tvd12.ezyfoxserver.embedded.test" // replace by your package
        };
    }
}
```

**2. Create a plugin entry**

```java
public static class HelloPluginEntry extends EzySimplePluginEntry {

    @Override
    protected String[] getScanablePackages() {
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

You can find the full example [here](https://youngmonkeys.org/use-embedded-server/)

# Client SDKs

1.  [Android](https://github.com/youngmonkeys/ezyfox-server-android-client)
2.  [C/C++](https://github.com/youngmonkeys/ezyfox-server-cpp-client)
3.  [CSharp](https://github.com/youngmonkeys/ezyfox-server-csharp-client)
4.  [Flutter](https://github.com/youngmonkeys/ezyfox-server-flutter-client)
5.  [Java](https://github.com/youngmonkeys/ezyfox-server-java-client)
6.  [Javascript ECMAScript 6](https://github.com/youngmonkeys/ezyfox-server-es6-client)
7.  [Javascript](https://github.com/youngmonkeys/ezyfox-server-js-client)
8.  [Netty](https://github.com/youngmonkeys/ezyfox-server-netty-client)
9.  [Swift](https://github.com/youngmonkeys/ezyfox-server-swift-client)
10. [React Native](https://github.com/youngmonkeys/ezyfox-react-native-client)

# Products

1. [Defi Warrior](https://defiwarrior.io/)

# Demos

1. [Free Chat](https://youngmonkeys.org/asset/freechat/)
2. [Space Game Cocos2d-x](https://youngmonkeys.org/asset/space-game/)
3. [Space Shooter Unity](https://youngmonkeys.org/asset/space-shooter/)
4. [Lucky Wheel Phaser HTML5](https://youngmonkeys.org/asset/lucky-wheel/)
5. [One Two Three Simple Game Server](https://github.com/tvd12/ezyfox-server-example/tree/master/one-two-three)
6. [Easy Smashers Unity](https://github.com/vu-luong/EzySmashers)

# Tutorials:

1. [EzyChat](https://youtube.com/playlist?list=PLlZavoxtKE1IfKY7ohkLLyv6YkHMkvH6G): A simple realtime chat application
2. [EzyRoulette](https://youtube.com/playlist?list=PLlZavoxtKE1LD6qI87wp3YjLGzL8rMbSG): A simple lucky wheel game

# Tests

Navigate to the source folder and run:
```
mvn test
```

# Contact us

- Get in touch with us on [Facebook](https://www.facebook.com/youngmonkeys.org)
- Ask us on [stackask.com](https://stackask.com)
- Email us at [Dzung](mailto:itprono3@gmail.com)

# Support Us: Make a Meaningful Donation.

Currently, our operating budget is fully supported by our own salaries, and all product development is still based on voluntary contributions from a few organization members. The low budget is causing significant difficulties for us. Therefore, with a clear roadmap and an ambitious goal to provide intellectual products for the community, we would greatly appreciate your support in the form of a donation to help us take further steps. Thank you in advance for your meaningful contributions!

Your donation will enable us to:
- Expand Development
- Enhance Support
- Community Growth
- Promote Openness

[https://youngmonkeys.org/donate/](https://youngmonkeys.org/donate/)

# License

- Apache License, Version 2.0
