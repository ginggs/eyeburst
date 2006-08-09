/*
 * MonitorFrame.java
 *
 * Created on June 14, 2006, 2:29 PM
 */

package za.co.turton.eyeburst.monitor;

import java.awt.Component;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import org.jfree.chart.JFreeChart;
import za.co.turton.eyeburst.*;
import za.co.turton.eyeburst.accumulation.AccumulationFrame;
import za.co.turton.eyeburst.config.Configuration;
import za.co.turton.eyeburst.config.ConfigurationException;

/**
 * This is the main Swing frame of the application
 * @author james
 */
public class MonitorFrame extends javax.swing.JFrame implements ConnectionListener, CurrentTowerListener {
    
    private MonitorThread monitorThread;
    
    private JFreeChart chart;
    
    private TowerTableModel towerTableModel;
    
    private ChartCanvas chartCanvas;
    
    private TowerPublisher towerPublisher;
    
    /**
     * Creates a new MonitorFrame
     */
    public MonitorFrame() {
        
        initComponents();
//        settingsDialog.setContentPane(new SettingsPanel());
//        settingsDialog.pack();
        this.towerPublisher = new TowerPublisher();
        setTitle(Configuration.getAppTitle());
        this.towerTableModel = new TowerTableModel();
        TableSorter sorter = new TableSorter(towerTableModel);
        sorter.setSortingStatus(0, TableSorter.ASCENDING);
        sorter.setTableHeader(towerTable.getTableHeader());
        towerTable.getTableHeader().setToolTipText("Click to specify sorting; Control-Click to specify secondary sorting");
        towerTable.setModel(sorter);
        towerTableModel.addTableModelListener(towerTable);
        chartCanvas = new ChartCanvas();
        graphPanel.add(chartCanvas);
        
        towerPublisher.addListener(towerTableModel);
        towerPublisher.addListener(chartCanvas);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        errorDialog = new javax.swing.JDialog();
        errorLabel = new javax.swing.JLabel();
        displayPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        graphPanel = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        towerTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        currentLabel = new javax.swing.JLabel();
        currentTower = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        connectButton = new javax.swing.JButton();
        accumulateButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();

        errorDialog.getContentPane().setLayout(new java.awt.FlowLayout());

        errorDialog.setTitle("Error");
        errorDialog.setAlwaysOnTop(true);
        errorDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        errorDialog.setModal(true);
        errorDialog.setName("Error Dialogue");
        errorLabel.setText("Error");
        errorDialog.getContentPane().add(errorLabel);

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Signal Data Overview");
        setName("eyeBurst Frame");
        displayPanel.setLayout(new java.awt.BorderLayout());

        displayPanel.setAutoscrolls(true);
        displayPanel.setMinimumSize(new java.awt.Dimension(400, 200));
        displayPanel.setOpaque(false);
        displayPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        graphPanel.setLayout(new javax.swing.BoxLayout(graphPanel, javax.swing.BoxLayout.X_AXIS));

        graphPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                graphPanelComponentResized(evt);
            }
        });

        jSplitPane1.setLeftComponent(graphPanel);

        towerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        towerTable.setToolTipText("Drag a table row into an accumulation frame to start accumulating data for the tower in that row");
        towerTable.setGridColor(new java.awt.Color(192, 192, 192));
        towerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        towerTable.setDragEnabled(true);
        tableScrollPane.setViewportView(towerTable);

        jSplitPane1.setRightComponent(tableScrollPane);

        displayPanel.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(displayPanel);

        currentLabel.setText("Currently aligned to");
        jPanel1.add(currentLabel);

        currentTower.setText("?");
        jPanel1.add(currentTower);

        getContentPane().add(jPanel1);

        connectButton.setMnemonic('c');
        connectButton.setText("Connect");
        connectButton.setToolTipText("Toggle the connection to your iBurst(TM) device");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(connectButton);

        accumulateButton.setMnemonic('a');
        accumulateButton.setText("Accumulate...");
        accumulateButton.setToolTipText("Create a new accumulation frame in which you can graph signal data accumulated over a number of readings");
        accumulateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accumulateButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(accumulateButton);

        resetButton.setMnemonic('r');
        resetButton.setText("Reset");
        resetButton.setToolTipText("Clears all recorded data");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(resetButton);

        getContentPane().add(buttonPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void accumulateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accumulateButtonActionPerformed
        String input = JOptionPane.showInputDialog(this, "Sample Size", 10);
        
        if (input == null)
            return;
        
        try {
            int sampleSize = Integer.parseInt(input);
            AccumulationFrame accFrame = new AccumulationFrame(towerPublisher, sampleSize);
            accFrame.setLocationByPlatform(true);
            accFrame.setVisible(true);
            
        } catch (NumberFormatException e) {
            Configuration.getLogger().log(Level.WARNING, "Could not parse sample size for accumulation frame", e);
            JOptionPane.showMessageDialog(this, e.toString(), "Invalid Sample Size", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_accumulateButtonActionPerformed
    
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        JButton button = (JButton) evt.getSource();
        String action = button.getText();
        
        if (action.equals("Connect")) {
            button.setEnabled(false);
            monitorThread = new MonitorThread(towerPublisher);
            monitorThread.addListener((ConnectionListener) this);
            monitorThread.addListener((CurrentTowerListener) this);
            monitorThread.start();
            
        } else if (action.equals("Disconnect")) {
            button.setEnabled(false);
            monitorThread.requestStop();
        }
    }//GEN-LAST:event_connectButtonActionPerformed
    
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        towerTableModel.clear();
        chartCanvas.clear();
    }//GEN-LAST:event_resetButtonActionPerformed
    
    private void graphPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_graphPanelComponentResized
        chartCanvas.setSize(graphPanel.getSize());
        chartCanvas.repaint();
    }//GEN-LAST:event_graphPanelComponentResized
        
    
    public void disconnected(final ConnectionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectButton.setText("Connect");
                connectButton.setEnabled(true);
            }
        });
    }
    
    public void currentTower(final DataEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTower.setText(e.getCurrentTower().getName());                
            }
        });
    }
    
    public void connected(final ConnectionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectButton.setText("Disconnect");
                connectButton.setEnabled(true);
            }
        });
    }
    
    /**
     *
     * @see MonitorThreadListener#connectFailed
     */
    public void connectFailed(final ConnectionEvent e) {
        final Component dialogParent = this;
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(dialogParent, e.getThreadException(), "Connect Failed", JOptionPane.ERROR_MESSAGE);
                connectButton.setEnabled(true);
            }
        });
    }
    
    /**
     * Application execution entry point
     * @param args are ignored, configuration is loaded from a properties file
     */
    public static void main(String args[]) {
        
        try {
            Configuration.configure();
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    MonitorFrame monitorFrame = new MonitorFrame();
                    monitorFrame.setLocationByPlatform(true);
                    monitorFrame.setVisible(true);
                }
            });
        } catch (ConfigurationException e) {
            Configuration.getLogger().log(Level.SEVERE, "Could not configure eyeBurst", e);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accumulateButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton connectButton;
    private javax.swing.JLabel currentLabel;
    private javax.swing.JLabel currentTower;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JDialog errorDialog;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton resetButton;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable towerTable;
    // End of variables declaration//GEN-END:variables
    
}
