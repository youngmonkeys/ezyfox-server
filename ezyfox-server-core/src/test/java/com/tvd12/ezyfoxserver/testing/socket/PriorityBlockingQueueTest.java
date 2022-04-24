package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.test.base.BaseTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTest extends BaseTest {

    @Test
    public void test() throws Exception {
        PriorityBlockingQueue<MyRequest> queue = new PriorityBlockingQueue<>(3, new MyRequestComparator());
        MyRequest handshakeRequest1 = new MyRequest(System.currentTimeMillis() - 100, EzyCommand.HANDSHAKE);
        MyRequest handshakeRequest2 = new MyRequest(System.currentTimeMillis(), EzyCommand.HANDSHAKE);
        MyRequest handshakeRequest3 = new MyRequest(System.currentTimeMillis() + 100, EzyCommand.HANDSHAKE);
        MyRequest loginRequest1 = new MyRequest(System.currentTimeMillis() - 100, EzyCommand.LOGIN);
        MyRequest loginRequest2 = new MyRequest(System.currentTimeMillis(), EzyCommand.LOGIN);
        MyRequest loginRequest3 = new MyRequest(System.currentTimeMillis() + 100, EzyCommand.LOGIN);

        assert queue.offer(handshakeRequest2);
        assert queue.offer(handshakeRequest3);
        assert queue.offer(handshakeRequest1);
        assert queue.offer(loginRequest2);
        assert queue.offer(loginRequest3);
        assert queue.offer(loginRequest1);

        assert queue.take() == handshakeRequest1;
        assert queue.take() == handshakeRequest2;
        assert queue.take() == handshakeRequest3;
        assert queue.take() == loginRequest1;
        assert queue.take() == loginRequest2;
        assert queue.take() == loginRequest3;

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; ++i) {
            queue.offer(loginRequest3);
        }
        long offset = System.currentTimeMillis() - start;
        System.out.println("addzzzz = " + offset);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class MyRequest {

        private long timestamp;
        private EzyCommand command;
    }

    public static class MyRequestComparator implements Comparator<MyRequest> {

        @Override
        public int compare(MyRequest one, MyRequest second) {
            if (one.getCommand().getPriority() > second.getCommand().getPriority()) {
                return 1;
            }
            if (one.getCommand().getPriority() < second.getCommand().getPriority()) {
                return -1;
            }
            if (one.getTimestamp() > second.getTimestamp()) {
                return 1;
            }
            if (one.getTimestamp() < second.getTimestamp()) {
                return -1;
            }
            return 0;
        }

    }
}
