/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.selectDialogs;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.jmiframes.ProductsIf;

/**
 *
 * @author Jonick
 */
public class ProductAddDialog extends javax.swing.JDialog {

    ProductCountDialog pdaccd;
    
    ArrayList<DocumentProduct> cartProducts = new ArrayList<>();
    Storage storage;
    PriceName priceName;
    Group group;
    long groupId = 1;
    Product product;
    List<Stock> stocks;
    
    static DocumentProduct cartProduct;
    
    TableRowSorter<TableModel> rowSorter;
    
    MutableTreeNode root = new DefaultMutableTreeNode("Каталог");
    MutableTreeNode leaf;
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();

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
    
    
    
    public ProductAddDialog(java.awt.Frame parent, boolean modal, Storage storage, PriceName priceName) {
        super(parent, modal);
        
        rowSorter = new TableRowSorter(tableModel);
        
        tableModel.addColumn("№");
        tableModel.addColumn("Артикул");
        tableModel.addColumn("Наименование");
        tableModel.addColumn("На складе");
        tableModel.addColumn("Ед.");
        tableModel.addColumn("Цена");
        tableModel.addColumn("Кол.");

        this.storage = storage;
        this.priceName = priceName;
        
        
        initComponents();
        
        jLabel4.setText(this.storage.getName());
        jLabel3.setText(this.priceName.getName());

        jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(150);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(64);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(72);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(64);
        
        
        jTable1.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        jTable1.setRowSorter(rowSorter);

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

        getProductList();
        getGroupList();        
    }
    // ************** получить список групп ************************
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
    // ***********   получить товары из группы ****************
    public void getProductList() {
        
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);        
        
        try {
            List products;
            if (!jCheckBox2.isSelected())
                products = new ProductDAO().list("FROM Product WHERE del=1 AND parent="+groupId);
            else
                products = new ProductDAO().list("FROM Product WHERE parent="+groupId);
            
            Iterator it = products.iterator();
            while (it.hasNext()) {
                product = (Product)it.next();
                
                float count = 0;
                stocks = product.getStock();        
                for (int i =0; i < stocks.size(); i++) {
                    if (storage.getId() == stocks.get(i).getStorage().getId())
                        count += stocks.get(i).getCount();
                }
                
                Float pCount = getCount(product.getId());
                
                if (!jCheckBox1.isSelected())                
                    tableModel.addRow(new Object[]{product.getId(), 
                        product.getArticul(),  product.getName(), count, 
                        product.getUnit()!=null?product.getUnit().getName():""
                        , String.format("%.2f", product.getActualPrice(priceName).getPrice()), pCount==null?"":pCount});
                if (jCheckBox1.isSelected() && count > 0)
                    tableModel.addRow(new Object[]{product.getId(), 
                        product.getArticul(),  product.getName(), count, 
                        product.getUnit()!=null?product.getUnit().getName():""
                        , String.format("%.2f", product.getActualPrice(priceName).getPrice()), pCount==null?"":pCount});                    
            }
            tableModel.fireTableDataChanged();
        } catch (Exception e) {System.out.print(e);}  
    }   
    
    public Integer testEntry(DocumentProduct iProd) {
        for (int i = 0; i < cartProducts.size(); i++) {            
            if (cartProducts.get(i).getProduct_id() == iProd.getProduct_id())
                return i;
        }
        return null;
    }

    public Float getCount(long id) {
        for (int i = 0; i < cartProducts.size(); i++)
        {
            if (cartProducts.get(i).getProduct().getId() == id)
                return cartProducts.get(i).getCount();
        }
        return null;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Номенклатура");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(680, 400));
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(642, 70));

        jLabel1.setText("Склад:");

        jLabel2.setText("Тип цен:");

        jTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField1.setPreferredSize(new java.awt.Dimension(73, 20));

        jLabel3.setText("____");

        jLabel4.setText("____");

        jLabel5.setText("Поиск");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 219, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(642, 42));

        jButton1.setText("Закрыть");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new java.awt.Dimension(90, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Добавить");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(90, 24));

        jCheckBox2.setText("Показывать удаленные");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Только наличие");
        jCheckBox1.setActionCommand("Наличие");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox1))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTree1.setModel(treeModel);
        jTree1.setMaximumSize(new java.awt.Dimension(160, 72));
        jTree1.setPreferredSize(new java.awt.Dimension(160, 72));
        jScrollPane1.setViewportView(jTree1);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        jTable1.setModel(tableModel        );
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        getProductList();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        getProductList();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            String id = tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 0).toString();
            String count = tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 6).toString();
            String price = tableModel.getValueAt(jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow())  , 5).toString();
            if (count.equals("")) 
                count = "1";
            try {
                product = new ProductDAO().getProduct(id);
            } catch (Exception ex) {
                Logger.getLogger(ProductToCartDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            pdaccd = new ProductCountDialog(null, true, product, count, price);
            pdaccd.setLocationRelativeTo(this);
            pdaccd.setVisible(true);
            
            if (cartProduct != null) {
                tableModel.setValueAt(cartProduct.getCount(), 
                        jTable1.getRowSorter().convertRowIndexToModel(jTable1.getSelectedRow()), 6);
                Integer listId = testEntry(cartProduct);
                if (listId == null)
                    cartProducts.add(cartProduct);
                else
                    cartProducts.get(listId).setCount(cartProduct.getCount());
            }
            cartProduct = null;
        }        
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
