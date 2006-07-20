/*
 * TowerTableModel.java
 *
 * Created on July 1, 2006, 10:29 AM
 */

package za.co.turton.eyeburst;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Implementation of javax.swing.TableModel to present tower data
 * @author james
 */
public class TowerTableModel implements TableModel {
    
    private Map<String, Integer> towersMap;
    
    private List<Tower> towers;
    
    private TimeSeriesCollection seriesCol;
    
    private Set<TableModelListener> listeners;
    
    private Map<Integer, Method> columnGetters;
    
    /** Creates a new instance of TowerTableModel */
    public TowerTableModel() {
        this.towersMap = new TreeMap<String, Integer>();
        this.towers = new ArrayList<Tower>();
        this.listeners = new HashSet<TableModelListener>();
        
        seriesCol = new TimeSeriesCollection();
        columnGetters = new HashMap<Integer, Method>();
        
        for (Method method : Tower.class.getMethods()) {
            Column column = method.getAnnotation(Column.class);
            
            if (column != null)
                columnGetters.put(column.number(), method);
        }
    }
    
    /**
     * Clears all tower data visible to this table model
     */
    public void clear() {
        towersMap.clear();
        towers.clear();
        seriesCol.removeAllSeries();
        fireTableChanged();
    }
    
    /**
     * Add a new tower datum to an existing or new tower
     * @param towerCode the code of the tower to which the datum applies
     * @param towerDatum the tower datum
     */
    public void addTowerDatum(TowerDatum towerDatum) {
        
        Tower tower = retrieveOrCreate(towerDatum.code);
        tower.addDatum(towerDatum);
        
        for (Tower t : towers)
            t.removeExpiredFromChart();
        
        fireTableChanged();
    }
    
    /**
     * Retrieve a tower, creating mapping it if it is not mapped
     * @param towerCode the code of the tower to be retrieved
     * @return a new or existing tower
     */
    private Tower retrieveOrCreate(final String towerCode) {
        Tower tower = null;
        Integer index = towersMap.get(towerCode);
        
        if (index == null) {
            tower = new Tower(towerCode);
            towers.add(tower);
            towersMap.put(towerCode, towers.size() - 1);
            seriesCol.addSeries(tower.getSeries());
        } else
            tower = towers.get(index);
        
        return tower;
    }
    
    /**
     * Remove this model's listener
     * @param l the listener
     */
    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }
    
    /**
     * Set this table model's listener
     * @param l the listener
     */
    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }
    
    /**
     *
     * @see TableModel#setValueAt
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Read only");
    }
    
    /**
     *
     * @see TableModel#getColumnName
     */
    public String getColumnName(int columnIndex) {
        Column column = columnGetters.get(columnIndex).getAnnotation(Column.class);
        return column.name();
    }
    
    /**
     *
     * @see TableModel#getColumnClass
     */
    public Class getColumnClass(int columnIndex) {
        return columnGetters.get(columnIndex).getReturnType();
    }
    
    /**
     *
     * @see TableModel#isCellEditable
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    /**
     *
     * @see TableModel#getValue
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        Tower tower = towers.get(rowIndex);
        Method getter = columnGetters.get(columnIndex);
        
        if (getter == null)
            throw new RuntimeException("No getter annotated with number = "+columnIndex);
        else
            try {
                return getter.invoke(tower, new Object[0]);
            } catch (Exception ex) {
                throw new RuntimeException("Could not invoke "+getter, ex);
            }
    }
    
    private void fireTableChanged() {
        for (TableModelListener listener : listeners)
            listener.tableChanged(new TableModelEvent(this));
    }
    
    /**
     *
     * @see TableModel#getRowCount
     */
    public int getRowCount() {
        return towersMap.size();
    }
    
    /**
     *
     * @see TableModel#getColumnCount
     */
    public int getColumnCount() {
        return columnGetters.size();
    }
    
    /**
     *
     * @see TableModel#getSeriesCol
     */
    public TimeSeriesCollection getSeriesCol() {
        return seriesCol;
    }
}
