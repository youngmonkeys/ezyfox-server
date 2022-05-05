package com.tvd12.ezyfoxserver.testing.context;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyStoppable;
import com.tvd12.ezyfoxserver.context.EzyAbstractContext;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyAbstractContextTest {

    @Test
    public void cmdNotNullByPropertiesTest() {
        // given
        A a = new A();
        B b = new B();
        InternalContext sut = new InternalContext();
        sut.init();
        sut.setProperty(A.class, a);
        sut.setProperty(B.class, b);

        // when
        A actual = sut.cmd(A.class);

        // then
        Asserts.assertEquals(actual, a);
    }

    @Test
    public void destroyTest() {
        // given
        A a = new A();
        B b = new B();
        InternalContext sut = new InternalContext();
        sut.init();
        sut.setProperty("a", a);
        sut.setProperty("b", b);

        // when
        sut.destroy();

        // then
        Asserts.assertTrue(a.stopped);
        Asserts.assertTrue(b.destroy);
    }

    private static class A implements EzyStoppable {
        private boolean stopped;

        @Override
        public void stop() {
            this.stopped = true;
        }
    }

    public static class B implements EzyDestroyable {
        private boolean destroy;

        @Override
        public void destroy() {
            this.destroy = true;
        }
    }

    private static class InternalContext extends EzyAbstractContext {
        @Override
        protected <T> T parentCmd(Class<T> clazz) {
            return null;
        }

        @Override
        protected <T> T parentGet(Class<T> clazz) {
            return null;
        }
    }
}
