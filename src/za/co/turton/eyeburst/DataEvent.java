/*
 * DataEvent.java
 *
 * Created on July 15, 2006, 4:35 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.EventObject;

/**
 * Carries event data to <code>MonitorThreadListener</code>s
 * @author james
 */
public class DataEvent extends EventObject {
    
    private TowerDatum datum;        
    
    private String currentTowerCode;
    
    /**
     * Creates a new instance of DataEvent
     * 
     * @param source the monitor thread originator
     */
    public DataEvent(TowerDataThread source) {
        super(source);
    }
    
    /**
     * 
     * @param source the monitor thread originator
     * @param datum A datum read by the monitor thread
     */
    public DataEvent(TowerDataThread source, TowerDatum datum) {
        super(source);
        this.datum = datum;
    }        
    
    /**
     * 
     * @param source the monitor thread originator
     * @param currentTower the current tower read by the monitor thread
     */
    public DataEvent(TowerDataThread source, String currentTowerCode) {
        super(source);
        this.currentTowerCode = currentTowerCode;
    }
    
    /**
     * 
     * @return the datum carried on this event
     */
    public TowerDatum getDatum() {
        return datum;
    }        
    
    /**
     * 
     * @return the tower carried on this event
     */
    public String getCurrentTowerCode() {
        return currentTowerCode;
    }
}
