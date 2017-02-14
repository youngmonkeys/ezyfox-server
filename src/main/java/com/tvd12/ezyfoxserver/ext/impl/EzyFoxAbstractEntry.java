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
public class EzyFoxAbstractEntry {

    private Logger logger;
    
    public EzyFoxAbstractEntry() {
        this.logger = LoggerFactory.getLogger(getClass());
    }
    
    protected Logger getLogger() {
        return this.logger;
    }
    
}
