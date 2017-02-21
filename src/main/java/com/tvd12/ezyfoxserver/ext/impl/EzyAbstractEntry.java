/**
 * 
 */
package com.tvd12.ezyfoxserver.ext.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tavandung12
 *
 */
public class EzyAbstractEntry {

    private Logger logger;
    
    public EzyAbstractEntry() {
        this.logger = LoggerFactory.getLogger(getClass());
    }
    
    protected Logger getLogger() {
        return this.logger;
    }
    
}
