/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.Documents;

import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.beanutils.BeanUtils;
import ru.jmirazors.jmiCalculator.beans.CalendarPopup;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Receipt;
import ru.jmirazors.jmiСalculator.entity.ReceiptProduct;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.jmiframes.ProductToCartDialog;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.ProductAddDialog;

/**
 *
 * @author User
 */
public final class DocReceipt extends javax.swing.JInternalFrame implements DocumentImpl{

    /**
     * Creates new form DocReceipt
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. оприход.");      
    
    String frameTitle = "Оприходование товаров";
    StringBuffer titleComplete = new StringBuffer();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    ArrayList<ReceiptProduct> products = new ArrayList<>();
    ArrayList<ReceiptProduct> newProducts = new ArrayList<>();
    ReceiptProduct receiptProduct;
    Product product;
    Receipt docReceipt;
    DocumentDAO documentDAO;
    Document parentDoc = null;
    CalendarPopup calendar;
    boolean saved = true;    
    
    
    // модель таблицы
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                if (col == 3 || col == 5)
                    return true;
                return false; 
            }
    };      
    
    public DocReceipt() {
        calendar = new CalendarPopup();
        documentDAO = new DocumentDAO();
        initTableColumns();
        docReceipt = new Receipt();
        products.clear();
        initComponents();
        initVisualComponents();                 
        docInit();
        
    }
    public DocReceipt(String id) {
        calendar = new CalendarPopup();                
        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
            docReceipt = (Receipt)documentDAO.getDocument(id.trim(), Receipt.class);
        } catch (Exception ex) {
            MainFrame.ifManager.showWarningDialog(this, "Ошибка!", "Не удалось получить документ.\n"+ex);
        }
        initComponents();
        initVisualComponents();
        getReceiptProducts(docReceipt);
    }
    
    public DocReceipt(Document doc, List<DocumentProduct> iproducts) {
        initTableColumns();
        docReceipt = new Receipt();        
        
        setByParentProducts(iproducts);
        
        initComponents();
        initVisualComponents();                        
                
        documentDAO = new DocumentDAO();               
        
        parentDoc = doc;                

        docReceipt.setIndate(new Date());
        docReceipt.setOrganization(parentDoc.getOrganization());
        docReceipt.setStorage(parentDoc.getStorage());        
        docReceipt.setDocuments(documentDAO.getDocumentType(10L));
        docReceipt.setOrganization(parentDoc.getOrganization());        
        docReceipt.setUsr(MainFrame.sessionParams.getUser()); 
        docReceipt.setStatus(documentDAO.getStatus(1L));        
        docReceipt.setDescr(parentDoc.getDocuments().getName());                
        
        updateProductsTable();
        recalculateDocument();
        repaintDocument();
        
    }      
    
    // *********************     Методы документа ****************************************
    void setByParentProducts(List<DocumentProduct> pProducts) {
        if (!pProducts.isEmpty()) {            
            ReceiptProduct dp;
            PriceName pn;
            try {
                pn = new PriceNameDAO().getPriceName(2L); // учетная цена
            } catch (Exception ex) {
                pn = null;
            }
            for (int i = 0; i < pProducts.size(); i++) {
                dp = new ReceiptProduct();
                dp.setProduct(pProducts.get(i).getProduct());
                dp.setCount(Math.abs(pProducts.get(i).getCount()));            
                dp.setReceipt(docReceipt);
                dp.setCost(pProducts.get(i).getProduct().getActualPrice(pn).getPrice() );
            products.add(dp);
        }                         
        }
    }    
    
    // закрытие документа    
    @Override
     public void dispose() {
          super.dispose();          
          documentDAO.ev(docReceipt);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocReceiptFrameOpen(false);
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
            jButton6.setEnabled(false);
            jButton4.setEnabled(false);
            jTable1.setEnabled(false);
            jComboBox1.setEnabled(false);
            jTextField1.setEnabled(false);
            jFormattedTextField1.setEditable(false);
            jButton9.setEnabled(false);
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
        jTable1.getColumnModel().getColumn(6).setMaxWidth(82);       
        
        getStorageList();        
        
        tableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 0 && e.getColumn() != 6) {
                recalculateDocument();
            }
        });
        calendar.getCPanel().addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarPanel1PropertyChange(evt);
            }
            private void calendarPanel1PropertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("selectedDate")) {            
                            calendar.getPopup().hide();
                            jFormattedTextField1.setText(calendar.getStringDate());
                            try {
                                docReceipt.setIndate(format.parse(jFormattedTextField1.getText()));
                            } catch (ParseException ex) {
                                docReceipt.setIndate(new Date());
                            }
                    }
            }
        });        
    } 
    
    // инициализировать документ
    @Override
    public void docInit() {
        docReceipt.init(10L);
        docReceipt.setStorage(getSelectedStorage());
        recalculateDocument();         
    }
    
    // перерисовать документ
    @Override
    public void repaintDocument() {
         // Заголовок 
        titleComplete.setLength(0);
        titleComplete.append(frameTitle).append(" /").append(docReceipt.getOrganization().getName())
                .append("/ [").append(docReceipt.getStatus().getName()).append("]");
        if (!saved)
            titleComplete.append("*");        
        this.setTitle(titleComplete.toString());
        // Номер документа
        jLabel4.setText(docReceipt.getFormattedID(docReceipt.getId()));
        // дата
        jFormattedTextField1.setText(format.format(docReceipt.getIndate()));
        // подразделение
        jTextField2.setText(docReceipt.getDepartment().getName());
        // пользователь
        jLabel12.setText(docReceipt.getUsr().getName());
        // Валюта
        jLabel17.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        // сумма
        jLabel8.setText(String.format("%.2f", docReceipt.getTotal()));
        // примечание
        jTextField1.setText(docReceipt.getDescr());
        // вес
        jLabel9.setText(String.format("%.3f", docReceipt.getWeight() ));
        // склад
        jComboBox1.setSelectedItem(docReceipt.getStorage().getName());
        
        if (docReceipt.getStatus().getId() == 2 || docReceipt.getStatus().getId() == 3) 
            closeButtons();        
        // основание
        if (parentDoc != null)
            jLabel14.setText(parentDoc.getDocumentName()+" №"+docReceipt.getFormattedID(parentDoc.getId()) + " от " +format.format(parentDoc.getIndate()));
        else
            jLabel14.setText("НЕТ"); 
        // наименований
        jLabel18.setText(String.format("%d", tableModel.getRowCount()));         
    }
    
    // расчитать вес
    Float getWeight() {
        Float weight = 0f;
        for (ReceiptProduct ip: products) 
            weight += ip.getProduct().getWeight()*ip.getCount();
        docReceipt.setWeight(weight);
        return weight;
    } 
    // private float сумма с НДС
    private float getResult() {
        return docReceipt.getTotal();
    }      
    
    // пересчитать таблицу
    void recalculateDocument() {
        Float sum = 0f;
        Float price;
        Float cost;
        for (int i = 0; i < tableModel.getRowCount(); i++) { 
            price = Float.parseFloat(jTable1.getValueAt(i, 5).toString());
            cost = Float.parseFloat(jTable1.getValueAt(i, 3).toString())*price;            
            
            sum += cost;
            tableModel.setValueAt(cost, i, 6);
            products.get(i).setCount(Float.parseFloat(jTable1.getValueAt(i, 3).toString()));
            products.get(i).setCost(Float.parseFloat(jTable1.getValueAt(i, 5).toString()));
        }
        docReceipt.setTotal(sum);
        getWeight();
        repaintDocument();
    } 
    
    // получить товары из документа и заполнить таблицу
    void getReceiptProducts(Receipt receipt) {
        products.clear();
            if (!receipt.getReceiptProducts().isEmpty())
                products.addAll(receipt.getReceiptProducts());            
            updateProductsTable();
    }     
    
    // обновить таблицу с товарами
    void updateProductsTable() {
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);
        int i = 0;                        
        
        try {
            Iterator it = products.iterator();
            while (it.hasNext()) {
                receiptProduct = (ReceiptProduct)it.next();                
                product = receiptProduct.getProduct();
                i++;
                tableModel.addRow(new Object[]{i, product.getArticul(), product.getName(), 
                    receiptProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), receiptProduct.getCost(), 
                    receiptProduct.getCost()*receiptProduct.getCount() });               
            }
        } catch (Exception e) {System.out.println("ERROR! " + e);}
        tableModel.fireTableDataChanged();
        recalculateDocument();
    }
    // добавить продукты в документ
    private void setReceiptProducts(ArrayList<? extends DocumentProduct> prods) throws IllegalAccessException, InvocationTargetException {
        newProducts.clear();
        ReceiptProduct rp;
        for (DocumentProduct pr : prods) {
            rp = new ReceiptProduct();
            if (pr.getCount() > 0) {
                BeanUtils.copyProperties(rp, pr);
                newProducts.add(rp);
            }
        }
        products = (ArrayList<ReceiptProduct>)newProducts.clone();
        for (ReceiptProduct pr : products) {
            pr.setReceipt(docReceipt);
        }
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
    
    // выполнить документ
    @Override
    public void executeDocument() {
        new DocumentCompletionDAO().completion(docReceipt, products);
        closeButtons();
    }
    
    // сохранить документ
    @Override
    public void saveDocument(long status){        
        try {            
            docReceipt.setDescr(jTextField1.getText());
            docReceipt.setStatus(documentDAO.getStatus(status));                
            docReceipt.setTotal(getResult());
            docReceipt.setReceiptProducts(products);                      
                    
            
            documentDAO.updateDocument(docReceipt); 
            if (parentDoc != null)
                new DocumentUtil().saveParent(parentDoc, docReceipt); 
            if (status == 2)
                executeDocument();            
            saved = true;
            repaintDocument();
            
            
        } catch (Exception ex) {
            MainFrame.ifManager.showWarningDialog(this, "Ошибка", "Не удалось сохранить документ. " +ex);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 109, 240), 3));
        setClosable(true);
        setIconifiable(true);
        setTitle(frameTitle);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/doc-plus.png"))); // NOI18N
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

        jPanel1.setPreferredSize(new java.awt.Dimension(648, 100));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("№");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Дата");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("_______");

        jComboBox1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jComboBox1.setPreferredSize(new java.awt.Dimension(200, 22));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Склад:");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/trash.png"))); // NOI18N
        jButton2.setToolTipText("Удалить документ");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton2.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton7.setToolTipText("Выполнить");
        jButton7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton7.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton7.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton6.setToolTipText("Сохранить");
        jButton6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton6.setFocusPainted(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton6.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton6.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton5.setToolTipText("Сохранить документ");
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton5.setFocusPainted(false);
        jButton5.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton5.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton5.setPreferredSize(new java.awt.Dimension(24, 24));
        jToolBar1.add(jButton5);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton4.setToolTipText("Подбор товара");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setFocusPainted(false);
        jButton4.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton4.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton4.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/documentadd.png"))); // NOI18N
        jButton3.setToolTipText("Ввести на основании");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton3.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton3.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton8.setToolTipText("Подчиненные документы");
        jButton8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton8.setFocusPainted(false);
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton8.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton8.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jLabel7.setText("Вес");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("0.000");

        jLabel15.setText("кг.");

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel16.setText("Подразделение");

        jTextField2.setEditable(false);
        jTextField2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });

        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd.MM.yyyy H:mm"))));
        jFormattedTextField1.setPreferredSize(new java.awt.Dimension(105, 20));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calendar.png"))); // NOI18N
        jButton9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton9.setFocusPainted(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 292, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)))
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4)
                        .addComponent(jLabel2)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel15)
                        .addComponent(jLabel7)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jButton1.setText("Закрыть");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 88, 102)));
        jButton1.setFocusPainted(false);
        jButton1.setMaximumSize(new java.awt.Dimension(70, 24));
        jButton1.setMinimumSize(new java.awt.Dimension(70, 24));
        jButton1.setPreferredSize(new java.awt.Dimension(70, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Сумма");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("0.00");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setText("Примечание");

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel11.setText("Пользователь:");

        jTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField1.setPreferredSize(new java.awt.Dimension(300, 20));

        jLabel12.setText("____");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel13.setText("Документ основание:");

        jLabel14.setText("____");

        jLabel17.setText("___");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Всего наименований:");

        jLabel18.setText("jLabel18");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel17)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

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

    // Комбобокс склад
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == 2) {
            try {
                docReceipt.setStorage(getSelectedStorage());
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        }          
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    // кнопки в таблице
    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = jTable1.getSelectedRow();
            products.remove(index);
            tableModel.removeRow(index);
            updateProductsTable();
        }  
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
        try {            
            PriceName p = new PriceNameDAO().getPriceName(1);
            ProductAddDialog pad = new ProductAddDialog(null, true, docReceipt.getStorage(), p, products);
            pad.setVisible(true);                   

            setReceiptProducts(products);
            updateProductsTable(); 
                            
        } catch (Exception ex) {
            MainFrame.ifManager.showWarningDialog(this, "Ошибка", ex.toString());
        }             
        }         
    }//GEN-LAST:event_jTable1KeyPressed

    // Кнопка добавить товары
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {            
            PriceName p = new PriceNameDAO().getPriceName(1);
            ProductAddDialog pad = new ProductAddDialog(null, true, docReceipt.getStorage(), p, products);
            pad.setVisible(true);                   

            setReceiptProducts(products);
            updateProductsTable(); 
                            
        } catch (Exception ex) {
            MainFrame.ifManager.showWarningDialog(this, "Ошибка", ex.toString());
        }       
    }//GEN-LAST:event_jButton4ActionPerformed

    // Выполнить
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveDocument(2L);       
    }//GEN-LAST:event_jButton7ActionPerformed

    // Сохранить    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        saveDocument(1L);        
    }//GEN-LAST:event_jButton6ActionPerformed

    // скрыть при сворачивании
    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified
    
    // Удалить
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (docReceipt.getId() != 0) {
            int dVal = JOptionPane.showOptionDialog(null, "Пометить документ как удаленный.", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, new Object[]{"Да", "Нет"}, "Да");
            if (dVal == JOptionPane.OK_OPTION) {
                saveDocument(3l);
                }            
        } 
    }//GEN-LAST:event_jButton2ActionPerformed

    // закрыть документ
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        docReceipt.showSubordins();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        calendar.getPopup(jButton8).show();
        docReceipt.setIndate(calendar.getDate());
        saved = false;          
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
        // TODO add your handling code here:
        docReceipt.showDepartmentDialog(docReceipt);
        jTextField2.setText(docReceipt.getDepartment().getName());        
    }//GEN-LAST:event_jTextField2MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        docReceipt.showUserDialog(docReceipt);
        jLabel12.setText(docReceipt.getUsr().getName());         
    }//GEN-LAST:event_jLabel12MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
