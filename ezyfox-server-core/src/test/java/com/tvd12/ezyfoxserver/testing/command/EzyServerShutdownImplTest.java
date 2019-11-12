package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.command.impl.EzyServerShutdownImpl;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzyServerShutdownImplTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(ExCtx.class);
        EzyServerShutdownImpl cmd = new EzyServerShutdownImpl(serverContext);
        cmd.execute();
    }
    
    public interface ExCtx extends EzyServerContext, EzyDestroyable {
    }
    
}
