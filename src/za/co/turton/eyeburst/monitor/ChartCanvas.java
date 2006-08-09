/*
 * ChartCanvas.java
 *
 * Created on July 4, 2006, 9:56 AM
 *
 */

package za.co.turton.eyeburst.monitor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import za.co.turton.eyeburst.*;
import za.co.turton.eyeburst.config.Configuration;

/**
 * Canvas onto which the signal strength chart is drawn
 * @author james
 */
public class ChartCanvas extends Canvas implements TowerPublicationListener {
    
    // Chart object that is painted onto this canvas
    private JFreeChart chart;
    
    private Map<String, TimeSeries> seriesMap;
    
    private TimeSeriesCollection seriesCol;
    
    // Padding on the right of the chart
    private static final int PADDING_RIGHT = 20;
    
    /**
     * Creates a new instance of ChartCanvas
     * @param seriesCol the JFreeChart timeseries collection for the chart to be painted on this canvas
     *
     */
    public ChartCanvas() {
        seriesCol = new TimeSeriesCollection();
        seriesMap = new HashMap<String, TimeSeries>();
        chart = ChartFactory.createTimeSeriesChart(Configuration.getChartTitle(), Configuration.getXAxisTitle(), Configuration.getYAxisTitle(), seriesCol,
                true, false, false);
        chart.getXYPlot().setRenderer(new XYLineAndShapeRenderer());
    }
    
    
    /**
     * Paints <code>chart</code> onto this canvas
     * @param g grpahics context
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        Dimension d = getSize();
        Rectangle rect = new Rectangle(d.width - PADDING_RIGHT, d.height);
        chart.draw(g2d, rect);
    }
    
    public void towerPublication(TowerPublicationEvent evt) {
        
        TowerDatum datum = evt.getTowerDatum();
        String towerCode = datum.code;
        TimeSeries series = seriesMap.get(towerCode);
        
        if (series == null) {
            String towerName = Configuration.getTowerNames().getProperty(towerCode);
            
            //@todo: extract the following?
            if (towerName == null)
                towerName = towerCode + " (Unknown)";
            
            series = new TimeSeries(towerName, org.jfree.data.time.Second.class);
            seriesCol.addSeries(series);
            seriesMap.put(towerCode, series);
        }
        
        Second second = new Second(datum.readingTime);
        TimeSeriesDataItem dataItem = new TimeSeriesDataItem(second, datum.cost);
        series.add(dataItem);
        
        removeExpired(series);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                repaint();
            }
        });
    }
    
    private void removeExpired(TimeSeries series) {
        long now = System.currentTimeMillis();
        int toCull = 0;
        
        Iterator<TimeSeriesDataItem> it = series.getItems().iterator();
        
        while (it.hasNext())
            if (it.next().getPeriod().getMiddleMillisecond() < now - Configuration.getChartDataExpiry())
                toCull++;
        
        series.delete(0, toCull - 1);
    }
    
    public void clear() {
        seriesCol.removeAllSeries();
        seriesMap.clear();
        repaint();
    }
}
