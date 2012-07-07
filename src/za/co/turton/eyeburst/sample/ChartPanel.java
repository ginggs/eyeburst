/*
 * ChartPanel.java
 *
 * Created on August 8, 2006, 4:08 PM
 *
 */

package za.co.turton.eyeburst.sample;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import za.co.turton.eyeburst.Tower;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

/**
 *
 * @author james
 */
public class ChartPanel extends org.jfree.chart.ChartPanel implements TowerCompletedListener {
    
    private JFreeChart chart;
    
    private DefaultBoxAndWhiskerCategoryDataset dataset;
    
    /**
     * Creates a new instance of ChartPanel
     */
    public @InjectionConstructor ChartPanel(
            @Inject("yAxisTitle") String yAxisTitle) {
        
        super(null);
        dataset = new DefaultBoxAndWhiskerCategoryDataset();
        CategoryAxis setupAxis = new CategoryAxis("Sample Group");
        NumberAxis valueAxis = new NumberAxis(yAxisTitle);
        valueAxis.setAutoRangeIncludesZero(false);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setBaseToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        CategoryPlot plot = new CategoryPlot(dataset, setupAxis, valueAxis, renderer);
        chart = new JFreeChart(plot);
        setChart(chart);
        setPreferredSize(new Dimension(400, 500));
        
    }    
    
    public void towerCompleted(TowerCompletedEvent tc) {
        Tower tower = tc.getTower();
        SampleGroup sampleGroup = (SampleGroup) tc.getSource();
        String towerName = tower.getName();
        String groupName = sampleGroup.getGroupName();
        dataset.add(tower.getSignalData(), towerName, groupName);               
    }
}
