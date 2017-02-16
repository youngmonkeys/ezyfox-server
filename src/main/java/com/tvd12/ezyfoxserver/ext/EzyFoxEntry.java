/**
 * 
 */
package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.entities.EzyFoxDestroyable;

/**
 * @author tavandung12
 *
 */
public interface EzyFoxEntry extends EzyFoxDestroyable {

    void start() throws Exception;
    
}
