package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.ext.EzyEntry;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.ext.EzyEntryFetcher;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;

import lombok.Getter;
import lombok.Setter;

public abstract class EzyChildComponent 
        extends EzyComponent
        implements EzyEntryAware, EzyEntryFetcher {
    
    @Getter
    @Setter
    @JsonIgnore
    protected EzyEntry entry;
    
    @Getter
    @JsonIgnore
    protected EzyEventControllers eventControllers = newEventControllers();
    
    @SuppressWarnings("rawtypes")
    public void addEventController(EzyConstant eventType, EzyEventController ctrl) {
        eventControllers.addController(eventType, ctrl);
    }
    
    protected EzyEventControllers newEventControllers() {
        return new EzyEventControllersImpl();
    }
    
    public void destroy() {
        if(entry != null)
            processWithLogException(() -> entry.destroy());
        if(eventControllers != null)
            processWithLogException(() -> eventControllers.destroy());
        this.entry = null;
        this.eventControllers = null;
    }
    
}
