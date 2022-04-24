package com.tvd12.ezyfoxserver.testing;

import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest extends BaseTest {

    @Test
    public void test() {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(3);
        System.out.println(queue.offer("1"));
        System.out.println(queue.offer("3"));
        System.out.println(queue.offer("2"));
        System.out.println(queue.offer("5"));
        System.out.println(queue);
    }

}
