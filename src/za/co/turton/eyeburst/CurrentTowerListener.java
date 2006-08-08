/*
 * CurrentTowerListener.java
 *
 * Created on August 7, 2006, 3:07 PM
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
public interface CurrentTowerListener extends EventListener {
    /**
     * Called when the monitor thread reads a current tower line from its line provider
     * @param e event object carrying any relevant information
     */
    public void currentTower(DataEvent e);
}
