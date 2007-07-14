/*
 * TowerDataThread.java
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
import java.util.logging.Logger;
import za.co.turton.eyeburst.config.ConfigurationChangedListener;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;
import za.co.turton.eyeburst.io.MonitorLineProvider;

/**
 * Thread to read and parse debug data from a line provider
 * @author james
 */
public class TowerDataThread extends Thread implements ConfigurationChangedListener {
    
    private MonitorLineProvider lineProvider;
    
    private TowerPublisher towerPublisher;
    
    private Logger logger;
    
    private String utdDebugMarker;
    
    private String alignedCode;
    
    private String dataCode;
    
    private int writerSleep;
    
    private Set<ConnectionListener> connectionListeners;
    
    private Set<CurrentTowerListener> ctListeners;
    
    private boolean mustRun;
    
    private static final String DELIM = " ";
    
    private static final String DUMMY_STR = "us";
    
    /**
     * Creates a new instance of TowerDataThread
     */
    public @InjectionConstructor TowerDataThread(
            @Inject("lineProvider") MonitorLineProvider lineProvider,
            @Inject("towerPublisher") TowerPublisher towerPublisher,
            @Inject("logger") Logger logger,
            @Inject("utdDebugMarker") String utdDebugMarker,
            @Inject("alignedCode") String alignedCode,
            @Inject("dataCode") String dataCode,
            @Inject("writerSleep") int writerSleep) {
        
        this.lineProvider = lineProvider;
        this.towerPublisher = towerPublisher;
        this.logger = logger;
        this.utdDebugMarker = utdDebugMarker;
        this.alignedCode = alignedCode;
        this.dataCode = dataCode;
        this.writerSleep = writerSleep;
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
            logger.fine(this+" running");
            
            new LineWriterThread().start();
            
            while (this.mustRun) {
                try {
                    String line = lineProvider.readLine();
                    StringTokenizer tokeniser = new StringTokenizer(line, DELIM);
                    
                    try {
                        String token = null;
                        
                        while (!utdDebugMarker.equals(token) && tokeniser.hasMoreTokens()) {
                            token = tokeniser.nextToken().trim();
                        }
                        
                        if (!tokeniser.hasMoreTokens())
                            // This line is not of interest
                            continue;
                        
                        String typeCode = tokeniser.nextToken().trim();
                        
                        if (typeCode.equals(alignedCode)) {
                            
                            tokeniser.nextToken();
                            tokeniser.nextToken();
                            tokeniser.nextToken();
                            
                            String currentTowerCode = tokeniser.nextToken().trim();
                            fireCurrentTower(currentTowerCode);
                            
                        } else if (typeCode.equals(dataCode)) {
                            
                            parseUntil("Bscc", tokeniser);
                            String towerCode = tokeniser.nextToken().trim();
                            TowerDatum towerDatum = new TowerDatum(towerCode);
                            
                            parseUntil("Cost", tokeniser);
                            towerDatum.cost = Float.parseFloat(tokeniser.nextToken().trim());
                            
                            parseUntil("Distance", tokeniser);
                            towerDatum.distance = Integer.parseInt(tokeniser.nextToken().trim());
                            
                            parseUntil("Load", tokeniser);
                            towerDatum.load = Integer.parseInt(tokeniser.nextToken().trim());
                            
                            towerPublisher.publish(towerDatum);
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Could not parse "+line, e);
                        fireUnparseableLine(e);
                        
                    }
                } catch (SocketTimeoutException e) {
                    if (this.mustRun)
                        logger.log(Level.WARNING, "Socket read timed out", e);
                    else
                        logger.log(Level.FINE, "Socket read timed out", e);
                    
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Could not read data from UTD", e);
                }
            }
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not connect to UTD", e);
            fireConnectFailed(e);
            
        } finally {
            try {
                if (lineProvider.isConnected())
                    lineProvider.disconnect();
                
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error while trying to disconnect", e);
            }
            
            logger.log(Level.FINE, "Monitor thread finishing");
            fireDisconnected();
        }
    }
    
    private void parseUntil(String token, StringTokenizer tokeniser) {
        
        while (tokeniser.hasMoreTokens() && !tokeniser.nextToken().equalsIgnoreCase(token));
    }
    
    private void fireUnparseableLine(Exception e) {
        ConnectionEvent event = new ConnectionEvent(this, e);
        
        for (ConnectionListener listener : connectionListeners)
            listener.unparseableLine(event);
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
        DataEvent event = new DataEvent(this, towerCode);
        
        for (CurrentTowerListener listener : ctListeners)
            listener.currentTower(event);
    }
    
    public void start() {
        this.mustRun = true;
        super.start();
    }
    
    /**
     * Requests the termination of this thread
     */
    public void requestStop() {
        mustRun = false;
    }
    
    public void configurationChanged() {
        requestStop();
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
         * Periodically write the current tower prompt to the configured line provider
         */
        public void run() {
            logger.log(Level.FINE, this+" running");
            
            while (mustRun) {
                try {
                    lineProvider.requestCurrentTower();
                    Thread.sleep(writerSleep);
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Could not write current tower prompt", e);
                } catch (InterruptedException e) {
                    logger.log(Level.FINE, "Interrupted while sleeping", e);
                }
            }
            
            logger.log(Level.FINE, "Writer thread finishing");
        }
    }
}