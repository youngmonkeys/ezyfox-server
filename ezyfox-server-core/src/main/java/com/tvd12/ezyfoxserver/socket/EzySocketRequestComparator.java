package com.tvd12.ezyfoxserver.socket;

import java.util.Comparator;

public class EzySocketRequestComparator implements Comparator<EzySocketRequest> {

    @Override
    public int compare(EzySocketRequest one, EzySocketRequest second) {
        if(one.getCommand().getPriority() > second.getCommand().getPriority())
            return 1;
        if(one.getCommand().getPriority() < second.getCommand().getPriority())
            return -1;
        if(one.getTimestamp() > second.getTimestamp())
            return 1;
        if(one.getTimestamp() < second.getTimestamp())
            return -1;
        return 0;
    }
    
}
