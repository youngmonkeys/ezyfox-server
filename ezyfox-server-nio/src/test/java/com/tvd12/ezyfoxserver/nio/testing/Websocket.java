package com.tvd12.ezyfoxserver.nio.testing;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class Websocket {
    public static void main(String[] args) throws Exception {
        Server server = new Server(2208);

        // needed.

        ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
        handlerCollection.addHandler(createWebsocketHandler());

        server.setHandler(handlerCollection);

        server.start();
    }

    private static ContextHandler createWebsocketHandler() {
        ContextHandler contextHandler = new ContextHandler("/ws");
        contextHandler.setAllowNullPathInfo(true); // disable redirect from /ws
        // to /ws/

        final WebSocketCreator webSocketcreator = (request, response) -> new Adapter();

        Handler webSocketHandler = new WebSocketHandler() {
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(webSocketcreator);
            }
        };

        contextHandler.setHandler(webSocketHandler);
        return contextHandler;
    }

    private static class Adapter extends WebSocketAdapter {
        @Override
        public void onWebSocketConnect(Session session) {
            System.out.print("client connected");
        }

        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len) {
            System.out.println("onWebSocketBinary");
        }

        @Override
        public void onWebSocketText(String message) {
            System.out.println("onWebSocketText");
        }
    }
}
