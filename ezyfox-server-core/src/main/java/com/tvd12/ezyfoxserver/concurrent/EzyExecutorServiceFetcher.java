package com.tvd12.ezyfoxserver.concurrent;

import java.util.concurrent.ScheduledExecutorService;

public interface EzyExecutorServiceFetcher {

    ScheduledExecutorService getExecutorService();

}
