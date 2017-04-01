package com.tvd12.ezyfoxserver.concurrent;

public class EzyFastThreadLocalThread extends Thread {

    public EzyFastThreadLocalThread() { }

    public EzyFastThreadLocalThread(Runnable target) {
        super(target);
    }

    public EzyFastThreadLocalThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public EzyFastThreadLocalThread(String name) {
        super(name);
    }

    public EzyFastThreadLocalThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public EzyFastThreadLocalThread(Runnable target, String name) {
        super(target, name);
    }

    public EzyFastThreadLocalThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public EzyFastThreadLocalThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }
}

