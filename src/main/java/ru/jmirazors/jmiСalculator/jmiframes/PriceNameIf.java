/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import ru.jmirazors.jmiCalculator.beans.ColorTablesRenderer;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.entity.PriceName;

/**
 *
 * @author User
 */
public class PriceNameIf extends javax.swing.JInternalFrame {

    /**
     * Creates new form PriceNameIf
     */    
    JToolBar tb;
    JButton dockButton = new JButton("Типы цен");    
    
    StringBuffer queryOption = new StringBuffer("from PriceName where del=1");
    ArrayList<PriceName> priceNameList;
    PriceName priceName;
    JComboBox priceCombo;
    String[] names;
    ColorTablesRenderer colorRenderer = new ColorTablesRenderer();
    
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            { return true; }
    };    
    
    
    
    public PriceNameIf(JToolBar toolBar) {
        this.tb = toolBar;
          dockButton.setFocusPainted(false);
          dockButton.addActionListener((ActionEvent evt) -> {
              toggleState();
        }); 
        tb.add(dockButton);         
        
        tableModel.addColumn("№");
        tableModel.addColumn("Наименование");
        tableModel.addColumn("Базовая цена");
        tableModel.addColumn("%");
        
        
                              
        initComponents(); 
        getPriceNameList();
        
        jTable1.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(priceCombo));
                      
        jTable1.getColumnModel().getColumn(0).setMaxWidth(24);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(160);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(160);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(48); 
        jTable1.setDefaultRenderer(Object.class, colorRenderer);
    }
    
    // ***********************************************************
     @Override
     public void dispose() {
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setPriceNameFrameOpen(false);
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
     // **************************************************************
    
    
    void updateArrayNames() {
        String[] arrayNames = new String[priceNameList.size()];
        int i = 0;
        Iterator iterator = priceNameList.iterator();
        while (iterator.hasNext()) {
            priceName = (PriceName) iterator.next();
            arrayNames[i] = priceName.getName();
            i++;
        }
        names = arrayNames;
        priceCombo = new JComboBox(names);
    }
    
    PriceName getNameFromId(long id) {
        for (int i = 0; i < priceNameList.size(); i++)
            if (priceNameList.get(i).getId() == id)
                return priceNameList.get(i);
        return null;
    }
    
    Long getValueByName(String pName) {
        for (PriceName pn: priceNameList)
            if (pn.getName().equals(pName))
                return pn.getId();
        return 1l;
    }

    public void getPriceNameList() {
        
        if (jToggleButton1.isSelected()) 
            this.queryOption = new StringBuffer("from PriceName");
        else
            queryOption = new StringBuffer("from PriceName where del=1");        
        
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);
        
        
        try {
            priceNameList = (ArrayList) new PriceNameDAO().list(queryOption.toString());
            updateArrayNames();            
            
            Iterator iterator = priceNameList.iterator();
            while (iterator.hasNext()) {
                priceName = (PriceName) iterator.next();                
                tableModel.addRow(new Object[]{priceName.getId(), priceName.getName(), getNameFromId(priceName.getPrevious()).getName(), priceName.getPercent()});                                                
            }
            tableModel.fireTableDataChanged();
        } catch (Exception e) {JOptionPane.showMessageDialog(this, "Не могу получить данные о типах цен. \n" + e, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
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
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Типы цен");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coins.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(340, 200));
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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-with-pen-tool.png"))); // NOI18N
        jButton1.setToolTipText("Новый тип цен");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_del.png"))); // NOI18N
        jToggleButton1.setToolTipText("Показывать удаленные");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            priceNameList.get(i).setName(tableModel.getValueAt(i, 1).toString());
            priceNameList.get(i).setPrevious(getValueByName(tableModel.getValueAt(i, 2).toString()));
            priceNameList.get(i).setPercent(Long.valueOf( tableModel.getValueAt(i, 3).toString()) );
            priceNameList.get(i).setDel((byte)1);
        }
        
        new PriceNameDAO().save(priceNameList);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tableModel.addRow(new Object[]{"", "Новый тип цен", getNameFromId( Long.valueOf(tableModel.getValueAt(0, 0).toString()) ).getName(), "0"});
        priceName = new PriceName();
        priceName.setName("Новый тип цен");
        priceName.setPrevious(Long.valueOf(tableModel.getValueAt(0, 0).toString()));
        priceName.setPercent(0);
        priceNameList.add(priceName);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
        hide();
    }//GEN-LAST:event_formInternalFrameIconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
