package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.context.EzyServerContext;

public class BaseCoreContextTest extends BaseCoreTest {

    protected EzyServerContext context;

     public BaseCoreContextTest() {
         super();
         context = newServerContext();
     }
    
}
