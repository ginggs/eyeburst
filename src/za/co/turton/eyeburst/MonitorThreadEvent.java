/*
 * MonitorThreadEvent.java
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
public class MonitorThreadEvent extends EventObject {
    
    private TowerDatum datum;
    
    private Exception threadException;
    
    private Tower currentTower;
    
    /**
     * Creates a new instance of MonitorThreadEvent
     * @param source the monitor thread originator
     */
    public MonitorThreadEvent(MonitorThread source) {
        super(source);
    }
    
    /**
     * 
     * @param source the monitor thread originator
     * @param datum A datum read by the monitor thread
     */
    public MonitorThreadEvent(MonitorThread source, TowerDatum datum) {
        super(source);
        this.datum = datum;
    }
    
    /**
     * 
     * @param source the monitor thread originator
     * @param threadException an exception encountered by the monitor thread
     */
    public MonitorThreadEvent(MonitorThread source, Exception threadException) {
        super(source);
        this.threadException = threadException;
    }
    
    /**
     * 
     * @param source the monitor thread originator
     * @param currentTower the current tower read by the monitor thread
     */
    public MonitorThreadEvent(MonitorThread source, Tower currentTower) {
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
     * @return the exception carried on this event
     */
    public Exception getThreadException() {
        return threadException;
    }
    
    /**
     * 
     * @return the tower carried on this event
     */
    public Tower getCurrentTower() {
        return currentTower;
    }
}
