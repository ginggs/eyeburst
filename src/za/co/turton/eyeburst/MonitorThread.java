/*
 * MonitorThread.java
 *
 * Created on June 14, 2006, 5:37 PM
 *
 */

package za.co.turton.eyeburst;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import za.co.turton.eyeburst.config.Configuration;
import za.co.turton.eyeburst.io.MonitorLineProvider;

/**
 * Thread to read and parse debug data from a line provider
 * @author james
 */
public class MonitorThread extends Thread {
    
    private MonitorLineProvider lineProvider;
    
    private Set<MonitorThreadListener> listeners;
    
    private boolean mustRun;
    
    private static final String DELIM = " ";
    
    /**
     * Creates a new instance of MonitorThread
     */
    public MonitorThread() {
        this.setName("MonitorThread");
        this.setDaemon(true);
        this.listeners = new HashSet<MonitorThreadListener>();
    }
    
    /**
     * Register a listener with this monitor thread
     * @param l the listener
     */
    public void addListener(MonitorThreadListener l) {
        listeners.add(l);
    }
    
    /**
     * Deregister a listener with this monitorr thread
     * @param l the listener
     */
    public void removeListener(MonitorThreadListener l) {
        listeners.remove(l);
    }
    
    /**
     * Read and parse debug data from the configured line provider
     */
    public void run() {
        try {
            lineProvider.connect();
            fireConnected();
            Configuration.getLogger().log(Level.FINE, this+" running");            
            
            new LineWriterThread().start();
            
            while (this.mustRun) {
                try {
                    String line = lineProvider.readLine();
                    
                    if (line == null) {
                        this.mustRun = false;
                        break;
                    }
                    
                    StringTokenizer tokeniser = new StringTokenizer(line, DELIM);
                    
                    try {
                        String token;
                        
                        do {
                            token = tokeniser.nextToken().trim();
                        } while (!token.equals(Configuration.getUtdDebugMarker()));
                        
                        String typeCode = tokeniser.nextToken().trim();
                        
                        if (typeCode.equals(Configuration.getAlignedCode())) {
                            
                            tokeniser.nextToken();
                            tokeniser.nextToken();
                            tokeniser.nextToken();
                            
                            String currentTowerCode = tokeniser.nextToken().trim();
                            fireCurrentTower(currentTowerCode);
                            
                        } else if (typeCode.equals(Configuration.getDataCode())) {
                            
                            // "BScc"
                            tokeniser.nextToken();
                            String towerCode = tokeniser.nextToken().trim();
                            TowerDatum towerDatum = new TowerDatum(towerCode);
                            // "Cost"
                            tokeniser.nextToken();
                            towerDatum.cost = Float.parseFloat(tokeniser.nextToken().trim());
                            // "Distance"
                            tokeniser.nextToken();
                            towerDatum.distance = Integer.parseInt(tokeniser.nextToken().trim());
                            // "Load"
                            tokeniser.nextToken();
                            towerDatum.load = Integer.parseInt(tokeniser.nextToken().trim());
                            
                            fireTowerDatum(towerDatum);
                        }
                    } catch (NumberFormatException e) {
                        Configuration.getLogger().log(Level.INFO, "Threw away malformed line: "+line);
                    } catch (NoSuchElementException e) {
                        Configuration.getLogger().log(Level.INFO, "Threw away malformed line: "+line);
                    }
                } catch (SocketTimeoutException e) {
                    if (this.mustRun)
                        Configuration.getLogger().log(Level.INFO, "Socket read timed out", e);
                    else
                        Configuration.getLogger().log(Level.FINE, "Socket read timed out after disconnect requested", e);
                    
                } catch (IOException e) {
                    Configuration.getLogger().log(Level.WARNING, "Could not read data from UTD", e);
                }
            }
            
        } catch (IOException e) {
            Configuration.getLogger().log(Level.SEVERE, "Could not connect to UTD", e);
            fireConnectFailed(e);
            
        } finally {
            try {
                if (lineProvider.isConnected())
                    lineProvider.disconnect();
                
            } catch (IOException e) {
                Configuration.getLogger().log(Level.WARNING, "Error while trying to disconnect", e);
            }
            
            Configuration.getLogger().log(Level.FINE, "Monitor thread finishing");            
            fireDisconnected();
        }
    }
    
    private void fireConnectFailed(Exception e) {
        MonitorThreadEvent event = new MonitorThreadEvent(this, e);
        
        for (MonitorThreadListener listener : listeners)
            listener.connectFailed(event);
    }
    
    private void fireDisconnected() {
        MonitorThreadEvent event = new MonitorThreadEvent(this);
        
        for (MonitorThreadListener listener : listeners)
            listener.disconnected(event);
    }
    
    private void fireConnected() {
        MonitorThreadEvent event = new MonitorThreadEvent(this);
        
        for (MonitorThreadListener listener : listeners)
            listener.connected(event);
    }
    
    private void fireTowerDatum(TowerDatum datum) {
        MonitorThreadEvent event = new MonitorThreadEvent(this, datum);
        
        for (MonitorThreadListener listener : listeners)
            listener.towerDatum(event);
    }
    
    private void fireCurrentTower(String towerCode) {
        MonitorThreadEvent event = new MonitorThreadEvent(this, new Tower(towerCode));
        
        for (MonitorThreadListener listener : listeners)
            listener.currentTower(event);
    }
    
    /**
     * Configures this threads line provider, adds a JVM shutdown hook to disconnect
     * the line provider and starts this thread.
     */
    public void start() {
        try {
            this.lineProvider = (MonitorLineProvider) Configuration.getLineProvider().newInstance();
        } catch (Exception e) {
            Configuration.getLogger().log(Level.SEVERE, "Could not instantiate line provider: "+Configuration.getLineProvider(), e);
            fireConnectFailed(e);
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook Disconnect") {
            public void run() {
                if (lineProvider.isConnected())
                    try {
                        lineProvider.disconnect();
                    } catch (IOException e) {
                        Configuration.getLogger().log(Level.WARNING, "Could not disconnect", e);
                    }
            }
        });
        
        this.mustRun = true;
        super.start();
    }
    
    /**
     * Requests the termination of this thread
     */
    public void requestStop() {
        mustRun = false;
    }        
    
    /**
     * Thread to write the 'current tower' prompt to the configured line provider at regular intervals
     */
    class LineWriterThread extends Thread {
        
        /**
         * Create a new LineWriterThread
         */
        public LineWriterThread() {
            this.setName("LineWriterThread");
            this.setDaemon(true);
        }
        
        /**
         * Periodicaly write the current tower prompt to the configured line provider
         */
        public void run() {
            Configuration.getLogger().log(Level.FINE, this+" running");
            
            while (mustRun) {
                try {
                    lineProvider.requestCurrentTower();
                    Thread.sleep(Configuration.getWriterSleep());
                } catch (IOException e) {
                    Configuration.getLogger().log(Level.WARNING, "Could not write current tower prompt", e);
                } catch (InterruptedException e) {
                    Configuration.getLogger().log(Level.FINE, "Interrupted while sleeping", e);
                }
            }
            
            Configuration.getLogger().log(Level.FINE, "Writer thread finishing");
        }
    }
}