/*
 * SampleFrame.java
 *
 * Created on August 1, 2006, 12:58 PM
 */

package za.co.turton.eyeburst.sample;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JOptionPane;

/**
 *
 * @author  james
 */
public class SampleFrame extends javax.swing.JFrame {    
            
    private TowerSampleDataSet tsds;
    
    private ChartCanvas chartCanvas;
    
    private int sampleSize;
    
    /**
     * Creates new form SampleFrame
     */
    public SampleFrame(int sampleSize) {
        
        initComponents();
        
        if (sampleSize <= 0)
            throw new IllegalArgumentException("Sample size must be >= 1");
        
        this.sampleSize = sampleSize;
        chartCanvas = new ChartCanvas(sampleSize);
        chartPanel.add(chartCanvas);               
    }    
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        sampleGroupMenu = new javax.swing.JPopupMenu();
        createGroupItem = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        chartPanel = new javax.swing.JPanel();
        pendingsPanel = new javax.swing.JPanel();

        sampleGroupMenu.setInvoker(pendingsPanel);
        sampleGroupMenu.setName("createGroupPopup");
        createGroupItem.setText("Create Sample Group");
        createGroupItem.setName("createSampleGroupItem");
        createGroupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGroupItemActionPerformed(evt);
            }
        });

        sampleGroupMenu.add(createGroupItem);

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("eyeBurst");
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        chartPanel.setLayout(new java.awt.GridLayout(1, 0));

        chartPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        chartPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                chartPanelComponentResized(evt);
            }
        });

        jSplitPane1.setLeftComponent(chartPanel);

        pendingsPanel.setLayout(new javax.swing.BoxLayout(pendingsPanel, javax.swing.BoxLayout.Y_AXIS));

        pendingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Pending Samples (right-click to create group)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        pendingsPanel.setComponentPopupMenu(sampleGroupMenu);
        pendingsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendingsPanelMouseClicked(evt);
            }
        });

        jSplitPane1.setRightComponent(pendingsPanel);

        getContentPane().add(jSplitPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void createGroupItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGroupItemActionPerformed
        String groupName = JOptionPane.showInputDialog(this, "Sample Group Name (e.g. Near Window)");
        
        if (groupName == null)
            return;
        
        for (Component child : pendingsPanel.getComponents())
            if (child.getName() != null && child.equals(groupName))
                return;
        
        SampleGroup sampleGroup = new SampleGroup(groupName, sampleSize);
        sampleGroup.addListener(chartCanvas);
        
        SampleGroupPanel groupPanel = new SampleGroupPanel(sampleGroup);
        pendingsPanel.add(groupPanel);
        groupPanel.setVisible(true);
        pack();
    }//GEN-LAST:event_createGroupItemActionPerformed
    
    private void pendingsPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingsPanelMouseClicked
        // Dummy method necessary for popup menu
    }//GEN-LAST:event_pendingsPanelMouseClicked
    
    private void chartPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_chartPanelComponentResized
        
        Dimension newSize = chartPanel.getSize();
        chartPanel.setPreferredSize(newSize);
        
        if (chartCanvas != null) {
            chartCanvas.setSize(newSize);
            chartCanvas.repaint();
        }
    }//GEN-LAST:event_chartPanelComponentResized
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartPanel;
    private javax.swing.JMenuItem createGroupItem;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pendingsPanel;
    private javax.swing.JPopupMenu sampleGroupMenu;
    // End of variables declaration//GEN-END:variables
    
}