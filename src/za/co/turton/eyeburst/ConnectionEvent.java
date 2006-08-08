/*
 * ConnectionEvent.java
 *
 * Created on August 1, 2006, 4:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.turton.eyeburst;

import java.util.EventObject;

/**
 *
 * @author james
 */
public class ConnectionEvent extends EventObject {
    
    private Exception threadException;
    
    /** Creates a new instance of ConnectionEvent */
    public ConnectionEvent(MonitorThread source) {
        super(source);
    }
    
    /**
     * 
     * @param source 
     * @param threadException 
     */
    public ConnectionEvent(MonitorThread source, Exception threadException) {
        super(source);
        this.threadException = threadException;
    }

    /**
     * 
     * @return the monitor thread exception carried by this event object
     */
    public Exception getThreadException() {
        return threadException;
    }
    
    
}
