/**
 * 
 */
package com.tvd12.ezyfoxserver.util;

/**
 * @author tavandung12
 *
 */
public final class EzyTimes {

	public static final long MILIS_OF_SECOND	= 1000;
	public static final long MILIS_OF_MINUTE	= 60 * MILIS_OF_SECOND;
	public static final long MILIS_OF_HOUR		= 60 * MILIS_OF_MINUTE;
	public static final long MILIS_OF_DAY		= 24 * MILIS_OF_HOUR;
	
    private EzyTimes() {
    }
    
    public static int getRemainTime(long maxTime, long startingTime) {
        if(maxTime <= 0)
            return 0;
        return (int)(maxTime - (System.currentTimeMillis() - startingTime));
    }
    
    public static int getPositiveRemainTime(long maxTime, long startingTime) {
        int result = getRemainTime(maxTime, startingTime);
        return result > 0 ? result : 0;
    }
    
}
