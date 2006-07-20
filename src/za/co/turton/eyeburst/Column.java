/*
 * Column.java
 *
 * Created on July 7, 2006, 3:00 PM
 */

package za.co.turton.eyeburst;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * When present on a <code>Tower</code> property accessor, indicates a column for
 * <code>TowerTableModel</code>
 * @author james
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    
    /**
     * Indicates the column number of the annotated property accessor
     */
    public int number();
    
    /**
     * Indicates the column name of the annotated property accessor
     */
    public String name();
}
