/*
 * TowerUpdateListener.java
 *
 * Created on August 7, 2006, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.turton.eyeburst;

import java.util.EventListener;

/**
 *
 * @author james
 */
public interface TowerUpdateListener extends EventListener {
    
    public void towerUpdate(TowerUpdateEvent evt);
}
