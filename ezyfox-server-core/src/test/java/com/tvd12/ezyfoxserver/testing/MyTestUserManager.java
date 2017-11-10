package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerUserManagerImpl;

public class MyTestUserManager extends EzyServerUserManagerImpl {

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyServerUserManagerImpl.Builder {
        @Override
        public MyTestUserManager build() {
            return new MyTestUserManager();
        }
    }

}
