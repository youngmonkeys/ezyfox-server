package com.tvd12.ezyfoxserver.nio.testing.compression;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyTcpClient;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzySslType;
import com.tvd12.ezyfoxserver.client.entity.EzyPlugin;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyAbstractDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionFailureHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyHandshakeHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginSuccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyPluginInfoHandler;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.request.EzyPluginInfoRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.setup.EzySetup;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class EzyRemoteCompressionClient {

    private static final String ZONE_NAME = "test";
    private static final long CASE_TIMEOUT = 20000;
    private static final int TEXT_SIZE = 6000;
    private static final int UPLOAD_SIZE = 6000;
    private static final int RANDOM_SIZE = 3000;
    private static final int SMALL_TEXT_SIZE = 10;
    private static final byte[] DEFLATE_TAIL = {0x00, 0x00, (byte) 0xFF, (byte) 0xFF};

    private final String host;
    private final List<String> failures = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EzyRemoteCompressionClient(String host) {
        this.host = host;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.security.enableAIAcaIssuers", "true");
        String host = args.length > 0 ? args[0] : "stackask.com";
        int exitCode = new EzyRemoteCompressionClient(host).run();
        System.exit(exitCode);
    }

    private int run() throws Exception {
        testSocket();
        testWebsocket();
        System.out.println();
        if (failures.isEmpty()) {
            System.out.println("ALL CASES PASSED");
            return 0;
        }
        System.out.println("FAILED CASES:");
        failures.forEach(it -> System.out.println("  - " + it));
        return 1;
    }

    private void testSocket() throws Exception {
        title("socket 1. no ssl, client does not request compression, port 3105");
        SocketResult plain = runSocket(new SocketCase("plain-off", 3105, false, null, false, false));
        checkSocketPayloads("plain-off", plain);
        check("plain-off: handshake compression = false", !plain.handshakeCompression, plain.describe());
        checkNotCompressed("plain-off", plain, false);

        title("socket 2. no ssl, client requests compression (server default), port 3105");
        SocketResult plainOn = runSocket(new SocketCase("plain-on", 3105, false, null, true, false));
        checkSocketPayloads("plain-on", plainOn);
        check("plain-on: handshake compression = true (server default enabled)", plainOn.handshakeCompression, plainOn.describe());
        checkCompressed("plain-on", plainOn, false);
        compareBytes("plain", plain, plainOn);

        title("socket 3. customization ssl + encrypted messages, no compression, port 3106");
        SocketResult customOff = runSocket(new SocketCase("custom-off", 3106, true, EzySslType.CUSTOMIZATION, false, true));
        checkSocketPayloads("custom-off", customOff);
        check("custom-off: handshake compression = false", !customOff.handshakeCompression, customOff.describe());
        checkNotCompressed("custom-off", customOff, true);

        title("socket 4. customization ssl + encrypted messages + compression, port 3106");
        SocketResult customOn = runSocket(new SocketCase("custom-on", 3106, true, EzySslType.CUSTOMIZATION, true, true));
        checkSocketPayloads("custom-on", customOn);
        check("custom-on: handshake compression = true", customOn.handshakeCompression, customOn.describe());
        checkCompressed("custom-on", customOn, true);
        compareBytes("customization", customOff, customOn);

        title("socket 5. certification ssl (TLS), no compression, port 3107");
        SocketResult certOff = runSocket(new SocketCase("cert-off", 3107, true, EzySslType.CERTIFICATION, false, false));
        checkSocketPayloads("cert-off", certOff);
        check("cert-off: handshake compression = false", !certOff.handshakeCompression, certOff.describe());

        title("socket 6. certification ssl (TLS) + compression, port 3107");
        SocketResult certOn = runSocket(new SocketCase("cert-on", 3107, true, EzySslType.CERTIFICATION, true, false));
        checkSocketPayloads("cert-on", certOn);
        check("cert-on: handshake compression = true", certOn.handshakeCompression, certOn.describe());
        compareBytes("certification (TLS bytes on wire)", certOff, certOn);

        title("socket 7. server compression-enable=false, client requests compression, port 3108");
        SocketResult disabled = runSocket(new SocketCase("server-disabled", 3108, false, null, true, false));
        checkSocketPayloads("server-disabled", disabled);
        check("server-disabled: handshake compression = false", !disabled.handshakeCompression, disabled.describe());
        checkNotCompressed("server-disabled", disabled, false);
    }

    private void testWebsocket() throws Exception {
        title("websocket 1. ws, client offers permessage-deflate (server default), port 3205");
        WsResult wsOn = runWebsocket("ws-on", 3205, false, true);
        checkWsPayloads("ws-on", wsOn);
        check("ws-on: server accepts permessage-deflate by default", wsOn.negotiated, "extensions: " + wsOn.extensions);
        checkWsCompressed("ws-on", wsOn);

        title("websocket 2. ws, client does not offer compression, port 3205");
        WsResult wsOff = runWebsocket("ws-off", 3205, false, false);
        checkWsPayloads("ws-off", wsOff);
        check("ws-off: no extension negotiated", !wsOff.negotiated, "extensions: " + wsOff.extensions);
        checkWsNotCompressed("ws-off", wsOff);
        compareWsBytes("ws", wsOff, wsOn);

        title("websocket 3. wss (TLS), client offers permessage-deflate, port 3206");
        WsResult wssOn = runWebsocket("wss-on", 3206, true, true);
        checkWsPayloads("wss-on", wssOn);
        check("wss-on: server accepts permessage-deflate", wssOn.negotiated, "extensions: " + wssOn.extensions);
        checkWsCompressed("wss-on", wssOn);

        title("websocket 4. wss (TLS), client does not offer compression, port 3206");
        WsResult wssOff = runWebsocket("wss-off", 3206, true, false);
        checkWsPayloads("wss-off", wssOff);
        check("wss-off: no extension negotiated", !wssOff.negotiated, "extensions: " + wssOff.extensions);
        checkWsNotCompressed("wss-off", wssOff);
        compareWsBytes("wss (websocket frame bytes inside TLS)", wssOff, wssOn);

        title("websocket 5. ws, server compression-enable=false, client offers permessage-deflate, port 3208");
        WsResult wsDisabled = runWebsocket("ws-server-disabled", 3208, false, true);
        checkWsPayloads("ws-server-disabled", wsDisabled);
        check("ws-server-disabled: server rejects permessage-deflate", !wsDisabled.negotiated, "extensions: " + wsDisabled.extensions);
        checkWsNotCompressed("ws-server-disabled", wsDisabled);
    }

    private SocketResult runSocket(SocketCase socketCase) throws Exception {
        SocketResult result = new SocketResult();
        boolean parseFrames = socketCase.sslType != EzySslType.CERTIFICATION;
        try (TcpProxy proxy = new TcpProxy(host, socketCase.port, parseFrames)) {
            EzyClientConfig config = EzyClientConfig.builder()
                .clientName("compression-" + socketCase.name + "-" + System.nanoTime())
                .zoneName(ZONE_NAME)
                .enableSSL(socketCase.ssl)
                .sslType(socketCase.sslType != null ? socketCase.sslType : EzySslType.CUSTOMIZATION)
                .verifySslHandshake(socketCase.sslType == EzySslType.CUSTOMIZATION)
                .sslHandshakeHostName(host)
                .enableCompression(socketCase.compression)
                .pingConfigBuilder()
                .pingPeriod(600000)
                .done()
                .reconnectConfigBuilder()
                .enable(false)
                .done()
                .build();
            EzyClient client = new EzyTcpClient(config);
            EzySetup setup = client.setup();
            setup.addEventHandler(EzyEventType.CONNECTION_FAILURE, new EzyConnectionFailureHandler() {
                @Override
                protected void onConnectionFailed(EzyConnectionFailureEvent event) {
                    result.error = "connection failure: " + event.getReason();
                }
            });
            setup.addDataHandler(EzyCommand.HANDSHAKE, new EzyHandshakeHandler() {
                @Override
                protected void preHandle(EzyArray data) {
                    result.handshakeCompression = data.get(4, boolean.class, false);
                    super.preHandle(data);
                }

                @Override
                protected EzyRequest getLoginRequest() {
                    return new EzyLoginRequest(ZONE_NAME, "c_" + socketCase.name.replace('-', '_'), "123456");
                }
            });
            setup.addDataHandler(EzyCommand.LOGIN, new EzyLoginSuccessHandler() {
                @Override
                protected void handleLoginSuccess(com.tvd12.ezyfox.entity.EzyData responseData) {
                    result.loggedIn = true;
                    client.send(new EzyPluginInfoRequest(EzyRemoteEchoPluginEntry.PLUGIN_NAME));
                }
            });
            setup.addDataHandler(EzyCommand.LOGIN_ERROR, new EzyAbstractDataHandler() {
                @Override
                public void handle(EzyArray data) {
                    result.error = "login error: " + data;
                }
            });
            setup.addDataHandler(EzyCommand.PLUGIN_INFO, new EzyPluginInfoHandler() {
                @Override
                protected void postHandle(EzyPlugin plugin, EzyArray data) {
                    result.upBefore = proxy.up.snapshot();
                    result.downBefore = proxy.down.snapshot();
                    sendEchoRequest(plugin, 0, socketCase.encrypted);
                }
            });
            setup.setupPlugin(EzyRemoteEchoPluginEntry.PLUGIN_NAME)
                .addDataHandler(EzyRemoteEchoPluginEntry.COMMAND, (plugin, data) -> {
                    result.responses.add((EzyObject) data);
                    if (result.responses.size() < 3) {
                        sendEchoRequest(plugin, result.responses.size(), socketCase.encrypted);
                    }
                });
            long startTime = System.currentTimeMillis();
            client.connect("127.0.0.1", proxy.getPort());
            while (result.responses.size() < 3
                && result.error == null
                && System.currentTimeMillis() - startTime < CASE_TIMEOUT) {
                try {
                    client.processEvents();
                } catch (Exception e) {
                    result.error = e.toString();
                }
                Thread.sleep(5);
            }
            Thread.sleep(200);
            result.elapsed = System.currentTimeMillis() - startTime;
            if (result.responses.size() < 3 && result.error == null) {
                result.error = "timeout, responses: " + result.responses.size();
            }
            result.upFrames = proxy.up.framesAfter(result.upBefore);
            result.downFrames = proxy.down.framesAfter(result.downBefore);
            result.upBytes = proxy.up.bytes.get() - (result.upBefore != null ? result.upBefore.bytes : 0);
            result.downBytes = proxy.down.bytes.get() - (result.downBefore != null ? result.downBefore.bytes : 0);
            client.close();
        }
        info(socketCase.name + ": " + result.describe());
        return result;
    }

    private static void sendEchoRequest(EzyPlugin plugin, int index, boolean encrypted) {
        EzyObject params;
        if (index == 0) {
            params = EzyEntityFactory.newObjectBuilder()
                .append("upload", EzyRemoteEchoPluginEntry.newText(UPLOAD_SIZE))
                .append("textSize", TEXT_SIZE)
                .append("encrypted", encrypted)
                .build();
        } else if (index == 1) {
            params = EzyEntityFactory.newObjectBuilder()
                .append("upload", "hi")
                .append("textSize", SMALL_TEXT_SIZE)
                .append("encrypted", encrypted)
                .build();
        } else {
            params = EzyEntityFactory.newObjectBuilder()
                .append("randomSize", RANDOM_SIZE)
                .append("encrypted", encrypted)
                .build();
        }
        plugin.send(EzyRemoteEchoPluginEntry.COMMAND, params, encrypted);
    }

    private void checkSocketPayloads(String name, SocketResult result) {
        check(name + ": login + 3 echo responses", result.loggedIn && result.responses.size() == 3, result.describe());
        if (result.responses.size() < 3) {
            return;
        }
        EzyObject big = result.responses.get(0);
        EzyObject small = result.responses.get(1);
        EzyObject random = result.responses.get(2);
        String upload = EzyRemoteEchoPluginEntry.newText(UPLOAD_SIZE);
        String text = EzyRemoteEchoPluginEntry.newText(TEXT_SIZE);
        check(
            name + ": server decoded the " + UPLOAD_SIZE + " chars upload correctly",
            big.get("uploadLength", int.class) == upload.length() && big.get("uploadHash", int.class) == upload.hashCode(),
            "uploadLength=" + big.get("uploadLength", int.class)
        );
        check(
            name + ": client decoded the " + TEXT_SIZE + " chars response correctly",
            text.equals(big.get("text", String.class)),
            "text length=" + big.get("text", String.class).length()
        );
        check(name + ": small response correct", small.get("text", String.class).length() == SMALL_TEXT_SIZE, "" + small);
        check(name + ": random response correct", random.get("random", byte[].class).length == RANDOM_SIZE, "");
    }

    private void checkCompressed(String name, SocketResult result, boolean encrypted) {
        if (result.downFrames.size() != 3 || result.upFrames.size() != 3) {
            check(name + ": 3 frames each direction", false, "up=" + result.upFrames + ", down=" + result.downFrames);
            return;
        }
        Frame upBig = result.upFrames.get(0);
        Frame downBig = result.downFrames.get(0);
        Frame downSmall = result.downFrames.get(1);
        Frame downRandom = result.downFrames.get(2);
        info(name + ": up frames " + result.upFrames + ", down frames " + result.downFrames);
        check(name + ": upload frame has compressed bit", upBig.compressed(), upBig.toString());
        check(name + ": upload frame really smaller (" + upBig.size + " < " + UPLOAD_SIZE + "/4)", upBig.size < UPLOAD_SIZE / 4, upBig.toString());
        check(name + ": big response frame has compressed bit", downBig.compressed(), downBig.toString());
        check(name + ": big response frame really smaller (" + downBig.size + " < " + TEXT_SIZE + "/4)", downBig.size < TEXT_SIZE / 4, downBig.toString());
        check(name + ": small response (< threshold) not compressed", !downSmall.compressed(), downSmall.toString());
        check(name + ": random (incompressible) response sent uncompressed", !downRandom.compressed() && downRandom.size >= RANDOM_SIZE, downRandom.toString());
        check(name + ": encrypted bit = " + encrypted, upBig.encrypted() == encrypted && downBig.encrypted() == encrypted, upBig + " " + downBig);
    }

    private void checkNotCompressed(String name, SocketResult result, boolean encrypted) {
        if (result.downFrames.size() != 3 || result.upFrames.size() != 3) {
            check(name + ": 3 frames each direction", false, "up=" + result.upFrames + ", down=" + result.downFrames);
            return;
        }
        info(name + ": up frames " + result.upFrames + ", down frames " + result.downFrames);
        boolean anyCompressed = false;
        for (Frame frame : result.upFrames) {
            anyCompressed |= frame.compressed();
        }
        for (Frame frame : result.downFrames) {
            anyCompressed |= frame.compressed();
        }
        check(name + ": no frame has compressed bit", !anyCompressed, "");
        check(name + ": big frames keep full size", result.upFrames.get(0).size >= UPLOAD_SIZE && result.downFrames.get(0).size >= TEXT_SIZE, "");
        check(name + ": encrypted bit = " + encrypted, result.downFrames.get(0).encrypted() == encrypted, result.downFrames.get(0).toString());
    }

    private void compareBytes(String name, SocketResult off, SocketResult on) {
        info(name + ": wire bytes for 3 echo requests, off: up=" + off.upBytes + " down=" + off.downBytes
            + ", on: up=" + on.upBytes + " down=" + on.downBytes);
        check(
            name + ": compression really reduces wire bytes (down on < 70% off)",
            on.downBytes > 0 && on.downBytes * 10 < off.downBytes * 7,
            "off=" + off.downBytes + ", on=" + on.downBytes
        );
        check(
            name + ": compression really reduces upload bytes (up on < 50% off)",
            on.upBytes > 0 && on.upBytes * 2 < off.upBytes,
            "off=" + off.upBytes + ", on=" + on.upBytes
        );
    }

    private WsResult runWebsocket(String name, int port, boolean tls, boolean offerDeflate) {
        WsResult result = new WsResult();
        try (WsRawClient client = new WsRawClient(host, port, tls, offerDeflate)) {
            result.extensions = client.extensions;
            result.negotiated = client.deflateNegotiated;
            client.sendJson(Arrays.asList(11, Arrays.asList(UUID.randomUUID().toString(), null, "WEB", "1.0", false, null)));
            List<Object> handshake = client.readCommand(11);
            result.handshakeCompression = ((List<?>) handshake.get(1)).size() > 4
                && Boolean.TRUE.equals(((List<?>) handshake.get(1)).get(4));
            client.sendJson(Arrays.asList(20, Arrays.asList(ZONE_NAME, "w_" + name.replace('-', '_'), "123456", null)));
            client.readCommand(20);
            result.loggedIn = true;
            client.sendJson(Arrays.asList(40, Collections.singletonList(EzyRemoteEchoPluginEntry.PLUGIN_NAME)));
            List<Object> pluginInfo = client.readCommand(40);
            int pluginId = ((Number) ((List<?>) pluginInfo.get(1)).get(0)).intValue();
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("upload", EzyRemoteEchoPluginEntry.newText(UPLOAD_SIZE));
            params.put("textSize", TEXT_SIZE);
            long upBefore = client.upBytes.get();
            long downBefore = client.downBytes.get();
            result.upMessage = client.sendJson(Arrays.asList(41, Arrays.asList(pluginId, Arrays.asList(EzyRemoteEchoPluginEntry.COMMAND, params))));
            List<Object> response = client.readCommand(41);
            result.downMessage = client.lastMessage;
            result.upBytes = client.upBytes.get() - upBefore;
            result.downBytes = client.downBytes.get() - downBefore;
            @SuppressWarnings("unchecked")
            Map<String, Object> echo = (Map<String, Object>) ((List<?>) ((List<?>) response.get(1)).get(1)).get(1);
            result.echo = echo;
        } catch (Exception e) {
            result.error = e.toString();
        }
        info(name + ": " + result.describe());
        return result;
    }

    private void checkWsPayloads(String name, WsResult result) {
        check(name + ": login + echo response", result.loggedIn && result.echo != null, result.describe());
        if (result.echo == null) {
            return;
        }
        String upload = EzyRemoteEchoPluginEntry.newText(UPLOAD_SIZE);
        String text = EzyRemoteEchoPluginEntry.newText(TEXT_SIZE);
        check(
            name + ": server decoded the " + UPLOAD_SIZE + " chars upload correctly",
            ((Number) result.echo.get("uploadLength")).intValue() == upload.length()
                && ((Number) result.echo.get("uploadHash")).intValue() == upload.hashCode(),
            "" + result.echo.get("uploadLength")
        );
        check(name + ": client decoded the " + TEXT_SIZE + " chars response correctly", text.equals(result.echo.get("text")), "");
    }

    private void checkWsCompressed(String name, WsResult result) {
        if (result.upMessage == null || result.downMessage == null) {
            return;
        }
        info(name + ": up " + result.upMessage + ", down " + result.downMessage);
        check(name + ": upload frame RSV1 (compressed) set", result.upMessage.rsv1, result.upMessage.toString());
        check(name + ": response frame RSV1 (compressed) set", result.downMessage.rsv1, result.downMessage.toString());
        check(
            name + ": response really smaller on wire (" + result.downMessage.wireLength + " < " + result.downMessage.textLength + "/4)",
            result.downMessage.wireLength * 4 < result.downMessage.textLength,
            result.downMessage.toString()
        );
    }

    private void checkWsNotCompressed(String name, WsResult result) {
        if (result.upMessage == null || result.downMessage == null) {
            return;
        }
        info(name + ": up " + result.upMessage + ", down " + result.downMessage);
        check(name + ": response frame RSV1 not set", !result.downMessage.rsv1, result.downMessage.toString());
        check(name + ": response keeps full size on wire", result.downMessage.wireLength >= TEXT_SIZE, result.downMessage.toString());
    }

    private void compareWsBytes(String name, WsResult off, WsResult on) {
        info(name + ": wire bytes for echo, off: up=" + off.upBytes + " down=" + off.downBytes
            + ", on: up=" + on.upBytes + " down=" + on.downBytes);
        check(
            name + ": permessage-deflate really reduces wire bytes (down on < 30% off)",
            on.downBytes > 0 && on.downBytes * 10 < off.downBytes * 3,
            "off=" + off.downBytes + ", on=" + on.downBytes
        );
        check(
            name + ": permessage-deflate really reduces upload bytes (up on < 30% off)",
            on.upBytes > 0 && on.upBytes * 10 < off.upBytes * 3,
            "off=" + off.upBytes + ", on=" + on.upBytes
        );
    }

    private void check(String name, boolean passed, String detail) {
        if (passed) {
            System.out.println("  [PASS] " + name);
        } else {
            System.out.println("  [FAIL] " + name + " -> " + detail);
            failures.add(name + " -> " + detail);
        }
    }

    private static void title(String title) {
        System.out.println();
        System.out.println("=== " + title);
    }

    private static void info(String message) {
        System.out.println("  [INFO] " + message);
    }

    private static class SocketCase {
        private final String name;
        private final int port;
        private final boolean ssl;
        private final EzySslType sslType;
        private final boolean compression;
        private final boolean encrypted;

        private SocketCase(
            String name,
            int port,
            boolean ssl,
            EzySslType sslType,
            boolean compression,
            boolean encrypted
        ) {
            this.name = name;
            this.port = port;
            this.ssl = ssl;
            this.sslType = sslType;
            this.compression = compression;
            this.encrypted = encrypted;
        }
    }

    private static class SocketResult {
        private volatile boolean handshakeCompression;
        private volatile boolean loggedIn;
        private volatile String error;
        private volatile long elapsed;
        private volatile Snapshot upBefore;
        private volatile Snapshot downBefore;
        private final List<EzyObject> responses = Collections.synchronizedList(new ArrayList<>());
        private List<Frame> upFrames = Collections.emptyList();
        private List<Frame> downFrames = Collections.emptyList();
        private long upBytes;
        private long downBytes;

        private String describe() {
            return "handshakeCompression=" + handshakeCompression
                + ", loggedIn=" + loggedIn
                + ", responses=" + responses.size()
                + ", elapsed=" + elapsed + "ms"
                + (error != null ? ", error=" + error : "");
        }
    }

    private static class WsResult {
        private String extensions;
        private boolean negotiated;
        private boolean handshakeCompression;
        private boolean loggedIn;
        private Map<String, Object> echo;
        private WsMessage upMessage;
        private WsMessage downMessage;
        private long upBytes;
        private long downBytes;
        private String error;

        private String describe() {
            return "extensions=" + extensions
                + ", loggedIn=" + loggedIn
                + ", echo=" + (echo != null)
                + (error != null ? ", error=" + error : "");
        }
    }

    private static class Frame {
        private final byte header;
        private final int size;

        private Frame(byte header, int size) {
            this.header = header;
            this.size = size;
        }

        private boolean encrypted() {
            return (header & (1 << 1)) != 0;
        }

        private boolean compressed() {
            return (header & (1 << 2)) != 0;
        }

        @Override
        public String toString() {
            return "{size=" + size + (compressed() ? ",zip" : "") + (encrypted() ? ",enc" : "") + "}";
        }
    }

    private static class Snapshot {
        private final long bytes;
        private final int frames;

        private Snapshot(long bytes, int frames) {
            this.bytes = bytes;
            this.frames = frames;
        }
    }

    private static class Direction {
        private final boolean parse;
        private final AtomicLong bytes = new AtomicLong();
        private final List<Frame> frames = new ArrayList<>();
        private byte[] pending = new byte[0];
        private int pendingLength;

        private Direction(boolean parse) {
            this.parse = parse;
        }

        private synchronized void onBytes(byte[] data, int length) {
            bytes.addAndGet(length);
            if (!parse) {
                return;
            }
            if (pendingLength + length > pending.length) {
                pending = Arrays.copyOf(pending, Math.max(pending.length * 2, pendingLength + length));
            }
            System.arraycopy(data, 0, pending, pendingLength, length);
            pendingLength += length;
            int offset = 0;
            while (pendingLength - offset >= 1) {
                byte header = pending[offset];
                int sizeLength = (header & 1) != 0 ? 4 : 2;
                if (pendingLength - offset < 1 + sizeLength) {
                    break;
                }
                int size = 0;
                for (int i = 0; i < sizeLength; ++i) {
                    size = (size << 8) | (pending[offset + 1 + i] & 0xFF);
                }
                if (pendingLength - offset < 1 + sizeLength + size) {
                    break;
                }
                frames.add(new Frame(header, size));
                offset += 1 + sizeLength + size;
            }
            System.arraycopy(pending, offset, pending, 0, pendingLength - offset);
            pendingLength -= offset;
        }

        private synchronized Snapshot snapshot() {
            return new Snapshot(bytes.get(), frames.size());
        }

        private synchronized List<Frame> framesAfter(Snapshot snapshot) {
            int from = snapshot != null ? snapshot.frames : 0;
            return new ArrayList<>(frames.subList(Math.min(from, frames.size()), frames.size()));
        }
    }

    private static class TcpProxy implements Closeable {
        private final ServerSocket serverSocket;
        private final Direction up;
        private final Direction down;
        private final List<Socket> sockets = Collections.synchronizedList(new ArrayList<>());

        private TcpProxy(String remoteHost, int remotePort, boolean parse) throws IOException {
            this.serverSocket = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
            this.up = new Direction(parse);
            this.down = new Direction(parse);
            Thread acceptor = new Thread(() -> {
                try {
                    Socket local = serverSocket.accept();
                    Socket remote = new Socket(remoteHost, remotePort);
                    sockets.add(local);
                    sockets.add(remote);
                    pump(local.getInputStream(), remote.getOutputStream(), up);
                    pump(remote.getInputStream(), local.getOutputStream(), down);
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }, "proxy-acceptor");
            acceptor.setDaemon(true);
            acceptor.start();
        }

        private void pump(InputStream in, OutputStream out, Direction direction) {
            Thread thread = new Thread(() -> {
                byte[] buffer = new byte[16384];
                try {
                    int read;
                    while ((read = in.read(buffer)) > 0) {
                        direction.onBytes(buffer, read);
                        out.write(buffer, 0, read);
                        out.flush();
                    }
                } catch (IOException ignored) {
                } finally {
                    closeQuietly();
                }
            }, "proxy-pump");
            thread.setDaemon(true);
            thread.start();
        }

        private int getPort() {
            return serverSocket.getLocalPort();
        }

        private void closeQuietly() {
            for (Socket socket : new ArrayList<>(sockets)) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        @Override
        public void close() throws IOException {
            serverSocket.close();
            closeQuietly();
        }
    }

    private static class WsMessage {
        private final boolean rsv1;
        private final int wireLength;
        private final int textLength;

        private WsMessage(boolean rsv1, int wireLength, int textLength) {
            this.rsv1 = rsv1;
            this.wireLength = wireLength;
            this.textLength = textLength;
        }

        @Override
        public String toString() {
            return "{rsv1=" + rsv1 + ", wire=" + wireLength + ", text=" + textLength + "}";
        }
    }

    private class WsRawClient implements Closeable {
        private final Socket socket;
        private final InputStream in;
        private final OutputStream out;
        private final String extensions;
        private final boolean deflateNegotiated;
        private final boolean clientNoContextTakeover;
        private final boolean serverNoContextTakeover;
        private final Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
        private final Inflater inflater = new Inflater(true);
        private final AtomicLong upBytes = new AtomicLong();
        private final AtomicLong downBytes = new AtomicLong();
        private final SecureRandom random = new SecureRandom();
        private WsMessage lastMessage;

        private WsRawClient(String host, int port, boolean tls, boolean offerDeflate) throws Exception {
            if (tls) {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, null, null);
                SSLSocket sslSocket = (SSLSocket) context.getSocketFactory().createSocket(host, port);
                SSLParameters parameters = sslSocket.getSSLParameters();
                parameters.setEndpointIdentificationAlgorithm("HTTPS");
                parameters.setServerNames(Collections.singletonList(new SNIHostName(host)));
                sslSocket.setSSLParameters(parameters);
                sslSocket.startHandshake();
                socket = sslSocket;
            } else {
                socket = new Socket(host, port);
            }
            socket.setSoTimeout(15000);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] keyBytes = new byte[16];
            random.nextBytes(keyBytes);
            StringBuilder request = new StringBuilder()
                .append("GET /ws HTTP/1.1\r\n")
                .append("Host: ").append(host).append(':').append(port).append("\r\n")
                .append("Upgrade: websocket\r\n")
                .append("Connection: Upgrade\r\n")
                .append("Sec-WebSocket-Key: ").append(Base64.getEncoder().encodeToString(keyBytes)).append("\r\n")
                .append("Sec-WebSocket-Version: 13\r\n");
            if (offerDeflate) {
                request.append("Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits\r\n");
            }
            request.append("\r\n");
            out.write(request.toString().getBytes(StandardCharsets.US_ASCII));
            out.flush();
            String response = readHttpResponse();
            if (!response.startsWith("HTTP/1.1 101")) {
                throw new IOException("upgrade failed: " + response.split("\r\n")[0]);
            }
            String ext = null;
            for (String line : response.split("\r\n")) {
                if (line.toLowerCase(Locale.ENGLISH).startsWith("sec-websocket-extensions:")) {
                    ext = line.substring(line.indexOf(':') + 1).trim();
                }
            }
            extensions = ext;
            String lowerExt = ext != null ? ext.toLowerCase(Locale.ENGLISH) : "";
            deflateNegotiated = lowerExt.contains("permessage-deflate");
            clientNoContextTakeover = lowerExt.contains("client_no_context_takeover");
            serverNoContextTakeover = lowerExt.contains("server_no_context_takeover");
        }

        private String readHttpResponse() throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int matched = 0;
            byte[] end = {'\r', '\n', '\r', '\n'};
            while (matched < 4) {
                int b = in.read();
                if (b < 0) {
                    throw new IOException("connection closed during upgrade");
                }
                buffer.write(b);
                matched = b == end[matched] ? matched + 1 : (b == '\r' ? 1 : 0);
            }
            return buffer.toString("US-ASCII");
        }

        private WsMessage sendJson(Object value) throws Exception {
            byte[] text = objectMapper.writeValueAsBytes(value);
            byte[] payload = text;
            boolean compressed = deflateNegotiated;
            if (compressed) {
                if (clientNoContextTakeover) {
                    deflater.reset();
                }
                deflater.setInput(text);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] chunk = new byte[8192];
                int count;
                do {
                    count = deflater.deflate(chunk, 0, chunk.length, Deflater.SYNC_FLUSH);
                    buffer.write(chunk, 0, count);
                } while (count == chunk.length);
                byte[] deflated = buffer.toByteArray();
                payload = Arrays.copyOf(deflated, deflated.length - DEFLATE_TAIL.length);
            }
            writeFrame(payload, compressed);
            return new WsMessage(compressed, payload.length, text.length);
        }

        private void writeFrame(byte[] payload, boolean rsv1) throws IOException {
            ByteArrayOutputStream frame = new ByteArrayOutputStream();
            frame.write(0x80 | (rsv1 ? 0x40 : 0) | 0x1);
            int length = payload.length;
            if (length < 126) {
                frame.write(0x80 | length);
            } else if (length < 65536) {
                frame.write(0x80 | 126);
                frame.write(length >>> 8);
                frame.write(length & 0xFF);
            } else {
                frame.write(0x80 | 127);
                for (int i = 7; i >= 0; --i) {
                    frame.write((int) (((long) length >>> (8 * i)) & 0xFF));
                }
            }
            byte[] mask = new byte[4];
            random.nextBytes(mask);
            frame.write(mask);
            for (int i = 0; i < length; ++i) {
                frame.write(payload[i] ^ mask[i % 4]);
            }
            byte[] bytes = frame.toByteArray();
            upBytes.addAndGet(bytes.length);
            out.write(bytes);
            out.flush();
        }

        private List<Object> readCommand(int command) throws Exception {
            while (true) {
                String text = readMessage();
                List<?> message = objectMapper.readValue(text, List.class);
                int cmd = ((Number) message.get(0)).intValue();
                if (cmd == command) {
                    @SuppressWarnings("unchecked")
                    List<Object> answer = (List<Object>) message;
                    return answer;
                }
                if (cmd == 14) {
                    throw new IOException("disconnected by server: " + text);
                }
            }
        }

        private String readMessage() throws Exception {
            ByteArrayOutputStream payload = new ByteArrayOutputStream();
            boolean rsv1 = false;
            boolean first = true;
            while (true) {
                int b0 = readByte();
                int b1 = readByte();
                downBytes.addAndGet(2);
                boolean fin = (b0 & 0x80) != 0;
                int opcode = b0 & 0x0F;
                long length = b1 & 0x7F;
                if (length == 126) {
                    length = (readByte() << 8) | readByte();
                    downBytes.addAndGet(2);
                } else if (length == 127) {
                    length = 0;
                    for (int i = 0; i < 8; ++i) {
                        length = (length << 8) | readByte();
                    }
                    downBytes.addAndGet(8);
                }
                byte[] data = new byte[(int) length];
                int offset = 0;
                while (offset < data.length) {
                    int read = in.read(data, offset, data.length - offset);
                    if (read < 0) {
                        throw new IOException("connection closed");
                    }
                    offset += read;
                }
                downBytes.addAndGet(length);
                if (opcode == 0x8) {
                    throw new IOException("closed by server");
                }
                if (opcode == 0x9 || opcode == 0xA) {
                    continue;
                }
                if (first) {
                    rsv1 = (b0 & 0x40) != 0;
                    first = false;
                }
                payload.write(data);
                if (fin) {
                    break;
                }
            }
            byte[] wire = payload.toByteArray();
            byte[] text = wire;
            if (rsv1) {
                if (serverNoContextTakeover) {
                    inflater.reset();
                }
                byte[] input = Arrays.copyOf(wire, wire.length + DEFLATE_TAIL.length);
                System.arraycopy(DEFLATE_TAIL, 0, input, wire.length, DEFLATE_TAIL.length);
                inflater.setInput(input);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] chunk = new byte[8192];
                while (true) {
                    int count = inflater.inflate(chunk);
                    if (count == 0) {
                        if (inflater.needsInput() || inflater.finished()) {
                            break;
                        }
                        if (inflater.needsDictionary()) {
                            throw new IOException("inflater needs dictionary");
                        }
                    }
                    buffer.write(chunk, 0, count);
                }
                text = buffer.toByteArray();
            }
            lastMessage = new WsMessage(rsv1, wire.length, text.length);
            return new String(text, StandardCharsets.UTF_8);
        }

        private int readByte() throws IOException {
            int b = in.read();
            if (b < 0) {
                throw new IOException("connection closed");
            }
            return b;
        }

        @Override
        public void close() throws IOException {
            socket.close();
            deflater.end();
            inflater.end();
        }
    }
}
