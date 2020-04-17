/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.reportsif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import ru.jmirazors.jmiCalculator.beans.reports.SaleDoc;
import ru.jmirazors.jmiСalculator.DAO.ReportsDAO;
import ru.jmirazors.jmiСalculator.entity.Kassa;

/**
 *
 * @author User
 */
public class ReportSaleDocInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form ReportSaleDocInternalFrame
     */
    public ReportSaleDocInternalFrame() {
        initComponents();
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
        jButton1 = new javax.swing.JButton();

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(394, 40));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(311, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(2, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {        
        JasperReport jr;
        jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/repTemplate/saledoc.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<>();      
        List<Object> rep = new ReportsDAO().list(Kassa.class);
        List<SaleDoc> data = new ArrayList<>();
        data.add(null);
        for (int i =0; i< rep.size(); i++) {
            Kassa k = (Kassa)rep.get(i);
            SaleDoc sd = new SaleDoc();
                sd.setNum(k.getDocNumber());
                sd.setDocType(k.getDocuments());
                sd.setData(k.getIndate().toString());
                sd.setDebt(k.getDebt());
                sd.setCrdt(k.getCredit());
            data.add(sd);
        }
            JasperPrint fr = JasperFillManager.fillReport(jr, parameters, new JRBeanCollectionDataSource(data));
            this.getContentPane().add(new JRViewer(fr));
            this.validate();        
        } catch (JRException ex) {}        
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
