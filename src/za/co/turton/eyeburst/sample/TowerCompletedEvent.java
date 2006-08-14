/*
 * TowerCompletedEvent.java
 *
 * Created on August 9, 2006, 3:03 PM
 *
 */

package za.co.turton.eyeburst.sample;

import za.co.turton.eyeburst.Tower;

/**
 *
 * @author james
 */
public class TowerCompletedEvent extends java.util.EventObject {
    
    private Tower tower;
    
    public TowerCompletedEvent(SampleGroup source, Tower tower) {
        super(source);
        this.setTower(tower);
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }
    
}
