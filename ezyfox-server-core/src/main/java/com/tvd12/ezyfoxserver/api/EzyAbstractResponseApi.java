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
        EzyConstant connectionType = getConnectionType();
        Collection<EzySession> recipients = pack.getRecipients(connectionType);
        if (recipients.isEmpty()) {
            return;
        }
        Object bytes = encodeData(pack.getData());
        EzyConstant transportType = pack.getTransportType();
        if (immediate) {
            for (EzySession session : recipients) {
                try {
                    EzyPacket packet = createPacket(
                        session,
                        transportType,
                        bytes
                    );
                    sendPacketNow(session, packet);
                } catch (Throwable e) {
                    logger.info("response data now to session: {} failed", session, e);
                }
            }
        } else {
            for (EzySession session : recipients) {
                try {
                    EzyPacket packet = createPacket(
                        session,
                        transportType,
                        bytes
                    );
                    sendPacket(session, packet);
                } catch (Throwable e) {
                    logger.info("response data to session: {} failed", session, e);
                }
            }
        }
    }

    protected final void secureResponse(
        EzyPackage pack,
        boolean immediate
    ) throws Exception {
        EzyConstant connectionType = getConnectionType();
        Collection<EzySession> recipients = pack.getRecipients(connectionType);
        if (recipients.isEmpty()) {
            return;
        }
        byte[] messageContent = dataToMessageContent(pack.getData());
        EzyConstant transportType = pack.getTransportType();
        if (immediate) {
            for (EzySession session : recipients) {
                try {
                    byte[] bytes = encryptMessageContent(messageContent, session.getSessionKey());
                    EzyPacket packet = createPacket(
                        session,
                        transportType,
                        bytes
                    );
                    sendPacketNow(session, packet);
                } catch (Throwable e) {
                    logger.info("response data now to session: {} failed", session, e);
                }
            }
        } else {
            for (EzySession session : recipients) {
                try {
                    byte[] bytes = encryptMessageContent(messageContent, session.getSessionKey());
                    EzyPacket packet = createPacket(
                        session,
                        transportType,
                        bytes
                    );
                    sendPacket(session, packet);
                } catch (Throwable e) {
                    logger.info("response data to session: {} failed", session, e);
                }
            }
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

    protected abstract EzyConstant getConnectionType();

    protected abstract Object encodeData(EzyArray data) throws Exception;

    protected byte[] dataToMessageContent(EzyArray data) throws Exception {
        throw new UnsupportedOperationException("unsupported");
    }

    protected byte[] encryptMessageContent(
        byte[] messageContent,
        byte[] encryptionKey
    ) throws Exception {
        throw new UnsupportedOperationException("unsupported");
    }
}
