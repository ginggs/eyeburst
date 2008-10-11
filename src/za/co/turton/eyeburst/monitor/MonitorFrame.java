/*
 * MonitorFrame.java
 *
 * Created on June 14, 2006, 2:29 PM
 */

package za.co.turton.eyeburst.monitor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import za.co.turton.eyeburst.*;
import za.co.turton.eyeburst.config.ConfigurationChangedListener;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;
import za.co.turton.eyeburst.config.Configuration;
import za.co.turton.eyeburst.config.SettingsDialog;
import za.co.turton.eyeburst.sample.SampleSizeDialog;

/**
 * This is the main Swing frame of the application
 * @author james
 */
public class MonitorFrame extends javax.swing.JFrame implements ConnectionListener,
        CurrentTowerListener, ConfigurationChangedListener {
    
    private TowerDataThread towerDataThread;
    
    private TowerTableModel towerTableModel;
    
    private TowerPublisher towerPublisher;
    
    private TowerNameService towerNameService;
    
    private ChartPanel chartPanel;
    
    private Logger logger;
    
    /**
     * Creates a new MonitorFrame
     */
    
    public @InjectionConstructor MonitorFrame(
            
            @Inject("towerTableModel")  TowerTableModel towerTableModel,
            @Inject("towerPublisher")   TowerPublisher towerPublisher,
            @Inject("towerNameService") TowerNameService towerNameService,
            @Inject("chartPanel")       ChartPanel chartPanel,
            @Inject("appTitle")         String appTitle,
            @Inject("logger")           Logger logger) {
        
        this.towerTableModel = towerTableModel;
        this.towerPublisher = towerPublisher;
        this.towerNameService = towerNameService;
        this.chartPanel = chartPanel;
        setTitle(appTitle);
        this.logger = logger;
        initComponents();
        
        TableSorter sorter = new TableSorter(towerTableModel);
        sorter.setSortingStatus(0, TableSorter.ASCENDING);
//        JTableHeader tableHEa
        sorter.setTableHeader(towerTable.getTableHeader());
        towerTable.getTableHeader().setToolTipText("Click to specify sorting; Control-Click to specify secondary sorting");
        towerTable.setModel(sorter);
        
        towerTableModel.addTableModelListener(towerTable);
        towerPublisher.addListener(towerTableModel);
        
        chartPanelContainer.add(chartPanel);
        Dimension size = chartPanelContainer.getSize();
        chartPanel.setPreferredSize(new Dimension(size.width - 50, size.height - 10));
        chartPanel.setSize(new Dimension(size.width - 50, size.height - 10));
        
        towerPublisher.addListener(chartPanel);
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
        chartPanelContainer = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        towerTable = new javax.swing.JTable();
        currentTowerPanel = new javax.swing.JPanel();
        currentLabel = new javax.swing.JLabel();
        currentTower = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        connectButton = new javax.swing.JButton();
        sampleButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
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
        setTitle("eyeBurst 1.1 - Live Data");
        setName("eyeBurst Frame");
        displayPanel.setLayout(new java.awt.BorderLayout());

        displayPanel.setAutoscrolls(true);
        displayPanel.setMinimumSize(new java.awt.Dimension(400, 200));
        displayPanel.setOpaque(false);
        displayPanel.setPreferredSize(new java.awt.Dimension(550, 500));
        jSplitPane1.setDividerLocation(320);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        chartPanelContainer.setToolTipText("");
        chartPanelContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                chartPanelContainerComponentResized(evt);
            }
        });

        org.jdesktop.layout.GroupLayout chartPanelContainerLayout = new org.jdesktop.layout.GroupLayout(chartPanelContainer);
        chartPanelContainer.setLayout(chartPanelContainerLayout);
        chartPanelContainerLayout.setHorizontalGroup(
            chartPanelContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 401, Short.MAX_VALUE)
        );
        chartPanelContainerLayout.setVerticalGroup(
            chartPanelContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 273, Short.MAX_VALUE)
        );
        jSplitPane1.setLeftComponent(chartPanelContainer);

        towerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        towerTable.setToolTipText("Drag a table row into a sample frame to start accumulating data for the tower in that row");
        towerTable.setGridColor(new java.awt.Color(192, 192, 192));
        towerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        towerTable.setDragEnabled(true);
        tableScrollPane.setViewportView(towerTable);

        jSplitPane1.setRightComponent(tableScrollPane);

        displayPanel.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(displayPanel);

        currentLabel.setText("Currently aligned to");
        currentLabel.setToolTipText("The last tower that your device reported that it was aligned to");
        currentTowerPanel.add(currentLabel);

        currentTower.setText("?");
        currentTowerPanel.add(currentTower);

        getContentPane().add(currentTowerPanel);

        connectButton.setMnemonic('c');
        connectButton.setText("Connect");
        connectButton.setToolTipText("Toggle the connection to your iBurst(TM) device");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(connectButton);

        sampleButton.setMnemonic('a');
        sampleButton.setText("Sample...");
        sampleButton.setToolTipText("Create a new sample frame in which you can accumulate signal statistics over a defined number of readings");
        sampleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(sampleButton);

        settingsButton.setMnemonic('s');
        settingsButton.setText("Settings...");
        settingsButton.setToolTipText("Configure various eyeBurst settings");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(settingsButton);

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
    
    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        new SettingsDialog(this, true).setVisible(true);
    }//GEN-LAST:event_settingsButtonActionPerformed
    
    private void chartPanelContainerComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_chartPanelContainerComponentResized
        Dimension size = chartPanelContainer.getSize();
        chartPanel.setMaximumSize(new Dimension(size.width - 30, size.height - 10));
        chartPanel.setSize(new Dimension(size.width - 30, size.height - 10));
    }//GEN-LAST:event_chartPanelContainerComponentResized
    
    private void sampleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleButtonActionPerformed
        new SampleSizeDialog(this, true).setVisible(true);
    }//GEN-LAST:event_sampleButtonActionPerformed
    
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        JButton button = (JButton) evt.getSource();
        String action = button.getText();
        
        if (action.equals("Connect")) {
            
            button.setEnabled(false);
            
            towerDataThread = Configuration.configure(TowerDataThread.class);
            towerDataThread.addListener((ConnectionListener) this);
            towerDataThread.addListener((CurrentTowerListener) this);
            towerDataThread.start();
            
        } else if (action.equals("Disconnect")) {
            button.setEnabled(false);
            towerDataThread.requestStop();
        }
    }//GEN-LAST:event_connectButtonActionPerformed
    
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        towerTableModel.clear();
        chartPanel.clear();
    }//GEN-LAST:event_resetButtonActionPerformed
    
    
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
                String towerCode = e.getCurrentTowerCode();
                String towerName = towerNameService.getTowerName(towerCode);
                currentTower.setText(towerName);
            }
        });
    }
    
    /**
     *
     * @see MonitorThreadListener#connected
     */
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
        
        final Logger logger = Logger.getLogger("Bootstrap");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//           UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not set system look and feel", e);
        }
        
        Configuration.initialise();
        launch(null);
    }
    
    public static void launch(final Point location) {
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {                
                MonitorFrame monitorFrame = Configuration.configure(MonitorFrame.class);
                
                if (location == null)
                    monitorFrame.setLocationByPlatform(true);
                else
                    monitorFrame.setLocation(location);
                
                monitorFrame.setVisible(true);
            }
        });
    }
    
    public void configurationChanged() {
        if (this.isVisible()) {
            Point location = getLocation();
            dispose();
            launch(location);
        }
    }

    public void unparseableLine(final ConnectionEvent e) {
        final Component dialogParent = this;
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                JOptionPane.showMessageDialog(dialogParent, e.getThreadException(), "Connect Failed", JOptionPane.ERROR_MESSAGE);
//                towerDataThread.requestStop();
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel chartPanelContainer;
    private javax.swing.JButton connectButton;
    private javax.swing.JLabel currentLabel;
    private javax.swing.JLabel currentTower;
    private javax.swing.JPanel currentTowerPanel;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JDialog errorDialog;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton sampleButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable towerTable;
    // End of variables declaration//GEN-END:variables
    
}
