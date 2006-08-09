/*
 * TowerPublicationEvent.java
 *
 * Created on August 7, 2006, 3:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.turton.eyeburst;

import java.util.EventObject;

/**
 *
 * @author james
 */
public class TowerPublicationEvent extends EventObject {
    
    private TowerDatum datum;
    
    /**
     * Creates a new instance of TowerPublicationEvent
     */
    public TowerPublicationEvent(TowerPublisher source, TowerDatum datum) {
        super(source); 
        this.datum = datum;
    }
    
    public TowerPublisher getSource() {
        return (TowerPublisher) source;
    }

    public TowerDatum getTowerDatum() {
        return datum;
    }
}
