package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.ext.EzyEntry;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.ext.EzyEntryFetcher;

import lombok.Getter;
import lombok.Setter;

public abstract class EzyChildComponent 
        extends EzyComponent
        implements EzyEntryAware, EzyEntryFetcher {
    
    @Getter
    @Setter
    @JsonIgnore
    protected EzyEntry entry;
    
    @SuppressWarnings("rawtypes")
    public void addEventController(EzyConstant eventType, EzyEventController ctrl) {
        eventControllers.addController(eventType, ctrl);
    }
    
    public void destroy() {
        super.destroy();
        if(entry != null)
            processWithLogException(() -> entry.destroy());
        if(eventControllers != null)
            processWithLogException(() -> eventControllers.destroy());
        this.entry = null;
    }
    
}
