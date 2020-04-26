/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import ru.jmirazors.jmiCalculator.beans.ColorTablesRenderer;
import ru.jmirazors.jmiСalculator.DAO.GroupDAO;
import ru.jmirazors.jmiСalculator.DAO.ProductDAO;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.Price;
import ru.jmirazors.jmiСalculator.entity.Product;

/**
 *
 * @author User
 */
public class ProductsIf extends javax.swing.JInternalFrame {

    /**
     * Creates new form ProductsIf
     */
    JToolBar tb;
    JButton dockButton = new JButton("Номенклатура");    
    
    //private static final Logger logger = Logger.getLogger("Product");
    Product product;
    Group group;
    ProductDAO productDAO;
    
    StringBuffer queryOption = new StringBuffer("FROM Product WHERE del=1");
    long groupId = 1;
  
    MutableTreeNode root = new DefaultMutableTreeNode("Каталог");
    MutableTreeNode leaf;
    
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
    
    DefaultListModel listModel = new DefaultListModel();
    
    ColorTablesRenderer colorRenderer = new ColorTablesRenderer();
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            { return false; }
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
    DefaultTableCellRenderer cellRenderer_onstock = new DefaultTableCellRenderer() {
        JLabel label = new JLabel();
        ImageIcon iconProduct = new ImageIcon(getClass().getResource("/images/barcode.png"));
        ImageIcon iconService = new ImageIcon(getClass().getResource("/images/service.png"));
        
        @Override
        public Component getTableCellRendererComponent (JTable table, Object val, 
                boolean isSelected, boolean hasFocus, int row, int col) {
            //label.setText((String)val);
                if ((byte)val == 1)
                    label.setIcon(iconProduct);
                if ((byte)val == 0)
                    label.setIcon(iconService);
                return label; 
        }
    };
    
    public ProductsIf(JToolBar toolBar) {
        this.tb = toolBar;
          dockButton.setFocusPainted(false);
          dockButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent evt) {
                    toggleState();
               }
          }); 
        tb.add(dockButton);         
        
        productDAO = new ProductDAO();
        
        tableModel.addColumn("");
        tableModel.addColumn("№");
        tableModel.addColumn("T");
        tableModel.addColumn("Арт.");        
        tableModel.addColumn("Наименование");
        tableModel.addColumn("Описание");
        
        initComponents();         
        
        treeCellRenderer.setLeafIcon(new ImageIcon(getClass().getResource("/images/package-cube-box-for-delivery.png")));
        treeCellRenderer.setClosedIcon(new ImageIcon(getClass().getResource("/images/inbox.png")));
        treeCellRenderer.setOpenIcon(new ImageIcon(getClass().getResource("/images/inbox.png")));
        jTree1.setCellRenderer(treeCellRenderer);
        
        jTree1.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent e) {
               leaf = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
                try {
                    if (leaf != null && leaf.isLeaf()) {
                        group = new GroupDAO().find(leaf.toString());
                        groupId = group.getId();
                    } else { groupId = 1; }
                } catch (Exception ex) {
                    Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                }
               getProductList();
            }
        });
        
        jTable1.getColumnModel().getColumn(0).setMaxWidth(17);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(48);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(120);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(17);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(cellRenderer_onstock);
        
        jTable1.setDefaultRenderer(Object.class, colorRenderer);
        
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable1.getSelectedRow()>=0) {
                    try {
                        product = productDAO.getProduct(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                    } catch (Exception ex) {
                        Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    // *****************************************************************
                    //jLabel3.setText(""+product.getPrice());
                    listModel.removeAllElements();
                    List<Price> price = product.getActualPriceList();                    
                    for (int i = 0; i < price.size(); i++) {
                        listModel.addElement("["+price.get(i).getPricename().getName()+"] " + price.get(i).getPrice()+" руб.");                        
                        //System.out.println(price.get(i).getPricename().getName()+" - " + price.get(i).getPrice());
                        }
                    
                    //******************************************************************
                    jLabel5.setText(""+product.getTotal());
                    jLabel1.setText(product.getUnit().getName());
                    jLabel3.setText(""+product.getWeight());
                    try {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(product.getImage()));
                        if (img != null)
                        jLabel8.setIcon(new ImageIcon(img
                                .getScaledInstance(jLabel8.getWidth()-4, jLabel8.getHeight()-4, Image.SCALE_SMOOTH)) );
                        else
                            jLabel8.setIcon(new ImageIcon( ImageIO.read( getClass().getResource("/images/no-image-icon.png") )
                                    .getScaledInstance(jLabel8.getWidth()-4, jLabel8.getHeight()-4, Image.SCALE_SMOOTH) ));
                    } catch (IOException ex) {
                        Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
        
        getProductList();
        getGroupList();        
    } 
     @Override
     public void dispose() {
          super.dispose();
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setProductsFrameOpen(false);
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
    
    public void getGroupList() {
        
        int cc = treeModel.getChildCount(root)-1;
        for (int i = cc; i >= 0; i--)
            root.remove(i);
        treeModel.nodeStructureChanged(root);      
        
        try {
            List groups = new GroupDAO().list("FROM Group WHERE del=1 AND id>1");
            Iterator it = groups.iterator();
            int index = 0;
            while (it.hasNext()) {
                group = (Group)it.next();
                
                root.insert(new DefaultMutableTreeNode(group.getName()), index);
                index++;
            }
            treeModel.nodeStructureChanged(root);
            jTree1.expandRow(0);
        } catch (Exception ex) {
            Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getProductList() {
        
        if (jToggleButton1.isSelected()) 
            this.queryOption = new StringBuffer("FROM Product WHERE parent="+groupId);
        else
            queryOption = new StringBuffer("FROM Product WHERE del=1 AND parent="+groupId);
        
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);
        
        try {
            List products = new ProductDAO().list(queryOption.toString());
            Iterator it = products.iterator();
            while (it.hasNext()) {
                product = (Product)it.next(); 
                tableModel.addRow(new Object[]{product.getDel(), product.getId(), product.getOnstock(), 
                    product.getArticul(),  product.getName(), product.getDescription()});               
            }
            tableModel.fireTableDataChanged();
        } catch (Exception e) {JOptionPane.showMessageDialog(this, "Не могу получить данные о номенклатуре.", "Ошибка",
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Номенклатура");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/boxes.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 400));
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
        jButton1.setToolTipText("Новый продукт");
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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton2.setToolTipText("Новая группа");
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

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_del.png"))); // NOI18N
        jToggleButton1.setToolTipText("Показать удаленные");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ellipsis-circle.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jPanel1.setPreferredSize(new java.awt.Dimension(620, 150));

        jLabel4.setText("Остаток:");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("____");

        jLabel8.setToolTipText("Thumb");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("___");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Цены"));

        jList1.setModel(listModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setEnabled(false);
        jScrollPane3.setViewportView(jList1);

        jLabel2.setText("Вес:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("____");

        jLabel6.setText("кг.");

        jLabel7.setText("Штрихкод");

        jLabel9.setText("____");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(52, 52, 52))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(tableModel);
        jTable1.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jTable1.setShowVerticalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(200, 322));

        jTree1.setModel(treeModel);
        jTree1.setDoubleBuffered(true);
        jTree1.setMaximumSize(new java.awt.Dimension(300, 300));
        jTree1.setName(""); // NOI18N
        jTree1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTree1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      ProductAddDialog productAddDialog = new ProductAddDialog(null, true, groupId);
      productAddDialog.setLocationRelativeTo(this);
      productAddDialog.setVisible(true);
      getProductList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        getProductList();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            ProductAddDialog productAddDialog;
            try {
                productAddDialog = new ProductAddDialog(null, true, new ProductDAO().getProduct(tableModel.getValueAt(jTable1.getSelectedRow(), 1).toString()));
                productAddDialog.setLocationRelativeTo(this);
                productAddDialog.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
            }
            getProductList();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE && jTable1.getSelectedRow() >= 0) {
            product = null;
            try {
                product = new ProductDAO().getProduct(tableModel.getValueAt(jTable1.getSelectedRow(), 1).toString());
            } catch (Exception ex) {
                Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
            }
            int dialogResult = JOptionPane.showOptionDialog(null, "Удалить товар?", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (dialogResult == 0 && product != null) {
                product.setDel((byte)0);
                try {
                    new ProductDAO().updateProduct(product);
                    getProductList();
                } catch (Exception ex) {
                    Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("NO");
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_M && jTable1.getSelectedRow() >=0) {
                try {
                    product = new ProductDAO().getProduct(tableModel.getValueAt(jTable1.getSelectedRow(), 1).toString());
                } catch (Exception ex) {
                    Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                }
            if (product != null) {
                    MoveProductDialog moveProductDialog = new MoveProductDialog(null, true, product);
                    moveProductDialog.setLocationRelativeTo(this);
                    moveProductDialog.setVisible(true);
                    getProductList();
                }
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        GroupAddDialog groupAddDialog = new GroupAddDialog(null, true);
        groupAddDialog.setLocationRelativeTo(this);
        groupAddDialog.setVisible(true);
        getGroupList();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTree1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            try {
                Group delGroup = new GroupDAO().find(jTree1.getLastSelectedPathComponent().toString());
                List<Product> products = new ProductDAO().list("From Product where parent="+delGroup.getId());
                
                int dialogResult;
                if (products.isEmpty()) {
                    dialogResult = JOptionPane.showOptionDialog(null, "Удалить группу ["+delGroup.getName()+"]?", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                } else
                    dialogResult = JOptionPane.showOptionDialog(null, "Группа не пустая! \n"
                            + "Удаление приведет к удалению всех товаров в группе. \n"
                            + "Удалить группу ["+delGroup.getName()+"]?", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (dialogResult == 0) {
                    delGroup.setDel((byte)0);
                    new GroupDAO().update(delGroup);
                    for (int i = 0; i < products.size(); i++)
                        products.get(i).setDel((byte)0);
                    new ProductDAO().saveAll(products);
                } else
                    System.out.println("NO");
                getGroupList();    
            } catch (Exception ex) {
                Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTree1KeyPressed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
