/*
 * SampleGroup.java
 *
 * Created on August 8, 2006, 3:59 PM
 *
 */

package za.co.turton.eyeburst.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import za.co.turton.eyeburst.Tower;
import za.co.turton.eyeburst.TowerPublicationEvent;
import za.co.turton.eyeburst.TowerPublicationListener;
import za.co.turton.eyeburst.TowerPublisher;

/**
 *
 * @author james
 */
public class SampleGroup implements TowerPublicationListener {
    
    private List<Tower> towers;
    
    private Map<String, Tower> pendingTowers;
    
    private String groupName;
    
    private int sampleSize;
    
    private Set<TowerCompletedListener> listeners;
    
    /**
     * Creates a new instance of SampleGroup
     */
    public SampleGroup(String groupName, int sampleSize) {
        this.pendingTowers = new HashMap<String, Tower>();
        this.towers = new ArrayList<Tower>();
        this.groupName = groupName;
        this.sampleSize = sampleSize;
        this.listeners = new HashSet<TowerCompletedListener>();
        TowerPublisher.getInstance().addListener(this);
    }
    
    void add(Tower tower) throws TowerAlreadyPending {
        if (pendingTowers.containsKey(tower.getCode()))
            throw new TowerAlreadyPending();
        
        pendingTowers.put(tower.getCode(), tower);
    }
    
    public Tower getTower(int index) {
        return towers.get(index);
    }
    
    public void towerPublication(TowerPublicationEvent evt) {
        
        for (Tower tower : pendingTowers.values()) {
            if (tower.getDataCount() >= this.getSampleSize()) {
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
    
    public String getGroupName() {
        return groupName;
    }
    
    public void addListener(TowerCompletedListener listener) {
        if (listener ==  null)
            throw new NullPointerException();
        
        this.listeners.add(listener);
    }
    
    public void removeListener(TowerCompletedListener listener) {
        if (listener ==  null)
            throw new NullPointerException();
        
        this.listeners.remove(listener);
    }

    public int getSampleSize() {
        return sampleSize;
    }
}
