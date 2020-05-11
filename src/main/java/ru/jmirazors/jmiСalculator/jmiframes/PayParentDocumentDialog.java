/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiCalculator.MainFrame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.Document;

/**
 *
 * @author User
 */
public class PayParentDocumentDialog extends javax.swing.JDialog {

    /**
     * Creates new form PayParentDocumentDialog
     */
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            { return false; }        
    };
    int documentType;
    Class documentClass;
    List<Document> documents;
    Document selectedDocument;
    
    public PayParentDocumentDialog(java.awt.Frame parent, boolean modal, String docName) {
        super(parent, modal);
        initComponents();        
        
        tableModel.addColumn("№");
        tableModel.addColumn("Дата");
        tableModel.addColumn("Контрагент");
        tableModel.addColumn("Сумма");
        
        jLabel2.setText(docName);
        
        documentClass = new DocumentUtil().getDocumentClass(getDocumentType(docName));
        
        Map<String, String> properties = new HashMap<>();        
            properties.put("showDel", "no");
            properties.put("user", "%");
            properties.put("organization", MainFrame.sessionParams.getOrganization().getName());
            properties.put("firstDate", "01.01.1980");
            properties.put("lastDate", "curdate");
            properties.put("rowCount", "0");
        
        try {
            documents = new DocumentDAO().list(documentClass, properties);
        } catch (Exception ex) {
            Logger.getLogger(PayParentDocumentDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Document document : documents) {
            tableModel.addRow(new Object[]{
                document.getId(), document.getIndate(), document.getContragent().getName(), document.getTotal()
            });
        }
            
    }
    
    private int getDocumentType(String name) {
        
        return Integer.valueOf(name.substring(1, name.indexOf("]")));
        
    } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Подбор документов");
        setModal(true);
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel1.setText("Документы");

        jLabel2.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        jLabel2.setText("___");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // выбрать документ
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            try {
                PayIf.parentDocument = new DocumentDAO().getDocument(tableModel.getValueAt(jTable1.getSelectedRow(), 0).toString(), documentClass);                
            } catch (Exception ex) {
                Logger.getLogger(PayParentDocumentDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
