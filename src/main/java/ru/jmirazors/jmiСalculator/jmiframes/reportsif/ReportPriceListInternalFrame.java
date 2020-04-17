/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.reportsif;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import ru.jmirazors.jmiCalculator.beans.reports.PriceList;
import ru.jmirazors.jmiСalculator.DAO.GroupDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.ReportsDAO;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.Price;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;



/**
 *
 * @author User
 */
public class ReportPriceListInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form Report1Test
     */
    List<PriceName> priceNames = null;
    List<Group> groups = null;
    
    static HashMap<String, String> selGroups;
    static HashMap<String, String> selPriceNames;
    
    long groupId = 1;
    
    public ReportPriceListInternalFrame() {                   

            initComponents();
            priceNames = new ArrayList<>();
                priceNames = new PriceNameDAO().list("");
            groups = new ArrayList<>();
                groups = new GroupDAO().list();
                
            this.selGroups = new HashMap<>();
                for (Group gr : groups) {
                    selGroups.put(gr.getName(), "1");
                }
            this.selPriceNames = new HashMap<>();
                for (PriceName pn : priceNames) {
                    selPriceNames.put(pn.getName(), "1");
                }                
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
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Прайс-Лист");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line-chart.png"))); // NOI18N

        jPanel1.setPreferredSize(new java.awt.Dimension(736, 40));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/report.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Типы цен");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton1.setText("Группы");
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
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(522, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        super.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            JasperReport jr;
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/repTemplate/pricelist.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<>();
                parameters.put("date", new Date().toString());
                parameters.put("group", "---");
                
                for (String pn : selPriceNames.keySet()) {
                    if (pn.equals("Закупочная")) {
                                if (selPriceNames.get(pn).equals("1"))
                                    parameters.put("prName1", true);
                                else
                                    parameters.put("prName1", false);
                                }
                    if (pn.equals("Учетная")) {
                                if (selPriceNames.get(pn).equals("1"))
                                    parameters.put("prName2", true);
                                else
                                    parameters.put("prName2", false);
                                }                    
                    if (pn.equals("Розничная")) {
                                if (selPriceNames.get(pn).equals("1"))
                                    parameters.put("prName4", true);
                                else
                                    parameters.put("prName4", false);
                                }   
                    if (pn.equals("Оптовая")) {
                                if (selPriceNames.get(pn).equals("1"))
                                    parameters.put("prName3", true);
                                else
                                    parameters.put("prName3", false);
                                }                     
                }
                
            List<Product> prod = new ReportsDAO().getPriceList();
            List<PriceList> data = new ArrayList();
            data.add(null);            
            for (Product product : prod) {
                PriceList pList = new PriceList();
                    pList.setArticul(product.getArticul());
                    pList.setName(product.getName());
                List<Price> price = product.getActualPriceList();
                    pList.setPriceList(price);
                data.add(pList);
            }
                        
            JasperPrint fr = JasperFillManager.fillReport(jr, parameters, new JRBeanCollectionDataSource(data));
            this.getContentPane().add(new JRViewer(fr));
            this.validate();
        } catch (JRException ex) {
            Logger.getLogger(ReportPriceListInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        
        super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // группы
        SelectGroupsDialog sgd = new SelectGroupsDialog(null, true, selGroups);
        sgd.setLocationRelativeTo(this);
        sgd.setVisible(true);        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // типы цены
        SelectPriceNameDialog spnd = new SelectPriceNameDialog(null, true, selPriceNames);
        spnd.setLocationRelativeTo(this);
        spnd.setVisible(true);        
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
