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
import java.util.logging.Level;
import za.co.turton.eyeburst.config.Configuration;

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
    
    /** Creates a new instance of FileMonitorLineProvider */
    public FileMonitorLineProvider() {
        super();
        connected = false;
    }
    
    /**
     *
     * @see MonitorLineProvider#connect()
     */
    public void connect() throws IOException {
        in = new FileInputStream(Configuration.getResourcePath());
        reader = new InputStreamReader(in);
        lineReader = new LineNumberReader(reader);
        connected = true;
        Configuration.getLogger().fine("Reading from "+in);
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
        Configuration.getLogger().fine(in+" closed");
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
            Thread.sleep(Configuration.getFileLineProviderInterval());
        } catch (InterruptedException e) {
        }
        
        return lineReader.readLine();
    }
    
    /**
     *
     * @see MonitorLineProvider#requestCurrentTower
     */
    public void requestCurrentTower() throws IOException {
        // Not implemented
    }
}
