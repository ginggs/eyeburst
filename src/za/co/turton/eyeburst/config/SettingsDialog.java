/*
 * SettingsDialog.java
 *
 * Created on August 23, 2006, 11:53 AM
 */

package za.co.turton.eyeburst.config;

import java.awt.Point;
import javax.swing.JFrame;
import za.co.turton.eyeburst.monitor.MonitorFrame;

/**
 *
 * @author  james
 */
public class SettingsDialog extends javax.swing.JDialog {
    
    private String towersFileLocation;
    
    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        towersFileLocation = Configuration.getProperty("towerNames");
    }
    
    public void setTowersFileLocation(String towersFileLocation) {
        this.towersFileLocation = towersFileLocation;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        loggerComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        currentTowerSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        signalLowerBoundSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        chartDataExpirySpinner = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        connectTimeoutSpinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        readTimeoutSpinner = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        browseButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        utdIPAddressField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("eyeBurst Settings");
        setLocationByPlatform(true);
        setModal(true);
        setName("settingsDialog");
        okButton.setMnemonic('o');
        okButton.setText("OK");
        okButton.setToolTipText("Applies new settings - be warned that the entire application will be reset!");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setMnemonic('c');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Logger Level");

        loggerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINEST" }));
        loggerComboBox.setSelectedItem(Configuration.getProperty("loggerLevel"));

        jLabel2.setText("Current Tower Prompt Interval");

        currentTowerSpinner.setValue(Configuration.getTransformedProperty(Integer.class, "writerSleep"));

        jLabel3.setText("ms");

        jLabel4.setText("Signal Lower Bound");

        signalLowerBoundSpinner.setValue(Configuration.getTransformedProperty(Integer.class, "signalLowerBound"));

        jLabel5.setText("dB");

        jLabel6.setText("Chart Data Expiry");

        chartDataExpirySpinner.setValue(Configuration.getTransformedProperty(Integer.class, "chartDataExpiry"));

        jLabel7.setText("ms");

        jLabel8.setText("Socket Connect Timeout");

        connectTimeoutSpinner.setValue(Configuration.getTransformedProperty(Integer.class, "connectTimeout"));

        jLabel9.setText("ms");

        jLabel10.setText("Socket Read Timeout");

        jLabel11.setText("ms");

        readTimeoutSpinner.setValue(Configuration.getTransformedProperty(Integer.class, "readTimeout"));

        jLabel12.setText("Tower Codes / Locations");

        browseButton.setMnemonic('b');
        browseButton.setText("Browse...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        jLabel13.setText("Device IP Address");

        utdIPAddressField.setText(Configuration.getProperty("utdIPAddress"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel13)
                    .add(jLabel10)
                    .add(jLabel8)
                    .add(jLabel6)
                    .add(jLabel4)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(layout.createSequentialGroup()
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(jLabel12))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(loggerComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cancelButton)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(readTimeoutSpinner)
                            .add(connectTimeoutSpinner)
                            .add(chartDataExpirySpinner)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, signalLowerBoundSpinner)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, currentTowerSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel9)
                            .add(jLabel3)
                            .add(jLabel7)
                            .add(jLabel5)
                            .add(jLabel11)))
                    .add(utdIPAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(browseButton))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(loggerComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(currentTowerSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(signalLowerBoundSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(chartDataExpirySpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(connectTimeoutSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(jLabel11)
                    .add(readTimeoutSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(utdIPAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(browseButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        new FileChooserDialog(this, true).setVisible(true);
    }//GEN-LAST:event_browseButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        Configuration.setProperty("loggerLevel", loggerComboBox.getSelectedItem().toString());
        Configuration.setProperty("writerSleep", currentTowerSpinner.getValue().toString());
        Configuration.setProperty("signalLowerBound", signalLowerBoundSpinner.getValue().toString());
        Configuration.setProperty("chartDataExpiry", chartDataExpirySpinner.getValue().toString());
        Configuration.setProperty("connectTimeout", connectTimeoutSpinner.getValue().toString());
        Configuration.setProperty("readTimeout", readTimeoutSpinner.getValue().toString());
        Configuration.setProperty("utdIPAddress", utdIPAddressField.getText());
        Configuration.setProperty("towerNames", towersFileLocation);
        Configuration.fireConfigurationChanged();        
    }//GEN-LAST:event_okButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SettingsDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JSpinner chartDataExpirySpinner;
    private javax.swing.JSpinner connectTimeoutSpinner;
    private javax.swing.JSpinner currentTowerSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox loggerComboBox;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner readTimeoutSpinner;
    private javax.swing.JSpinner signalLowerBoundSpinner;
    private javax.swing.JTextField utdIPAddressField;
    // End of variables declaration//GEN-END:variables
    
}
