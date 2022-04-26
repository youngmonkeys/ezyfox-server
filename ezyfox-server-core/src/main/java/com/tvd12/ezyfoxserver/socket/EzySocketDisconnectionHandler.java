package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySocketDisconnectionHandler extends EzySocketAbstractEventHandler {

    @Setter
    protected EzySocketDisconnectionQueue disconnectionQueue;
    @Setter
    protected EzySocketDataHandlerGroupRemover dataHandlerGroupRemover;

    @Override
    public void handleEvent() {
        processDisconnectionQueue();
    }

    @Override
    public void destroy() {
        processWithLogException(() -> disconnectionQueue.clear());
    }

    private void processDisconnectionQueue() {
        try {
            EzySocketDisconnection disconnection = disconnectionQueue.take();
            processDisconnection(disconnection);
        } catch (InterruptedException e) {
            logger.info("disconnection-handler thread interrupted");
        } catch (Throwable throwable) {
            logger.warn("problems in disconnection-handler, thread", throwable);
        }
    }

    private void processDisconnection(EzySocketDisconnection disconnection) {
        try {
            EzySession session = disconnection.getSession();
            EzyConstant disconnectReason = disconnection.getDisconnectReason();
            EzySocketDataHandlerGroup handlerGroup = removeDataHandlerGroup(session);
            if (handlerGroup != null) {
                handlerGroup.fireChannelInactive(disconnectReason);
            } else {
                logger.warn(
                    "has no handler group with session: {}, ignore disconnection: {}",
                    session,
                    disconnection
                );
            }
        } finally {
            disconnection.release();
        }
    }

    protected EzySocketDataHandlerGroup removeDataHandlerGroup(EzySession session) {
        return dataHandlerGroupRemover.removeHandlerGroup(session);
    }
}
