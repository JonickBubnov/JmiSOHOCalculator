/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocReceipt;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocDeduct;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocInvoice;
import ru.jmirazors.jmiCalculator.MainFrame;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.OrganizationSelectDialog;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Inventory;
import ru.jmirazors.jmiСalculator.entity.InventoryProduct;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author Jonick
 */
 

public final class DocInventory extends javax.swing.JInternalFrame implements DocumentImpl{

    /**
     * Creates new form DocInventory
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. инвент. |");    
    
    String frameTitle = "Инвентаризация товаров";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static ArrayList<InventoryProduct> products = new ArrayList<>();
    InventoryProduct inventoryProduct;
    Product product;
    Inventory docInventory;
    DocumentDAO documentDAO; 
    
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        
        @Override
        public Component getTableCellRendererComponent (JTable table, Object val, 
                boolean isSelected, boolean hasFocus, int row, int col) {
                
                Color DARK_GREEN = new Color(0, 153, 0);
            
                String str = val.toString();
                setForeground(DARK_GREEN);
                if (Float.parseFloat(str) == 0) 
                    setForeground(Color.GRAY);
                if (Float.parseFloat(str) < 0)
                    setForeground(Color.RED);
                setHorizontalAlignment(SwingConstants.RIGHT);
                setText(str);
                return this; 
        }
    };    
    // модель таблицы
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                if (col == 5)
                    return true;
                return false; 
            }
    };    
    
    public DocInventory() {
        
        initTableColumns();
        docInventory = new Inventory();        
        products.clear();
        initComponents();
        initVisualComponents(); 
        
        documentDAO = new DocumentDAO(); 
        docInit();
        repaintDocument();
    }
     
    
    public DocInventory(String id) { 
        
        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
            docInventory = (Inventory)documentDAO.getDocument(id.trim(), Inventory.class);            
        } catch (Exception ex) {
            Logger.getLogger(DocInventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        initVisualComponents();
        getInventoryProducts(docInventory);
        repaintDocument();                
    }    
    // ************ имплементированные методы документа ********************
    // закрытие документа
     @Override
     public void dispose() {
          super.dispose();
          documentDAO.ev(docInventory);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocInventoryFrameOpen(false);
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
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jDateChooser1.setEnabled(false);
        jTextField2.setEnabled(false);
        jTable1.setEnabled(false);
        jComboBox1.setEnabled(false);
    }
    // инициализировать таблицу
    @Override
    public void initTableColumns() {
        tableModel.addColumn("№");
        tableModel.addColumn("Артикул");
        tableModel.addColumn("Наименование");        
        tableModel.addColumn("Ед.изм.");
        tableModel.addColumn("Кол.скл");        
        tableModel.addColumn("Кол.факт");
        tableModel.addColumn("Разница");        
    }

    @Override
    public void initVisualComponents() {
        this.tb = MainFrame.jToolBar2;
          dockButton.setFocusPainted(false);          
          dockButton.addActionListener((ActionEvent evt) -> {
              toggleState();
        }); 
        tb.add(dockButton);  
        
        jTable1.getColumnModel().getColumn(0).setMaxWidth(32);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(170);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(70); 
        
        jTable1.getColumnModel().getColumn(6).setCellRenderer(cellRenderer);
        
        getStorageList();
        
        tableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 0 && e.getColumn() != 6 && e.getColumn() != 4) {
                recalculateDocument();
            }
        });        
    }
    // Инициализировать новый документ
    @Override
    public void docInit() {
        docInventory.setDocuments(documentDAO.getDocumentType(7L));
        docInventory.setOrganization(docInventory.getSessionOrganization());        
        docInventory.setUsr(docInventory.getSessionUser());                
        docInventory.setIndate(new Date());
        docInventory.setStatus(documentDAO.getStatus(1L));        
        docInventory.setStorage(getSelectedStorage());
        docInventory.setDescr("");
    }

    @Override
    public void repaintDocument() {
        this.setTitle(frameTitle + " ["+docInventory.getStatus().getName()+"]");
        if (docInventory.getId() != 0)
            jLabel2.setText(""+docInventory.getId());        
        jDateChooser1.setDate(docInventory.getIndate()); 
        jTextField1.setText(docInventory.getOrganization().getName());
        jLabel13.setText(docInventory.getUsr().getName());
        jTextField2.setText(docInventory.getDescr());
        jComboBox1.setSelectedItem(docInventory.getStorage().getName());
        if (docInventory.getStatus().getId() == 2 || docInventory.getStatus().getId() == 3) {
            closeButtons();
        }        
    }
    // пересчитать таблицу
    void recalculateDocument() {
        float diff = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {            
            diff = Float.parseFloat(jTable1.getValueAt(i, 5).toString().replace(",", ".")) -
                    Float.parseFloat(jTable1.getValueAt(i, 4).toString());
            products.get(i).setCount(Float.valueOf(jTable1.getValueAt(i, 5).toString().replace(",", ".")));
            tableModel.setValueAt(i+1, i, 0);
            tableModel.setValueAt(diff, i, 6);
        }
    }
    // получить товары из документа и заполнить таблицу
    void getInventoryProducts(Inventory inventory) {
        products.clear();
            if (!inventory.getInventoryProducts().isEmpty())
                products.addAll(inventory.getInventoryProducts());                
            
            updateProductsTable();
    }    
    // обновить таблицу с товарами
    void updateProductsTable() {
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);
        int index = 0; 
        try {
            Iterator it = products.iterator();
            while (it.hasNext()) {
                inventoryProduct = (InventoryProduct)it.next();                
                product = inventoryProduct.getProduct();
                index++;
                tableModel.addRow(new Object[]{index, product.getArticul(), product.getName(), product.getUnit().getName()==null?"":product.getUnit().getName(),
                inventoryProduct.getOnstock(), inventoryProduct.getCount(), 0});
            } 
        } catch (Exception e){System.out.println("ERROR! " + e);} 
        tableModel.fireTableDataChanged();
        recalculateDocument();        
    }    
    // установить склады
    void getStorageList() {        
        jComboBox1.removeAllItems();                
        try {            
            List storages = new StorageDAO().list('e');
            Iterator it = storages.iterator();
            while (it.hasNext()) {
                Storage storage = (Storage)it.next();
                jComboBox1.addItem(storage.getName());                
            }
        } catch (Exception e) {System.out.print(e);}   
    }
    
    // получить выбранный склад
    public Storage getSelectedStorage() {
        Storage storage;
        try {
            storage = new StorageDAO().findStorage(jComboBox1.getSelectedItem().toString());
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return storage;
    }    
    @Override
    public void saveDocument(long status) {
        try {
            docInventory.setIndate(jDateChooser1.getDate());
            docInventory.setDescr(jTextField2.getText());
            docInventory.setStorage(getSelectedStorage());
            docInventory.setUsr(docInventory.getSessionUser());
            docInventory.setOrganization(docInventory.getOrganization());
            docInventory.setStatus(documentDAO.getStatus(status));
            
            for (int i = 0; i < products.size(); i++) {
                products.get(i).setInventory(docInventory);
            }
            
            docInventory.setInventoryProducts(products);
            
            documentDAO.updateDocument(docInventory);                
            
            if (status == 2)
                executeDocument();
            
            repaintDocument();
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(DocInventory.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, 
//                    "Не удалось сохранить документ. \n "+ex, "Ошибка", JOptionPane.ERROR_MESSAGE); 
        }
    }

    @Override
    public void executeDocument() {
        closeButtons();
    }    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jMenuItem1.setText("Оприходование товаров");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Списание товаров");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setClosable(true);
        setIconifiable(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/inventory.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(840, 460));
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

        jLabel12.setText("Пользователь");

        jLabel13.setText("___");

        jLabel14.setText("Примечание");

        jButton8.setText("Закрыть");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(94, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/trash.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/documentadd.png"))); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jLabel1.setText("№");

        jLabel2.setText("____");

        jLabel3.setText("Дата");

        jLabel4.setText("Организация");

        jTextField1.setEditable(false);
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jLabel5.setText("Склад");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 18, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        docInventory.showSubordins();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ProductToInventoryDialog pdtcd = new ProductToInventoryDialog(null, true, getSelectedStorage(), products);
        pdtcd.setLocationRelativeTo(this);
        pdtcd.setVisible(true);
        
        updateProductsTable();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == 2) {
            try {
                docInventory.setStorage(getSelectedStorage());
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = jTable1.getSelectedRow();
            if (docInventory.getInventoryProducts() != null)
                docInventory.getInventoryProducts().remove((InventoryProduct)products.get(index));            
            products.remove(index);
            tableModel.removeRow(index);

            updateProductsTable();
        }  
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(), products, null, "inventory");
            pdtcd.setLocationRelativeTo(this);
            pdtcd.setVisible(true);
        
            updateProductsTable();            
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        OrganizationSelectDialog osd = new OrganizationSelectDialog(null, true, docInventory);
        osd.setLocationRelativeTo(this);
        osd.setVisible(true);
        //jTextField1.setText(docInvoice.getOrganization().getName());
        jTextField1.setText(docInventory.getOrganization().getName());
        recalculateDocument();
    }//GEN-LAST:event_jTextField1MouseClicked

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        saveDocument(1L);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jPopupMenu1.show(jButton6, jButton6.getWidth()/2, jButton6.getHeight()/2);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Оприходование
        if (docInventory.getId() != 0) {
            List<DocumentProduct> dp = new ArrayList<>();
            DocumentProduct p;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getCount()-products.get(i).getOnstock() > 0) {
                    p = new DocumentProduct();
                        p.setCount(products.get(i).getCount()-products.get(i).getOnstock());
                        p.setProduct(products.get(i).getProduct());
                        p.setCost(0);
                    dp.add(p);
                    }
            }
            
            if (!MainFrame.ifManager.isDocReceiptFrameOpen()) {
                MainFrame.ifManager.showFrame(new DocReceipt(docInventory, dp), false);
                MainFrame.ifManager.setDocReceiptFrameOpen(true);
            }  
        } else
                JOptionPane.showMessageDialog(null, 
                    "Сначала сохраните документ.", "Ошибка", JOptionPane.ERROR_MESSAGE);        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // списание
        // добавим только товары которых излишек на складе
        if (docInventory.getId() != 0) {
            List<DocumentProduct> dp = new ArrayList<>();
            DocumentProduct p;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getCount()-products.get(i).getOnstock() < 0) {
                    p = new DocumentProduct();
                        p.setCount(products.get(i).getCount()-products.get(i).getOnstock());
                        p.setProduct(products.get(i).getProduct());
                        p.setCost(0);
                    dp.add(p);
                    }
            }
            
            if (!MainFrame.ifManager.isDocDeductFrameOpen()) {
                MainFrame.ifManager.showFrame(new DocDeduct(docInventory, dp), false);
                MainFrame.ifManager.setDocDeductFrameOpen(true);
            } 
        } else
            JOptionPane.showMessageDialog(null, 
                    "Сначала сохраните документ.", "Ошибка", JOptionPane.ERROR_MESSAGE);          
    }//GEN-LAST:event_jMenuItem2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

}
