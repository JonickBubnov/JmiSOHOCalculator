/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocTransfer;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocReceipt;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocOffer;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocDeduct;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocArrival;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocInvoice;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
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
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.ProductDAO;
import ru.jmirazors.jmiСalculator.entity.ArrivalProduct;
import ru.jmirazors.jmiСalculator.entity.DeductProduct;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.InvoiceProduct;
import ru.jmirazors.jmiСalculator.entity.OfferProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.ReceiptProduct;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.TransferProduct;

/**
 *
 * @author User
 */
public final class ProductToCartDialog extends javax.swing.JDialog {

    /**
     * Creates new form ProductToCartDialog
     */
   
    ProductToCartCountDialog pdaccd;
    Product product;
    Group group;
    Storage storage;
    List<Stock> stocks;
    PriceName priceName;
    TableRowSorter<TableModel> rowSorter;

    static ArrayList<DocumentProduct> cartProducts = new ArrayList<>();
    static DocumentProduct cartProduct;
    String strDocument;
    
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
    
    public ProductToCartDialog(java.awt.Frame parent, boolean modal, Storage storage, ArrayList products, PriceName priceName, String doc) {
        super(parent, modal);       
        
        this.strDocument = doc;
        this.storage = storage;
        if (priceName != null)
            this.priceName = priceName;
        else {
            try {
                this.priceName = new PriceNameDAO().getPriceName(1l);
            } catch (Exception ex) {
                Logger.getLogger(ProductToCartDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }  
        
        //cartProducts.clear();
        cartProducts = products; 

        
        rowSorter = new TableRowSorter(tableModel);
        
        tableModel.addColumn("№");
        tableModel.addColumn("Артикул");
        tableModel.addColumn("Наименование");
        tableModel.addColumn("На складе");
        tableModel.addColumn("Ед.");
        tableModel.addColumn("Цена");
        tableModel.addColumn("Кол.");
        
        initComponents();
        
        jLabel2.setText(this.storage.getName());
        jLabel5.setText(this.priceName.getName());
        
        // ***********************************************************
        jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(150);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(64);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(72);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(64);
        
        
        jTable1.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
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
                    } else { groupId = 1; }
                } catch (Exception ex) {
                    Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
                }
               getProductList();
            }
        });        
        
        //**************************************************************
        
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
    
    public Integer testEntry(DocumentProduct iProd) {
        for (int i = 0; i < cartProducts.size(); i++) {            
            if (cartProducts.get(i).getProduct_id() == iProd.getProduct_id())
                return i;
        }
        return null;
    }
    
    public Float getCount(long id) {
        //System.out.println("ID>"+id);
        for (int i = 0; i < cartProducts.size(); i++)
        {
            //System.out.println("ID>"+cartProducts.get(i).getId()+" P_ID>"+cartProducts.get(i).getProduct().getId());
            if (cartProducts.get(i).getProduct().getId() == id)
                return cartProducts.get(i).getCount();
        }
        return null;
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
                    product.getUnit()!=null?product.getUnit().getName():""
                    , product.getActualPrice(priceName).getPrice(), getCount(product.getId())==null?"":getCount(product.getId())});                 
            }
            tableModel.fireTableDataChanged();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Подбор товара");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(640, 300));
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 65));

        jLabel1.setText("Склад");

        jLabel2.setText("____");

        jLabel3.setText("Поиск");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel4.setText("Тип цен");

        jLabel5.setText("____");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModel);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 40));

        jButton9.setText("Отмена");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Добавить");
        jButton10.setActionCommand("");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(410, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addGap(18, 18, 18)
                .addComponent(jButton9)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel3.setMinimumSize(new java.awt.Dimension(100, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(150, 240));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jTree1.setModel(treeModel);
        jScrollPane2.setViewportView(jTree1);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyPressed

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
            pdaccd = new ProductToCartCountDialog(null, true, product, count, price, strDocument);
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

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:        
        switch (strDocument) {
            case "invoice":
                DocInvoice.products = (ArrayList<InvoiceProduct>)cartProducts.clone();
                break;
//            case "arrival":
//                DocArrival.products = (ArrayList<ArrivalProduct>)cartProducts.clone();
//                break;
            case "offer":
                DocOffer.products = (ArrayList<OfferProduct>)cartProducts.clone();
                break;
            case "transfer":
                DocTransfer.products = (ArrayList<TransferProduct>)cartProducts.clone();
                break;
//            case "deduct":
//                DocDeduct.products = (ArrayList<DeductProduct>)cartProducts.clone();
//                break; 
//            case "receipt":
//                DocReceipt.products = (ArrayList<ReceiptProduct>)cartProducts.clone();
//                break;
        }
        dispose();
    }//GEN-LAST:event_jButton10ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton9;
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
