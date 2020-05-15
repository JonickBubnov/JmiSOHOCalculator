/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocInvoice;
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
import ru.jmirazors.jmiСalculator.entity.Act;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocAct;

/**
 *
 * @author User
 */
public class ListActInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form ListActInternalFrame
     */
    JToolBar tb;
    JButton dockButton = new JButton("Спр. акты");
    
    // **************  Параметры списка *****************************
    Map <String, String> properties;
    ListUtil listUtil = new ListUtil();    

    
    DocInvoice docInvoice;
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
    
    public ListActInternalFrame(JToolBar toolBar) {
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
        tableModel.addColumn("Контрагент");
        tableModel.addColumn("Сумма");
        tableModel.addColumn("Пользователь");
        tableModel.addColumn("Подразделение");        
        tableModel.addColumn("Примечание");
        
        rowSorter = new TableRowSorter(tableModel);        
        
        initComponents();
        
        //status
        jTable1.getColumnModel().getColumn(0).setMaxWidth(17);
        jTable1.getColumnModel().getColumn(0).setMinWidth(17);
        //id
        jTable1.getColumnModel().getColumn(1).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(1).setMinWidth(70);
        //date
        jTable1.getColumnModel().getColumn(2).setMaxWidth(105);
        jTable1.getColumnModel().getColumn(2).setMinWidth(105);
        //Organization
        jTable1.getColumnModel().getColumn(3).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(3).setMinWidth(200);                 
        //contragent
        jTable1.getColumnModel().getColumn(4).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(4).setMinWidth(200);        
        //total
        jTable1.getColumnModel().getColumn(5).setMaxWidth(96);
        jTable1.getColumnModel().getColumn(5).setMinWidth(96);        
        //user
        jTable1.getColumnModel().getColumn(6).setMaxWidth(156);
        jTable1.getColumnModel().getColumn(6).setMinWidth(156);
        //department
        jTable1.getColumnModel().getColumn(7).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(7).setMinWidth(200);         
        
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
        
        properties = listUtil.readProperties("act");
        
        getActList();        
    }
    
    // ******************************************************
     @Override
     public void dispose() {
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setActListFrameOpen(false);
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

 public void getActList() {        
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }            
        tableModel.setRowCount(0);        
        
        try {
            List acts = new DocumentDAO().list(Act.class, properties); //, new Date(), new Date(), MainFrame.organization, MainFrame.user, new Contragent());
            Iterator it = acts.iterator();
            while (it.hasNext()) {
                Act act = (Act)it.next(); 
                tableModel.addRow(new Object[]{act.getStatus().getId(), listUtil.getFormattedID(act.getId()), 
                format.format(act.getIndate()), act.getOrganization().getName(),
                act.getContragent().getName(), String.format("%.2f", act.getTotal()),
                act.getUsr().getName(), act.getDepartment().getName(), act.getDescr()});                
            }
        } catch (Exception e) {System.out.print(e);} 
        tableModel.fireTableDataChanged();
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
        setTitle("Справочник \"Акты выполненных работ\"");
        setDoubleBuffered(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/complete.png"))); // NOI18N
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
        jToolBar1.setPreferredSize(new java.awt.Dimension(584, 34));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-with-pen-tool.png"))); // NOI18N
        jButton1.setToolTipText("Новый документ \"Акт выполненных работ\"");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton1.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton1.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton2.setToolTipText("Сохранить справочник");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton2.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton2.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings.png"))); // NOI18N
        jButton3.setToolTipText("Настройки списка");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton3.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton3.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        jButton4.setToolTipText("Обновить список");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setFocusPainted(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton4.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton4.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 24));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/funnel.png"))); // NOI18N
        jPanel1.add(jLabel1);

        jTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField1.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel1.add(jTextField1);

        jToolBar1.add(jPanel1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jTable1.setRowSorter(rowSorter);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // новый документ
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         if (!MainFrame.ifManager.isDocActFrameOpen()) {
            MainFrame.ifManager.showFrame(new DocAct(), false);
            MainFrame.ifManager.setDocActFrameOpen(true);
        } else {
            MainFrame.ifManager.infoMessage("Документ уже открыт. Невозмозможно открыть несколько копий документа.");
        }         
    }//GEN-LAST:event_jButton1ActionPerformed
    // обновить
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        getActList();
    }//GEN-LAST:event_jButton4ActionPerformed
    // свернуть
    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified
    // настройки
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ListConfigDialog ilcd = new ListConfigDialog(null, true, properties);
        ilcd.setLocationRelativeTo(this);
        ilcd.setVisible(true);
        listUtil.writeProperties("act", properties);
        getActList();
    }//GEN-LAST:event_jButton3ActionPerformed
    // сохранить в файл
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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !MainFrame.ifManager.isDocActFrameOpen()) { 
            System.out.println(tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow()), 1).toString());
            String id = tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow()), 1).toString();
            MainFrame.ifManager.showFrame(new DocAct(id), false);
            MainFrame.ifManager.setDocActFrameOpen(true);
        } else {
            MainFrame.ifManager.infoMessage("Документ уже открыт. Невозмозможно открыть несколько копий документа.");
        }                     
    }//GEN-LAST:event_jTable1MouseClicked


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
