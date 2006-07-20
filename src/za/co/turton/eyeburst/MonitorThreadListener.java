/*
 * MonitorThreadListener.java
 *
 * Created on July 15, 2006, 4:38 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.EventListener;

/**
 * Defines the contract for <code>MonitorThreadListener</code>s.  When a 
 * <code>MonitorThreadListener</code> is registered with a monitor thread, said 
 * thread will invoke the methods in this interface at the appropriate times.
 *
 * @author james
 */
public interface MonitorThreadListener extends EventListener {
    
    /**
     * Called when the monitor thread's line provider becomes connected
     * @param e event object carrying any relevant information
     */
    public void connected(MonitorThreadEvent e);
    
    /**
     * Called when the monitor thread's line provider becomes disconnected
     * @param e event object carrying any relevant information
     */
    public void disconnected(MonitorThreadEvent e);
    
    /**
     * Called when the monitor thread's line provider fails to connect
     * @param e event object carrying any relevant information
     */
    public void connectFailed(MonitorThreadEvent e);
    
    /**
     * Called when the monitor thread reads a tower datum line from its line provider
     * @param e event object carrying any relevant information
     */
    public void towerDatum(MonitorThreadEvent e);
    
    /**
     * Called when the monitor thread reads a current tower line from its line provider
     * @param e event object carrying any relevant information
     */
    public void currentTower(MonitorThreadEvent e);
}
