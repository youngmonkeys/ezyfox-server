package com.tvd12.ezyfoxserver.nio.testing.ssl;

import com.tvd12.ezyfox.util.EzyReturner;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactory;
import com.tvd12.ezyfoxserver.ssl.EzySslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySslSocketPerformanceTest {

    private static final String PASSWORD = "secret";
    private static final int CPUS = Runtime.getRuntime().availableProcessors();
    private static final int MAX_REQUEST_SIZE = 1024 * 1024;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final int SERVER_READ_BUFFER_SIZE = 64 * 1024;
    private static final String ROW_FORMAT =
        "%-52s %8s %7s %10s %9s %10s %10s %10s %10s%n";

    private final Map<String, String> options;
    private final String host;
    private final SSLContext clientSslContext;
    private final EzySimpleSslContextFactory sslContextFactory =
        new EzySimpleSslContextFactory();
    private final List<AutoCloseable> closeables = new CopyOnWriteArrayList<>();
    private final Map<String, AtomicLong> serverExceptions = new ConcurrentHashMap<>();
    private Path workingDir;
    private EzySslConfig sslConfig;

    public EzySslSocketPerformanceTest(Map<String, String> options) throws Exception {
        this.options = options;
        this.host = option("host", "127.0.0.1");
        this.clientSslContext = SSLContext.getInstance(option("protocol", "TLS"));
        this.clientSslContext.init(null, new TrustManager[] {new TrustAllManager()}, null);
    }

    public static void main(String[] args) throws Exception {
        EzySslSocketPerformanceTest test = new EzySslSocketPerformanceTest(
            parseOptions(args)
        );
        Runtime.getRuntime().addShutdownHook(new Thread(test::close));
        test.run();
    }

    private void run() throws Exception {
        String mode = option("mode", "all");
        printEnvironment(mode);
        if ("server".equals(mode)) {
            runServer();
        } else if ("client".equals(mode)) {
            runClient(intOption("sslPort", 0), intOption("plainPort", 0));
        } else {
            runAll();
        }
    }

    private void runAll() throws Exception {
        setupKeyStore();
        EzySslContextProxy sslContextProxy = newSslContextProxy();
        int sslPort = startServer(0, () -> sslContextProxy);
        int noCacheSslPort = startServer(0, this::newSslContextProxy);
        int plainPort = startServer(0, null);
        printSession(sslPort);
        printHeader();
        runHandshake("handshake (cached ssl context)", sslPort);
        runHandshake("handshake (new ssl context per connection)", noCacheSslPort);
        runLatency(sslPort, plainPort);
        runThroughput(sslPort, plainPort);
        runReloadUnderLoad(sslPort, sslContextProxy);
        printServerExceptions();
    }

    private void runServer() throws Exception {
        setupKeyStore();
        EzySslContextProxy sslContextProxy = newSslContextProxy();
        int sslPort = startServer(intOption("sslPort", 0), () -> sslContextProxy);
        int plainPort = startServer(intOption("plainPort", 0), null);
        System.out.println("ssl server listening on " + host + ":" + sslPort);
        System.out.println("plain server listening on " + host + ":" + plainPort);
        System.out.println("press Ctrl+C to stop");
        Thread.sleep(Long.MAX_VALUE);
    }

    private void runClient(int sslPort, int plainPort) throws Exception {
        if (sslPort <= 0 || plainPort <= 0) {
            throw new IllegalArgumentException(
                "--sslPort and --plainPort are required in client mode"
            );
        }
        printSession(sslPort);
        printHeader();
        runHandshake("handshake", sslPort);
        runLatency(sslPort, plainPort);
        runThroughput(sslPort, plainPort);
    }

    private void runHandshake(String name, int port) throws Exception {
        int threads = intOption("threads", CPUS);
        OperationFactory factory = () -> () -> handshake(port);
        runParallel(threads, intOption("handshakeWarmup", 200), factory);
        printResult(
            name + " x " + threads + " threads",
            0,
            runParallel(threads, intOption("handshakes", 2000), factory)
        );
    }

    private void runLatency(int sslPort, int plainPort) throws Exception {
        int warmup = intOption("warmup", 2000);
        int messages = intOption("messages", 10000);
        for (int payloadSize : intListOption("payloads", "64,1024,16384,65536")) {
            for (boolean ssl : new boolean[] {false, true}) {
                int port = ssl ? sslPort : plainPort;
                runParallel(
                    1,
                    warmup,
                    () -> new EchoOperation(connect(port, ssl), payloadSize, true)
                );
                printResult(
                    "latency " + protocolName(ssl) + " " + formatSize(payloadSize),
                    payloadSize,
                    runParallel(
                        1,
                        messages,
                        () -> new EchoOperation(connect(port, ssl), payloadSize, false)
                    )
                );
            }
        }
    }

    private void runThroughput(int sslPort, int plainPort) throws Exception {
        int connections = intOption("connections", CPUS * 2);
        int messages = intOption("throughputMessages", 100000);
        int payloadSize = intOption("throughputPayload", 1024);
        for (boolean ssl : new boolean[] {false, true}) {
            int port = ssl ? sslPort : plainPort;
            OperationFactory factory = () ->
                new EchoOperation(connect(port, ssl), payloadSize, false);
            runParallel(connections, Math.max(1, messages / 10), factory);
            printResult(
                "throughput " + protocolName(ssl) + " " + formatSize(payloadSize)
                    + " x " + connections + " conns",
                payloadSize,
                runParallel(connections, messages, factory)
            );
        }
    }

    private void runReloadUnderLoad(
        int sslPort,
        EzySslContextProxy sslContextProxy
    ) throws Exception {
        int connections = intOption("connections", CPUS * 2);
        int messages = intOption("reloadMessages", 50000);
        int payloadSize = intOption("throughputPayload", 1024);
        int reconnectEvery = intOption("reconnectEvery", 100);
        int maxReloads = intOption("reloads", 20);
        int reloadInterval = intOption("reloadInterval", 50);
        String name = "ssl " + formatSize(payloadSize) + " x " + connections
            + " conns, reconnect/" + reconnectEvery;
        OperationFactory factory = () ->
            new ReconnectingEchoOperation(sslPort, payloadSize, reconnectEvery);

        runParallel(connections, Math.max(1, messages / 10), factory);
        printResult(name + ", no reload", payloadSize, runParallel(connections, messages, factory));

        List<Long> reloadTimes = new CopyOnWriteArrayList<>();
        AtomicBoolean running = new AtomicBoolean(true);
        AtomicReference<Throwable> reloadError = new AtomicReference<>();
        Thread reloader = new Thread(() -> {
            while (running.get() && reloadTimes.size() < maxReloads) {
                try {
                    Thread.sleep(reloadInterval);
                    long startTime = System.nanoTime();
                    sslContextProxy.reloadSsl();
                    reloadTimes.add(System.nanoTime() - startTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    reloadError.compareAndSet(null, e);
                    return;
                }
            }
        }, "ssl-reloader");
        reloader.setDaemon(true);
        reloader.start();
        Result result = runParallel(connections, messages, factory);
        running.set(false);
        reloader.join();
        printResult(name + ", reload", payloadSize, result);

        long totalReloadTime = 0;
        long maxReloadTime = 0;
        for (long reloadTime : reloadTimes) {
            totalReloadTime += reloadTime;
            maxReloadTime = Math.max(maxReloadTime, reloadTime);
        }
        System.out.println(
            "  reloads = " + reloadTimes.size()
                + ", avg = " + micros(reloadTimes.isEmpty() ? 0 : totalReloadTime / reloadTimes.size())
                + " us, max = " + micros(maxReloadTime) + " us"
                + (reloadError.get() != null ? ", error = " + reloadError.get() : "")
        );
    }

    private Result runParallel(
        int threads,
        int total,
        OperationFactory factory
    ) throws Exception {
        long[] samples = new long[total];
        Arrays.fill(samples, -1L);
        AtomicInteger index = new AtomicInteger();
        AtomicInteger errors = new AtomicInteger();
        AtomicReference<Throwable> firstError = new AtomicReference<>();
        CountDownLatch readyLatch = new CountDownLatch(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);
        for (int t = 0; t < threads; ++t) {
            Thread thread = new Thread(() -> {
                Operation operation = null;
                try {
                    operation = factory.create();
                } catch (Exception e) {
                    errors.incrementAndGet();
                    firstError.compareAndSet(null, e);
                }
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int i;
                    while ((i = index.getAndIncrement()) < total) {
                        try {
                            if (operation == null) {
                                operation = factory.create();
                            }
                            samples[i] = operation.execute();
                        } catch (Exception e) {
                            errors.incrementAndGet();
                            firstError.compareAndSet(null, e);
                            closeQuietly(operation);
                            operation = null;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    closeQuietly(operation);
                    doneLatch.countDown();
                }
            }, "perf-client-" + t);
            thread.setDaemon(true);
            thread.start();
        }
        readyLatch.await();
        long startTime = System.nanoTime();
        startLatch.countDown();
        doneLatch.await();
        long elapsedTime = System.nanoTime() - startTime;
        return new Result(samples, errors.get(), elapsedTime, firstError.get());
    }

    private long handshake(int port) throws Exception {
        long startTime = System.nanoTime();
        try (SSLSocket socket = (SSLSocket) connect(port, true)) {
            long elapsedTime = System.nanoTime() - startTime;
            socket.getSession().invalidate();
            return elapsedTime;
        }
    }

    private Socket connect(int port, boolean ssl) throws IOException {
        Socket socket = ssl
            ? clientSslContext.getSocketFactory().createSocket()
            : new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), SOCKET_TIMEOUT);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(SOCKET_TIMEOUT);
            if (ssl) {
                ((SSLSocket) socket).startHandshake();
            }
            return socket;
        } catch (IOException e) {
            socket.close();
            throw e;
        }
    }

    private int startServer(
        int port,
        Supplier<EzySslContextProxy> sslContextProxySupplier
    ) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(host, port), 1024);
        closeables.add(serverChannel);
        Thread acceptThread = new Thread(
            () -> acceptConnections(serverChannel, sslContextProxySupplier),
            "perf-server-accept"
        );
        acceptThread.setDaemon(true);
        acceptThread.start();
        return serverChannel.socket().getLocalPort();
    }

    private void acceptConnections(
        ServerSocketChannel serverChannel,
        Supplier<EzySslContextProxy> sslContextProxySupplier
    ) {
        while (serverChannel.isOpen()) {
            SocketChannel socketChannel;
            try {
                socketChannel = serverChannel.accept();
            } catch (IOException e) {
                break;
            }
            Thread handlerThread = new Thread(
                () -> handleConnection(socketChannel, sslContextProxySupplier),
                "perf-server-handler"
            );
            handlerThread.setDaemon(true);
            handlerThread.start();
        }
    }

    private void handleConnection(
        SocketChannel socketChannel,
        Supplier<EzySslContextProxy> sslContextProxySupplier
    ) {
        try {
            socketChannel.socket().setTcpNoDelay(true);
            if (sslContextProxySupplier == null) {
                plainEcho(socketChannel);
            } else {
                secureEcho(
                    socketChannel,
                    new EzyNioSecureSocketChannel(
                        socketChannel,
                        sslContextProxySupplier.get(),
                        SOCKET_TIMEOUT,
                        MAX_REQUEST_SIZE
                    )
                );
            }
        } catch (Exception e) {
            serverExceptions
                .computeIfAbsent(
                    e.getClass().getName() + ": " + e.getMessage(),
                    k -> new AtomicLong()
                )
                .incrementAndGet();
        } finally {
            processWithLogException(socketChannel::close);
        }
    }

    private static void plainEcho(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(SERVER_READ_BUFFER_SIZE);
        while (true) {
            buffer.clear();
            if (socketChannel.read(buffer) < 0) {
                return;
            }
            buffer.flip();
            writeFully(socketChannel, buffer);
        }
    }

    private static void secureEcho(
        SocketChannel socketChannel,
        EzyNioSecureSocketChannel channel
    ) throws Exception {
        socketChannel.configureBlocking(false);
        channel.handshake();
        socketChannel.configureBlocking(true);
        byte[] pendingBytes = channel.read(ByteBuffer.allocate(0));
        if (pendingBytes.length > 0) {
            writeFully(socketChannel, ByteBuffer.wrap(channel.pack(pendingBytes)));
        }
        ByteBuffer buffer = ByteBuffer.allocate(SERVER_READ_BUFFER_SIZE);
        while (true) {
            buffer.clear();
            if (socketChannel.read(buffer) < 0) {
                return;
            }
            buffer.flip();
            byte[] plainBytes = channel.read(buffer);
            if (plainBytes.length == 0) {
                continue;
            }
            writeFully(socketChannel, ByteBuffer.wrap(channel.pack(plainBytes)));
        }
    }

    private static void writeFully(
        SocketChannel socketChannel,
        ByteBuffer buffer
    ) throws IOException {
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    private void setupKeyStore() throws Exception {
        workingDir = Files.createTempDirectory("ezyfox-ssl-performance");
        Path keyStoreFile = workingDir.resolve("ssl-keystore.jks");
        Path keyStorePasswordFile = workingDir.resolve("ssl-keystore-password.txt");
        Path certificatePasswordFile = workingDir.resolve("ssl-certificate-password.txt");
        Files.write(keyStorePasswordFile, PASSWORD.getBytes(StandardCharsets.UTF_8));
        Files.write(certificatePasswordFile, PASSWORD.getBytes(StandardCharsets.UTF_8));
        generateKeyStore(keyStoreFile);
        sslConfig = new EzySslConfig() {
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
    }

    private void generateKeyStore(Path keyStoreFile) throws Exception {
        String keytool = System.getProperty("java.home")
            + File.separator + "bin" + File.separator + "keytool";
        List<String> command = new ArrayList<>(Arrays.asList(
            keytool,
            "-genkeypair",
            "-alias", "ezyfox",
            "-dname", "CN=ezyfox-performance",
            "-validity", "1",
            "-storetype", "JKS",
            "-keystore", keyStoreFile.toString(),
            "-storepass", PASSWORD,
            "-keypass", PASSWORD
        ));
        if ("ec".equalsIgnoreCase(option("keyAlg", "rsa"))) {
            command.addAll(Arrays.asList("-keyalg", "EC", "-keysize", "256"));
        } else {
            command.addAll(Arrays.asList("-keyalg", "RSA", "-keysize", option("rsaKeySize", "2048")));
        }
        Process process = new ProcessBuilder(command)
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
    }

    private EzySslContextProxy newSslContextProxy() {
        return new EzySslContextProxy(() ->
            EzyReturner.returnWithException(
                () -> sslContextFactory.newSslContext(sslConfig)
            )
        );
    }

    private void printEnvironment(String mode) {
        System.out.println();
        System.out.println(
            "java = " + System.getProperty("java.version")
                + " (" + System.getProperty("java.vendor") + ")"
                + ", os = " + System.getProperty("os.name")
                + " " + System.getProperty("os.arch")
                + ", cpus = " + CPUS
        );
        System.out.println(
            "mode = " + mode
                + ", host = " + host
                + ", client protocol = " + option("protocol", "TLS")
                + ("client".equals(mode) ? "" : ", key = " + option("keyAlg", "rsa"))
        );
        if ("all".equals(mode)) {
            System.out.println("note: client and server share this JVM and its CPUs");
        }
    }

    private void printSession(int sslPort) throws Exception {
        try (SSLSocket socket = (SSLSocket) connect(sslPort, true)) {
            SSLSession session = socket.getSession();
            X509Certificate certificate =
                (X509Certificate) session.getPeerCertificates()[0];
            System.out.println(
                "negotiated protocol = " + session.getProtocol()
                    + ", cipher = " + session.getCipherSuite()
                    + ", server key = " + certificate.getPublicKey().getAlgorithm()
            );
        }
        System.out.println();
    }

    private static void printHeader() {
        System.out.printf(
            ROW_FORMAT,
            "scenario",
            "ops",
            "errors",
            "ops/s",
            "MB/s",
            "p50(us)",
            "p95(us)",
            "p99(us)",
            "max(us)"
        );
    }

    private static void printResult(String name, int payloadSize, Result result) {
        double opsPerSecond = result.samples.length / (result.elapsedNanos / 1e9);
        String mbPerSecond = payloadSize > 0
            ? String.format("%.2f", opsPerSecond * payloadSize / (1024.0 * 1024.0))
            : "-";
        System.out.printf(
            ROW_FORMAT,
            name,
            String.valueOf(result.samples.length),
            String.valueOf(result.errors),
            String.format("%.0f", opsPerSecond),
            mbPerSecond,
            micros(result.percentile(50)),
            micros(result.percentile(95)),
            micros(result.percentile(99)),
            micros(result.percentile(100))
        );
        if (result.firstError != null) {
            System.out.println("  first error: " + result.firstError);
        }
    }

    private void printServerExceptions() {
        System.out.println();
        System.out.println(
            "server exceptions (EzyConnectionCloseException is a normal client close):"
        );
        if (serverExceptions.isEmpty()) {
            System.out.println("  none");
        }
        serverExceptions.forEach((type, count) ->
            System.out.println("  " + type + " = " + count.get())
        );
    }

    private static String micros(long nanos) {
        return String.format("%.1f", nanos / 1000.0);
    }

    private static String protocolName(boolean ssl) {
        return ssl ? "ssl  " : "plain";
    }

    private static String formatSize(int size) {
        return size < 1024 ? size + "B" : (size / 1024) + "KB";
    }

    private String option(String name, String defaultValue) {
        return options.getOrDefault(name, defaultValue);
    }

    private int intOption(String name, int defaultValue) {
        String value = options.get(name);
        return value != null ? Integer.parseInt(value.trim()) : defaultValue;
    }

    private List<Integer> intListOption(String name, String defaultValue) {
        List<Integer> answer = new ArrayList<>();
        for (String value : option(name, defaultValue).split(",")) {
            if (!value.trim().isEmpty()) {
                answer.add(Integer.parseInt(value.trim()));
            }
        }
        return answer;
    }

    private static Map<String, String> parseOptions(String[] args) {
        Map<String, String> answer = new HashMap<>();
        for (String arg : args) {
            String text = arg.startsWith("--") ? arg.substring(2) : arg;
            int separatorIndex = text.indexOf('=');
            if (separatorIndex > 0) {
                answer.put(text.substring(0, separatorIndex), text.substring(separatorIndex + 1));
            } else {
                answer.put(text, "true");
            }
        }
        return answer;
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            processWithLogException(closeable::close);
        }
    }

    private synchronized void close() {
        for (int i = closeables.size() - 1; i >= 0; --i) {
            closeQuietly(closeables.get(i));
        }
        closeables.clear();
        if (workingDir != null) {
            Path dir = workingDir;
            workingDir = null;
            processWithLogException(() -> deleteDirectory(dir));
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    private static void echo(
        Socket socket,
        byte[] payload,
        byte[] answer
    ) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(payload);
        outputStream.flush();
        readFully(socket.getInputStream(), answer);
    }

    private static void readFully(InputStream inputStream, byte[] buffer) throws IOException {
        int offset = 0;
        while (offset < buffer.length) {
            int read = inputStream.read(buffer, offset, buffer.length - offset);
            if (read < 0) {
                throw new IOException("connection closed");
            }
            offset += read;
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

    private static byte[] newPayload(int size) {
        byte[] payload = new byte[size];
        new Random(size).nextBytes(payload);
        return payload;
    }

    private interface Operation extends AutoCloseable {

        long execute() throws Exception;

        @Override
        default void close() throws Exception {}
    }

    private interface OperationFactory {
        Operation create() throws Exception;
    }

    private static class EchoOperation implements Operation {
        private final Socket socket;
        private final byte[] payload;
        private final byte[] answer;
        private final boolean verify;

        EchoOperation(Socket socket, int payloadSize, boolean verify) {
            this.socket = socket;
            this.payload = newPayload(payloadSize);
            this.answer = new byte[payloadSize];
            this.verify = verify;
        }

        @Override
        public long execute() throws Exception {
            long startTime = System.nanoTime();
            echo(socket, payload, answer);
            long elapsedTime = System.nanoTime() - startTime;
            if (verify && !Arrays.equals(payload, answer)) {
                throw new IllegalStateException("echo mismatch");
            }
            return elapsedTime;
        }

        @Override
        public void close() throws Exception {
            socket.close();
        }
    }

    private class ReconnectingEchoOperation implements Operation {
        private final int port;
        private final byte[] payload;
        private final byte[] answer;
        private final int reconnectEvery;
        private Socket socket;
        private long count;

        ReconnectingEchoOperation(int port, int payloadSize, int reconnectEvery) {
            this.port = port;
            this.payload = newPayload(payloadSize);
            this.answer = new byte[payloadSize];
            this.reconnectEvery = Math.max(1, reconnectEvery);
        }

        @Override
        public long execute() throws Exception {
            long startTime = System.nanoTime();
            if (socket == null || count % reconnectEvery == 0) {
                close();
                socket = connect(port, true);
            }
            ++count;
            echo(socket, payload, answer);
            return System.nanoTime() - startTime;
        }

        @Override
        public void close() throws Exception {
            if (socket != null) {
                Socket current = socket;
                socket = null;
                current.close();
            }
        }
    }

    private static class Result {
        private final long[] samples;
        private final int errors;
        private final long elapsedNanos;
        private final Throwable firstError;

        Result(long[] rawSamples, int errors, long elapsedNanos, Throwable firstError) {
            this.samples = Arrays.stream(rawSamples).filter(it -> it >= 0).sorted().toArray();
            this.errors = errors;
            this.elapsedNanos = elapsedNanos;
            this.firstError = firstError;
        }

        long percentile(double percent) {
            if (samples.length == 0) {
                return 0;
            }
            int index = (int) Math.ceil(percent / 100.0 * samples.length) - 1;
            return samples[Math.max(0, Math.min(samples.length - 1, index))];
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
