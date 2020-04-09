/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import java.awt.Color;
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
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.GroupDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.ProductDAO;
import ru.jmirazors.jmiСalculator.DAO.StockDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Sale;
import ru.jmirazors.jmiСalculator.entity.SaleProduct;
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author User
 */
public class DocSale extends javax.swing.JInternalFrame implements DocumentImpl {

    /**
     * Creates new form DocSale
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. Продажа |");    
    
    String frameTitle = "Розничная продажа";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    ArrayList<SaleProduct> products = new ArrayList<>();
    SaleProduct saleProduct;
    Product product;
    Sale docSale;
    Group group;
    int groupId = 1;
    StockDAO stockDAO;
    
    static float pCount = 1;
    static float pCost = 0;
    static boolean add = false;
    
    
    DocumentDAO documentDAO;
    ProductDAO productDAO;
    
    ProductToSaleCountDialog ptscd;
    
    // модели таблицы
    DefaultTableModel tableModelDocument = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                if (col == 3)
                    return true;
                return false; 
            }
    };  
    DefaultTableModel tableModelGroup = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                return false; 
            }
    };  
    DefaultTableModel tableModelProduct = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                return false; 
            }
    };    
    
    public DocSale() {
        initTableColumns();
        docSale = new Sale();        
        products.clear();
        initComponents();
        initVisualComponents(); 
               
        documentDAO = new DocumentDAO();
        productDAO = new ProductDAO();
        stockDAO = new StockDAO();
        docInit();
        repaintDocument();
    }
    
    // ************ имплементированные методы документа ********************
    // закрытие документа
     @Override
     public void dispose() {
          super.dispose();
          documentDAO.ev(docSale);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocSaleFrameOpen(false);
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
        
    }
    // инициализировать таблицу
    @Override
    public void initTableColumns() {
        tableModelDocument.addColumn("№");
        tableModelDocument.addColumn("Артикул");
        tableModelDocument.addColumn("Наименование");
        tableModelDocument.addColumn("Кол-во");
        tableModelDocument.addColumn("Ед.изм.");
        tableModelDocument.addColumn("Цена");
        tableModelDocument.addColumn("Сумма");
        
        tableModelGroup.addColumn("№");
        tableModelGroup.addColumn("Наименование");
        
        tableModelProduct.addColumn("№");
        tableModelProduct.addColumn("Артикул");
        tableModelProduct.addColumn("Наименование");
    }

    // инициализировать визуальные компоненты
    @Override
    public void initVisualComponents() {
        this.tb = MainFrame.jToolBar2;
          dockButton.setFocusPainted(false);          
          dockButton.addActionListener((ActionEvent evt) -> {
              toggleState();
        }); 
        tb.add(dockButton); 

        jTable2.getColumnModel().getColumn(0).setMaxWidth(32);
        jTable2.getColumnModel().getColumn(1).setMaxWidth(150);
        jTable2.getColumnModel().getColumn(3).setMaxWidth(50);
        jTable2.getColumnModel().getColumn(4).setMaxWidth(60);
        jTable2.getColumnModel().getColumn(5).setMaxWidth(70);
        jTable2.getColumnModel().getColumn(6).setMaxWidth(70);
        
        jTable3.getColumnModel().getColumn(0).setMaxWidth(32);
        
        jTable1.getColumnModel().getColumn(0).setMaxWidth(32);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(150);
        
        jFormattedTextField1.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                float total = docSale.getTotal() - 
                        docSale.getTotal() * 
                        Float.parseFloat(jFormattedTextField1.getText().replace(",", "."))/100;                
                jLabel8.setText(String.format("%.2f", total));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });        

        getStorageList();
        getPriceNameList();
        getGroupList();
        getProductsFromSelectedGroup(groupId);
        
    }

    @Override
    public void docInit() {
        docSale.setDocuments(documentDAO.getDocumentType(12L));
        docSale.setOrganization(docSale.getSessionOrganization());        
        docSale.setUsr(docSale.getSessionUser());
        docSale.setContragent(MainFrame.sessionParams.getParam().getContragent());
        docSale.setWeight(0);
        docSale.setDiscount(0);
        docSale.setIndate(new Date());
        docSale.setTotal(0);
        docSale.setStatus(documentDAO.getStatus(1l));
        docSale.setPriceName(getSelectedPriceName());
        docSale.setStorage(getSelectedStorage());
        docSale.setDescr("");
    }
    
    private void getGroupList() { 
        for (int i=tableModelGroup.getRowCount(); i > 0; i--) {
            tableModelGroup.removeRow(i-1);
        }
        tableModelGroup.setRowCount(0);     
        groupId = 1;
        try {
            List groups = new GroupDAO().list("FROM Group WHERE del=1");
            Iterator it = groups.iterator();
            while (it.hasNext()) {
                group = (Group)it.next();
                
                tableModelGroup.addRow(new Object[]{group.getId(), group.getName()});
            }
            tableModelGroup.fireTableDataChanged();
        } catch (Exception ex) {
            Logger.getLogger(ProductsIf.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void getProductsFromSelectedGroup(int id) {
        for (int i=tableModelProduct.getRowCount(); i > 0; i--) {
            tableModelProduct.removeRow(i-1);
        }
        tableModelProduct.setRowCount(0);        
        
        try {
            List products = new ProductDAO().list("FROM Product WHERE del=1 AND parent="+id);//ORDER BY parent");
            Iterator it = products.iterator();
            while (it.hasNext()) {
                product = (Product)it.next();
                
                
                tableModelProduct.addRow(new Object[]{product.getId(), 
                    product.getArticul(),  product.getName()});                 
            }
            tableModelProduct.fireTableDataChanged();
        } catch (Exception e) {System.out.print(e);}          
    }

    @Override
    public void repaintDocument() {
        this.setTitle(frameTitle + " ["+docSale.getStatus().getName()+"]");
        if (docSale.getId() != 0)
            jLabel2.setText(""+docSale.getId());  
        jLabel4.setText(format.format(new Date()));
        jLabel19.setText(String.format("%.3f", getWeight()));
        jLabel11.setText(MainFrame.sessionParams.getUser().getName());
        jLabel21.setText(MainFrame.sessionParams.getOrganization().getName());
        jComboBox1.setSelectedItem(docSale.getStorage().getName());
        
        jLabel9.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        
        if (docSale.getStatus().getId() == 2 || docSale.getStatus().getId() == 3) {
            closeButtons();
        }
        
        jLabel23.setText("НЕ ОПЛАЧЕН");
        
    }
    // расчитать вес
    Float getWeight() {
        Float weight = 0f;
        for (SaleProduct ip: products) 
            weight += ip.getProduct().getWeight()*ip.getCount();
        docSale.setWeight(weight);
        return weight;
    }
    
    // получить товары из документа и заполнить таблицу
    void getSaleProducts(Sale sale) {
        products.clear();
            if (!sale.getSaleProducts().isEmpty())
                products.addAll(sale.getSaleProducts());                
            
            updateProductsTable();
    }   
    
    // пересчитать таблицу
    void recalculateDocument() {
        Float sum = 0f;
//        Float price;
        Float cost;
        for (int i = 0; i < tableModelDocument.getRowCount(); i++) { 
            cost = Float.parseFloat(jTable2.getValueAt(i, 3).toString()) * Float.parseFloat(jTable2.getValueAt(i, 5).toString());            
            sum += cost;
            tableModelDocument.setValueAt(i+1, i, 0);
            tableModelDocument.setValueAt(cost, i, 6);
        }
        float total = sum-sum*Float.parseFloat(jFormattedTextField1.getText().replace(",", "."))/100;        
        jLabel8.setText(String.format("%.2f", total));

        docSale.setTotal(Float.parseFloat(jLabel8.getText().replace(",", ".")));
        repaintDocument();
    } 
    
    // обновить таблицу с товарами
    void updateProductsTable() {
        for (int i=tableModelDocument.getRowCount(); i > 0; i--) {
            tableModelDocument.removeRow(i-1);
        }
        tableModelDocument.setRowCount(0);
        int index = 0;                        
        
        try {
            Iterator it = products.iterator();
            while (it.hasNext()) {
                saleProduct = (SaleProduct)it.next();                
                product = saleProduct.getProduct();
                index++;
                tableModelDocument.addRow(new Object[]{index, product.getArticul(), product.getName(),                     
                    saleProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), saleProduct.getCost(),                     
                    saleProduct.getCost()*saleProduct.getCount() });               
            }
        } catch (Exception e) {System.out.println("ERROR! " + e);}
        tableModelDocument.fireTableDataChanged();
        recalculateDocument();
    }    
    // установить типы цен
    void getPriceNameList() {
        
        jComboBox2.removeAllItems();
        
        try {
            List priceNames = new PriceNameDAO().list("FROM PriceName WHERE del=1");
            Iterator it = priceNames.iterator();
            while (it.hasNext()) {
                PriceName priceName = (PriceName)it.next();
                jComboBox2.addItem(priceName.getName());
            }
        } catch (Exception e) {System.out.println(e);}
    }
    // получить выбранный тип цены
    public PriceName getSelectedPriceName() {
        PriceName priceName;
        try {
            priceName = new PriceNameDAO().findPriceName(jComboBox2.getSelectedItem().toString());
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return priceName;        
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
                docSale.setIndate(new Date());
                docSale.setDescr("");
                docSale.setDiscount(Integer.parseInt(jFormattedTextField1.getText().replace(",", ".")));
                docSale.setStorage(getSelectedStorage());
                docSale.setPriceName(getSelectedPriceName());
                docSale.setUsr(docSale.getSessionUser());
                docSale.setOrganization(docSale.getOrganization());
                docSale.setTotal(Float.valueOf(jLabel8.getText().replace(",", ".")));
                docSale.setStatus(documentDAO.getStatus(status));

                for (int i = 0; i < products.size(); i++) {                    
                    products.get(i).setSale(docSale);
                }  
            
                docSale.setSaleProducts(products);
            
                documentDAO.updateDocument(docSale);
                
                if (status == 2) {
                    executeDocument();
                    if (!MainFrame.ifManager.isDocPayFrameOpen()) {
                        MainFrame.ifManager.showFrame(new PayIf(docSale, false), false);
                    }
                }
            
                repaintDocument();
            
            } catch (Exception ex) {
                Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex); }      
    }

    @Override
    public void executeDocument() {
       new DocumentCompletionDAO().completion(docSale, products);        
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
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(frameTitle);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cash-register.png"))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 200));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(250, 50));

        jLabel14.setText("Цена");

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("0,00");

        jLabel16.setText("На складе");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setText("0");

        jLabel24.setText("___");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addGap(55, 55, 55)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel24))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setPreferredSize(new java.awt.Dimension(453, 90));

        jTable3.setModel(tableModelGroup);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel5.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(tableModelProduct);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel2.setMinimumSize(new java.awt.Dimension(100, 25));
        jPanel2.setPreferredSize(new java.awt.Dimension(753, 25));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 982, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(tableModelDocument);
        jScrollPane2.setViewportView(jTable2);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel7.setText("Итого");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("0,00");

        jLabel9.setText("___");

        jLabel10.setText("Пользователь");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("____");

        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Сохранить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel12.setText("%");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.setText("0");
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });

        jLabel13.setText("Скидка");

        jButton3.setText("Выполнить");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel18.setText("Вес");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("____");
        jLabel19.setToolTipText("");

        jLabel22.setText("Оплата");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("___");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 28, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(jLabel22)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel6.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel7.setPreferredSize(new java.awt.Dimension(503, 60));

        jLabel1.setText("№");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("____");

        jLabel3.setText("Дата");

        jLabel4.setText("____");

        jLabel5.setText("Склад");

        jComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Тип цен");

        jComboBox2.setPreferredSize(new java.awt.Dimension(200, 20));

        jLabel20.setText("Организация");

        jLabel21.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel21.setText("____");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1, 0, 150, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, 150, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        String id = tableModelGroup.getValueAt(jTable3.getSelectedRow(), 0).toString();
        getProductsFromSelectedGroup(Integer.valueOf(id));
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        String id = tableModelProduct.getValueAt(jTable1.getSelectedRow(), 0).toString();        
        product = productDAO.getProduct(id);
        float price = product.getActualPrice(getSelectedPriceName()).getPrice();
        jLabel15.setText(String.valueOf(price));
        jLabel24.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        float count = stockDAO.findStock(getSelectedStorage(), product).getCount();
        if (count >= 0)
            jLabel17.setForeground(Color.GREEN);
        else jLabel17.setForeground(Color.RED);
        jLabel17.setText(String.valueOf(count));
        if (evt.getClickCount() == 2) {
            ptscd = new ProductToSaleCountDialog(null, true, product, price);
            ptscd.setLocationRelativeTo(this);
            ptscd.setVisible(true);
            
            if (add) {
                saleProduct = new SaleProduct();
                    saleProduct.setProduct(product);
                    saleProduct.setCost(pCost);
                    saleProduct.setCount(pCount);
                    saleProduct.setDiscount(0);
                products.add(saleProduct);
                updateProductsTable();
                add = false;
            }            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        saveDocument(1L);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        saveDocument(2L);
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables


}
