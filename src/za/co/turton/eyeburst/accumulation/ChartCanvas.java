/*
 * ChartCanvas.java
 *
 * Created on August 8, 2006, 4:08 PM
 *
 */

package za.co.turton.eyeburst.accumulation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import za.co.turton.eyeburst.Tower;

/**
 *
 * @author james
 */
public class ChartCanvas extends java.awt.Canvas implements TowerCompletedListener {
    
    private JFreeChart chart;
    
    private DefaultBoxAndWhiskerCategoryDataset dataset;
    
    /** Creates a new instance of ChartCanvas */
    public ChartCanvas() {
        this.dataset = new DefaultBoxAndWhiskerCategoryDataset();
        CategoryAxis setupAxis = new CategoryAxis("Setup");
        NumberAxis valueAxis = new NumberAxis("Signal Strength (dB)");
        valueAxis.setAutoRangeIncludesZero(false);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();        
        CategoryPlot plot = new CategoryPlot(dataset, setupAxis, valueAxis, renderer);
        chart = new JFreeChart(plot);        
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        Rectangle rect = new Rectangle(getSize());
        chart.draw(g2d, rect);
    }

    public void towerCompleted(TowerCompletedEvent tc) {
        Tower tower = tc.getTower();
        AccumulationCategory ac = (AccumulationCategory) tc.getSource();
        dataset.add(tower.getSignalData(), tower.getName(), ac.getCategoryName());
        repaint();
    }
}
