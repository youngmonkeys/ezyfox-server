package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.codec.EzyMessageDataEncoder;
import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.codec.EzySimpleMessageDataEncoder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzySocketResponseApi extends EzyAbstractResponseApi {

    protected final EzyMessageDataEncoder encoder;
    protected final int compressionThreshold;

    public EzySocketResponseApi(Object encoder, int compressionThreshold) {
        this.encoder = new EzySimpleMessageDataEncoder(
            (EzyObjectToByteEncoder) encoder
        );
        this.compressionThreshold = compressionThreshold;
    }

    @Override
    protected Object encodeData(EzyArray data) throws Exception {
        return encoder.encode(data);
    }

    @Override
    protected byte[] dataToMessageContent(
        EzyArray data
    ) throws Exception {
        return encoder.toMessageContent(data);
    }

    @Override
    protected boolean isCompressionEnable(EzySession session) {
        return session.isCompressionEnable();
    }

    @Override
    protected Object encodeCompressedData(EzyArray data) throws Exception {
        byte[] messageContent = encoder.toMessageContent(data);
        byte[] compressedMessageContent = compressMessageContent(messageContent);
        return compressedMessageContent != null
            ? encoder.encryptMessageContent(compressedMessageContent, null, true)
            : encoder.encryptMessageContent(messageContent, null);
    }

    @Override
    protected byte[] compressMessageContent(
        byte[] messageContent
    ) throws Exception {
        if (messageContent.length < compressionThreshold) {
            return null;
        }
        byte[] compressedMessageContent = encoder.compressMessageContent(
            messageContent
        );
        return compressedMessageContent.length < messageContent.length
            ? compressedMessageContent
            : null;
    }

    @Override
    protected byte[] encryptMessageContent(
        byte[] messageContent,
        byte[] encryptionKey
    ) throws Exception {
        return encoder.encryptMessageContent(
            messageContent,
            encryptionKey
        );
    }

    @Override
    protected byte[] encryptMessageContent(
        byte[] messageContent,
        byte[] encryptionKey,
        boolean compressed
    ) throws Exception {
        return encoder.encryptMessageContent(
            messageContent,
            encryptionKey,
            compressed
        );
    }

    @Override
    protected String getConnectionType() {
        return EzyConnectionType.SOCKET.getName();
    }
}
