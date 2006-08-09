/*
 * MonitorThread.java
 *
 * Created on June 14, 2006, 5:37 PM
 *
 */

package za.co.turton.eyeburst;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import za.co.turton.eyeburst.config.Configuration;
import za.co.turton.eyeburst.io.MonitorLineProvider;

/**
 * Thread to read and parse debug data from a line provider
 * @author james
 */
public class MonitorThread extends Thread {
    
    private MonitorLineProvider lineProvider;
    
    private TowerPublisher dataHub;
    
    private Set<ConnectionListener> connectionListeners;
    
    private Set<CurrentTowerListener> ctListeners;
    
    private boolean mustRun;
    
    private static final String DELIM = " ";
    
    /**
     * Creates a new instance of MonitorThread
     */
    public MonitorThread(TowerPublisher dataHub) {
        this.dataHub = dataHub;
        this.setName("MonitorThread");
        this.setDaemon(true);
        this.connectionListeners = new HashSet<ConnectionListener>();
        this.ctListeners = new HashSet<CurrentTowerListener>();
    }
    
    /**
     * Register a listener with this monitor thread
     * @param l the listener
     */
    public void addListener(ConnectionListener l) {
        connectionListeners.add(l);
    }
    
    /**
     * Deregister a listener with this monitorr thread
     * @param l the listener
     */
    public void removeListener(ConnectionListener l) {
        connectionListeners.remove(l);
    }
    
    /**
     * Register a listener with this monitor thread
     * @param l the listener
     */
    public void addListener(CurrentTowerListener l) {
        ctListeners.add(l);
    }
    
    /**
     * Deregister a listener with this monitor thread
     * @param l the listener
     */
    public void removeListener(CurrentTowerListener l) {
        ctListeners.remove(l);
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
                                                        
                            dataHub.take(towerDatum);
                        }
                    } catch (Exception e) {
                        Configuration.getLogger().log(Level.FINE, "Could not parse "+line, e);
                    }
                } catch (SocketTimeoutException e) {
                    if (this.mustRun)
                        Configuration.getLogger().log(Level.INFO, "Socket read timed out", e);
                    else
                        Configuration.getLogger().log(Level.FINE, "Socket read timed out", e);
                    
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
        ConnectionEvent event = new ConnectionEvent(this, e);
        
        for (ConnectionListener listener : connectionListeners)
            listener.connectFailed(event);
    }
    
    private void fireDisconnected() {
        ConnectionEvent event = new ConnectionEvent(this);
        
        for (ConnectionListener listener : connectionListeners)
            listener.disconnected(event);
    }
    
    private void fireConnected() {
        ConnectionEvent event = new ConnectionEvent(this);
        
        for (ConnectionListener listener : connectionListeners)
            listener.connected(event);
    }
    
    private void fireCurrentTower(String towerCode) {
        DataEvent event = new DataEvent(this, new Tower(towerCode));
        
        for (CurrentTowerListener listener : ctListeners)
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