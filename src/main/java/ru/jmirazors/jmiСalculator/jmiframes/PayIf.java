/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocumentImpl;
import ru.jmirazors.jmiCalculator.MainFrame;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.ContragentSelectDialog;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JToolBar;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PayDAO;
import ru.jmirazors.jmiСalculator.entity.Document;
import ru.jmirazors.jmiСalculator.entity.DocumentType;
import ru.jmirazors.jmiСalculator.entity.Pay;
import ru.jmirazors.jmiСalculator.entity.PayType;

/**
 *
 * @author User
 */
public class PayIf extends javax.swing.JInternalFrame implements DocumentImpl{

    JToolBar tb;
    JButton dockButton = new JButton("Док. Оплата |"); 
    
    PayDAO payDAO;
    Pay docPay;
    DocumentDAO documentDAO;
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static Document parentDocument = null;
     
    public PayIf() {
        docPay = new Pay();        
        payDAO = new PayDAO();
        documentDAO = new DocumentDAO();
        initComponents();
        initVisualComponents();  
        
        getPayTypes();
        getDocTypes();
        docInit();
    }
    
    public PayIf(String id) {
        payDAO = new PayDAO();
        documentDAO = new DocumentDAO();
        try {
            docPay = (Pay) new DocumentDAO().getDocument(id, Pay.class);
        } catch (Exception ex) {
            Logger.getLogger(PayIf.class.getName()).log(Level.SEVERE, null, ex);
        }
        parentDocument = new DocumentUtil().getMainDocument(docPay);
           
        initComponents();
        initVisualComponents();
        
        getPayTypes();
        getDocTypes();
        repaintDocument();       
        closeButtons();
    }
    
    public PayIf(Document document, boolean deb) {
        docPay = new Pay();
        payDAO = new PayDAO();
        documentDAO = new DocumentDAO();
        initComponents();
        initVisualComponents();
        
        docInit();
        parentDocument = document;
                
        docPay.setContragent(document.getContragent());
        docPay.setTotal(parentDocument.getTotal());
        if (deb) docPay.setDebcr((byte)1);
            else docPay.setDebcr((byte)0);        
        
        
        getPayTypes();
        getDocTypes();
                
        jComboBox1.setSelectedIndex(docPay.getDebcr());
        jTextField1.setText(document.getContragent().getName());            
        jFormattedTextField1.setText(String.valueOf(document.getTotal()).replace(".", ","));
        jLabel4.setText(parentDocument.getDocuments().getName()
                +" №"+parentDocument.getId()+" от "+format.format(parentDocument.getIndate()));            
    }
    
    // ************ имплементированные методы документа ********************
    // закрытие документа
     @Override
     public void dispose() {
          super.dispose();
          documentDAO.ev(docPay);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocSaleFrameOpen(false);
     }    
    // переключение состояния тулбара
    @Override
    public void toggleState() {
          try {
               if(this.isVisible())
                    this.hide();
               else {
                    this.setIcon(false);
                    this.show(); 
               }
          } catch (PropertyVetoException ex) {
          }
    }

    @Override
    public void closeButtons() {  
        jComboBox2.setEnabled(false);
        jComboBox1.setEnabled(false);
        jComboBox3.setEnabled(false);
        jFormattedTextField1.setEnabled(false);
        jTextField2.setEnabled(false);
        jButton2.setEnabled(false);
    }

    @Override
    public void initTableColumns() {        
    }

    @Override
    public void initVisualComponents() {
        this.tb = MainFrame.jToolBar2;
          dockButton.setFocusPainted(false);          
          dockButton.addActionListener((ActionEvent evt) -> {
              toggleState();
        }); 
        tb.add(dockButton);          
    }

    @Override
    public void docInit() {
        docPay.setOrganization(MainFrame.sessionParams.getOrganization());
        docPay.setUsr(MainFrame.sessionParams.getUser());
    }

    @Override
    public void repaintDocument() {
        if (docPay.getContragent() != null)
            jTextField1.setText(docPay.getContragent().getName());
        jFormattedTextField1.setText(String.format("%.2f", docPay.getTotal()).replace(".", ","));
        jTextField2.setText(docPay.getDescr());
        jLabel11.setText(String.valueOf(docPay.getId()));
        jLabel13.setText(format.format(docPay.getIndate()));       
        jComboBox2.setSelectedItem(docPay.getPaytype().getName());        
        jComboBox1.setSelectedIndex(docPay.getDebcr());
        if (parentDocument != null) {
            jLabel4.setText(parentDocument.getDocuments().getName()
                +" №"+parentDocument.getId()+" от "+format.format(parentDocument.getIndate()));            
         }
        
        
    }

    @Override
    public void saveDocument(long satus) {
    }

    @Override
    public void executeDocument() {
 
        docPay.setDebcr(getDebCr());
        docPay.setPaytype(getPayType());
        docPay.setDescr(jTextField2.getText());
        docPay.setTotal(Float.valueOf(jFormattedTextField1.getText().replace(",", ".")));
        docPay.setIndate(new Date());
        docPay.setUsr(MainFrame.sessionParams.getUser());
        docPay.setOrganization(MainFrame.sessionParams.getOrganization());
        docPay.setDocuments(new DocumentDAO().getDocumentType(8l));
        docPay.setStatus(new DocumentDAO().getStatus(2l));
        
        new DocumentCompletionDAO().completion(docPay, parentDocument);
        
        repaintDocument();      
    }    
    
    // Список типы оплаты
    void getPayTypes() {
        jComboBox2.removeAllItems();
        try {
            List payTypes = payDAO.payTypeList();
            Iterator it = payTypes.iterator();
            while (it.hasNext()) {
                PayType pt = (PayType)it.next();
                jComboBox2.addItem(pt.getName());
            }
        } catch (Exception ex) {
            Logger.getLogger(PayIf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Список документов основание
    void getDocTypes() {
        jComboBox3.removeAllItems();
        try {
            List documentTypes = documentDAO.listDocumenTypes();
            Iterator it = documentTypes.iterator();
            while (it.hasNext()) {
                DocumentType dt = (DocumentType)it.next();
                jComboBox3.addItem("["+dt.getId()+"] "+dt.getName());
            }
        } catch (Exception ex) {System.out.println("GetDocTypes: " + ex);}
    }
    
    // получить выбранный способ оплаты
    PayType getPayType() {
        PayType pt = payDAO.getPayType((String) jComboBox2.getSelectedItem());
        return pt;
    }
    
    // получить выбраный вид операции
    byte getDebCr() {
        if (jComboBox1.getSelectedIndex() == 1) 
            return 1;
        else
            return 0;
    }
    
    // установить документ в комбобокс
    void setDocumentType() {
        jComboBox3.setSelectedItem(parentDocument.getDocuments().getName());
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
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setTitle("Оплата");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coin-stack.png"))); // NOI18N
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

        jPanel1.setPreferredSize(new java.awt.Dimension(394, 50));

        jButton1.setText("Закрыть");
        jButton1.setPreferredSize(new java.awt.Dimension(92, 32));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Выполнить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Чек");
        jButton4.setPreferredSize(new java.awt.Dimension(92, 32));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jLabel1.setText("Контрагент");

        jTextField1.setEditable(false);
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jLabel2.setText("Вид операции");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Поступление средств", "Расход средств" }));

        jLabel3.setText("Документ основание");

        jLabel4.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 255));
        jLabel4.setText("Нет");
        jLabel4.setToolTipText("Документ основание");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel8.setText("Сумма");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setText("0,00");
        jFormattedTextField1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        jLabel9.setText("Примечание");

        jLabel10.setText("№");

        jLabel11.setText("____");

        jLabel12.setText("дата");
        jLabel12.setToolTipText("");

        jLabel13.setText("_____");

        jLabel14.setText("Способ оплаты");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

      // Сохранить
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      executeDocument();        
    }//GEN-LAST:event_jButton2ActionPerformed

    //Выбор документа основания
    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        PayParentDocumentDialog ppdd = new PayParentDocumentDialog(null, true, jComboBox3.getSelectedItem().toString());
        ppdd.setLocationRelativeTo(this);
        ppdd.setVisible(true);
        if (parentDocument != null) {
            jLabel4.setText(parentDocument.getDocuments().getName()
                +" №"+parentDocument.getId()+" от "+format.format(parentDocument.getIndate()));
            jFormattedTextField1.setText(String.valueOf(parentDocument.getTotal()).replace(".", ","));
            docPay.setContragent(parentDocument.getContragent());
            jTextField1.setText(docPay.getContragent().getName());
        }
    }//GEN-LAST:event_jLabel4MouseClicked
    
    // Выбрать контрагента
    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        ContragentSelectDialog csd = new ContragentSelectDialog(null, true, docPay);
        csd.setLocationRelativeTo(this);
        csd.setVisible(true);
        
        jTextField1.setText(docPay.getContragent().getName());         
    }//GEN-LAST:event_jTextField1MouseClicked

    // печать чека
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    // скрыть при сворачивании
    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}
