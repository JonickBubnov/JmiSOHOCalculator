/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import ru.jmirazors.jmiCalculator.beans.ColorTablesRenderer;
import ru.jmirazors.jmiСalculator.DAO.ContragentDAO;
import ru.jmirazors.jmiСalculator.entity.Contacts;
import ru.jmirazors.jmiСalculator.entity.Contragent;
import ru.jmirazors.jmiСalculator.entity.ContragentStatus;

/**
 *
 * @author User
 */
public class ContragentsIf extends javax.swing.JInternalFrame {

    /**
     * Creates new form ContragentsIf
     */
    JToolBar tb;
    JButton dockButton = new JButton("Контрагенты");    
    
    Contragent contragent;
    ContragentStatus status;
    List<Contacts> contacts;
    ImageIcon star = new ImageIcon(getClass().getResource("/images/star.png"));
    ImageIcon starEmpty = new ImageIcon(getClass().getResource("/images/star_empty.png"));
    static Object newRow[]; 
    List<JLabel> stars;
    ColorTablesRenderer colorRenderer = new ColorTablesRenderer();
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            { return false; }
    };
    DefaultTableModel tableModelContact = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        JLabel label = new JLabel();
        ImageIcon iconNormal = new ImageIcon(getClass().getResource("/images/ellipsis-circle.png"));
        ImageIcon iconDel = new ImageIcon(getClass().getResource("/images/cancel-button.png"));
        
        @Override
        public Component getTableCellRendererComponent (JTable table, Object val, 
                boolean isSelected, boolean hasFocus, int row, int col) {
            //label.setText((String)val);
            if ((byte)val == 1)
                label.setIcon(iconNormal);
            if ((byte)val == 0)
                label.setIcon(iconDel);
            return label;
        }
    };
 
    public ContragentsIf(JToolBar toolBar) {
        this.tb = toolBar;
          dockButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent evt) {
                    toggleState();
               }
          }); 
        tb.add(dockButton);        
        
        tableModel.addColumn("");
        tableModel.addColumn("№");
        tableModel.addColumn("Название");
        tableModel.addColumn("Телефон");
        tableModel.addColumn("e-mail");
        tableModel.addColumn("www");
        tableModel.addColumn("Адрес");
        
        tableModelContact.addColumn("№");
        tableModelContact.addColumn("Имя");
        tableModelContact.addColumn("Телефон");
        tableModelContact.addColumn("e-mail");
                
        initComponents();
        
        stars = new ArrayList();
        stars.add(jLabel15);
        stars.add(jLabel2);
        stars.add(jLabel3);
        stars.add(jLabel4);
        stars.add(jLabel5);
        
//        stars.get(0).setCursor(new Cursor(Cursor.HAND_CURSOR));
//        stars.get(1).setCursor(new Cursor(Cursor.HAND_CURSOR));
//        stars.get(2).setCursor(new Cursor(Cursor.HAND_CURSOR));
//        stars.get(3).setCursor(new Cursor(Cursor.HAND_CURSOR));
//        stars.get(4).setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        jTable1.getColumnModel().getColumn(0).setMaxWidth(17);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(32);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        
        jTable2.getColumnModel().getColumn(0).setMaxWidth(32);
        
        jTable1.setDefaultRenderer(Object.class, colorRenderer);
        jTable2.setDefaultRenderer(Object.class, colorRenderer);
        
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable1.getSelectedRow()>=0) {
                    try {
                        contragent = new ContragentDAO().getById(tableModel.getValueAt(jTable1.getSelectedRow(), 1).toString());
                        contacts = contragent.getContacts();
                        int rating = contragent.getRating();
                        jLabel13.setText(contragent.getCstatus().getName());
                        
                        jLabel15.setIcon(starEmpty);
                        jLabel2.setIcon(starEmpty);
                        jLabel3.setIcon(starEmpty);
                        jLabel4.setIcon(starEmpty);
                        jLabel5.setIcon(starEmpty);
                        for (int i = 0; i < rating; i++)
                            stars.get(i).setIcon(star);
                        
//                        status = contragent.getcStatus();
                        updateContactList(contacts);
//                        jLabel13.setText(status.getName());
                    } catch (Exception ex) {
                        Logger.getLogger(ContragentsIf.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
        
        getContragentList();
        
    }
     @Override
     public void dispose() {
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setContragentsFrameOpen(false);
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
    
        public void updateContactList(List<Contacts> contacts) {
            for (int i=tableModelContact.getRowCount(); i > 0; i--) {
                tableModelContact.removeRow(i-1);
            }
            //tableModel.setRowCount(0);
            if (contacts.size() > 0) {
            for (int i = 0; i < contacts.size(); i++) {
                tableModelContact.addRow(new Object[]{contacts.get(i).getId(), contacts.get(i).getName(), 
                    contacts.get(i).getPhone(), contacts.get(i).getEmail()});
            }
            }
        }
        
        public void getContragentList(){
            
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }    
        //tableModel.setRowCount(0);
        
        try {
            List contragents = new ContragentDAO().list();
            Iterator it = contragents.iterator();
            while (it.hasNext()) {
                Contragent ctg = (Contragent)it.next(); 
                tableModel.addRow(new Object[]{ctg.getDel(), ctg.getId(), ctg.getName(),
                    ctg.getPhone(), ctg.getEmail(), ctg.getWww(), ctg.getAddres2()});               
            }
        } catch (Exception e) {System.out.print(e);}        
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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Контрагенты");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/group.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(640, 480));
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
        jButton1.setToolTipText("Добавить");
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
        jToggleButton1.setToolTipText("Показать удаленные");
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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-contacts.png"))); // NOI18N
        jButton2.setToolTipText("Добавить контакт");
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jLabel1.setText("Поиск ");
        jToolBar1.add(jLabel1);
        jToolBar1.add(jTextField1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jTable1.setSelectionBackground(new java.awt.Color(204, 204, 255));
        jTable1.setShowVerticalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(598, 150));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 150));

        jLabel13.setText("_____");

        jLabel15.setPreferredSize(new java.awt.Dimension(17, 17));
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        jLabel2.setPreferredSize(new java.awt.Dimension(17, 17));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.setPreferredSize(new java.awt.Dimension(17, 17));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Контакты"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(tableModelContact);
        jScrollPane2.setViewportView(jTable2);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      ContragentAddDialog contragentAddDialog = new ContragentAddDialog(null, true);
      contragentAddDialog.setLocationRelativeTo(this);
      contragentAddDialog.setVisible(true);
      getContragentList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // добавить контакт
        if (jTable1.getSelectedRow() != -1) {
            ContactAddDialog contactAddDialog = new ContactAddDialog(null, true, contragent);
            contactAddDialog.setLocationRelativeTo(this);
            contactAddDialog.setVisible(true);
            
            tableModelContact.addRow(newRow);
            //contacts = contragent.getContacts();
            //updateContactList(contacts);
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            ContragentAddDialog contragentAddDialog = new ContragentAddDialog(null, true, contragent);
            contragentAddDialog.setLocationRelativeTo(this);
            contragentAddDialog.setVisible(true);
            getContragentList();            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        // одна звезда
//        contragent.setRating(1);
//        jLabel15.setIcon(starEmpty);
//        jLabel2.setIcon(starEmpty);
//        jLabel3.setIcon(starEmpty);
//        jLabel4.setIcon(starEmpty);
//        jLabel5.setIcon(starEmpty);        
//        for (int i = 0; i < contragent.getRating(); i++)
//            stars.get(i).setIcon(star);
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // две звезды
//        contragent.setRating(2);
//        jLabel15.setIcon(starEmpty);
//        jLabel2.setIcon(starEmpty);
//        jLabel3.setIcon(starEmpty);
//        jLabel4.setIcon(starEmpty);
//        jLabel5.setIcon(starEmpty);        
//        for (int i = 0; i < contragent.getRating(); i++)
//            stars.get(i).setIcon(star);        
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // три звезды
//        contragent.setRating(3);
//        jLabel15.setIcon(starEmpty);
//        jLabel2.setIcon(starEmpty);
//        jLabel3.setIcon(starEmpty);
//        jLabel4.setIcon(starEmpty);
//        jLabel5.setIcon(starEmpty);                
//        for (int i = 0; i < contragent.getRating(); i++)
//            stars.get(i).setIcon(star);        
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // четыре звезды
//        contragent.setRating(4);
//        jLabel15.setIcon(starEmpty);
//        jLabel2.setIcon(starEmpty);
//        jLabel3.setIcon(starEmpty);
//        jLabel4.setIcon(starEmpty);
//        jLabel5.setIcon(starEmpty);                
//        for (int i = 0; i < contragent.getRating(); i++)
//            stars.get(i).setIcon(star);        
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // пять звезд
//        contragent.setRating(5);
//        jLabel15.setIcon(starEmpty);
//        jLabel2.setIcon(starEmpty);
//        jLabel3.setIcon(starEmpty);
//        jLabel4.setIcon(starEmpty);
//        jLabel5.setIcon(starEmpty);                
//        for (int i = 0; i < contragent.getRating(); i++)
//            stars.get(i).setIcon(star);        
    }//GEN-LAST:event_jLabel5MouseClicked

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
        hide();
    }//GEN-LAST:event_formInternalFrameIconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
