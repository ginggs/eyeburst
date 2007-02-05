/*
 * Tower.java
 *
 * Created on June 30, 2006, 5:33 PM
 *
 */
package za.co.turton.eyeburst;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import za.co.turton.eyeburst.config.Configure;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.monitor.Column;

/**
 * Represents an iBurst tower
 * @author james
 */
public class Tower {
    
    private String code;
    
    private String name;
    
    private float totalCost;
    
    private float totalSquaredCost;
    
    private float min;
    
    private float max;
    
    private float distance;
    
    private float load;
    
    private List<TowerDatum> towerData;   
    
    /**
     * Creates a new instance of Tower
     * @param code The code of the tower
     */
    public Tower(String code, String name) {
        this.code = code;
        this.name = name;        
        towerData = new LinkedList<TowerDatum>();
        totalCost = totalSquaredCost = 0f;
        min = Float.POSITIVE_INFINITY;
        max = Float.NEGATIVE_INFINITY;
    }
    
    /**
     * Adds a new datum to this Tower
     * @param datum The datum to add
     */
    public synchronized void addDatum(TowerDatum datum) {
        int readings = towerData.size();
        towerData.add(datum);
        
        totalCost += datum.cost;
        totalSquaredCost += datum.cost * datum.cost;
        
        min = Math.min(min, datum.cost);
        max = Math.max(max, datum.cost);
        
        distance = datum.distance;
        load = datum.load;
    }
    
    /**
     *
     * @return This tower's code
     */
    @Column(name = "Code", number = 0)
    public String getCode() {
        return code;
    }
    
    /**
     *
     * @return This tower's name
     */
    @Column(name = "Location", number = 1)
    public String getName() {
        return name;
    }
    
    /**
     *
     * @return This tower's minimum reported signal
     */
    @Column(name = "Minimum", number = 2)
    public Float getMin() {
        return min;
    }
    
    /**
     *
     * @return This tower's average reported signal
     */
    @Column(name = "Average", number = 3)
    public Float getAvg() {
        return totalCost / towerData.size();
    }
    
    /**
     *
     * @return This tower's maximum reported signal
     */
    @Column(name = "Maximum", number = 4)
    public Float getMax() {
        return max;
    }
    
    /**
     *
     * @return The standard deviation of this tower's mesaured signals
     */
    @Column(name = "Std Dev", number = 5)
    public Float getStdDev() {
        float variance = (totalSquaredCost / towerData.size()) - (getAvg() * getAvg());
        return (float) Math.sqrt(variance);
    }
    
    /**
     *
     * @return This tower's last reported signal
     */
    @Column(name = "Last", number = 6)
    public Float getLast() {
        return towerData.get(towerData.size()  - 1).cost;
    }
    
    /**
     *
     * @return The number signals reported for this Tower
     */
    @Column(name = "Count", number = 7)
    public Integer getDataCount() {
        return towerData.size();
    }
    
    /**
     *
     * @return This tower's reported distance
     */
    //@Column(name = "Distance", number = 8)
    public Float getDistance() {
        return distance;
    }
    
    /**
     *
     * @return This tower's reported load
     */
    @Column(name = "Load", number = 8)
    public Float getLoad() {
        return load;
    }     
        
    public List<Float> getSignalData() {
        List<Float> signalData = new ArrayList();
        
        for (TowerDatum datum : towerData)
            signalData.add(datum.cost);
        
        return signalData;
    }
}