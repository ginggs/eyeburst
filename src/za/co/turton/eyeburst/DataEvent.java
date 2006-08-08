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
    
    private Tower currentTower;
    
    /**
     * Creates a new instance of DataEvent
     * 
     * @param source the monitor thread originator
     */
    public DataEvent(MonitorThread source) {
        super(source);
    }
    
    /**
     * 
     * @param source the monitor thread originator
     * @param datum A datum read by the monitor thread
     */
    public DataEvent(MonitorThread source, TowerDatum datum) {
        super(source);
        this.datum = datum;
    }        
    
    /**
     * 
     * @param source the monitor thread originator
     * @param currentTower the current tower read by the monitor thread
     */
    public DataEvent(MonitorThread source, Tower currentTower) {
        super(source);
        this.currentTower = currentTower;
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
    public Tower getCurrentTower() {
        return currentTower;
    }
}
