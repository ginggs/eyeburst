/*
 * TowerSampleDataSet.java
 *
 * Created on August 8, 2006, 4:49 PM
 *
 */

package za.co.turton.eyeburst.accumulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import za.co.turton.eyeburst.Tower;

/**
 *
 * @author james
 */
public class TowerSampleDataSet {
    
    private LinkedHashMap<String, AccumulationCategory> setups;
    
    private Map<Tower, BoxAndWhiskerItem> statistics;
    
    private Set<DatasetChangeListener> listeners;
    
    private static final float OUTLIER_COEFF = 1.2f;
    
    private static final float FAROUT_COEFF = 1.2f;
        
    /** Creates a new instance of TowerSampleDataSet */
    public TowerSampleDataSet() {
//        this.setups = setups;
        this.statistics = new HashMap<Tower, BoxAndWhiskerItem>();
        this.listeners = new HashSet<DatasetChangeListener>();
    }

    private AccumulationCategory getSetup(int index) {
        int i = 0;
        
        for (AccumulationCategory setup : setups.values())
            if (i++ == index)
                return setup;
        
        throw new IndexOutOfBoundsException();
    }        
    
    private Tower getTower(int setupIndex, int towerIndex) {
        return getSetup(setupIndex).getTower(towerIndex);
    }
    
    private BoxAndWhiskerItem getStatistics(int series, int item) {
        return statistics.get(getTower(series, item));
    }
    
    /*
    public void towerCompleted(TowerCompletedEvent tc) {
        Tower tower = tc.getTower();
        BoxAndWhiskerItem item = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(tower.getSignalData());
        statistics.put(tower, item);
        
        DatasetChangeEvent evt = new DatasetChangeEvent(this, this);
        
        for (DatasetChangeListener listener : listeners)
            listener.datasetChanged(evt);
    }

    */
}
