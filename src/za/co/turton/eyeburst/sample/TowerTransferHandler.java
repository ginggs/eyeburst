package za.co.turton.eyeburst.sample;

import javax.swing.*;
import java.awt.datatransfer.*;

public class TowerTransferHandler extends TransferHandler {
    
    public boolean importData(JComponent target, Transferable t) {
        
        SampleFrame accFrame = null;
        
        try {
            accFrame = (SampleFrame) target.getTopLevelAncestor();
            String row = (String) t.getTransferData(DataFlavor.stringFlavor);
            String towerCode = row.substring(0, row.indexOf('\t'));            
            return accFrame.addTower(towerCode);
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(accFrame, e, "Drop failed", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }
    
    public boolean canImport(JComponent target, DataFlavor[] transferFlavors) {
        
//        if (target.getName() != null && target.getName().equals("chartPanel")) {
//            for (DataFlavor flavour : transferFlavors)
//                if (DataFlavor.stringFlavor.equals(flavour))
//                    return true;
//        }
//
//        return false;
        return true;
    }
    
//    protected Transferable createTransferable(JComponent comp) {
//        JTable sourceTable = (JTable) comp;
//        int row = sourceTable.getSelectedRow();
//        TowerTableModel model = (TowerTableModel) sourceTable.getModel();
//        return model.getTowerAt(row);
//    }
    
    
}
