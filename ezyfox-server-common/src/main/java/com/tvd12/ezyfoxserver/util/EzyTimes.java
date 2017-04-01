/**
 * 
 */
package com.tvd12.ezyfoxserver.util;

/**
 * @author tavandung12
 *
 */
public abstract class EzyTimes {

    private EzyTimes() {
    }
    
    /**
     * Tinh toan thoi gian con lai cua 1 trang thai nao do dua vao
     * thoi gian toi da cua trang thai va thoi gian bat dau cua trang
     * thai do
     * 
     * @param maxTime thoi gian toi da cua 1 trang thai
     * @param startingTime thoi gian bat dau cua trang thai
     * @return thoi gian con lai cua trang thai. Tra ve 0 neu maxTime = 0
     */
    public static int getRemainTime(long maxTime, long startingTime) {
        if(maxTime == 0)
            return 0;
        return (int)(maxTime - (System.currentTimeMillis() - startingTime));
    }
    
    /**
     * Tinh toan thoi gian con lai cua 1 trang thai nao do dua vao
     * thoi gian toi da cua trang thai va thoi gian bat dau cua trang
     * thai do
     * 
     * @param maxTime thoi gian toi da cua 1 trang thai
     * @param startingTime thoi gian bat dau cua trang thai
     * @return thoi gian con lai cua trang thai. Tra ve 0 neu maxTime = 0
     */
    public static int getPositiveRemainTime(long maxTime, long startingTime) {
        if(maxTime == 0)
            return 0;
        int result = (int)(maxTime - (System.currentTimeMillis() - startingTime));
        return result > 0 ? result : 0;
    }
    
}
