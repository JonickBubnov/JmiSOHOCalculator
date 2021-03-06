/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

/**
 *
 * @author User
 */


import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import ru.jmirazors.jmiCalculator.beans.ColorTablesRenderer;
import ru.jmirazors.jmiСalculator.DAO.UserDAO;
import ru.jmirazors.jmiСalculator.entity.User;

public class UsersIf extends javax.swing.JInternalFrame {
    
    JToolBar tb;
    JButton dockButton = new JButton("Пользователи");
    
    ColorTablesRenderer colorRenderer = new ColorTablesRenderer();
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                return false; 
            }
    };    

    /**
     * Creates new form UsersIf
     * @param toolBar
     */
    public UsersIf(JToolBar toolBar) {
        this.tb = toolBar;
          dockButton.setFocusPainted(false);
          dockButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent evt) {
                    toggleState();
               }
          }); 
        tb.add(dockButton);        
        
        tableModel.addColumn("№");
        tableModel.addColumn("Имя");
        tableModel.addColumn("Подразделение");
        tableModel.addColumn("Права");
        
        initComponents();
        
        jTable1.getColumnModel().getColumn(0).setMinWidth(24);
        jTable1.getColumnModel().getColumn(1).setMinWidth(180);
        jTable1.getColumnModel().getColumn(2).setMinWidth(150);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(24);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(180);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
        jTable1.setDefaultRenderer(Object.class, colorRenderer);

        
        getUsers();        
    }
    
    // ************************************************************
     @Override
     public void dispose() {
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setUsersFrameOpen(false);
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
    // ***********************************************************     
    
    void getUsers() {
            for (int i=tableModel.getRowCount(); i > 0; i--) {
                tableModel.removeRow(i-1);
            }
            tableModel.setRowCount(0);
            
            
        try {
            List<User> users;
            if (!jToggleButton1.isSelected())
                users = new UserDAO().list("FROM User WHERE del=1");
            else
                users = new UserDAO().list("FROM User");
            for (int i = 0; i < users.size(); i++) {
                tableModel.addRow(new Object[]{users.get(i).getId(), users.get(i).getName(),
                    users.get(i).getDepartment().getName(), users.get(i).getPriv().getName()});
            }
        } catch (Exception ex) {
            Logger.getLogger(UsersIf.class.getName()).log(Level.SEVERE, null, ex);
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

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Пользователи");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/teamwork.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(460, 200));
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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-contacts.png"))); // NOI18N
        jButton1.setToolTipText("Новый пользователь");
        jButton1.setFocusPainted(false);
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
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        jButton2.setToolTipText("Обновить");
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        UserAddDialog userAddDialog = new UserAddDialog(null, true, null);
        userAddDialog.setLocationRelativeTo(this);
        userAddDialog.setVisible(true);
        getUsers();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            User user = null;
            try {
                user = new UserDAO().findUser((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
            } catch (SQLException ex) {
                Logger.getLogger(UsersIf.class.getName()).log(Level.SEVERE, null, ex);
            }
            UserAddDialog userAddDialog = new UserAddDialog(null, true, user);
            userAddDialog.setLocationRelativeTo(this);
            userAddDialog.setVisible(true);
            getUsers();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        getUsers();
    }//GEN-LAST:event_jToggleButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
