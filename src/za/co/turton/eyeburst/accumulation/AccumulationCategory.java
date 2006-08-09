/*
 * AccumulationCategory.java
 *
 * Created on August 8, 2006, 3:59 PM
 *
 */

package za.co.turton.eyeburst.accumulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import za.co.turton.eyeburst.Tower;
import za.co.turton.eyeburst.TowerPublicationEvent;
import za.co.turton.eyeburst.TowerPublicationListener;

/**
 *
 * @author james
 */
public class AccumulationCategory implements TowerPublicationListener {
    
    private List<Tower> towers;
    
    private Map<String, Tower> pendingTowers;
    
    private String categoryName;
    
    private int sampleSize;
    
    private Set<TowerCompletedListener> listeners;
    
    /**
     * Creates a new instance of AccumulationCategory
     */
    public AccumulationCategory(String setupName, int sampleSize) {
        this.pendingTowers = new HashMap<String, Tower>();
        this.towers = new ArrayList<Tower>();
        this.categoryName = setupName;
        this.sampleSize = sampleSize;
        this.listeners = new HashSet<TowerCompletedListener>();
    }
    
    void add(Tower tower) {
        pendingTowers.put(tower.getCode(), tower);
    }
    
    public Tower getTower(int index) {
        return towers.get(index);
    }
    
    public void towerPublication(TowerPublicationEvent evt) {
        
        for (Tower tower : pendingTowers.values()) {
            if (tower.getDataCount() >= this.sampleSize) {
                String towerCode = tower.getCode();
                pendingTowers.remove(tower.getCode());
                towers.add(tower);
                
                TowerCompletedEvent tce = new TowerCompletedEvent(this, tower);
                
                for (TowerCompletedListener listener : listeners)
                    listener.towerCompleted(tce);
            }
        }
    }
    
    public int getTowerCount() {
        return towers.size();
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void addListener(TowerCompletedListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(TowerCompletedListener listener) {
        this.listeners.remove(listener);
    }
}
