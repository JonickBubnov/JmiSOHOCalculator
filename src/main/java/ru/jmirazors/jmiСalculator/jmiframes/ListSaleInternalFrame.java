/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocSale;
import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ru.jmirazors.jmiCalculator.beans.ColorTablesRenderer;
import ru.jmirazors.jmiCalculator.beans.ListUtil;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.entity.Sale;

/**
 *
 * @author User
 */
public class ListSaleInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form ListSaleInternalFrame
     */
    JToolBar tb;
    JButton dockButton = new JButton("Спр. продажи |");
    
    // **************  Параметры списка *****************************
    Map <String, String> properties;
    ListUtil listUtil = new ListUtil();    

    
    //DocSale docSale;
    TableRowSorter<TableModel> rowSorter;
    ColorTablesRenderer colorRenderer = new ColorTablesRenderer();
    
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            { return false; }
    };
        
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        JLabel label = new JLabel();
        ImageIcon docNew = new ImageIcon(getClass().getResource("/images/doc_saved.png"));
        ImageIcon docDel = new ImageIcon(getClass().getResource("/images/doc_delete.png"));
        ImageIcon docComplete = new ImageIcon(getClass().getResource("/images/doc_complete.png"));
        
        @Override
        public Component getTableCellRendererComponent (JTable table, Object val, 
                boolean isSelected, boolean hasFocus, int row, int col) {
            if ((long)val == 1L)
                label.setIcon(docNew);
            if ((long)val == 3L)
                label.setIcon(docDel);
            if ((long)val == 2L)
                label.setIcon(docComplete);                                       
            
            return label;
        }
    };    
    
    
    public ListSaleInternalFrame(JToolBar toolBar) {
        
        this.tb = toolBar;
          dockButton.setFocusPainted(false);          
          dockButton.addActionListener((ActionEvent evt) -> {
              toggleState();
        }); 
        tb.add(dockButton);         
        
        
        tableModel.addColumn("");
        tableModel.addColumn("№");
        tableModel.addColumn("Дата");
        tableModel.addColumn("Организация");        
        tableModel.addColumn("Сумма");
        tableModel.addColumn("Склад");
        tableModel.addColumn("Пользователь");
        tableModel.addColumn("Примечание");        
        
        rowSorter = new TableRowSorter(tableModel);
        
        initComponents();
        
        //status
        jTable1.getColumnModel().getColumn(0).setMaxWidth(17);
        jTable1.getColumnModel().getColumn(0).setMinWidth(17);
        //id
        jTable1.getColumnModel().getColumn(1).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(1).setMinWidth(50);
        //date
        jTable1.getColumnModel().getColumn(2).setMaxWidth(105);
        jTable1.getColumnModel().getColumn(2).setMinWidth(105);
        //Organization
        jTable1.getColumnModel().getColumn(3).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(3).setMinWidth(200);         
//        //contragent
//        jTable1.getColumnModel().getColumn(4).setMaxWidth(200);
//        jTable1.getColumnModel().getColumn(4).setMinWidth(200);        
        //total
        jTable1.getColumnModel().getColumn(4).setMaxWidth(96);
        jTable1.getColumnModel().getColumn(4).setMinWidth(96);
        //sklad
        jTable1.getColumnModel().getColumn(5).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(5).setMinWidth(200);        
        //user
        jTable1.getColumnModel().getColumn(6).setMaxWidth(156);
        jTable1.getColumnModel().getColumn(6).setMinWidth(156);
        
        jTable1.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTable1.setDefaultRenderer(Object.class, colorRenderer);
        
        // Поиск
        jTextField1.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (jTextField1.getText().trim().length() == 0)
                    rowSorter.setRowFilter(null);
                else
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)"+jTextField1.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (jTextField1.getText().trim().length() == 0)
                    rowSorter.setRowFilter(null);
                else
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)"+jTextField1.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        
        properties = listUtil.readProperties("sale");
        
        getSaleList();        
    }
    
    // ******************************************************
     @Override
     public void dispose() {
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setSaleListFrameOpen(false);
     }
     private void toggleState() {
          try {
               if(this.isVisible())
                    this.hide();
               else {
                    this.setIcon(false);
                    this.show(); 
               }
          } catch (PropertyVetoException ex) {}
     }     
     // *******************************************************************  
     
     public void getSaleList() {
                 
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }            
        tableModel.setRowCount(0);        
        
        try {
            List sales = new DocumentDAO().list(Sale.class, properties); //, new Date(), new Date(), MainFrame.organization, MainFrame.user, new Contragent());
            Iterator it = sales.iterator();
            while (it.hasNext()) {
                Sale sale = (Sale)it.next(); 
                tableModel.addRow(new Object[]{sale.getStatus().getId(), sale.getId(), format.format(sale.getIndate()), sale.getOrganization().getName(),
                sale.getTotal(), sale.getStorage().getName(), sale.getUsr().getName(), sale.getDescr()});                
            }
        } catch (Exception e) {System.out.print(e);} 
        jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());        
     }      

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Справочник \"Продажи\"");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cash-register.png"))); // NOI18N
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

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-with-pen-tool.png"))); // NOI18N
        jButton1.setToolTipText("Новый документ");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton2.setToolTipText("Сохранить");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/funnel.png"))); // NOI18N
        jButton3.setToolTipText("Настройки");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        jButton4.setToolTipText("Обновить");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jPanel1.setPreferredSize(new java.awt.Dimension(468, 25));

        jLabel1.setText("Поиск");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jToolBar1.add(jPanel1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // обновить
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        getSaleList();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified
    // настройки
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ListConfigDialog ilcd = new ListConfigDialog(null, true, properties);
        ilcd.setLocationRelativeTo(this);
        ilcd.setVisible(true);
        listUtil.writeProperties("sale", properties);
        getSaleList();        
    }//GEN-LAST:event_jButton3ActionPerformed
    // Сохранить список
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        JFileChooser fileChooser = new JFileChooser();
        
        fileChooser.setDialogTitle("Сохранить файл");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {            
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".xls"))
                fileToSave = new File(fileToSave.getAbsolutePath()+".xls");               
        
            try {
                listUtil.saveXLS(jTable1, fileToSave);
            } catch (IOException ex) {
                Logger.getLogger(ListInvoiceInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       if (!MainFrame.ifManager.isDocSaleFrameOpen()) {
           MainFrame.ifManager.showFrame(new DocSale(), true);
           MainFrame.ifManager.setDocSaleFrameOpen(true);
       }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
