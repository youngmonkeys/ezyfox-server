package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfoxserver.ext.EzyEntry;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.ext.EzyEntryFetcher;

import lombok.Getter;
import lombok.Setter;

public class EzyChildComponent 
        extends EzyComponent
        implements EzyEntryAware, EzyEntryFetcher {
    
    @Getter
    @Setter
    @JsonIgnore
    protected EzyEntry entry;
    
    public void destroy() {
        processWithLogException(() -> entry.destroy());
    }
    
}
