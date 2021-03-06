/*
 * FileChooserDialog.java
 *
 * Created on August 23, 2006, 1:49 PM
 */

package za.co.turton.eyeburst.config;

import java.awt.Dialog;
import java.io.File;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author  james
 */
public class FileChooserDialog extends javax.swing.JDialog {
    
    private FileFilter fileFilter;
    
    /** Creates new form FileChooserDialog */
    public FileChooserDialog(Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        fileFilter = new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".properties");
            }
            
            public String getDescription() {
                return "Properties Files";
            }
        };
        
        fileChooser.setFileFilter(fileFilter);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        fileChooser = new javax.swing.JFileChooser();

        getContentPane().setLayout(new java.awt.FlowLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load a tower codes / locations properties file");
        setLocationByPlatform(true);
        setModal(true);
        setName("fileChooserDialog");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        fileChooser.setApproveButtonText("Load");
        fileChooser.setApproveButtonToolTipText("");
        fileChooser.setCurrentDirectory(new java.io.File("conf"));
        fileChooser.setDialogTitle("Load a tower codes / locations properties file");
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        getContentPane().add(fileChooser);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // Hack to work around JTable column header bug in Aqua L&F JFileChooser
        
        if (UIManager.getLookAndFeel().getName().equals("Mac OS X Aqua")) {            
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                
            }
        }
    }//GEN-LAST:event_formWindowClosed
    
    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed
        if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            String path = fileChooser.getSelectedFile().getPath();
            ((SettingsDialog) getParent()).setTowersFileLocation(path);
        }
        
        dispose();
    }//GEN-LAST:event_fileChooserActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    // End of variables declaration//GEN-END:variables
    
}
