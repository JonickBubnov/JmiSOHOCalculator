/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.reportsif;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import ru.jmirazors.jmiCalculator.beans.reports.SaleDoc;
import ru.jmirazors.jmiСalculator.DAO.ContragentDAO;
import ru.jmirazors.jmiСalculator.DAO.ReportsDAO;
import ru.jmirazors.jmiСalculator.entity.Contragent;
import ru.jmirazors.jmiСalculator.entity.Kassa;
import ru.jmirazors.jmiCalculator.MainFrame;

/**
 *
 * @author User
 */
public class ReportSaleDocInternalFrame extends javax.swing.JInternalFrame {

    JToolBar tb;
    JButton dockButton = new JButton("Продажи |"); 
    
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
    List<Contragent> contragents = null;
    
    static HashMap<String, String> selContragents;
    /**
     * Creates new form ReportSaleDocInternalFrame
     */
    public ReportSaleDocInternalFrame(JToolBar toolBar) {
        this.tb = toolBar;
          dockButton.setFocusPainted(false);
          dockButton.addActionListener((ActionEvent evt) -> {
              toggleState();
        }); 
        tb.add(dockButton);          
        
        initComponents();
        
        contragents = new ContragentDAO().list();
        this.selContragents = new HashMap<>();                
        for (Contragent ct : contragents) {
            selContragents.put(ct.getName(), "1");
            }        
        
        jDateChooser1.setDate(new Date());
        jDateChooser2.setDate(new Date());
    }
    // ***********************************************************
     @Override
     public void dispose() {          
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setReportSaleDocOpen(false);
     }
     private void toggleState() {
          try {
               if(this.isVisible())
                    this.hide();
               else {
                    this.setIcon(false);
                    this.show(); 
               }
          } catch (Exception ex) {
               ex.printStackTrace();
          }
     } 
     List<Contragent> getSelectedContragentList() {
         List<Contragent> selectedContragents = new ArrayList<>();
         for (Contragent gr : contragents) {
             if (selContragents.get(gr.getName()) == "1")
                 selectedContragents.add(gr);
         }
         return selectedContragents;
     }     
     
     // ************************************************************** 
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
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Продажи по документам");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line-chart.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameIconified(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(394, 40));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/report.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jDateChooser1.setPreferredSize(new java.awt.Dimension(125, 24));

        jLabel1.setText("Период c");

        jLabel2.setText("по");

        jDateChooser2.setPreferredSize(new java.awt.Dimension(125, 24));

        jButton2.setText("Контрагент");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(256, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10))
        );

        jLabel2.getAccessibleContext().setAccessibleName("по");

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:        
        super.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {                
        JasperReport jr;
        jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/repTemplate/saledoc.jrxml").getFile() );
        Map<String, Object> parameters = new HashMap<>();
            parameters.put("data", format.format(new Date()));
            parameters.put("org", MainFrame.sessionParams.getOrganization().getName());            
            parameters.put("period", format.format(jDateChooser1.getDate())+"-"+format.format(jDateChooser2.getDate()));
            //parameters.put("contr", "Контрагент");
        List<Kassa> rep = new ReportsDAO().getKassa(getSelectedContragentList());
        List<SaleDoc> data = new ArrayList<>();
        data.add(null);
        for (int i =0; i< rep.size(); i++) {
            Kassa k = rep.get(i);
            SaleDoc sd = new SaleDoc();
                sd.setNum(k.getDocNumber());
                sd.setDocType(k.getDocuments());
                sd.setData(format.format(k.getIndate()));
                sd.setDebt(k.getDebt());
                sd.setCrdt(k.getCredit());
                sd.setContr(k.getContragent());
            data.add(sd);
        }
            JasperPrint fr = JasperFillManager.fillReport(jr, parameters, new JRBeanCollectionDataSource(data));
            this.getContentPane().add(new JRViewer(fr));
            this.validate();   
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (JRException ex) {
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(null, 
                    "Не удалось сформировать отчет \n"+ex, "Ошибка", JOptionPane.ERROR_MESSAGE);            
        }        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        SelectContragentDialog scd = new SelectContragentDialog(null, true, selContragents);
        scd.setLocationRelativeTo(this);
        scd.setVisible(true);        
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
