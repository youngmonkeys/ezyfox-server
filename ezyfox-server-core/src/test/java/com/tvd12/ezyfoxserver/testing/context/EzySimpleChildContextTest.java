package com.tvd12.ezyfoxserver.testing.context;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.context.EzyAbstractZoneChildContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleChildContextTest extends BaseCoreTest {

    private EzyServerContext context;
    private EzyAbstractZoneChildContext ctx;

     public EzySimpleChildContextTest() {
         super();
         context = newServerContext();
         context.setProperty(Integer.class, 1);
         ctx = new ChildContext();
         ctx.setParent(context.getZoneContext("example"));
         ctx.init();
         ctx.setProperty(String.class, "a");
     }
     
     @Test
     public void test1() {
         ctx.getParent();
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
        public void destroy() {
            
        }
     }
     
     public static class UnsafeCommand {
     }
     
}
