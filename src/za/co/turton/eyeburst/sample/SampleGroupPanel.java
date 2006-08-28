/*
 * SampleGroupPanel.java
 *
 * Created on August 14, 2006, 11:48 AM
 */

package za.co.turton.eyeburst.sample;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import za.co.turton.eyeburst.Tower;
import za.co.turton.eyeburst.TowerPublicationEvent;
import za.co.turton.eyeburst.TowerPublicationListener;
import za.co.turton.eyeburst.TowerPublisher;
import za.co.turton.eyeburst.config.Configuration;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

/**
 *
 * @author  james
 */
public class SampleGroupPanel extends javax.swing.JPanel implements TowerPublicationListener, TowerCompletedListener {
    
    private SampleGroup sampleGroup;
    
    private Map<String, JProgressBar> progressBars;    
    
    private TowerPublisher towerPublisher;

    private boolean isVirgin;
    
    /** Creates new form SampleGroupPanel */
    public @InjectionConstructor SampleGroupPanel(
            @Inject("towerPublisher") TowerPublisher towerPublisher,
            @Inject("towerTransferHandler") TowerTransferHandler towerTransferHandler) {
        
        initComponents();
        setTransferHandler(towerTransferHandler);
        this.progressBars = new HashMap<String, JProgressBar>();
        this.towerPublisher = towerPublisher;
        towerPublisher.addListener(this);
        this.isVirgin = true;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Drag towers here...");
        jLabel1.setEnabled(false);
        jLabel1.setName("dragPrompt");
        add(jLabel1, "card2");

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    
    public boolean addTower(String towerCode) {
        
        Tower tower = towerPublisher.createTower(towerCode);
        
        try {
            sampleGroup.add(tower);
            
            if (this.isVirgin) {             
                this.removeAll();
                GridLayout layout = new GridLayout(0, 2);
                setLayout(layout);
                layout.setHgap(10);
                this.isVirgin = false;
            }
            
            GridLayout layout = (GridLayout) getLayout();
            layout.setRows(layout.getRows()+1);            
            
            JLabel towerLabel = new JLabel(tower.getName());
            towerLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            add(towerLabel);
            
            JProgressBar towerProgress = new JProgressBar(0, sampleGroup.getSampleSize());
            add(towerProgress);
            progressBars.put(towerCode, towerProgress);
            
            towerLabel.setVisible(true);
            towerProgress.setVisible(true);
            
            ((JFrame) getTopLevelAncestor()).pack();
            
            return true;
        } catch (TowerAlreadyPending e) {
            return false;
        }
    }
    
    public void towerPublication(TowerPublicationEvent evt) {
        String towerCode = evt.getTowerDatum().code;
        JProgressBar progressBar = progressBars.get(towerCode);
        
        if (progressBar != null)
            progressBar.setValue(progressBar.getValue() + 1);
    }
    
    public void towerCompleted(TowerCompletedEvent tc) {
        String towerCode = tc.getTower().getCode();
        JProgressBar progressBar = progressBars.get(towerCode);
        int index = Arrays.asList(getComponents()).indexOf(progressBar);
        remove(progressBar);
        remove(index - 1);
        
        GridLayout layout = (GridLayout) getLayout();
        layout.setRows(layout.getRows() - 1);
        progressBars.remove(towerCode);
        ((JFrame) getTopLevelAncestor()).pack();
    }
    
    public void setSampleGroup(SampleGroup sampleGroup) {
        this.sampleGroup = sampleGroup;
        sampleGroup.addListener(this);
        ((TitledBorder) getBorder()).setTitle(sampleGroup.getGroupName());
    }
    
}
