package com.tvd12.ezyfoxserver.testing;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.tvd12.test.base.BaseTest;

public class ByteBufferTest extends BaseTest {

    @Test
    public void test() {
        ByteBuffer buffer = ByteBuffer.allocate(32768);
        long start = System.currentTimeMillis();
        for(int i = 0 ; i < 1000000 ; ++i) {
            byte[] bytes = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
        }
        long offset = System.currentTimeMillis() - start;
        System.out.println("offset1 = " + offset);
        
        start = System.currentTimeMillis();
        for(int i = 0 ; i < 1000000 ; ++i) {
            ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        }
        offset = System.currentTimeMillis() - start;
        System.out.println("offset2 = " + offset);
    }
    
}
