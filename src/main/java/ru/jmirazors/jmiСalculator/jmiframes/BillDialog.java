/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocOffer;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocInvoice;
import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiCalculator.beans.InvoiceBean;
import ru.jmirazors.jmiCalculator.beans.MonthToTextBean;
import ru.jmirazors.jmiCalculator.beans.NumberToTextBean;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.SubordinDAO;
import ru.jmirazors.jmiСalculator.entity.Bill;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.Document;
import ru.jmirazors.jmiСalculator.entity.Invoice;
import ru.jmirazors.jmiСalculator.entity.InvoiceProduct;
import ru.jmirazors.jmiСalculator.entity.Pay;
import ru.jmirazors.jmiСalculator.entity.Subordin;

/**
 *
 * @author User
 */
public class BillDialog extends javax.swing.JDialog {

    /**
     * Creates new form BillDialog
     */
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm"); 
    Bill bill = null;
    Document parentDocument;
    Float sum = 0f;
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col){
            return false;
        }
    };
    
    public BillDialog(java.awt.Frame parent, boolean modal, Bill bill) {
        super(parent, modal);
        
        tableModel.addColumn("Документ");
        tableModel.addColumn("Дата");
        tableModel.addColumn("Сумма");
        
        initComponents();                
        
        
        this.bill = bill;
        // документ счет
        StringBuilder doc = new StringBuilder("");
        doc.append(bill.getDocuments().getName()).append(" №").append(bill.getId()).append(" от ")
               .append(new MonthToTextBean().dateToText(format.format(bill.getIndate())));
        jLabel4.setText(doc.toString());
        // ***********************************************************************
        // Документ основание
        Subordin subordin = new SubordinDAO().getSubDocument(bill);
        parentDocument = new DocumentUtil().getMainDocument(subordin);
        doc.setLength(0);
        doc.append(parentDocument.getDocuments().getName()).append(" №").append(parentDocument.getId()).append(" от ")
                .append(new MonthToTextBean().dateToText(format.format(parentDocument.getIndate())));
        jLabel2.setText(doc.toString());
        // ****************************************************
        jLabel6.setText(bill.getTotal()+" руб.");
        // Оплаты
        List<Document> subDocs = new DocumentUtil().getSubordinDocuments(bill);
        if (!subDocs.isEmpty()) {
            for (Document subDoc : subDocs) {
                tableModel.addRow(new Object[]{subDoc.getDocuments().getName()+" №"+subDoc.getId(), 
                    format.format(subDoc.getIndate()), subDoc.getTotal()});
                sum += subDoc.getTotal();
            }
        }
        jLabel10.setText(String.valueOf(sum)+" руб.");
        // *********************************
        
//        Pay pay = bill.getPay();
//        if (pay == null) {
//            jLabel8.setText("не оплачен");
//            jLabel10.setText("-");            
//        } else {
//            String doc3 = pay.getDocuments().getName()+" №"+pay.getId()+" от "
//                +new MonthToTextBean().dateToText(format.format(pay.getIndate()));
//            jLabel8.setText(doc3);
//            jLabel10.setText(pay.getTotal()+" руб.");
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Счет на оплату");
        setModal(true);
        setResizable(false);

        jLabel1.setText("Документ основание");

        jLabel2.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 109, 240));
        jLabel2.setText("__");
        jLabel2.setToolTipText("");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.setText("Документ");

        jLabel4.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel4.setText("__");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.setText("Сумма");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("__");

        jLabel9.setText("Итого");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("__");

        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Сохранить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(tableModel);
        jTable1.setDoubleBuffered(true);
        jTable1.setSelectionBackground(new java.awt.Color(169, 169, 169));
        jScrollPane1.setViewportView(jTable1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-document.png"))); // NOI18N
        jButton3.setToolTipText("Добавить документ оплаты");
        jButton3.setFocusPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton3))
                        .addGap(0, 99, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        
           
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        dispose(); 
        if (parentDocument.getDocuments().getId() == 1) {
            DocInvoice docInvoice = new DocInvoice(String.valueOf(parentDocument.getId()));
            //MainFrame.jDesktopPane1.add(docInvoice);
            MainFrame.ifManager.showFrame(docInvoice, false);
            docInvoice.show();}
        if (parentDocument.getDocuments().getId() == 3) {
            DocOffer docOffer = new DocOffer(String.valueOf(parentDocument.getId()));
            //MainFrame.jDesktopPane1.add(docOffer);
            MainFrame.ifManager.showFrame(docOffer, false);
            docOffer.show();}        
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // сохранить счет
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        JFileChooser fileChooser = new JFileChooser();
        
        fileChooser.setDialogTitle("Сохранить файл");   
 
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {            
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".pdf"))
                fileToSave = new File(fileToSave.getAbsolutePath()+".pdf");
        
        try {
            super.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            JasperReport jr;
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/bill.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("num", String.valueOf(bill.getId()));
                parameters.put("date",  new MonthToTextBean().dateToText(format.format(bill.getIndate())));
                parameters.put("bank", MainFrame.sessionParams.getOrganization().getBank());
                parameters.put("inn", MainFrame.sessionParams.getOrganization().getInn());
                parameters.put("kpp", MainFrame.sessionParams.getOrganization().getKpp());
                parameters.put("pname", MainFrame.sessionParams.getOrganization().getName());
                parameters.put("bik", MainFrame.sessionParams.getOrganization().getBIK());
                parameters.put("bank", MainFrame.sessionParams.getOrganization().getBank());
                parameters.put("acc", MainFrame.sessionParams.getOrganization().getRs());
                parameters.put("kacc", MainFrame.sessionParams.getOrganization().getKs());
                
//                Invoice inv = bill.getInvoice();
                Invoice inv = null;
                try {
                    inv = (Invoice)new DocumentDAO().getDocument(""+parentDocument.getId(), Invoice.class);
                } catch (Exception ex) {
                    Logger.getLogger(BillDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                List<InvoiceProduct> plist  = inv.getInvoiceProducts();
                int count = plist.size();
                
                parameters.put("total", String.valueOf(bill.getTotal()));
                parameters.put("weight", String.valueOf(inv.getWeight()));
                parameters.put("count", String.valueOf(count));
                parameters.put("strsum", new NumberToTextBean().numberToText( Double.parseDouble(String.valueOf(bill.getTotal())) ));
                
                String org = MainFrame.sessionParams.getOrganization().getName() + "; ИНН:" +
                        MainFrame.sessionParams.getOrganization().getInn() + "; КПП:" +
                        MainFrame.sessionParams.getOrganization().getKpp() + "; " +
                        MainFrame.sessionParams.getOrganization().getAddresone() + "; тел.:" +
                        MainFrame.sessionParams.getOrganization().getPhone();
                String contr = bill.getContragent().getName() + "; ИНН:" +
                        bill.getContragent().getINN() + "; КПП: " +
                        bill.getContragent().getKPP() + "; " +
                        bill.getContragent().getAddres1() + "; тел.: " +
                        bill.getContragent().getPhone();
                
                parameters.put("user", org);
                parameters.put("contragent", contr);                
                
            List<InvoiceBean> ib = new ArrayList<>();
            ib.add(null);
            for (int i = 0; i < plist.size(); i++)
                ib.add(new InvoiceBean(String.valueOf(i+1),
                        plist.get(i).getProduct().getArticul(), plist.get(i).getProduct().getName(),
                        plist.get(i).getCount(), plist.get(i).getProduct().getUnit().getName(),
                        plist.get(i).getCost()*(100-plist.get(i).getDiscount())/100, 
                        plist.get(i).getCost()*(100-plist.get(i).getDiscount())/100*plist.get(i).getCount()
                ));            

            JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
               parameters,  new JRBeanCollectionDataSource(ib));

            JasperExportManager.exportReportToPdfFile(jasperPrint,
               fileToSave.getCanonicalPath());  
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            MainFrame.infoPanel.append("\nФайл " + fileToSave.getCanonicalPath() + " сохранен.");
            
        } catch (JRException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) { 
                Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }   // конец файлчузера          
    }//GEN-LAST:event_jButton2ActionPerformed

    // Добавить оплату
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
            PayIf pay = new PayIf(bill, false);
            dispose();
            MainFrame.ifManager.showFrame(pay, false);
            pay.show();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
