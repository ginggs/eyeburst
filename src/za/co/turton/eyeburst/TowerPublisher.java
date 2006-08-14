/*
 * TowerPublisher.java
 *
 * Created on August 3, 2006, 6:38 PM
 */

package za.co.turton.eyeburst;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import za.co.turton.eyeburst.config.Configuration;

/**
 *
 * @author james
 */
public class TowerPublisher {
    
    private Map<String, Set<WeakReference<Tower>>> towers;
    
    private Set<TowerPublicationListener> listeners;
        
    private static TowerPublisher publisher = null;
    
    public synchronized static TowerPublisher getInstance() {
        if (publisher == null)
            publisher = new TowerPublisher();
        
        return publisher;
    }
    
    /**
     *
     * Creates a new instance of TowerPublisher
     */
    private TowerPublisher() {
        this.towers = new HashMap<String, Set<WeakReference<Tower>>>();
        this.listeners = new HashSet<TowerPublicationListener>();
    }
    
    public Tower createTower(String towerCode) {
        Tower tower = new Tower(towerCode);
        WeakReference<Tower> towerRef = new WeakReference<Tower>(tower);        
        Set<WeakReference<Tower>> towerSet = towers.get(towerCode);
        
        if (towerSet == null) {
            towerSet = new HashSet<WeakReference<Tower>>();                        
            towers.put(towerCode, towerSet);
        }
        
        towerSet.add(towerRef);
        return tower;
    }
    
    public Tower createTowerWithDatum(TowerDatum datum) {
        Tower tower = createTower(datum.code);
        tower.addDatum(datum);
        return tower;
    }
    
    public void take(TowerDatum datum) {
        
        if (datum.cost < Configuration.getSignalLowerBound())
            return;
        
        Set<WeakReference<Tower>> towerSet = towers.get(datum.code);
        
        if (towerSet != null) {
            Set<WeakReference<Tower>> deadRefs = new HashSet();
        
            for (WeakReference<Tower> towerRef : towerSet) {
                Tower tower = towerRef.get();
                
                if (tower != null)
                    tower.addDatum(datum);                    
                else
                    deadRefs.add(towerRef);
            }
                
            for (WeakReference<Tower> deadRef : deadRefs)
                towerSet.remove(deadRef);
        }
        
        for (TowerPublicationListener listener : listeners)
            listener.towerPublication(new TowerPublicationEvent(this, datum));
    }
    
    public void addListener(TowerPublicationListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(TowerPublicationListener listener) {
        listeners.remove(listener);
    }
}
