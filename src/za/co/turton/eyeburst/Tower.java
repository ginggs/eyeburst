/*
 * Tower.java
 *
 * Created on June 30, 2006, 5:33 PM
 *
 */
package za.co.turton.eyeburst;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.omg.CORBA.UNKNOWN;
import za.co.turton.eyeburst.config.Configuration;

/**
 * Represents an iBurst tower
 * @author james
 */
public class Tower implements Comparable {
    
    private String code;
    
    private String name;
    
    private float totalCost;
    
    private float totalSquaredCost;
    
    private float min;
    
    private float max;
    
    private float distance;
    
    private float load;
    
    private List<TowerDatum> towerData;
    
    private TimeSeries series;
    
    /**
     * Creates a new instance of Tower
     * @param code The code of the tower
     */
    public Tower(String code) {
        this.code = code;
        this.name = Configuration.getTowerNames().getProperty(code);
        
        if (this.name == null)
            this.name = this.code + " (Unknown)";
        
        towerData = new LinkedList();
        totalCost = totalSquaredCost = 0f;
        min = Float.POSITIVE_INFINITY;
        max = Float.NEGATIVE_INFINITY;
        series = new TimeSeries(name, org.jfree.data.time.Second.class);
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
        
        Second second = new Second(datum.readingTime);
        TimeSeriesDataItem dataItem = new TimeSeriesDataItem(second, datum.cost);
        series.add(dataItem);
    }
    
    /**
     * Retrieve the JFreeChart TimeSeries held by this Tower
     * @return the TimeSeries
     */
    public TimeSeries getSeries() {
        return this.series;
    }
    
    /**
     * Remove all expired chart data from this Tower's TimeSeries
     */
    public synchronized void removeExpiredFromChart() {
        long now = System.currentTimeMillis();
        int toCull = 0;
        
        Iterator<TimeSeriesDataItem> it = series.getItems().iterator();
        
        while (it.hasNext())
            if (it.next().getPeriod().getMiddleMillisecond() < now - Configuration.getChartDataExpiry())
                toCull++;
        
        series.delete(0, toCull - 1);
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
    @Column(name = "Name", number = 1)
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
        return towerData.get(towerData.size() - 1).cost;
    }
    
    /**
     *
     * @return The number signals reported for this Tower
     */
    @Column(name = "Count", number = 7)
    public Integer getCount() {
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
    //@Column(name = "Load", number = 9)
    public Float getLoad() {
        return load;
    }
    
    /**
     * Two towers are equal iff they have the same code
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        return (obj instanceof Tower && ((Tower) obj).code.equals(code));
    }
    
    /**
     *
     * @return
     */
    public int hashCode() {
        return Integer.parseInt(this.code);
    }
    
    /**
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        Tower other = (Tower) o;
        return getAvg().compareTo(other.getAvg());
    }   
}
