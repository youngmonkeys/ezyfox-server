package com.tvd12.ezyfoxserver.nio.testing;

import com.tvd12.ezyfox.util.EzyReturner;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketSecureServerCreator;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactory;
import com.tvd12.ezyfoxserver.ssl.EzySslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import org.eclipse.jetty.server.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySslReloadWithoutRestartTest {

    private static final String HOST = "127.0.0.1";
    private static final String PASSWORD = "secret";
    private static final String OLD_SUBJECT = "CN=ezyfox-old";
    private static final String NEW_SUBJECT = "CN=ezyfox-new";

    private Path workingDir;
    private Path keyStoreFile;
    private AtomicInteger sslContextLoadCount;
    private EzySslContextProxy sslContextProxy;
    private final List<AutoCloseable> closeables = new CopyOnWriteArrayList<>();

    @BeforeMethod
    public void setup() throws Exception {
        workingDir = Files.createTempDirectory("ezyfox-ssl-reload");
        keyStoreFile = workingDir.resolve("ssl-keystore.jks");
        Path keyStorePasswordFile = workingDir.resolve("ssl-keystore-password.txt");
        Path certificatePasswordFile = workingDir.resolve("ssl-certificate-password.txt");
        Files.write(keyStorePasswordFile, PASSWORD.getBytes(StandardCharsets.UTF_8));
        Files.write(certificatePasswordFile, PASSWORD.getBytes(StandardCharsets.UTF_8));
        replaceKeyStore(OLD_SUBJECT);

        EzySslConfig sslConfig = new EzySslConfig() {
            @Override
            public String getKeyStoreFile() {
                return keyStoreFile.toString();
            }

            @Override
            public String getKeyStorePasswordFile() {
                return keyStorePasswordFile.toString();
            }

            @Override
            public String getCertificatePasswordFile() {
                return certificatePasswordFile.toString();
            }
        };
        EzySimpleSslContextFactory sslContextFactory = new EzySimpleSslContextFactory();
        sslContextLoadCount = new AtomicInteger();
        sslContextProxy = new EzySslContextProxy(() -> {
            sslContextLoadCount.incrementAndGet();
            return EzyReturner.returnWithException(
                () -> sslContextFactory.newSslContext(sslConfig)
            );
        });
    }

    @AfterMethod
    public void tearDown() throws Exception {
        for (int i = closeables.size() - 1; i >= 0; --i) {
            processWithLogException(closeables.get(i)::close);
        }
        closeables.clear();
        try (Stream<Path> paths = Files.walk(workingDir)) {
            paths.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    @Test
    public void socketReloadSslWithoutRestartTest() throws Exception {
        int port = startSecureSocketEchoServer();

        TlsClient oldClient = connect(port);
        Asserts.assertEquals(oldClient.getServerSubject(), OLD_SUBJECT);
        Asserts.assertEquals(oldClient.echo("before reload"), "before reload");

        replaceKeyStore(NEW_SUBJECT);
        TlsClient beforeReloadClient = connect(port);
        Asserts.assertEquals(beforeReloadClient.getServerSubject(), OLD_SUBJECT);

        sslContextProxy.reloadSsl();

        TlsClient newClient = connect(port);
        Asserts.assertEquals(newClient.getServerSubject(), NEW_SUBJECT);
        Asserts.assertEquals(newClient.echo("after reload"), "after reload");

        Asserts.assertEquals(oldClient.echo("old connection alive"), "old connection alive");
        Asserts.assertEquals(
            beforeReloadClient.echo("before reload connection alive"),
            "before reload connection alive"
        );
        Asserts.assertEquals(sslContextLoadCount.get(), 2);
    }

    @Test
    public void socketReloadSslFailedKeepOldCertificateTest() throws Exception {
        int port = startSecureSocketEchoServer();
        Asserts.assertEquals(connect(port).getServerSubject(), OLD_SUBJECT);

        Files.write(keyStoreFile, "invalid keystore".getBytes(StandardCharsets.UTF_8));

        Throwable e = Asserts.assertThrows(sslContextProxy::reloadSsl);

        Asserts.assertNotNull(e);
        TlsClient client = connect(port);
        Asserts.assertEquals(client.getServerSubject(), OLD_SUBJECT);
        Asserts.assertEquals(client.echo("still works"), "still works");

        replaceKeyStore(NEW_SUBJECT);
        sslContextProxy.reloadSsl();
        Asserts.assertEquals(connect(port).getServerSubject(), NEW_SUBJECT);
    }

    @Test
    public void webSocketReloadSslWithoutRestartTest() throws Exception {
        int port = getFreePort();
        int sslPort = getFreePort();
        EzySimpleWebSocketSetting setting = new EzySimpleWebSocketSetting();
        setting.setAddress(HOST);
        setting.setPort(port);
        setting.setSslPort(sslPort);
        setting.setManagementEnable(false);
        Server server = new EzyWebSocketSecureServerCreator(sslContextProxy)
            .setting(setting)
            .create();
        server.start();
        closeables.add(server::stop);

        TlsClient oldClient = connect(sslPort);
        Asserts.assertEquals(oldClient.getServerSubject(), OLD_SUBJECT);

        replaceKeyStore(NEW_SUBJECT);
        Asserts.assertEquals(connect(sslPort).getServerSubject(), OLD_SUBJECT);

        sslContextProxy.reloadSsl();

        TlsClient newClient = connect(sslPort);
        Asserts.assertEquals(newClient.getServerSubject(), NEW_SUBJECT);
        Asserts.assertTrue(
            newClient.httpGet("/ws").startsWith("HTTP/1.1")
        );
        Asserts.assertTrue(server.isRunning());
        Asserts.assertEquals(sslContextLoadCount.get(), 2);
    }

    private int startSecureSocketEchoServer() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(HOST, 0));
        closeables.add(serverChannel);
        Thread acceptThread = new Thread(() -> {
            while (serverChannel.isOpen()) {
                try {
                    SocketChannel socketChannel = serverChannel.accept();
                    closeables.add(socketChannel);
                    Thread handlerThread = new Thread(
                        () -> handleEchoConnection(socketChannel)
                    );
                    handlerThread.setDaemon(true);
                    handlerThread.start();
                } catch (IOException e) {
                    break;
                }
            }
        });
        acceptThread.setDaemon(true);
        acceptThread.start();
        return serverChannel.socket().getLocalPort();
    }

    private void handleEchoConnection(SocketChannel socketChannel) {
        EzyNioSecureSocketChannel channel = new EzyNioSecureSocketChannel(
            socketChannel,
            sslContextProxy,
            5000,
            1024
        );
        processWithLogException(() -> echo(socketChannel, channel));
        channel.close();
    }

    private void echo(
        SocketChannel socketChannel,
        EzyNioSecureSocketChannel channel
    ) throws Exception {
        channel.handshake();
        ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
        while (socketChannel.isOpen()) {
            buffer.clear();
            if (socketChannel.read(buffer) < 0) {
                break;
            }
            buffer.flip();
            byte[] plainBytes = channel.read(buffer);
            if (plainBytes.length == 0) {
                continue;
            }
            ByteBuffer packedBuffer = ByteBuffer.wrap(channel.pack(plainBytes));
            while (packedBuffer.hasRemaining()) {
                socketChannel.write(packedBuffer);
            }
        }
    }

    private TlsClient connect(int port) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] {new TrustAllManager()}, null);
        SSLSocket socket = (SSLSocket) sslContext.getSocketFactory().createSocket();
        socket.connect(new InetSocketAddress(HOST, port), 5000);
        socket.setSoTimeout(5000);
        socket.startHandshake();
        closeables.add(socket);
        return new TlsClient(socket);
    }

    private void replaceKeyStore(String subject) throws Exception {
        Path tempKeyStoreFile = workingDir.resolve("ssl-keystore-" + System.nanoTime() + ".jks");
        String keytool = System.getProperty("java.home")
            + File.separator + "bin" + File.separator + "keytool";
        Process process = new ProcessBuilder(
            keytool,
            "-genkeypair",
            "-alias", "ezyfox",
            "-keyalg", "RSA",
            "-keysize", "2048",
            "-validity", "1",
            "-dname", subject,
            "-storetype", "JKS",
            "-keystore", tempKeyStoreFile.toString(),
            "-storepass", PASSWORD,
            "-keypass", PASSWORD
        )
            .redirectErrorStream(true)
            .start();
        if (!process.waitFor(60, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            throw new IllegalStateException("keytool timeout");
        }
        if (process.exitValue() != 0) {
            throw new IllegalStateException(
                "keytool failed: " + readAll(process.getInputStream())
            );
        }
        Files.move(
            tempKeyStoreFile,
            keyStoreFile,
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE
        );
    }

    private static int getFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static String readAll(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        StringBuilder builder = new StringBuilder();
        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            builder.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
        }
        return builder.toString();
    }

    private static class TlsClient {
        private final SSLSocket socket;

        TlsClient(SSLSocket socket) {
            this.socket = socket;
        }

        String getServerSubject() throws Exception {
            X509Certificate certificate = (X509Certificate) socket
                .getSession()
                .getPeerCertificates()[0];
            return certificate.getSubjectX500Principal().getName();
        }

        String echo(String message) throws IOException {
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            InputStream inputStream = socket.getInputStream();
            byte[] answer = new byte[bytes.length];
            int offset = 0;
            while (offset < answer.length) {
                int read = inputStream.read(answer, offset, answer.length - offset);
                if (read < 0) {
                    throw new IOException("connection closed");
                }
                offset += read;
            }
            return new String(answer, StandardCharsets.UTF_8);
        }

        String httpGet(String path) throws IOException {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(
                ("GET " + path + " HTTP/1.1\r\nHost: " + HOST + "\r\nConnection: close\r\n\r\n")
                    .getBytes(StandardCharsets.UTF_8)
            );
            outputStream.flush();
            return readAll(socket.getInputStream());
        }
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
