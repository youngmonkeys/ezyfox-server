package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.ezyfoxserver.EzyStarter.Builder;

public class MyTestRunner extends EzyRunner {

    @Override
    protected Builder<?> newStarterBuilder() {
        return MyTestStarter.builder();
    }

}
