/*
 * FileMonitorLineProvider.java
 *
 * Created on June 19, 2006, 10:50 AM
 *
 */

package za.co.turton.eyeburst.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.logging.Logger;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

/**
 * Provides debug data lines read from a file at regular intervals.
 * Mostly useful for dev and test when not connected to a UTD.
 *
 * @author james
 */
public class FileMonitorLineProvider extends MonitorLineProvider {
    
    private InputStream in;
    
    private InputStreamReader reader;
    
    private LineNumberReader lineReader;
    
    private boolean connected;
    
    private String resourcePath;
    
    private int lineProviderInterval;
    
    private Logger logger;
            
    /** Creates a new instance of FileMonitorLineProvider */
    public @InjectionConstructor FileMonitorLineProvider(
            @Inject("resourcePath") String resourcePath,
            @Inject("fileLineProviderInterval") int lineProviderInterval,
            @Inject("logger") Logger logger) {
        
        super(logger);
        this.resourcePath = resourcePath;
        this.lineProviderInterval = lineProviderInterval;
        this.logger = logger;
        connected = false;
    }
    
    /**
     *
     * @see MonitorLineProvider#connect()
     */
    public void connect() throws IOException {
        in = new FileInputStream(resourcePath);
        reader = new InputStreamReader(in);
        lineReader = new LineNumberReader(reader);
        connected = true;
        logger.fine("Reading from "+in);
    }
    
    /**
     *
     * @see MonitorLineProvider#disconnect()
     */
    public void disconnect() throws IOException {
        if (lineReader != null)
            lineReader.close();
        
        if (reader != null)
            reader.close();
        
        if (in != null)
            in.close();
        
        connected = false;
        logger.fine(in+" closed");
    }
    
    /**
     *
     * @see MonitorLineProvider#isConnected()
     */
    public boolean isConnected() {
        return connected;
    }
    
    /**
     *
     * @see MonitorLineProvider#readLine
     */
    public String readLine() throws IOException {
        try {
            Thread.sleep(lineProviderInterval);
        } catch (InterruptedException e) {
        }
        
        String line = lineReader.readLine();
        
        if (line == null) {
            disconnect();
            connect();
            line = lineReader.readLine();
        }
        
        return line;
    }
    
    /**
     *
     * @see MonitorLineProvider#requestCurrentTower
     */
    public void requestCurrentTower() throws IOException {
        // Not implemented
    }
}
