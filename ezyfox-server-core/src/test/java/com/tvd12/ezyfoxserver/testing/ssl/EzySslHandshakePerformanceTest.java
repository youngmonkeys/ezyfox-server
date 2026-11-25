package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.security.EzyAsyCrypt;
import com.tvd12.ezyfox.security.EzyBase64;
import com.tvd12.ezyfox.security.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyHandshakeController;
import com.tvd12.ezyfoxserver.request.EzySimpleHandshakeRequest;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeSigner;
import com.tvd12.ezyfoxserver.testing.MyTestSession;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class EzySslHandshakePerformanceTest {

    private static final int CLIENT_KEY_COUNT = 16;
    private static final ThreadLocal<EzyResponse> LAST_RESPONSE = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        int iterations = args.length > 0 ? Integer.parseInt(args[0]) : 2000;
        int threads = args.length > 1
            ? Integer.parseInt(args[1])
            : Runtime.getRuntime().availableProcessors();

        List<KeyPair> clientKeys = new ArrayList<>();
        for (int i = 0; i < CLIENT_KEY_COUNT; ++i) {
            clientKeys.add(EzyKeysGenerator.builder().build().generate());
        }
        List<EzyArray> handshakeData = new ArrayList<>();
        for (KeyPair keyPair : clientKeys) {
            handshakeData.add(newHandshakeData(keyPair));
        }
        EzySslHandshakeSigner rsaSigner = newSigner("test-input/ssl-handshake-keystore.jks");
        EzySslHandshakeSigner ecSigner = newSigner("test-input/ssl-handshake-ec-keystore.jks");

        EzyHandshakeController noSignerController = new EzyHandshakeController();
        EzyHandshakeController rsaController = new EzyHandshakeController();
        EzyHandshakeController ecController = new EzyHandshakeController();
        EzyServerContext noSignerContext = newServerContext(null);
        EzyServerContext rsaContext = newServerContext(rsaSigner);
        EzyServerContext ecContext = newServerContext(ecSigner);

        List<Sample> rsaSamples = newSamples(rsaController, rsaContext, clientKeys, handshakeData);
        List<Sample> ecSamples = newSamples(ecController, ecContext, clientKeys, handshakeData);
        for (Sample sample : rsaSamples) {
            sample.check();
        }
        for (Sample sample : ecSamples) {
            sample.check();
        }

        System.out.println();
        System.out.println("iterations = " + iterations + ", threads = " + threads
            + ", java = " + System.getProperty("java.version"));
        System.out.println("rsa proof = " + rsaSamples.get(0).proof.length + " bytes"
            + ", ec proof = " + ecSamples.get(0).proof.length + " bytes");
        System.out.println();

        measure("client: generate RSA client key", Math.max(20, iterations / 20), index ->
            EzyKeysGenerator.builder().build().generate()
        );
        measure("server: handshake, no signer", iterations, index ->
            handshake(noSignerController, noSignerContext, handshakeData, index)
        );
        measure("server: handshake, RSA signer", iterations, index ->
            handshake(rsaController, rsaContext, handshakeData, index)
        );
        measure("server: handshake, EC signer", iterations, index ->
            handshake(ecController, ecContext, handshakeData, index)
        );
        measure("server: sign only, RSA", iterations, index -> {
            Sample sample = rsaSamples.get(index % rsaSamples.size());
            rsaSigner.sign(sample.clientKey, sample.encryptedSessionKey);
        });
        measure("server: sign only, EC", iterations, index -> {
            Sample sample = ecSamples.get(index % ecSamples.size());
            ecSigner.sign(sample.clientKey, sample.encryptedSessionKey);
        });
        measure("client: verify proof, RSA", iterations, index ->
            rsaSamples.get(index % rsaSamples.size()).verify()
        );
        measure("client: verify proof, EC", iterations, index ->
            ecSamples.get(index % ecSamples.size()).verify()
        );
        measure("client: decrypt session key", iterations, index ->
            rsaSamples.get(index % rsaSamples.size()).decrypt()
        );
        measure("end to end, no signer", iterations, index -> {
            EzyResponse response = handshake(noSignerController, noSignerContext, handshakeData, index);
            new Sample(clientKeys.get(index % clientKeys.size()), null, response).decrypt();
        });
        measure("end to end, RSA signer", iterations, index -> {
            EzyResponse response = handshake(rsaController, rsaContext, handshakeData, index);
            Sample sample = new Sample(clientKeys.get(index % clientKeys.size()), null, response);
            sample.verify();
            sample.decrypt();
        });
        measure("end to end, EC signer", iterations, index -> {
            EzyResponse response = handshake(ecController, ecContext, handshakeData, index);
            Sample sample = new Sample(clientKeys.get(index % clientKeys.size()), null, response);
            sample.verify();
            sample.decrypt();
        });
        System.out.println();

        measureConcurrent("server: handshake, no signer", iterations, threads, index ->
            handshake(noSignerController, noSignerContext, handshakeData, index)
        );
        measureConcurrent("server: handshake, RSA signer", iterations, threads, index ->
            handshake(rsaController, rsaContext, handshakeData, index)
        );
        measureConcurrent("server: handshake, EC signer", iterations, threads, index ->
            handshake(ecController, ecContext, handshakeData, index)
        );
        System.out.println();
    }

    private static EzyResponse handshake(
        EzyHandshakeController controller,
        EzyServerContext context,
        List<EzyArray> handshakeData,
        int index
    ) {
        MyTestSession session = new MyTestSession();
        session.setConnectionType(EzyConnectionType.SOCKET.getName());
        EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
        request.deserializeParams(handshakeData.get(index % handshakeData.size()));
        request.setSession(session);
        controller.handle(context, request);
        EzyResponse response = LAST_RESPONSE.get();
        LAST_RESPONSE.remove();
        return response;
    }

    private static List<Sample> newSamples(
        EzyHandshakeController controller,
        EzyServerContext context,
        List<KeyPair> clientKeys,
        List<EzyArray> handshakeData
    ) {
        List<Sample> answer = new ArrayList<>();
        for (int i = 0; i < clientKeys.size(); ++i) {
            MyTestSession session = new MyTestSession();
            session.setConnectionType(EzyConnectionType.SOCKET.getName());
            EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
            request.deserializeParams(handshakeData.get(i));
            request.setSession(session);
            controller.handle(context, request);
            answer.add(new Sample(clientKeys.get(i), session.getSessionKey(), LAST_RESPONSE.get()));
            LAST_RESPONSE.remove();
        }
        return answer;
    }

    private static EzyArray newHandshakeData(KeyPair keyPair) {
        return EzyEntityFactory.newArrayBuilder()
            .append("vibe-game#1")
            .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
            .append("cpp")
            .append("1.0.0")
            .append(true)
            .append("reconnectToken#1")
            .build();
    }

    private static EzySslHandshakeSigner newSigner(String keyStoreFile) {
        EzySslHandshakeSigner signer = new EzySslHandshakeSigner(() -> {
            EzySimpleSslConfig sslConfig = new EzySimpleSslConfig();
            sslConfig.setKeyStoreFile(keyStoreFile);
            sslConfig.setKeyStorePasswordFile("test-input/ssl-handshake-password.txt");
            sslConfig.setCertificatePasswordFile("test-input/ssl-handshake-password.txt");
            return sslConfig;
        });
        if (!signer.reload()) {
            throw new IllegalStateException("cannot load signer: " + keyStoreFile);
        }
        return signer;
    }

    private static EzyServerContext newServerContext(EzySslHandshakeSigner signer) {
        EzySocketSetting socketSetting = newProxy(EzySocketSetting.class, (method, args) ->
            "isCustomizationSslActive".equals(method.getName()) ? Boolean.TRUE : defaultValue(method)
        );
        EzySettings settings = newProxy(EzySettings.class, (method, args) ->
            "getSocket".equals(method.getName()) ? socketSetting : defaultValue(method)
        );
        EzyServer server = newProxy(EzyServer.class, (method, args) ->
            "getSettings".equals(method.getName()) ? settings : defaultValue(method)
        );
        return newProxy(EzyServerContext.class, (method, args) -> {
            switch (method.getName()) {
                case "getServer":
                    return server;
                case "getProperty":
                    return args[0] == EzySslHandshakeSigner.class ? signer : null;
                case "send":
                    LAST_RESPONSE.set((EzyResponse) args[0]);
                    return null;
                default:
                    return defaultValue(method);
            }
        });
    }

    private static <T> T newProxy(Class<T> type, Handler handler) {
        return type.cast(
            Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] {type},
                (proxy, method, args) -> handler.handle(method, args)
            )
        );
    }

    private static Object defaultValue(Method method) {
        Class<?> type = method.getReturnType();
        if (type == boolean.class) {
            return Boolean.FALSE;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == char.class) {
            return (char) 0;
        }
        if (type == float.class) {
            return 0F;
        }
        if (type == double.class) {
            return 0D;
        }
        return null;
    }

    private static void measure(String name, int iterations, Task task) throws Exception {
        int warmup = Math.max(50, iterations / 5);
        for (int i = 0; i < warmup; ++i) {
            task.run(i);
        }
        long start = System.nanoTime();
        for (int i = 0; i < iterations; ++i) {
            task.run(i);
        }
        print(name, iterations, System.nanoTime() - start);
    }

    private static void measureConcurrent(
        String name,
        int iterations,
        int threads,
        Task task
    ) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            runConcurrent(executor, Math.max(50, iterations / 5), threads, task);
            long elapsed = runConcurrent(executor, iterations, threads, task);
            print(name + ", " + threads + " threads", iterations, elapsed);
        } finally {
            executor.shutdown();
        }
    }

    private static long runConcurrent(
        ExecutorService executor,
        int iterations,
        int threads,
        Task task
    ) throws Exception {
        AtomicInteger counter = new AtomicInteger();
        AtomicReference<Throwable> error = new AtomicReference<>();
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        for (int t = 0; t < threads; ++t) {
            executor.execute(() -> {
                try {
                    ready.countDown();
                    go.await();
                    int index;
                    while ((index = counter.getAndIncrement()) < iterations) {
                        task.run(index);
                    }
                } catch (Throwable e) {
                    error.compareAndSet(null, e);
                } finally {
                    done.countDown();
                }
            });
        }
        ready.await();
        long start = System.nanoTime();
        go.countDown();
        done.await();
        long elapsed = System.nanoTime() - start;
        if (error.get() != null) {
            throw new IllegalStateException(error.get());
        }
        return elapsed;
    }

    private static void print(String name, int iterations, long elapsedNanos) {
        double microsPerOp = elapsedNanos / 1000.0 / iterations;
        double opsPerSecond = iterations * 1_000_000_000.0 / elapsedNanos;
        System.out.println(
            String.format("%-48s %10.1f us/op %12.0f ops/s", name, microsPerOp, opsPerSecond)
        );
    }

    private interface Task {
        void run(int index) throws Exception;
    }

    private interface Handler {
        Object handle(Method method, Object[] args) throws Throwable;
    }

    private static class Sample {
        private final KeyPair clientKeyPair;
        private final byte[] clientKey;
        private final byte[] sessionKey;
        private final byte[] proof;
        private final byte[] encryptedSessionKey;

        private Sample(KeyPair clientKeyPair, byte[] sessionKey, EzyResponse response) {
            EzyArray data = response.serialize().get(1, EzyArray.class);
            this.clientKeyPair = clientKeyPair;
            this.clientKey = clientKeyPair.getPublic().getEncoded();
            this.sessionKey = sessionKey;
            this.proof = data.get(0, byte[].class);
            this.encryptedSessionKey = data.get(3, byte[].class);
        }

        private void check() throws Exception {
            if (!verify()) {
                throw new IllegalStateException("invalid proof");
            }
            if (!Arrays.equals(decrypt(), sessionKey)) {
                throw new IllegalStateException("invalid session key");
            }
        }

        private boolean verify() throws Exception {
            ByteBuffer buffer = ByteBuffer.wrap(proof);
            if (buffer.get() != EzySslHandshakeSigner.PROOF_VERSION) {
                throw new IllegalStateException("invalid proof version");
            }
            byte[] signed = readField(buffer);
            int count = buffer.get() & 0xFF;
            if (count == 0 || count > EzySslHandshakeSigner.MAX_CERTIFICATES) {
                throw new IllegalStateException("invalid certificate count: " + count);
            }
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            List<X509Certificate> certificates = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                certificates.add(
                    (X509Certificate) factory.generateCertificate(
                        new ByteArrayInputStream(readField(buffer))
                    )
                );
            }
            if (buffer.hasRemaining()) {
                throw new IllegalStateException("trailing proof bytes");
            }
            PublicKey leafKey = certificates.get(0).getPublicKey();
            Signature signature = Signature.getInstance(
                "RSA".equals(leafKey.getAlgorithm()) ? "SHA256withRSA" : "SHA256withECDSA"
            );
            signature.initVerify(leafKey);
            signature.update(EzySslHandshakeSigner.signedMessage(clientKey, encryptedSessionKey));
            if (!signature.verify(signed)) {
                throw new IllegalStateException("invalid signature");
            }
            return true;
        }

        private byte[] decrypt() throws Exception {
            return EzyAsyCrypt.builder()
                .privateKey(clientKeyPair.getPrivate().getEncoded())
                .build()
                .decrypt(encryptedSessionKey);
        }

        private static byte[] readField(ByteBuffer buffer) {
            int length = buffer.getShort() & 0xFFFF;
            byte[] answer = new byte[length];
            buffer.get(answer);
            return answer;
        }
    }
}
