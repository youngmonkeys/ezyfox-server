package com.tvd12.ezyfoxserver.support.test.asm;

import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlerImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlersImplementer;
import org.testng.annotations.Test;

import java.util.Arrays;

public class EzyExceptionHandlersImplementerTest {

    @Test
    public void test() {
        EzyExceptionHandlerImplementer.setDebug(true);
        EzyExceptionHandlersImplementer implementer = new EzyExceptionHandlersImplementer();
        implementer.implement(Arrays.asList(new ExExceptionHandler()));
        EzyExceptionHandlerImplementer.setDebug(false);
        implementer.implement(Arrays.asList(new ExExceptionHandler()));
    }

    @EzyExceptionHandler
    public static class ExExceptionHandler {

        @EzyTryCatch(IllegalArgumentException.class)
        public void handle(EzySession session, IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }
}
