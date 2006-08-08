/*
 * TowerPublisher.java
 *
 * Created on August 3, 2006, 6:38 PM
 */

package za.co.turton.eyeburst;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import za.co.turton.eyeburst.config.Configuration;

/**
 *
 * @author james
 */
public class TowerPublisher {
    
    private Map<String, List<Tower>> towers;
    
    private Set<TowerUpdateListener> listeners;
    
    /**
     * Creates a new instance of TowerPublisher
     */
    public TowerPublisher() {
        this.towers = new HashMap<String, List<Tower>>();
        this.listeners = new HashSet<TowerUpdateListener>();
    }
    
    public Tower createTower(String towerCode) {
        Tower tower = new Tower(towerCode);
        List<Tower> towerList = towers.get(towerCode);
        
        if (towerList == null) {
            towerList = new LinkedList<Tower>();
            towers.put(towerCode, towerList);
        }
        
        towerList.add(tower);
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
        
        List<Tower> towerList = towers.get(datum.code);
        
        if (towerList != null) {
            for (Tower tower : towerList)
                tower.addDatum(datum);
        }
        
        for (TowerUpdateListener listener : listeners)
            listener.towerUpdate(new TowerUpdateEvent(this, datum));
    }
    
    public void addListener(TowerUpdateListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(TowerUpdateListener listener) {
        listeners.remove(listener);
    }
}
