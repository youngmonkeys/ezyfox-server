package com.tvd12.ezyfoxserver.embedded.test.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class CountingTcpProxy implements AutoCloseable {

    private final String targetHost;
    private final int targetPort;
    private final ServerSocket serverSocket;
    private final AtomicLong upstreamBytes = new AtomicLong();
    private final AtomicLong downstreamBytes = new AtomicLong();
    private final List<Socket> sockets = new CopyOnWriteArrayList<>();
    private volatile boolean active = true;

    public CountingTcpProxy(String targetHost, int targetPort) throws IOException {
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.serverSocket = new ServerSocket();
        this.serverSocket.bind(
            new InetSocketAddress(InetAddress.getByName(targetHost), 0)
        );
        Thread acceptThread = new Thread(this::acceptLoop, "counting-proxy-accept");
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public long getUpstreamBytes() {
        return upstreamBytes.get();
    }

    public long getDownstreamBytes() {
        return downstreamBytes.get();
    }

    private void acceptLoop() {
        while (active) {
            try {
                Socket client = serverSocket.accept();
                Socket target = new Socket(targetHost, targetPort);
                client.setTcpNoDelay(true);
                target.setTcpNoDelay(true);
                sockets.add(client);
                sockets.add(target);
                startPump(client, target, upstreamBytes, "counting-proxy-up");
                startPump(target, client, downstreamBytes, "counting-proxy-down");
            } catch (IOException e) {
                if (active) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startPump(
        Socket from,
        Socket to,
        AtomicLong counter,
        String threadName
    ) {
        Thread thread = new Thread(() -> {
            byte[] buffer = new byte[8192];
            try {
                InputStream in = from.getInputStream();
                OutputStream out = to.getOutputStream();
                int read;
                while ((read = in.read(buffer)) != -1) {
                    counter.addAndGet(read);
                    out.write(buffer, 0, read);
                    out.flush();
                }
            } catch (IOException expected) {
            } finally {
                closeQuietly(from);
                closeQuietly(to);
            }
        }, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    private static void closeQuietly(Socket socket) {
        try {
            socket.close();
        } catch (IOException expected) {
        }
    }

    @Override
    public void close() throws IOException {
        active = false;
        serverSocket.close();
        sockets.forEach(CountingTcpProxy::closeQuietly);
    }
}
