/*
 * TowerCompletedListener.java
 *
 * Created on August 9, 2006, 3:05 PM
 *
 */

package za.co.turton.eyeburst.accumulation;

import java.util.EventListener;

/**
 *
 * @author james
 */
public interface TowerCompletedListener extends EventListener {
    
    public void towerCompleted(TowerCompletedEvent tc);
}
