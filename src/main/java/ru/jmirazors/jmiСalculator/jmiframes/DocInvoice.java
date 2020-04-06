/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import com.sun.glass.events.KeyEvent;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiCalculator.beans.MonthToTextBean;
import ru.jmirazors.jmiCalculator.beans.NumberToTextBean;
import ru.jmirazors.jmiCalculator.beans.ProductBean;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Invoice;
import ru.jmirazors.jmiСalculator.entity.InvoiceProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author User
 */


public final class DocInvoice extends javax.swing.JInternalFrame implements DocumentImpl {

    /**
     * Creates new form DocInvoice
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. заказ |");    
    
    String frameTitle = "Заказ покупателя";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static ArrayList<InvoiceProduct> products = new ArrayList<>();
    InvoiceProduct invoiceProduct;
    Product product;
    Invoice docInvoice;
    DocumentDAO documentDAO;

    boolean firstOpen = true;    
    
    // модель таблицы
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                if (col == 3 || col == 5 || col == 6)
                    return true;
                return false; 
            }
    };
                                          
    public DocInvoice() {
        
        initTableColumns();
        docInvoice = new Invoice();        
        products.clear();
        initComponents();
        initVisualComponents(); 
               
        documentDAO = new DocumentDAO();        
        docInit();
        repaintDocument();
    }
    
    public DocInvoice(String id) { 
        
        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
            docInvoice = (Invoice)documentDAO.getDocument(id.trim(), Invoice.class);            
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        initVisualComponents();
        getInvoiceProducts(docInvoice);
        repaintDocument();                
    }
    
    // ************ имплементированные методы документа ********************
    // закрытие документа
     @Override
     public void dispose() {
          super.dispose();
          documentDAO.ev(docInvoice);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocInvoiceFrameOpen(false);
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
     
    // заблокировать все элементы управления
    @Override
    public void closeButtons() {
            jButton7.setEnabled(false);
            jButton9.setEnabled(false);
            jButton6.setEnabled(false);
            jButton4.setEnabled(false);
            jTable1.setEnabled(false);
            jComboBox1.setEnabled(false);
            jComboBox2.setEnabled(false);
            jTextField2.setEnabled(false);
            jFormattedTextField1.setEnabled(false);
            jDateChooser2.setEnabled(false);
    }
    
    // инициализировать таблицу
    @Override
    public void initTableColumns() {
        tableModel.addColumn("№");
        tableModel.addColumn("Артикул");
        tableModel.addColumn("Наименование");
        tableModel.addColumn("Кол-во");
        tableModel.addColumn("Ед.изм.");
        tableModel.addColumn("Цена");
        tableModel.addColumn("Скидка");
        tableModel.addColumn("Со скидкой");
        tableModel.addColumn("Сумма");
              
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
        
        jTable1.getColumnModel().getColumn(0).setMaxWidth(32);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(170);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(72);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(72);
        jTable1.getColumnModel().getColumn(8).setMaxWidth(72);        
        
        getStorageList();
        getPriceNameList();
        
        jFormattedTextField1.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                float total = Float.parseFloat(jLabel10.getText().replace(",", ".")) - 
                        Float.parseFloat(jLabel10.getText().replace(",", ".")) * 
                        Float.parseFloat(jFormattedTextField1.getText().replace(",", "."))/100;
                float nds = total*docInvoice.getOrganization().getNds()/100;
                jLabel11.setText(String.format("%.2f", total));
                jLabel24.setText(String.format("%.2f", nds));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        
        tableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 7 && e.getColumn() != 8 && e.getColumn() != 0) {
                recalculateDocument();
            }
        });
    }
    
    // Инициализировать новый документ
    @Override
    public void docInit() {                        
        docInvoice.setDocuments(documentDAO.getDocumentType(1L));
        docInvoice.setOrganization(docInvoice.getSessionOrganization());        
        docInvoice.setUsr(docInvoice.getSessionUser());
        docInvoice.setWeight(0);
        docInvoice.setDiscount(0);
        docInvoice.setIndate(new Date());
        docInvoice.setTotal(0);
        docInvoice.setStatus(documentDAO.getStatus(1l));
        docInvoice.setPriceName(getSelectedPriceName());
        docInvoice.setStorage(getSelectedStorage());
        docInvoice.setDescr("");
    }
    
    // заполнить отображение документа
    @Override
    public void repaintDocument() {
        this.setTitle(frameTitle + " ["+docInvoice.getStatus().getName()+"]");
        if (docInvoice.getId() != 0)
            jLabel2.setText(""+docInvoice.getId());
        
        jDateChooser2.setDate(docInvoice.getIndate());
        jTextField1.setText(docInvoice.getOrganization().getName());
        jLabel14.setText(docInvoice.getUsr().getName());
        jFormattedTextField1.setText(String.valueOf(docInvoice.getDiscount()).replace(".", ","));
        jTextField2.setText(docInvoice.getDescr());
        jComboBox1.setSelectedItem(docInvoice.getStorage().getName());
        jComboBox2.setSelectedItem(docInvoice.getPriceName().getName());
        if (docInvoice.getContragent() != null)
            jTextField3.setText(docInvoice.getContragent().getName());
        //recalculateDocument();
        if (docInvoice.getStatus().getId() == 2 || docInvoice.getStatus().getId() == 3) {
            closeButtons();
        }
        firstOpen = false;
    }
    
    // расчитать вес
    Float getWeight() {
        Float weight = 0f;
        for (InvoiceProduct ip: products) 
            weight += ip.getProduct().getWeight()*ip.getCount();
        docInvoice.setWeight(weight);
        return weight;
    }
    
    // пересчитать таблицу
    void recalculateDocument() {
        Float sum = 0f;
        Float price;
        Float cost;
        for (int i = 0; i < tableModel.getRowCount(); i++) { 
            price = Float.parseFloat(jTable1.getValueAt(i, 5).toString()) -
                    Float.parseFloat(jTable1.getValueAt(i, 5).toString()) *
                    Float.parseFloat(jTable1.getValueAt(i, 6).toString())/100;
            cost = Float.parseFloat(jTable1.getValueAt(i, 3).toString())*price;            
            
            sum += cost;
            tableModel.setValueAt(i+1, i, 0);
            tableModel.setValueAt(price, i, 7);
            tableModel.setValueAt(cost, i, 8);
            products.get(i).setCount(Float.parseFloat(jTable1.getValueAt(i, 3).toString()));
            products.get(i).setCost(Float.parseFloat(jTable1.getValueAt(i, 5).toString()));
            products.get(i).setDiscount(Float.parseFloat(jTable1.getValueAt(i, 6).toString()));
        }
        jLabel10.setText(String.format("%.2f", sum));
        float total = sum-sum*Float.parseFloat(jFormattedTextField1.getText().replace(",", "."))/100;
        float nds = total*docInvoice.getOrganization().getNds()/100;
        jLabel24.setText(String.format("%.2f", nds));
        jLabel11.setText(String.format("%.2f", total));
        jLabel19.setText(String.format("%.3f", getWeight()));
        docInvoice.setTotal(Float.parseFloat(jLabel11.getText().replace(",", ".")));
    }
    
    // получить товары из документа и заполнить таблицу
    void getInvoiceProducts(Invoice invoice) {
        products.clear();
            if (!invoice.getInvoiceProducts().isEmpty())
                products.addAll(invoice.getInvoiceProducts());                
            
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
                invoiceProduct = (InvoiceProduct)it.next();                
                product = invoiceProduct.getProduct();
                index++;
                tableModel.addRow(new Object[]{index, product.getArticul(), product.getName(), 
                    invoiceProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), invoiceProduct.getCost(), 
                    invoiceProduct.getDiscount(), 
                    invoiceProduct.getCost()-(invoiceProduct.getCost()*invoiceProduct.getDiscount()/100),
                    (invoiceProduct.getCost()-invoiceProduct.getCost()*invoiceProduct.getDiscount()/100)*invoiceProduct.getCount() });               
            }
        } catch (Exception e) {System.out.println("ERROR! " + e);}
        tableModel.fireTableDataChanged();
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
    
    // Выполнить документ
    @Override
    public void executeDocument() {
        new DocumentCompletionDAO().completion(docInvoice);
        closeButtons();
    }
    
    // сохранить документ
    @Override
    public void saveDocument(long status){
        
        if (docInvoice.getContragent() != null) {                               
            try {            
                docInvoice.setIndate(jDateChooser2.getDate());
                docInvoice.setDescr(jTextField2.getText());
                docInvoice.setDiscount(Float.parseFloat(jFormattedTextField1.getText().replace(",", ".")));
                docInvoice.setStorage(getSelectedStorage());
                docInvoice.setPriceName(getSelectedPriceName());
                docInvoice.setUsr(docInvoice.getSessionUser());
                docInvoice.setOrganization(docInvoice.getOrganization());
                docInvoice.setTotal(Float.valueOf(jLabel11.getText().replace(",", ".")));
                docInvoice.setStatus(documentDAO.getStatus(status));

                for (int i = 0; i < products.size(); i++) {                    
                    products.get(i).setInvoice(docInvoice);
                }  
            
                docInvoice.setInvoiceProducts(products);
            
                documentDAO.updateDocument(docInvoice);
                
                if (status == 2)
                    executeDocument();
            
                repaintDocument();
            
            } catch (Exception ex) {
                Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex); }
        } else {
                JOptionPane.showMessageDialog(null, 
                    "Не удалось сохранить документ. \n Заполните поле контрагент.", "Ошибка", JOptionPane.ERROR_MESSAGE);                    
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPopupMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPopupMenu1MouseClicked(evt);
            }
        });

        jMenuItem5.setText("Счет на оплату");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem5);

        jMenuItem1.setText("Реализация товаров");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem6.setText("Акт выполненных работ");
        jPopupMenu1.add(jMenuItem6);
        jPopupMenu1.add(jSeparator1);

        jMenuItem3.setText("Списание товаров");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        jMenuItem4.setText("Оприходование товаров");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

        setClosable(true);
        setIconifiable(true);
        setTitle(frameTitle);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/shopping-bag.png"))); // NOI18N
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

        jLabel7.setText("Сумма");

        jLabel9.setText("Итого");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("0.00");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("0.00");

        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setText("Примечание");

        jLabel13.setText("Пользователь");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("_____");
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel6.setText("руб.");

        jLabel16.setText("руб.");

        jLabel23.setText("НДС");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("0.00");

        jLabel25.setText("руб.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(25, 25, 25)
                                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel6)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(0, 18, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(704, 120));

        jLabel1.setText("№");

        jLabel2.setText("_____");

        jLabel3.setText("Дата");

        jLabel5.setText("Контрагент");

        jLabel15.setText("Склад");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTextField3.setEditable(false);
        jTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });

        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel17.setText("Тип цен");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/trash.png"))); // NOI18N
        jButton2.setToolTipText("Пометить как удаленный");
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

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton7.setToolTipText("Выполнить");
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton9.setToolTipText("Сохранить");
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton5.setToolTipText("Сохранить документ");
        jButton5.setFocusPainted(false);
        jButton5.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton6.setToolTipText("Добавить товары");
        jButton6.setFocusPainted(false);
        jButton6.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/documentadd.png"))); // NOI18N
        jButton4.setToolTipText("Ввести на основании");
        jButton4.setFocusPainted(false);
        jButton4.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton3.setToolTipText("Подчиненные документы");
        jButton3.setFocusPainted(false);
        jButton3.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jLabel22.setText("Организация");

        jTextField1.setEditable(false);
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jLabel21.setText("%");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setText("0");

        jLabel8.setText("Скидка");

        jLabel18.setText("Вес");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("0.000");

        jLabel20.setText("кг.");

        jDateChooser2.setMinimumSize(new java.awt.Dimension(50, 24));
        jDateChooser2.setPreferredSize(new java.awt.Dimension(118, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel22))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox2, 0, 225, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 225, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jLabel15)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel22)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

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
    
    // подчиненные документы
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
          docInvoice.showSubordins();
    }//GEN-LAST:event_jButton3ActionPerformed

    
    // кнопка ЗАКРЫТЬ 
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // кнопка ДОБАВИТЬ ТОВАРЫ    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(), products, getSelectedPriceName(), "invoice");
        pdtcd.setLocationRelativeTo(this);
        pdtcd.setVisible(true);
        
        updateProductsTable();
    }//GEN-LAST:event_jButton6ActionPerformed

    // комбобокс СКЛАД
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == 2) {
            try {
                docInvoice.setStorage(getSelectedStorage());
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    // клавиши в таблице    
    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = jTable1.getSelectedRow();
            if (docInvoice.getInvoiceProducts() != null)
                docInvoice.getInvoiceProducts().remove((InvoiceProduct)products.get(index));            
            products.remove(index);
            tableModel.removeRow(index);

            updateProductsTable();
        }  
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(), products, getSelectedPriceName(), "invoice");
            pdtcd.setLocationRelativeTo(this);
            pdtcd.setVisible(true);
        
            updateProductsTable();            
        }
    }//GEN-LAST:event_jTable1KeyPressed

    // выбор типа цен
    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if (evt.getStateChange() == 2 && !firstOpen) {
            int dVal = 0;
                dVal = JOptionPane.showOptionDialog(null, "Пересчитать цены в документе", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, new Object[]{"Да", "Нет"}, "Да");
            if (dVal == JOptionPane.OK_OPTION) {                
                for (int i = 0; i < products.size(); i ++) {
                    tableModel.setValueAt(products.get(i).getProduct().getActualPrice(docInvoice.getPriceName()).getPrice(), i, 5);
                }
                recalculateDocument();               
            }
            docInvoice.setPriceName(getSelectedPriceName());
        }         
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    // Сохранить шаблон    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        JFileChooser fileChooser = new JFileChooser();
        
        fileChooser.setDialogTitle("Сохранить файл");   
 
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {            
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".pdf"))
                fileToSave = new File(fileToSave.getAbsolutePath()+".pdf");
        
        try {
            super.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            JasperReport jr;
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/invoice.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<>();
                parameters.put("docId", jLabel2.getText());
                parameters.put("date",  new MonthToTextBean().dateToText(format.format(jDateChooser2.getDate())));
                parameters.put("user", docInvoice.getUsr().getName());
                parameters.put("contragent", docInvoice.getContragent().getName());
                parameters.put("sum", jLabel10.getText());
                parameters.put("discount", jFormattedTextField1.getText());
                parameters.put("total", jLabel11.getText());
                parameters.put("weight", jLabel19.getText());
                parameters.put("count", tableModel.getRowCount());
                parameters.put("strsum", new NumberToTextBean().numberToText(Double.valueOf(jLabel11.getText())));
                
            List<ProductBean> ib = new ArrayList<>();
            ib.add(null);           
            for (int i = 0; i < tableModel.getRowCount(); i++)
                ib.add(new ProductBean(Integer.parseInt(tableModel.getValueAt(i, 0).toString()),
                        tableModel.getValueAt(i, 1).toString(), "", tableModel.getValueAt(i, 2).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 3).toString()), tableModel.getValueAt(i, 4).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 7).toString()), 0f, 
                        Float.valueOf(tableModel.getValueAt(i, 8).toString())
                ));            

            JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
               parameters,  new JRBeanCollectionDataSource(ib));

            JasperExportManager.exportReportToPdfFile(jasperPrint,
               fileToSave.getCanonicalPath());  
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            MainFrame.infoPanel.append("\nФайл " + fileToSave.getCanonicalPath() + " сохранен.");
            
        } catch (JRException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) { 
                Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }   // конец файлчузера    
    }//GEN-LAST:event_jButton5ActionPerformed

    // Попап подчиненных документов    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jPopupMenu1.show(jButton4, jButton4.getWidth()/2, jButton4.getHeight()/2);
    }//GEN-LAST:event_jButton4ActionPerformed

    // Реализация товаров
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (docInvoice.getId() != 0) {
            DocOffer docOffer = new DocOffer(docInvoice);
            MainFrame.jDesktopPane1.add(docOffer);
            docOffer.show();
        } else
        JOptionPane.showMessageDialog(null, 
                    "Сначала сохраните документ.", "Ошибка", JOptionPane.ERROR_MESSAGE);     
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    // Счет на оплату
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        
        new DocumentUtil().createBillDocument(docInvoice);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jPopupMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPopupMenu1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPopupMenu1MouseClicked

    // Выбрать организацию
    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        //OrganizationSelectDialog osd = new OrganizationSelectDialog(null, true, docInvoice);
        OrganizationSelectDialog osd = new OrganizationSelectDialog(null, true, docInvoice);
        osd.setLocationRelativeTo(this);
        osd.setVisible(true);
        jTextField1.setText(docInvoice.getOrganization().getName());
        recalculateDocument();
    }//GEN-LAST:event_jTextField1MouseClicked

    // Добавить контрагента
    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        ContragentSelectDialog csd = new ContragentSelectDialog(null, true, docInvoice);
        csd.setLocationRelativeTo(this);
        csd.setVisible(true);
        jTextField3.setText(docInvoice.getContragent().getName());
    }//GEN-LAST:event_jTextField3MouseClicked

    // Сохранить
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        saveDocument(1L);
    }//GEN-LAST:event_jButton9ActionPerformed

    // Выполнить
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveDocument(2L);                          
    }//GEN-LAST:event_jButton7ActionPerformed

    // Удалить
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (docInvoice.getId() != 0) {
                int dVal = JOptionPane.showOptionDialog(null, "Пометить документ как удаленный.", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, new Object[]{"Да", "Нет"}, "Да");
            if (dVal == JOptionPane.OK_OPTION) {
                saveDocument(3l);
                }            
        }        
    }//GEN-LAST:event_jButton2ActionPerformed

    // скрыть при сворачивании
    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified
    // Списание товаров
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (docInvoice.getId() != 0) {
            List<DocumentProduct> dp = new ArrayList<>();
            DocumentProduct p;
            for (int i = 0; i < products.size(); i++) {                
                    p = new DocumentProduct();
                        p.setCount(products.get(i).getCount());
                        p.setProduct(products.get(i).getProduct());
                        p.setCost(products.get(i).getCost());
                    dp.add(p);                    
            }
            
            if (!MainFrame.ifManager.isDocDeductFrameOpen()) {
                MainFrame.ifManager.showFrame(new DocDeduct(docInvoice, dp), false);
                MainFrame.ifManager.setDocDeductFrameOpen(true);
            } 
        } else
            JOptionPane.showMessageDialog(null, 
                    "Сначала сохраните документ.", "Ошибка", JOptionPane.ERROR_MESSAGE);          
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    // оприходование товаров
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (docInvoice.getId() != 0) {
            List<DocumentProduct> dp = new ArrayList<>();
            DocumentProduct p;
            for (int i = 0; i < products.size(); i++) {                
                    p = new DocumentProduct();
                        p.setCount(products.get(i).getCount());
                        p.setProduct(products.get(i).getProduct());
                        p.setCost(products.get(i).getCost());
                    dp.add(p);                    
            }
            
            if (!MainFrame.ifManager.isDocReceiptFrameOpen()) {
                MainFrame.ifManager.showFrame(new DocReceipt(docInvoice, dp), false);
                MainFrame.ifManager.setDocReceiptFrameOpen(true);
            } 
        } else
            JOptionPane.showMessageDialog(null, 
                    "Сначала сохраните документ.", "Ошибка", JOptionPane.ERROR_MESSAGE);        
    }//GEN-LAST:event_jMenuItem4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser2;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
