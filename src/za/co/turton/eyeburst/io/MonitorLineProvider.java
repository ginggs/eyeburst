/*
 * MonitorLineProvider.java
 *
 * Created on June 15, 2006, 10:48 AM
 */

package za.co.turton.eyeburst.io;

import java.io.IOException;

/**
 * <code>MonitorLineProvider</code>s are able to connect to some source read debug lines from it
 * @author james
 */
public interface MonitorLineProvider {
    
    /**
     * Connect this provider to its underlying data source
     * @throws java.io.IOException if an exceptional circumstance arises while trying to connect
     */
    void connect() throws IOException;
    
    /**
     * Disconnect this provider from its underlying data source
     * @throws java.io.IOException if an exceptional circumstance arises while trying to disconnect
     */
    void disconnect() throws IOException;
    
    /**
     * Determine whether this provider is connected to its underlying data source
     * @return true iff this provider is connected
     */
    boolean isConnected();
    
    /**
     * Read a line from the underlying datasource
     * @return the line read
     * @throws java.io.IOException if an exceptional circumstance arises while trying to read a line from the underlying data source
     */
    String readLine() throws IOException;
    
    /**
     * Request the underlying data source to write out the currently aligned iBurst tower
     * @throws java.io.IOException if an exceptional circumstance arises while trying to prompt the underlying data source
     */
    void requestCurrentTower() throws IOException;
}
