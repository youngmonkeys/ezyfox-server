package com.tvd12.ezyfoxserver.embedded.test.client;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyTcpClient;
import com.tvd12.ezyfoxserver.client.EzyUTClient;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyAbstractDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionFailureHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyHandshakeHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginSuccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyUdpHandshakeHandler;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.setup.EzySetup;
import com.tvd12.ezyfoxserver.client.socket.EzySocketResponseApi;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.BooleanSupplier;

public class JavaClientTester implements AutoCloseable {

    public static final long TIMEOUT = 10000;

    private final EzyClient client;
    private final Queue<String> responses = new ArrayDeque<>();
    private volatile String username;
    private volatile boolean handshaked;
    private volatile boolean loggedIn;
    private volatile boolean udpConnected;
    private volatile String error;
    private volatile RuntimeException handshakeError;

    public JavaClientTester(EzyClientConfig config, boolean udp) {
        this.client = udp ? new EzyUTClient(config) : new EzyTcpClient(config);
        setupHandlers();
    }

    public static EzyClientConfig.Builder newConfigBuilder(String clientName) {
        return EzyClientConfig.builder()
            .clientName(clientName + "-" + System.nanoTime())
            .zoneName(JavaClientTestServer.ZONE_NAME)
            .reconnectConfigBuilder()
            .enable(false)
            .done();
    }

    private void setupHandlers() {
        EzySetup setup = client.setup();
        setup.addEventHandler(EzyEventType.CONNECTION_FAILURE, new EzyConnectionFailureHandler() {
            @Override
            protected void onConnectionFailed(EzyConnectionFailureEvent event) {
                error = "connection failure: " + event.getReason();
            }
        });
        setup.addDataHandler(EzyCommand.HANDSHAKE, new EzyHandshakeHandler() {
            @Override
            protected void preHandle(EzyArray data) {
                try {
                    super.preHandle(data);
                    handshaked = true;
                } catch (RuntimeException e) {
                    handshakeError = e;
                    error = "handshake error: " + e.getMessage();
                    throw e;
                }
            }

            @Override
            protected EzyRequest getLoginRequest() {
                return new EzyLoginRequest(
                    JavaClientTestServer.ZONE_NAME,
                    username,
                    "123456"
                );
            }
        });
        setup.addDataHandler(EzyCommand.LOGIN, new EzyLoginSuccessHandler() {
            @Override
            protected void handleLoginSuccess(EzyData responseData) {
                loggedIn = true;
            }
        });
        setup.addDataHandler(EzyCommand.LOGIN_ERROR, new EzyAbstractDataHandler() {
            @Override
            public void handle(EzyArray data) {
                error = "login error: " + data;
            }
        });
        setup.addDataHandler(EzyCommand.UDP_HANDSHAKE, new EzyUdpHandshakeHandler() {
            @Override
            protected void onAuthenticated(EzyArray data) {
                udpConnected = true;
            }

            @Override
            protected void onAuthenticationError(EzyArray data) {
                error = "udp handshake error: " + data;
            }
        });
        setup.addDataHandler(EzyCommand.PLUGIN_REQUEST, new EzyAbstractDataHandler() {
            @Override
            public void handle(EzyArray data) {
                responses.add(data.get(1, EzyArray.class).get(0, String.class));
            }
        });
    }

    public boolean connectAndLogin(String host, int port, String username) {
        this.username = username;
        this.handshaked = false;
        this.loggedIn = false;
        this.udpConnected = false;
        this.error = null;
        this.handshakeError = null;
        client.connect(host, port);
        return await(() -> loggedIn);
    }

    public String echo(int pluginId, String payload, boolean encrypted) {
        client.send(
            EzyCommand.PLUGIN_REQUEST,
            EzyEntityArrays.newArray(
                pluginId,
                EzyEntityArrays.newArray(false, encrypted, payload)
            ),
            encrypted
        );
        return awaitResponse();
    }

    public boolean udpConnect(int udpPort) {
        ((EzyUTClient) client).udpConnect(udpPort);
        return await(() -> udpConnected);
    }

    public String udpEcho(int pluginId, String payload) {
        ((EzyUTClient) client).udpSend(
            EzyCommand.PLUGIN_REQUEST,
            EzyEntityArrays.newArray(
                pluginId,
                EzyEntityArrays.newArray(true, false, payload)
            )
        );
        return awaitResponse();
    }

    public boolean disconnect() {
        client.disconnect(EzyDisconnectReason.CLOSE.getId());
        return await(() -> client.getStatus() == EzyConnectionStatus.DISCONNECTED);
    }

    private String awaitResponse() {
        await(() -> !responses.isEmpty());
        return responses.poll();
    }

    private boolean await(BooleanSupplier condition) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < TIMEOUT) {
            try {
                client.processEvents();
            } catch (RuntimeException e) {
                if (error == null) {
                    error = e.toString();
                }
            }
            if (condition.getAsBoolean()) {
                return true;
            }
            if (error != null) {
                return false;
            }
            sleep();
        }
        if (error == null) {
            error = "timeout after " + TIMEOUT + "ms";
        }
        return false;
    }

    private static void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isTcpCompressionEnable() {
        Object socketClient = client.getSocket();
        return ((EzySocketResponseApi) getFieldValue(socketClient, "responseApi"))
            .isCompressionEnable();
    }

    public boolean isUdpCompressionEnable() {
        Object udpClient = getFieldValue(client.getSocket(), "udpClient");
        return ((EzySocketResponseApi) getFieldValue(udpClient, "responseApi"))
            .isCompressionEnable();
    }

    private static Object getFieldValue(Object object, String fieldName) {
        Class<?> type = object.getClass();
        while (type != null) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalArgumentException(
            "not found field: " + fieldName + " in " + object.getClass()
        );
    }

    public EzyClient getClient() {
        return client;
    }

    public boolean isHandshaked() {
        return handshaked;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getError() {
        return error;
    }

    public RuntimeException getHandshakeError() {
        return handshakeError;
    }

    @Override
    public void close() {
        client.close();
    }
}
