package za.co.turton.eyeburst.sample;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

public class TowerTransferHandler extends TransferHandler {
    
    private Logger logger;
    
    public @InjectionConstructor TowerTransferHandler(
            @Inject("logger") Logger logger) {
        
        this.logger = logger;
    }
    
    public boolean importData(JComponent target, Transferable t) {
        
        SampleGroupPanel panel = null;
        
        try {
            panel = (SampleGroupPanel) target;
            String row = (String) t.getTransferData(DataFlavor.stringFlavor);
            String towerCode = row.substring(0, row.indexOf('\t'));
            return panel.addTower(towerCode);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Drop failed", e);
            JOptionPane.showMessageDialog(panel.getTopLevelAncestor(), e, "Drop failed", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }
    
    public boolean canImport(JComponent target, DataFlavor[] transferFlavors) {
        return true;
    }
}
