package com.tvd12.ezyfoxserver.testing.context;

import static org.mockito.Mockito.spy;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyAbstractComplexContext;

public class EzyAbstractComplexContextTest {

    @Test
    public void test() {
        EzyAbstractComplexContext ctx = spy(EzyAbstractComplexContext.class);
        ctx.init();
        ctx.destroy();
    }
    
}
