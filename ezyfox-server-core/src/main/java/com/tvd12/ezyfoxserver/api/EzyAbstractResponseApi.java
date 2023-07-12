package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
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
        if (immediate) {
            for (EzySession session : recipients) {
                try {
                    Object packedBytes = packMessage(session, bytes);
                    session.sendNow(createPacket(packedBytes, pack));
                } catch (Throwable e) {
                    logger.info("response data now to session: {} failed", session, e);
                }
            }
        } else {
            for (EzySession session : recipients) {
                try {
                    Object packedBytes = packMessage(session, bytes);
                    session.send(createPacket(packedBytes, pack));
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
        if (immediate) {
            for (EzySession session : recipients) {
                try {
                    byte[] bytes = encryptMessageContent(messageContent, session.getSessionKey());
                    Object packedBytes = packMessage(session, bytes);
                    session.sendNow(createPacket(packedBytes, pack));
                } catch (Throwable e) {
                    logger.info("response data now to session: {} failed", session, e);
                }
            }
        } else {
            for (EzySession session : recipients) {
                try {
                    byte[] bytes = encryptMessageContent(messageContent, session.getSessionKey());
                    Object packedBytes = packMessage(session, bytes);
                    session.send(createPacket(packedBytes, pack));
                } catch (Throwable e) {
                    logger.info("response data to session: {} failed", session, e);
                }
            }
        }
    }

    protected EzySimplePacket createPacket(Object bytes, EzyPackage pack) {
        EzySimplePacket packet = new EzySimplePacket();
        packet.setTransportType(pack.getTransportType());
        packet.setData(bytes);
        return packet;
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

    protected Object packMessage(
        EzySession session,
        Object message
    ) throws Exception {
        return message;
    }
}
