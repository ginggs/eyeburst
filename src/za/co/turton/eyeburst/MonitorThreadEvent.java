/*
 * MonitorThreadEvent.java
 *
 * Created on July 15, 2006, 4:35 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.EventObject;

/**
 *
 * @author james
 */
public class MonitorThreadEvent extends EventObject {
    
    private TowerDatum datum;
    
    private Exception threadException;
    
    private Tower currentTower;
    
    /** Creates a new instance of MonitorThreadEvent */
    public MonitorThreadEvent(MonitorThread source) {
        super(source);
    }
    
    public MonitorThreadEvent(MonitorThread source, TowerDatum datum) {
        super(source);
        this.datum = datum;
    }
    
    public MonitorThreadEvent(MonitorThread source, Exception threadException) {
        super(source);
        this.threadException = threadException;
    }
    
    public MonitorThreadEvent(MonitorThread source, Tower currentTower) {
        super(source);
        this.currentTower = currentTower;
    }
    
    public TowerDatum getDatum() {
        return datum;
    }
    
    public Exception getThreadException() {
        return threadException;
    }
    
    public Tower getCurrentTower() {
        return currentTower;
    }
}
