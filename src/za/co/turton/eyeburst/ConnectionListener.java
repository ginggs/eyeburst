/*
 * ConnectionListener.java
 *
 * Created on July 15, 2006, 4:38 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.EventListener;

/**
 * Defines the contract for <code>ConnectionListener</code>s.  When a
 * <code>ConnectionListener</code> is registered with a monitor thread, said
 * thread will invoke the methods in this interface at the appropriate times.
 * 
 * @author james
 */
public interface ConnectionListener extends EventListener {
    
    /**
     * Called when the monitor thread's line provider becomes connected
     * @param e event object carrying any relevant information
     */
    public void connected(ConnectionEvent e);
    
    /**
     * Called when the monitor thread's line provider becomes disconnected
     * @param e event object carrying any relevant information
     */
    public void disconnected(ConnectionEvent e);
    
    /**
     * Called when the monitor thread's line provider fails to connect
     * @param e event object carrying any relevant information
     */
    public void connectFailed(ConnectionEvent e);        
}
