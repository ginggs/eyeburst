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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import za.co.turton.eyeburst.Tower;

/**
 *
 * @author james
 */
public class TowerSampleDataSet implements BoxAndWhiskerCategoryDataset, TowerCompletedListener {
    
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
    
    
    public void towerCompleted(TowerCompletedEvent tc) {
        Tower tower = tc.getTower();
        BoxAndWhiskerItem item = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(tower.getSignalData());
        statistics.put(tower, item);
        
        DatasetChangeEvent evt = new DatasetChangeEvent(this, this);
        
        for (DatasetChangeListener listener : listeners)
            listener.datasetChanged(evt);
    }

    public int getRowIndex(Comparable key) {
    }

    public int getColumnIndex(Comparable key) {
    }

    public Comparable getRowKey(int row) {
    }

    public Comparable getColumnKey(int column) {
    }

    public void removeChangeListener(DatasetChangeListener listener) {
    }

    public void addChangeListener(DatasetChangeListener listener) {
    }

    public void setGroup(DatasetGroup group) {
    }

    public Number getValue(Comparable rowKey, Comparable columnKey) {
    }

    public Number getValue(int row, int column) {
    }

    public List getRowKeys() {
    }

    public Number getMedianValue(int row, int column) {
    }

    public Number getMeanValue(Comparable rowKey, Comparable columnKey) {
    }

    public Number getMeanValue(int row, int column) {
    }

    public Number getMaxRegularValue(Comparable rowKey, Comparable columnKey) {
    }

    public Number getMaxRegularValue(int row, int column) {
    }

    public Number getMaxOutlier(Comparable rowKey, Comparable columnKey) {
    }

    public Number getMaxOutlier(int row, int column) {
    }

    public DatasetGroup getGroup() {
    }

    public List getColumnKeys() {
    }

    public int getColumnCount() {
    }

    public Number getMedianValue(Comparable rowKey, Comparable columnKey) {
    }

    public Number getMinOutlier(int row, int column) {
    }

    public Number getMinOutlier(Comparable rowKey, Comparable columnKey) {
    }

    public Number getMinRegularValue(int row, int column) {
    }

    public Number getMinRegularValue(Comparable rowKey, Comparable columnKey) {
    }

    public List getOutliers(int row, int column) {
    }

    public List getOutliers(Comparable rowKey, Comparable columnKey) {
    }

    public Number getQ1Value(int row, int column) {
    }

    public Number getQ1Value(Comparable rowKey, Comparable columnKey) {
    }

    public Number getQ3Value(int row, int column) {
    }

    public Number getQ3Value(Comparable rowKey, Comparable columnKey) {
    }

    public int getRowCount() {
    }    
}
