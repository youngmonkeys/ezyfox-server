package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;

public class EzySimpleCodecFactory implements EzyCodecFactory {

    protected final EzySocketSetting socketSetting;
    protected final EzyWebSocketSetting websocketSetting;

    protected final EzyCodecCreator socketCodecCreator;
    protected final EzyCodecCreator websocketCodecCreator;

    public EzySimpleCodecFactory(Builder builder) {
        this.socketSetting = builder.socketSetting;
        this.websocketSetting = builder.websocketSetting;
        this.socketCodecCreator = newSocketCodecCreator();
        this.websocketCodecCreator = newWebsocketCodecCreator();
    }

    @Override
    public Object newEncoder(EzyConstant type) {
        if(type == EzyConnectionType.SOCKET) {
            if(socketCodecCreator != null)
                return socketCodecCreator.newEncoder();
        }
        else {
            if(websocketCodecCreator != null)
                return websocketCodecCreator.newEncoder();
        }
        return null;
    }

    @Override
    public Object newDecoder(EzyConstant type) {
        if(type == EzyConnectionType.SOCKET) {
            if(socketCodecCreator != null) {
                int maxRequestSize = socketSetting.getMaxRequestSize();
                return socketCodecCreator.newDecoder(maxRequestSize);
            }
        }
        else {
            if(websocketCodecCreator != null) {
                int maxFrameSize = websocketSetting.getMaxFrameSize();
                return websocketCodecCreator.newDecoder(maxFrameSize);
            }
        }
        return null;
    }

    private EzyCodecCreator newSocketCodecCreator() {
        if(socketSetting.isActive()) {
            return EzyClasses.newInstance(
                    socketSetting.getCodecCreator(),
                    new Class<?>[] {boolean.class},
                    new Object[] {socketSetting.isSslActive()});
        }
        return null;
    }

    private EzyCodecCreator newWebsocketCodecCreator() {
        if(websocketSetting.isActive())
            return EzyClasses.newInstance(websocketSetting.getCodecCreator());
        return null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyCodecFactory> {
        protected EzySocketSetting socketSetting;
        protected EzyWebSocketSetting websocketSetting;

        public Builder socketSetting(EzySocketSetting settings) {
            this.socketSetting = settings;
            return this;
        }

        public Builder websocketSetting(EzyWebSocketSetting settings) {
            this.websocketSetting = settings;
            return this;
        }

        @Override
        public EzyCodecFactory build() {
            return new EzySimpleCodecFactory(this);
        }
    }

}
