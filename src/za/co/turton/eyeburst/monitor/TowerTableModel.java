/*
 * TowerTableModel.java
 *
 * Created on July 1, 2006, 10:29 AM
 */

package za.co.turton.eyeburst.monitor;

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
import za.co.turton.eyeburst.*;

/**
 * Implementation of javax.swing.TableModel to present tower data
 * @author james
 */
public class TowerTableModel implements TableModel, TowerPublicationListener {
    
    private Map<String, Integer> towersMap;
    
    private List<Tower> towers;
         
    private Set<TableModelListener> listeners;
    
    private Map<Integer, Method> columnGetters;
    
    /** Creates a new instance of TowerTableModel */
    public TowerTableModel() {
        this.towersMap = new TreeMap<String, Integer>();
        this.towers = new ArrayList<Tower>();
        this.listeners = new HashSet<TableModelListener>();
        
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
        fireTableChanged();
    }  
    
    public Tower retrieve(String towerCode) {
        Integer index = towersMap.get(towerCode);
        return towers.get(index);
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

    public void towerPublication(TowerPublicationEvent evt) {
        Tower tower = null;
        String towerCode = evt.getTowerDatum().code;
        Integer index = towersMap.get(towerCode);
        
        if (index == null) {
            tower = evt.getSource().createTowerWithDatum(evt.getTowerDatum());
            towers.add(tower);
            towersMap.put(towerCode, towers.size() - 1);
        } else
            tower = towers.get(index);
        
        fireTableChanged();
    }
}
