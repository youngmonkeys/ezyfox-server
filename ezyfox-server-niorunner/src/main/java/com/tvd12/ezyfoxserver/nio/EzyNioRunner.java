package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.ezyfoxserver.EzyStarter.Builder;

public class EzyNioRunner extends EzyRunner {

    public static void main(String[] args) throws Exception {
        new EzyNioRunner().run(args);
    }

    @Override
    protected Builder<?> newStarterBuilder() {
        return EzyNioStarter.builder();
    }
}
