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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import za.co.turton.eyeburst.Tower;
import za.co.turton.eyeburst.TowerPublicationEvent;
import za.co.turton.eyeburst.TowerPublicationListener;
import za.co.turton.eyeburst.TowerPublisher;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

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
    
    private TowerPublisher towerPublisher;
    
    /**
     * Creates a new instance of SampleGroup
     */
    public @InjectionConstructor SampleGroup(
            @Inject("towerPublisher") TowerPublisher towerPublisher) {
        
        this.pendingTowers = new HashMap<String, Tower>();
        this.towers = new ArrayList<Tower>();
        this.towerPublisher = towerPublisher;
        towerPublisher.addListener(this);
        this.listeners = new HashSet<TowerCompletedListener>();
    }
    
    void add(Tower tower) throws TowerAlreadyPending {
        synchronized (pendingTowers) {
            if (pendingTowers.containsKey(tower.getCode()))
                throw new TowerAlreadyPending();
            
            pendingTowers.put(tower.getCode(), tower);
        }
    }
    
    public Tower getTower(int index) {
        return towers.get(index);
    }
    
    public void towerPublication(TowerPublicationEvent evt) {
        
        synchronized (pendingTowers) {
            LinkedList<String> completed = new LinkedList<String>();
                    
            for (Tower tower : pendingTowers.values()) {
                if (tower.getDataCount() >= this.getSampleSize()) {
                    String towerCode = tower.getCode();
                    completed.add(tower.getCode());
                            
                    towers.add(tower);
                    
                    TowerCompletedEvent tce = new TowerCompletedEvent(this, tower);
                    
                    for (TowerCompletedListener listener : listeners)
                        listener.towerCompleted(tce);
                }
            }
            
            for (String code : completed)
                pendingTowers.remove(code);
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
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }
}
