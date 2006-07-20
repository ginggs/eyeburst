/*
 * ChartCanvas.java
 *
 * Created on July 4, 2006, 9:56 AM
 *
 */

package za.co.turton.eyeburst;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import za.co.turton.eyeburst.config.Configuration;

/**
 * Canvas onto which the signal strength chart is drawn
 * @author james
 */
public class ChartCanvas extends Canvas {
    
    // Chart object that is painted onto this canvas
    private JFreeChart chart;
    
    // Padding on the right of the chart
    private static final int PADDING_RIGHT = 20;
    
    /**
     * Creates a new instance of ChartCanvas
     * @param seriesCol the JFreeChart timeseries collection for the chart to be painted on this canvas
     *
     */
    public ChartCanvas(TowerTableModel tableModel) {
        TimeSeriesCollection seriesCol = tableModel.getSeriesCol();
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
}
