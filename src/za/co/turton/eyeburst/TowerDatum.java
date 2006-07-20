/*
 * Tower.java
 *
 * Created on June 20, 2006, 5:27 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.Date;

/**
 * Represents a signal set of measured data for an iBurst tower
 * @author james
 */
public class TowerDatum {
    
    String code;
    /**
     * When this measurement was taken
     */
    Date readingTime;
    
    /**
     * The reported signal
     */
    float cost;
    
    /**
     * The reported distance
     */
    int distance;
    
    /**
     * The reported load
     */
    int load;
    
    
    /** Creates a new instance of TowerData */
    public TowerDatum(String code) {
        this.code = code;
        this.readingTime = new Date(System.currentTimeMillis());
    }
}
