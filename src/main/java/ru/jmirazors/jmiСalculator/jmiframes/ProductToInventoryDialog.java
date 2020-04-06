/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import ru.jmirazors.jmiСalculator.DAO.GroupDAO;
import ru.jmirazors.jmiСalculator.DAO.ProductDAO;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.InventoryProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.SetPriceProduct;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;
import static ru.jmirazors.jmiСalculator.jmiframes.ProductToSetPriceDialog.cartProducts;

/**
 *
 * @author User
 */
public class ProductToInventoryDialog extends javax.swing.JDialog {
    
    Product product;
    Group group;
    Storage storage;
    List<Stock> stocks;
    PriceName priceName;
    TableRowSorter<TableModel> rowSorter;

    static ArrayList<InventoryProduct> cartProducts = new ArrayList<>();
    static InventoryProduct cartProduct;
    //String strDocument;

    long groupId = 1;
    MutableTreeNode root = new DefaultMutableTreeNode("Каталог");
    MutableTreeNode leaf;
    
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
    
    DefaultListModel listModel = new DefaultListModel();   
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            { return false; }
    };
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
    DefaultTableCellRenderer cellRenderer1 = new DefaultTableCellRenderer() {
        JLabel label = new JLabel();
        ImageIcon check = new ImageIcon(getClass().getResource("/images/check.png"));
        ImageIcon empty = new ImageIcon(getClass().getResource("/images/empty.png"));
        
        @Override
        public Component getTableCellRendererComponent (JTable table, Object val, 
                boolean isSelected, boolean hasFocus, int row, int col) {
            if (val == "0")
                label.setIcon(empty);
            if (val == "1")
                label.setIcon(check);                                       
            
            return label;
        }
    };     
         

    /**
     * Creates new form ProductToInventoryDialog
     */
    public ProductToInventoryDialog(java.awt.Frame parent, boolean modal, Storage storage, ArrayList<InventoryProduct> products) {
        super(parent, modal);
        
        rowSorter = new TableRowSorter(tableModel);
        
        tableModel.addColumn("№");
        tableModel.addColumn("Артикул");
        tableModel.addColumn("Наименование");
        tableModel.addColumn("На складе");
        tableModel.addColumn("Ед.");
        tableModel.addColumn(" ");        
        
        initComponents();
        this.storage = storage;
        jLabel2.setText(storage.getName());
        cartProducts = products;
        
        // ***********************************************************
        jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(24);
        
        jTable1.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(cellRenderer1);
        jTable1.setRowSorter(rowSorter);

        // *************************************************************
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
                    } else { groupId = 1; group = new GroupDAO().getById(groupId);}
                } catch (Exception ex) {
                    Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                }
                getProductList();
//               jLabel4.setText(group.getName());
            }
        });        
        
        //************************************************************** 
        getProductList();
        getGroupList();        
    }
    
    public String testEntry(Product iProd) {
        for (int i = 0; i < cartProducts.size(); i++) {            
            if (cartProducts.get(i).getProduct().getId() == iProd.getId())
                return "1";
        }
        return "0";
    }    
    void removeProductFromList(Product rProduct) {
        int index = -1;
        for (int i = 0; i < cartProducts.size(); i++) {
            if (cartProducts.get(i).getProduct().getId() == rProduct.getId())
                index = i;
        }
        if (index >=0)
               cartProducts.remove(index);
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
        
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);        
        
        try {
            List products = new ProductDAO().list("FROM Product WHERE del=1 AND parent="+groupId);//ORDER BY parent");
            Iterator it = products.iterator();
            while (it.hasNext()) {
                product = (Product)it.next();
                
                float count = 0;
                stocks = product.getStock();        
                for (int i =0; i < stocks.size(); i++) {
                    if (storage.getId() == stocks.get(i).getStorage().getId())
                        count += stocks.get(i).getCount();
                }
                
                
                tableModel.addRow(new Object[]{product.getId(), 
                    product.getArticul(),  product.getName(), count, 
                    product.getUnit()!=null?product.getUnit().getName():"",
                    testEntry(product)});                 
            }
            tableModel.fireTableDataChanged();
            jLabel4.setText(group.getName());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Подбор товаров");
        setModal(true);
        setResizable(false);

        jTree1.setModel(treeModel);
        jTree1.setPreferredSize(new java.awt.Dimension(150, 72));
        jScrollPane1.setViewportView(jTree1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 54));

        jLabel1.setText("Склад");

        jLabel2.setText("____");

        jLabel3.setText("Группа");

        jLabel4.setText("____");

        jCheckBox1.setText("Выделить все");
        jCheckBox1.setFocusPainted(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jCheckBox1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 50));

        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Добавить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(457, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        String sel = tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 5).toString();
        try {
            product = new ProductDAO().getProduct(tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 0).toString());
        } catch (Exception ex) {
            Logger.getLogger(ProductToSetPriceDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (sel.equals("1")) {
            tableModel.setValueAt("0", jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 5); 
            removeProductFromList(product);
        }
        else {
            tableModel.setValueAt("1", jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 5);
            cartProduct = new InventoryProduct();
            cartProduct.setProduct_id(product.getId());
            cartProduct.setProduct(product);
            cartProduct.setOnstock(Float.valueOf(tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow()), 3).toString()));
            cartProduct.setCount(0);
            cartProducts.add(cartProduct);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        for (int i = 0; i < cartProducts.size(); i++)
            DocInventory.products = (ArrayList<InventoryProduct>)cartProducts.clone();
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        String sel;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            sel  = tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(i)  , 5).toString();
        if (sel.equals("1")) {
            tableModel.setValueAt("0", jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 5); 
            removeProductFromList(product);
        }
        else {
            tableModel.setValueAt("1", jTable1.getRowSorter().convertRowIndexToModel(i)  , 5);
            try {
                product = new ProductDAO().getProduct(tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(i)  , 0).toString());
            } catch (Exception ex) {
                Logger.getLogger(ProductToSetPriceDialog.class.getName()).log(Level.SEVERE, null, ex);
            }            
            cartProduct = new InventoryProduct();
            cartProduct.setProduct_id(product.getId());
            cartProduct.setProduct(product);
            cartProduct.setOnstock(Float.valueOf(tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(i), 3).toString()));
            cartProduct.setCount(0);
            cartProducts.add(cartProduct);
        }            
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
