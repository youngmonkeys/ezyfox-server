package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyPongResponse implements EzyResponse {
    private static final long serialVersionUID = -8041496097838048962L;
    private final static EzyPongResponse INSTANCE = new EzyPongResponse();
    private final EzyArray data;

    private EzyPongResponse() {
        this.data = newData();
    }

    public static EzyPongResponse getInstance() {
        return INSTANCE;
    }

    @Override
    public EzyArray serialize() {
        return data;
    }

    @Override
    public EzyConstant getCommand() {
        return EzyCommand.PONG;
    }

    @Override
    public void release() {
    }

    private EzyArray newData() {
        EzyArray array = EzyEntityFactory.newArray();
        array.add(getCommand().getId());
        return array;
    }
}
