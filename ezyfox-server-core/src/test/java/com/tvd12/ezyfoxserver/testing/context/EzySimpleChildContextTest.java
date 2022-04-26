package com.tvd12.ezyfoxserver.testing.context;

import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.context.EzyAbstractZoneChildContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzySimpleChildContextTest extends BaseCoreTest {

    private final EzyServerContext context;
    private final EzyAbstractZoneChildContext ctx;
    private final String zoneName = "example";

    public EzySimpleChildContextTest() {
        super();
        context = newServerContext();
        context.setProperty(Integer.class, 1);
        ctx = new ChildContext();
        ctx.setParent(context.getZoneContext(zoneName));
        ctx.init();
        ctx.setProperty(String.class, "a");
    }

    @Test
    public void test1() {
        Asserts.assertEquals(ctx.getParent(), context.getZoneContext(zoneName));
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test2() {
        ctx.get(UnsafeCommand.class);
    }

    @Test
    public void test3() {
        assert ctx.get(String.class).equals("a");
    }

    public static class ChildContext extends EzyAbstractZoneChildContext {

        public ChildContext() {
            this.component = new EzyComponent();
        }

        @Override
        public void destroy() {}
    }

    public static class UnsafeCommand {}
}
