package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.command.EzyCommand;
import com.tvd12.ezyfoxserver.command.impl.EzyAddCommandImpl;
import com.tvd12.ezyfoxserver.context.EzyAbstractContext;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class EzyAddCommandImplTest extends BaseTest {

    @Test
    public void test() {
        EzyAbstractContext ctx = spy(EzyAbstractContext.class);
        ctx.init();
        EzyAddCommandImpl cmd = new EzyAddCommandImpl(ctx);
        cmd.add(Cmd.class, () -> new Cmd());
    }

    public static class Cmd implements EzyCommand<Boolean> {

        @Override
        public Boolean execute() {
            return Boolean.TRUE;
        }

    }

}
