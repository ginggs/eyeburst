/*
 * MonitorLineProvider.java
 *
 * Created on June 15, 2006, 10:48 AM
 */

package za.co.turton.eyeburst.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.turton.eyeburst.config.Configuration;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

/**
 * <code>MonitorLineProvider</code>s are able to connect to some source read debug lines from it
 * @author james
 */
public abstract class MonitorLineProvider {
    
    protected Logger logger;
    
    public MonitorLineProvider(Logger logger) {
        
        this.logger = logger;
        final Logger finalLogger = logger;
        
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook Disconnect") {
            public void run() {
                if (isConnected())
                    try {
                        disconnect();
                    } catch (IOException e) {
                        finalLogger.log(Level.WARNING, "Could not disconnect", e);
                    }
            }
        });
    }
    
    /**
     * Connect this provider to its underlying data source
     * @throws java.io.IOException if an exceptional circumstance arises while trying to connect
     */
    public abstract void connect() throws IOException;
    
    /**
     * Disconnect this provider from its underlying data source
     * @throws java.io.IOException if an exceptional circumstance arises while trying to disconnect
     */
    public abstract void disconnect() throws IOException;
    
    /**
     * Determine whether this provider is connected to its underlying data source
     * @return true iff this provider is connected
     */
    public abstract boolean isConnected();
    
    /**
     * Read a line from the underlying datasource
     * @return the line read
     * @throws java.io.IOException if an exceptional circumstance arises while trying to read a line from the underlying data source
     */
    public abstract String readLine() throws IOException;
    
    /**
     * Request the underlying data source to write out the currently aligned iBurst tower
     * @throws java.io.IOException if an exceptional circumstance arises while trying to prompt the underlying data source
     */
    public abstract void requestCurrentTower() throws IOException;
}
