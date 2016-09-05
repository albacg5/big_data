/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.dtim.bolster.ontomatchmerge.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingWorker;
import org.upc.dtim.bolster.ontomatchmerge.extraction.OntologyExtractionCoordinator;
import org.upc.dtim.bolster.ontomatchmerge.extraction.ioartifacts.IOArtifact;
import org.upc.dtim.bolster.ontomatchmerge.matching.OntologyMatchingCoordinator;

/**
 *
 * @author Rizkallah
 */
public class TaskProgressDialog extends javax.swing.JDialog {

    private OntologyConstructionTask task;

    class OntologyConstructionTask extends SwingWorker<Void, String> {

        @Override
        public Void doInBackground() {
            publish("Ontology construction starting...\n---------------------");
            // 1. Extract ontology from each source

            ///XXX ER Line added to get sources
            ArrayList<IOArtifact> sources = parent.getSources();

            for (IOArtifact source : sources) {
                publish("Extracting ontology from source: " + source.getSourceName());
                OntologyExtractionCoordinator.extractOntology(source, new Properties());
                publish("Generating mappings to source: " + source.getSourceName());
            }

            // 2. Run the matching process using all available matchers
            publish("Matching ontologies using: " + OntologyMatchingCoordinator.FALCON_ISUB_MATCHER.replace("_", " "));
            parent.runMatchingAndMergingProcess(OntologyMatchingCoordinator.FALCON_ISUB_MATCHER);
            publish("Matching ontologies using: " + OntologyMatchingCoordinator.FALCON_VDOC_MATCHER.replace("_", " "));
            parent.runMatchingAndMergingProcess(OntologyMatchingCoordinator.FALCON_VDOC_MATCHER);
            publish("Matching ontologies using: " + OntologyMatchingCoordinator.FALCON_COMBINED_MATCHER.replace("_", " "));
            parent.runMatchingAndMergingProcess(OntologyMatchingCoordinator.FALCON_COMBINED_MATCHER);
            publish("---------------------\nOntology extraction and matching completed successfully!");

            return null;
        }

        @Override
        public void process(List<String> messages) {
            for (String msg : messages) {
                taskProgressTextArea.append(msg + "\n");
            }
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            taskDoneButton.setEnabled(true);
        }
    }

    private MainJFrame parent;

    /**
     * Creates new form ProjectProgressDialog
     */
    public TaskProgressDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        centerWindow();

        this.parent = (MainJFrame) parent;
        this.taskDoneButton.setEnabled(false);
        // Start ontology construction task
        task = new OntologyConstructionTask();
        task.execute();

        taskDoneButton.setEnabled(false);
        this.setVisible(true);
    }

    private void centerWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taskProgressTextArea = new javax.swing.JTextArea();
        taskDoneButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ontology Construction Progress");

        taskProgressTextArea.setEditable(false);
        taskProgressTextArea.setColumns(20);
        taskProgressTextArea.setRows(5);
        jScrollPane1.setViewportView(taskProgressTextArea);

        taskDoneButton.setText("Show Matching Results");
        taskDoneButton.setEnabled(false);
        taskDoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskDoneButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(taskDoneButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(taskDoneButton)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void taskDoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskDoneButtonActionPerformed
        dispose();
        MatcherResultsDialog matcherResDialog = new MatcherResultsDialog(parent, true);
        matcherResDialog.setVisible(true);
    }//GEN-LAST:event_taskDoneButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TaskProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaskProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaskProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaskProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaskProgressDialog dialog = new TaskProgressDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton taskDoneButton;
    private javax.swing.JTextArea taskProgressTextArea;
    // End of variables declaration//GEN-END:variables
}