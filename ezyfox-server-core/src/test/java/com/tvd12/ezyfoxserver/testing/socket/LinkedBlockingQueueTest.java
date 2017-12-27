package com.tvd12.ezyfoxserver.testing.socket;

import java.util.concurrent.LinkedBlockingQueue;

import org.testng.annotations.Test;

import com.tvd12.test.base.BaseTest;

public class LinkedBlockingQueueTest extends BaseTest {

    @Test
    public void test() throws Exception {
        LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        Object object1 = new Object();
        Object object2 = new Object();
        queue.add(object1);
        queue.add(object2);
        System.out.print("\n" + queue.remainingCapacity() + "\n");
        
        assert queue.take().equals(object1);
    }
    
    @Test
    public void test1() {
        LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(3);
        assert queue.offer(new Object());
        assert queue.offer(new Object());
        assert queue.offer(new Object());
        assert !queue.offer(new Object());
    }
    
}
