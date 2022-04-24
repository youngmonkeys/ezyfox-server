package com.tvd12.ezyfoxserver.testing.context;

import com.tvd12.ezyfoxserver.context.EzyAbstractComplexContext;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class EzyAbstractComplexContextTest {

    @Test
    public void test() {
        EzyAbstractComplexContext ctx = spy(EzyAbstractComplexContext.class);
        ctx.init();
        ctx.destroy();
    }

}
