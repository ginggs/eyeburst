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
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;
import za.co.turton.eyeburst.config.Singleton;

/**
 *
 * @author james
 */

public @Singleton class TowerPublisher {
    
    private Map<String, Set<WeakReference<Tower>>> towers;
    
    private Set<TowerPublicationListener> listeners;
    
    private int signalLowerBound;
    
    private TowerNameService towerNameService;
    
    /**
     *
     * Creates a new instance of TowerPublisher
     */
    public @InjectionConstructor TowerPublisher(
            @Inject("signalLowerBound") int signalLowerBound,
            @Inject("towerNameService") TowerNameService towerNameService ) {
        
        this.signalLowerBound = signalLowerBound;
        this.towerNameService = towerNameService;
        this.towers = new HashMap<String, Set<WeakReference<Tower>>>();
        this.listeners = new HashSet<TowerPublicationListener>();
    }
    
    public Tower createTower(String towerCode) {
        Tower tower = new Tower(towerCode, towerNameService.getTowerName(towerCode));
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
        
        if (datum.cost < signalLowerBound)
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

    public void setSignalLowerBound(int signalLowerBound) {
        this.signalLowerBound = signalLowerBound;
    }

    public void setTowerNameService(TowerNameService towerNameService) {
        this.towerNameService = towerNameService;
    }
}
