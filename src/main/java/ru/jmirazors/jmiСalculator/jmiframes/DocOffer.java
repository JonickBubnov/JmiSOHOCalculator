/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.JFormattedTextField;
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
import ru.jmirazors.jmiCalculator.beans.OfferBean;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.DAO.SubordinDAO;
import ru.jmirazors.jmiСalculator.entity.Document;
import ru.jmirazors.jmiСalculator.entity.Invoice;
import ru.jmirazors.jmiСalculator.entity.InvoiceProduct;
import ru.jmirazors.jmiСalculator.entity.Offer;
import ru.jmirazors.jmiСalculator.entity.OfferProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.Subordin;

/**
 *
 * @author User
 */
public final class DocOffer extends javax.swing.JInternalFrame implements DocumentImpl {

    /**
     * Creates new form DocOffer
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. реализ.|");     
    
    String frameTitle = "Реализация товаров";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static ArrayList<OfferProduct> products = new ArrayList<>();
    OfferProduct offerProduct;
    Product product;
    Offer docOffer;
    JFormattedTextField jFormattedTextFieldCell;
    DocumentDAO documentDAO;
    Invoice subInvoice = null;
    Document parentDoc = null;    
    
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
    
    public DocOffer() {        
        initTableColumns();
        docOffer = new Offer();        
        products.clear();
        initComponents();
        initVisualComponents();
                
        documentDAO = new DocumentDAO();
        docInit();
        repaintDocument();
    }
        
    public DocOffer(String id) {        

        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
           docOffer = (Offer) documentDAO.getDocument(id.trim(), Offer.class);
        } catch (Exception ex) {
            Logger.getLogger(DocOffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        initVisualComponents();
        getOfferProducts(docOffer);
        repaintDocument();                             
        
    }
    
    public DocOffer(Invoice doc) {
        initTableColumns();
        docOffer = new Offer();        
        products.clear();
        initComponents();
        initVisualComponents();                        
        
        jDateChooser1.setDate(new Date());
        jLabel14.setText(MainFrame.sessionParams.getUser().getName()); 
        documentDAO = new DocumentDAO();               
        
        subInvoice = doc;
        //subDoc = doc;
        jLabel20.setText(subInvoice.getDocuments().getName()+" №"+subInvoice.getId()+" от "+format.format(subInvoice.getIndate()));
        OfferProduct op;
        List<InvoiceProduct> pr = subInvoice.getInvoiceProducts();
        for (int i = 0; i < pr.size(); i++) {
            op = new OfferProduct();
            op.setCost(pr.get(i).getCost());
            op.setCount(pr.get(i).getCount());
            op.setDiscount(pr.get(i).getDiscount());
            op.setProduct_id(pr.get(i).getProduct().getId());
            op.setOffer(docOffer);
            op.setProduct(pr.get(i).getProduct());
            products.add(op);
        }
        docOffer.setStorage(subInvoice.getStorage());        
        docOffer.setPriceName(subInvoice.getPriceName());
        docOffer.setDocuments(documentDAO.getDocumentType(3l));
        docOffer.setOrganization(MainFrame.sessionParams.getOrganization());        
        docOffer.setUsr(MainFrame.sessionParams.getUser());
        docOffer.setContragent(subInvoice.getContragent());
        docOffer.setDiscount(subInvoice.getDiscount());

        jFormattedTextField1.setText(String.valueOf(docOffer.getDiscount()).replace(".", ","));
        jComboBox1.setSelectedItem(docOffer.getStorage().getName());
        jComboBox2.setSelectedItem(docOffer.getPriceName().getName());
        jTextField3.setText(docOffer.getContragent().getName());
        getOfferProducts(docOffer);
        
        recalculateDocument();
        
    }
    
    // *********************     Методы документа ****************************************
    // закрытие документа    
    @Override
     public void dispose() {
          super.dispose();          
          documentDAO.ev(docOffer);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocOfferFrameOpen(false);
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
            //jButton7.setEnabled(false);
            jComboBox1.setEnabled(false);
            jComboBox2.setEnabled(false);
            jButton6.setEnabled(false);            
            jButton4.setEnabled(false); 
            jTextField2.setEnabled(false);
            jFormattedTextField1.setEnabled(false);
            jButton9.setEnabled(false);            
            jButton10.setEnabled(false);                                           
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
        
        Subordin sd = null;
        if (docOffer.getId() != 0) {
            sd = new SubordinDAO().getSubDocument(docOffer);
        }
        if (sd != null) {
            parentDoc = new DocumentUtil().getMainDocument(sd);
            jLabel20.setText(parentDoc.getDocuments().getName()+" №"+parentDoc.getId()+" от "+format.format(parentDoc.getIndate()));
        } else {
            jLabel20.setText("Нет");
        } 
        
        jFormattedTextField1.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                jLabel11.setText(""+(Float.parseFloat(jLabel10.getText())-
                        Float.parseFloat(jLabel10.getText())*Float.parseFloat(jFormattedTextField1.getText().replace(",", "."))/100));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
//                jLabel11.setText(""+(Float.parseFloat(jLabel10.getText())-
//                        Float.parseFloat(jLabel10.getText())*Float.parseFloat(jFormattedTextField1.getText())/100));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        
        tableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 7 && e.getColumn() != 8 && e.getColumn() != 0) {
                recalculateDocument();
            }
        });
    }   
    
    // инициализировать новый документ
    @Override
    public void docInit() {
        docOffer.setDocuments(documentDAO.getDocumentType(3L));
        docOffer.setOrganization(MainFrame.sessionParams.getOrganization());        
        docOffer.setUsr(MainFrame.sessionParams.getUser());
        docOffer.setWeight(0);
        docOffer.setTotal(0);
        docOffer.setDiscount(0);
        docOffer.setIndate(new Date());
        docOffer.setStatus(documentDAO.getStatus(1L));
        docOffer.setStorage(getSelectedStorage());
        docOffer.setPriceName(getSelectedPriceName());
        docOffer.setDescr("");       
    }
    
    // заполнить отображение документа
    @Override
    public void repaintDocument() {
        this.setTitle(frameTitle + " ["+docOffer.getStatus().getName()+"]");        
        if (docOffer.getId() != 0)
            jLabel2.setText(""+docOffer.getId()); 
        jDateChooser1.setDate(docOffer.getIndate());
        jTextField2.setText(docOffer.getDescr());
        jLabel14.setText(docOffer.getUsr().getName());
        jFormattedTextField1.setText( (""+docOffer.getDiscount()).replace(".", ",") );
        jLabel11.setText(""+docOffer.getTotal());
        jTextField1.setText(docOffer.getOrganization().getName());
        if (docOffer.getContragent() != null)        
            jTextField3.setText(docOffer.getContragent().getName());
        jComboBox1.setSelectedItem(docOffer.getStorage().getName());
        jComboBox2.setSelectedItem(docOffer.getPriceName().getName());
    
        if (docOffer.getStatus().getId() == 2 || docOffer.getStatus().getId() == 3) {
            closeButtons();
        }          
    }
    
    // расчитать вес
    Float getWeight() {
        float weight = 0f;
        for (OfferProduct ap: products) 
            weight += ap.getProduct().getWeight()*ap.getCount();
        docOffer.setWeight(weight);
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

        jLabel10.setText(""+sum);
        jLabel11.setText(""+(sum-sum*Float.parseFloat(jFormattedTextField1.getText().replace(",", "."))/100));
        jLabel21.setText(""+getWeight());
        docOffer.setTotal(Float.parseFloat(jLabel11.getText()));        
    } 
    
    // получить товары из документа и заполнить таблицу
    void getOfferProducts(Offer offer) {            
            products.clear();
            if (offer.getOfferProducts() != null)
                products.addAll(offer.getOfferProducts());
            
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
                offerProduct = (OfferProduct)it.next();                
                product = offerProduct.getProduct();
                //product = new ProductDAO().getProduct(""+offerProduct.getProduct_id());
                i++;
                tableModel.addRow(new Object[]{i, product.getArticul(), product.getName(), 
                    offerProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), offerProduct.getCost(), 
                    offerProduct.getDiscount(), 
                    offerProduct.getCost()-(offerProduct.getCost()*offerProduct.getDiscount()/100),
                    (offerProduct.getCost()-offerProduct.getCost()*offerProduct.getDiscount()/100)*offerProduct.getCount() });               
            }
        } catch (Exception e) {System.out.println("ERROR! " + e);}
        tableModel.fireTableDataChanged();
        recalculateDocument();
    }   
    
    // установить тыпы цен
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
            Logger.getLogger(DocOffer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DocOffer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return storage;
    }   
    
    // выполнить документ
    @Override
    public void executeDocument() {
        new DocumentCompletionDAO().completion(docOffer, products);
        closeButtons();        
    }
    
    // сохранить документ
    @Override
    public void saveDocument(long status) {
        if (docOffer.getContragent() != null) {
        try {            
            docOffer.setIndate(jDateChooser1.getDate());
            docOffer.setDescr(jTextField2.getText());
            docOffer.setDiscount(Float.parseFloat(jFormattedTextField1.getText().replace(",", ".")));
            docOffer.setStorage(getSelectedStorage());
            docOffer.setPriceName(getSelectedPriceName());
            docOffer.setTotal(Float.valueOf(jLabel11.getText().replace(",", ".")));
            docOffer.setStatus(documentDAO.getStatus(status));
            
            for (int i = 0; i < products.size(); i++) {                    
                    products.get(i).setOffer(docOffer);
            }  
            
            docOffer.setOfferProducts(products);                                   
            
            documentDAO.updateDocument(docOffer);
            if (parentDoc != null)
                new DocumentUtil().saveParent(parentDoc, docOffer);              
            
            if (status == 2)
                executeDocument();
            
                
            repaintDocument();            
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Не удалось сохранить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);} 
        } else {
                JOptionPane.showMessageDialog(null, 
                    "Не удалось сохранить документ. \n Заполните поле контрагент.", "Ошибка", JOptionPane.ERROR_MESSAGE);                    
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
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
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
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
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jMenuItem1.setText("Оплата");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Заказ покупателя");
        jPopupMenu1.add(jMenuItem2);

        setClosable(true);
        setIconifiable(true);
        setTitle(frameTitle);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fast-delivery.png"))); // NOI18N
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

        jPanel1.setPreferredSize(new java.awt.Dimension(1085, 120));

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

        jLabel14.setText("_____");

        jLabel6.setText("руб.");

        jLabel16.setText("руб.");

        jLabel24.setText("НДС");

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("0.00");

        jLabel26.setText("руб.");

        jLabel19.setText("Основание:");

        jLabel20.setText("_____");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel19))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel16))
                        .addGap(0, 160, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel6)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel16))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jButton1))
                        .addContainerGap())))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(755, 120));

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
        jButton2.setToolTipText("Удалить");
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

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton9.setToolTipText("Выполнить");
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

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton10.setToolTipText("Сохранить");
        jButton10.setFocusPainted(false);
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton6.setToolTipText("Подбор товара");
        jButton6.setFocusPainted(false);
        jButton6.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

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

        jLabel8.setText("Скидка");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setText("0");

        jLabel23.setText("%");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("0.000");

        jLabel22.setText("кг.");

        jLabel18.setText("Вес");

        jLabel27.setText("Организация");

        jTextField1.setEditable(false);
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jDateChooser1.setPreferredSize(new java.awt.Dimension(129, 22));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel8)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(24, 24, 24))
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
    
    // кнопка ЗАКРЫТЬ
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Подчиненные документы    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        docOffer.showSubordins();       
    }//GEN-LAST:event_jButton3ActionPerformed

    // комбобокс СКЛАД    
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == 2) {
            try {
                docOffer.setStorage(getSelectedStorage());
            } catch (Exception ex) {
                Logger.getLogger(DocOffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    // кнопка ДОБАВИТЬ ТОВАР
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(), products, getSelectedPriceName(), "offer");
        pdtcd.setLocationRelativeTo(this);
        pdtcd.setVisible(true);

        updateProductsTable();
    }//GEN-LAST:event_jButton6ActionPerformed
    
    // клавиши в таблице    
    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = jTable1.getSelectedRow();
            docOffer.getOfferProducts().remove((OfferProduct)products.get(index));
            products.remove(index);
            tableModel.removeRow(index);

            updateProductsTable();
        }
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(), products, getSelectedPriceName(), "offer");
            pdtcd.setLocationRelativeTo(this);
            pdtcd.setVisible(true);
        
            updateProductsTable();            
        }        
    }//GEN-LAST:event_jTable1KeyPressed
               
    // комбобокс ЦЕНА
    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if (evt.getStateChange() == 2) {
            try {
                docOffer.setPriceName(getSelectedPriceName());
            } catch (Exception ex) {
                Logger.getLogger(DocOffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    // кнопка СОХРАНИТЬ ШАБЛОН    
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
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/offer.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<>();
                parameters.put("docId", jLabel2.getText());
                parameters.put("date",  new MonthToTextBean().dateToText(format.format(jDateChooser1.getDate())));
                parameters.put("storage", docOffer.getStorage().getName());
                parameters.put("company", docOffer.getUsr().getName());
                parameters.put("contragent", docOffer.getContragent().getName());
                parameters.put("sum", jLabel10.getText());
                parameters.put("discount", jFormattedTextField1.getText());
                parameters.put("total", jLabel11.getText());
                parameters.put("weight", jLabel21.getText());
                parameters.put("count", tableModel.getRowCount());
                parameters.put("strsum", new NumberToTextBean().numberToText(Double.valueOf(jLabel11.getText())));
                
            List<OfferBean> ob = new ArrayList<OfferBean>();
            ob.add(null);           
            for (int i = 0; i < tableModel.getRowCount(); i++)
                ob.add(new OfferBean(tableModel.getValueAt(i, 0).toString(),
                        tableModel.getValueAt(i, 1).toString(), tableModel.getValueAt(i, 2).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 3).toString()), tableModel.getValueAt(i, 4).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 7).toString()), Float.valueOf(tableModel.getValueAt(i, 8).toString())
                ));            

            JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
               parameters,  new JRBeanCollectionDataSource(ob));

            JasperExportManager.exportReportToPdfFile(jasperPrint,
               fileToSave.getCanonicalPath());  
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            MainFrame.infoPanel.append("\nФайл " + fileToSave.getCanonicalPath() + " сохранен.");
            
        } catch (JRException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) { 
                Logger.getLogger(Offer.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }   // конец файлчузера           
    }//GEN-LAST:event_jButton5ActionPerformed

    // Ввести на основании -  ОПЛАТА    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (docOffer.getId() != 0) {
            PayIf payIf = new PayIf(docOffer, false);
            MainFrame.jDesktopPane1.add(payIf);
            payIf.show(); 
        } else
            JOptionPane.showMessageDialog(null, 
                    "Сначала сохраните документ.", "Ошибка", JOptionPane.ERROR_MESSAGE); 
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Ввести на основании
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jPopupMenu1.show(jButton4, jButton4.getWidth()/2, jButton4.getHeight()/2);
    }//GEN-LAST:event_jButton4ActionPerformed

    // Выполнить
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        saveDocument(2L);                                       
    }//GEN-LAST:event_jButton9ActionPerformed

    // Сохранить
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        saveDocument(1L);         
    }//GEN-LAST:event_jButton10ActionPerformed

     // Контрагент
    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        ContragentSelectDialog csd = new ContragentSelectDialog(null, true, docOffer);
        csd.setLocationRelativeTo(this);
        csd.setVisible(true);
        jTextField3.setText(docOffer.getContragent().getName());        
    }//GEN-LAST:event_jTextField3MouseClicked

    // Удалить
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (docOffer.getId() != 0) {
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
