/*
 * SignalStrengthComparator.java
 *
 * Created on August 9, 2006, 11:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.turton.eyeburst;

import java.util.Comparator;

/**
 *
 * @author james
 */
public class SignalStrengthComparator implements Comparator {
        
    public int compare(Object o1, Object o2) {
        return Float.compare(((TowerDatum) o1).cost, ((TowerDatum) o2).cost);
    }
    
}
