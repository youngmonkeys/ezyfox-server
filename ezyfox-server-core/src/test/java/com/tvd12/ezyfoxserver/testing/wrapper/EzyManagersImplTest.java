package com.tvd12.ezyfoxserver.testing.wrapper;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyStartable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyManagersImpl;
import com.tvd12.test.performance.Performance;

public class EzyManagersImplTest extends BaseCoreTest {

    @SuppressWarnings("unused")
    @Test
    public void test() {
        EzyManagers managers = EzyManagersImpl.builder().build();
        managers.addManager(Manager1.class, new Manager1());
        managers.addManager(Manager2.class, new Manager2());
        managers.addManager(Manager3.class, new Manager3());
        managers.startManagers();
        managers.stopManagers();
        
        long time = Performance.create()
            .test(() -> {
                Manager2 manager2 = managers.getManager(Manager2.class);
            })
            .getTime();
        System.out.println("performace time: " + time);
    }
    
    public static class Manager1 implements EzyStartable, EzyDestroyable {

        @Override
        public void destroy() {
        }

        @Override
        public void start() throws Exception {
        }
        
    }
    
    public static class Manager2 {
        
    }
    
    public static class Manager3 implements EzyStartable, EzyDestroyable {

        @Override
        public void destroy() {
            throw new RuntimeException();
        }

        @Override
        public void start() throws Exception {
            throw new RuntimeException();
        }
        
    }
    
}
