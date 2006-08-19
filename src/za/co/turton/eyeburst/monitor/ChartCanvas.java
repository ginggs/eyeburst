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
import java.util.List;
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
import za.co.turton.eyeburst.config.Configure;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

/**
 * Canvas onto which the signal strength chart is drawn
 * @author james
 */
public class ChartCanvas extends Canvas implements TowerPublicationListener {
    
    // Chart object that is painted onto this canvas
    private JFreeChart chart;
    
    private Map<String, TimeSeries> seriesMap;
    
    private TimeSeriesCollection seriesCol;
    
    private static final int PADDING_RIGHT = 20;    
    
    private int chartDataExpiry;
    
    private TowerNameService towerNameService;
    
    /**
     * Creates a new instance of ChartCanvas
     * @param seriesCol the JFreeChart timeseries collection for the chart to be painted on this canvas
     *
     */
    public @InjectionConstructor ChartCanvas(
            @Inject("chartTitle")       String chartTitle,
            @Inject("xAxisTitle")       String xAxisTitle,
            @Inject("yAxisTitle")       String yAxisTitle,
            @Inject("chartDataExpiry")   int chartDataExpiry,
            @Inject("towerNameService") TowerNameService towerNameService) {
        
        this.towerNameService = towerNameService;
        this.chartDataExpiry = chartDataExpiry;
        
        seriesCol = new TimeSeriesCollection();
        seriesMap = new HashMap<String, TimeSeries>();
        chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisTitle, yAxisTitle, seriesCol,
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
            String towerName = towerNameService.getTowerName(towerCode);            
            series = new TimeSeries(towerName, org.jfree.data.time.Second.class);
            seriesCol.addSeries(series);
            seriesMap.put(towerCode, series);
        }
        
        Second second = new Second(datum.readingTime);
        TimeSeriesDataItem dataItem = new TimeSeriesDataItem(second, datum.cost);
        series.add(dataItem);
        
        removeExpired();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                repaint();
            }
        });
    }
    
    private void removeExpired() {
        long now = System.currentTimeMillis();
        
        for (TimeSeries series : (List<TimeSeries>) seriesCol.getSeries()) {
            int toCull = 0;
            
            for (TimeSeriesDataItem item : (List<TimeSeriesDataItem>) series.getItems())
                if (item.getPeriod().getMiddleMillisecond() < now - chartDataExpiry)
                    toCull++;
            
            series.delete(0, toCull - 1);
        }
    }
    
    public void clear() {
        seriesCol.removeAllSeries();
        seriesMap.clear();
        repaint();
    }
}
