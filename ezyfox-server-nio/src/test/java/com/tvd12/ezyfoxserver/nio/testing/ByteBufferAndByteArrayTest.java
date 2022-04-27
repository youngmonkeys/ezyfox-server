package com.tvd12.ezyfoxserver.nio.testing;

import com.tvd12.ezyfox.io.EzyArrays;
import com.tvd12.ezyfox.io.EzyBytes;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

public class ByteBufferAndByteArrayTest extends BaseTest {

    @SuppressWarnings("unused")
    @Test
    public void test() {
        byte[] bc = new byte[200];
        for (int i = 0; i < 200; ++i) {
            bc[i] = 1;
        }
        long time1 = Performance.create()
            .test(() -> {
                ByteBuffer byteBuffer = ByteBuffer.allocate(3 + 200);
                byteBuffer.put((byte) 0);
                byteBuffer.putShort((short) 47);
                byteBuffer.put(bc);
                byteBuffer.flip();
                byte[] bytes = new byte[3 + 200];
                byteBuffer.get(bytes);
            })
            .getTime();
        long time2 = Performance.create()
            .test(() -> {
                byte[] bytes = new byte[3 + 200];
                bytes[0] = 0;
                EzyArrays.copy(EzyBytes.getBytes((short) 47), bytes, 1);
                EzyArrays.copy(bc, bytes, 3);
            })
            .getTime();

        long time3 = Performance.create()
            .test(() -> {
                ByteBuffer byteBuffer = ByteBuffer.allocate(3276);
            }).getTime();

        long time4 = Performance.create()
            .test(() -> {
                byte[] bytes = new byte[3276];
            }).getTime();

        System.out.println(time1);
        System.out.println(time2);
        System.out.println(time3);
        System.out.println(time4);
    }
}
