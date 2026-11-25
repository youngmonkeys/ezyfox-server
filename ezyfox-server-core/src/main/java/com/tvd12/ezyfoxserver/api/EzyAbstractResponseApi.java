package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;

import java.util.Collection;

public abstract class EzyAbstractResponseApi
    extends EzyLoggable
    implements EzyResponseApi {

    @Override
    public void response(
        EzyPackage pack,
        boolean immediate
    ) throws Exception {
        if (pack.isEncrypted()) {
            secureResponse(pack, immediate);
        } else {
            normalResponse(pack, immediate);
        }
    }

    protected final void normalResponse(
        EzyPackage pack,
        boolean immediate
    ) throws Exception {
        Collection<EzySession> recipients = pack.getRecipients(
            getConnectionType()
        );
        if (recipients.isEmpty()) {
            return;
        }
        EzyArray data = pack.getData();
        Object bytes = null;
        Object compressedBytes = null;
        for (EzySession session : recipients) {
            if (isCompressionEnable(session)) {
                if (compressedBytes == null) {
                    compressedBytes = encodeCompressedData(data);
                }
            } else if (bytes == null) {
                bytes = encodeData(data);
            }
        }
        EzyConstant transportType = pack.getTransportType();
        for (EzySession session : recipients) {
            EzyPacket packet = null;
            try {
                packet = createPacket(
                    session,
                    transportType,
                    isCompressionEnable(session) ? compressedBytes : bytes
                );
                sendPacketToSession(session, packet, immediate);
            } catch (Throwable e) {
                if (packet != null) {
                    packet.release();
                }
                logResponseFailed(session, immediate, e);
            }
        }
    }

    protected final void secureResponse(
        EzyPackage pack,
        boolean immediate
    ) throws Exception {
        Collection<EzySession> recipients = pack.getRecipients(
            getConnectionType()
        );
        if (recipients.isEmpty()) {
            return;
        }
        byte[] messageContent = dataToMessageContent(pack.getData());
        byte[] compressedMessageContent = null;
        for (EzySession session : recipients) {
            if (isCompressionEnable(session)) {
                compressedMessageContent = compressMessageContent(messageContent);
                break;
            }
        }
        EzyConstant transportType = pack.getTransportType();
        for (EzySession session : recipients) {
            EzyPacket packet = null;
            try {
                byte[] bytes = compressedMessageContent != null
                    && isCompressionEnable(session)
                    ? encryptMessageContent(
                        compressedMessageContent,
                        session.getSessionKey(),
                        true
                    )
                    : encryptMessageContent(
                        messageContent,
                        session.getSessionKey()
                    );
                packet = createPacket(
                    session,
                    transportType,
                    bytes
                );
                sendPacketToSession(session, packet, immediate);
            } catch (Throwable e) {
                if (packet != null) {
                    packet.release();
                }
                logResponseFailed(session, immediate, e);
            }
        }
    }

    private void sendPacketToSession(
        EzySession session,
        EzyPacket packet,
        boolean immediate
    ) throws Exception {
        if (immediate) {
            sendPacketNow(session, packet);
        } else {
            sendPacket(session, packet);
        }
    }

    private void logResponseFailed(
        EzySession session,
        boolean immediate,
        Throwable e
    ) {
        if (immediate) {
            logger.info("response data now to session: {} failed", session, e);
        } else {
            logger.info("response data to session: {} failed", session, e);
        }
    }

    private EzyPacket createPacket(
        EzySession session,
        EzyConstant transportType,
        Object bytes
    ) {
        EzyConstant actualTransportType = transportType;
        if (actualTransportType == EzyTransportType.UDP_OR_TCP) {
            actualTransportType = session.getDatagramChannelPool() != null
                ? EzyTransportType.UDP
                : EzyTransportType.TCP;
        }
        return createPacket(actualTransportType, bytes);
    }

    protected EzySimplePacket createPacket(
        EzyConstant transportType,
        Object bytes
    ) {
        EzySimplePacket packet = new EzySimplePacket();
        packet.setTransportType(transportType);
        packet.setData(bytes);
        return packet;
    }

    private void sendPacket(
        EzySession session,
        EzyPacket packet
    ) throws Exception {
        if (packet.getTransportType() == EzyTransportType.UDP) {
            session.send(packet);
        } else {
            sendTcpPacket(session, packet);
        }
    }

    protected void sendTcpPacket(
        EzySession session,
        EzyPacket packet
    ) throws Exception {
        session.send(packet);
    }

    private void sendPacketNow(
        EzySession session,
        EzyPacket packet
    ) throws Exception {
        if (packet.getTransportType() == EzyTransportType.UDP) {
            session.sendNow(packet);
        } else {
            sendTcpPacketNow(session, packet);
        }
    }

    protected void sendTcpPacketNow(
        EzySession session,
        EzyPacket packet
    ) throws Exception {
        session.sendNow(packet);
    }

    protected abstract String getConnectionType();

    protected abstract Object encodeData(EzyArray data) throws Exception;

    protected byte[] dataToMessageContent(EzyArray data) throws Exception {
        throw new UnsupportedOperationException("unsupported");
    }

    protected boolean isCompressionEnable(EzySession session) {
        return false;
    }

    protected Object encodeCompressedData(EzyArray data) throws Exception {
        return encodeData(data);
    }

    protected byte[] compressMessageContent(
        byte[] messageContent
    ) throws Exception {
        return null;
    }

    protected byte[] encryptMessageContent(
        byte[] messageContent,
        byte[] encryptionKey
    ) throws Exception {
        throw new UnsupportedOperationException("unsupported");
    }

    protected byte[] encryptMessageContent(
        byte[] messageContent,
        byte[] encryptionKey,
        boolean compressed
    ) throws Exception {
        throw new UnsupportedOperationException("unsupported");
    }
}
