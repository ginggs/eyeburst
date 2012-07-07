/*
 * Tower.java
 *
 * Created on June 20, 2006, 5:27 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.Date;

/**
 * Represents a single datum measured for an iBurst tower
 * @author james
 */
public class TowerDatum {
    
    public String code;
    /**
     * When this measurement was taken
     */
    public Date readingTime;
    
    /**
     * The reported signal
     */
    public float cost;
    
    /**
     * The reported distance
     */
    public float distance;
    
    /**
     * The reported load
     */
    public int load;
    
    
    /** Creates a new instance of TowerData */
    public TowerDatum(String code) {
        this.code = code;
        this.readingTime = new Date(System.currentTimeMillis());
    }
}
