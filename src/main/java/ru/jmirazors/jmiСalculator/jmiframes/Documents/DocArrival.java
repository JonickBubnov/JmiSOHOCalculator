/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.Documents;

/**
 *
 * @author Jonick
 */

import ru.jmirazors.jmiCalculator.MainFrame;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.beanutils.BeanUtils;
import ru.jmirazors.jmiCalculator.beans.ArrivalBean;
import ru.jmirazors.jmiCalculator.beans.CalendarPopup;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiCalculator.beans.MonthToTextBean;
import ru.jmirazors.jmiCalculator.beans.NumberToTextBean;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.Arrival;
import ru.jmirazors.jmiСalculator.entity.ArrivalProduct;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.ProductAddDialog;

public class DocArrival extends javax.swing.JInternalFrame implements DocumentImpl{

    /**
     * Creates new form DocArrival
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. поступление");     
    
    String frameTitle = "Поступление товаров";
    StringBuffer titleComplete = new StringBuffer();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    ArrayList<ArrivalProduct> products = new ArrayList<>();
    ArrayList<ArrivalProduct> newProducts = new ArrayList<>();
    ArrivalProduct arrivalProduct;
    Product product;
    Arrival docArrival;
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
    
    public DocArrival() {
        
        calendar = new CalendarPopup();
        documentDAO = new DocumentDAO();
        initTableColumns();
        docArrival = new Arrival();
        products.clear();
        initComponents();
        initVisualComponents();                 
        docInit();
              
               
    }
    
    public DocArrival(String id){
        calendar = new CalendarPopup();        
        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
            docArrival = (Arrival)documentDAO.getDocument(id.trim(), Arrival.class);
        } catch (Exception ex) {
            MainFrame.ifManager.showWarningDialog(this, "Ошибка!", "Не удалось получить документ.\n"+ex);
        }
        initComponents();
        initVisualComponents();
        getArrivalProducts(docArrival);
        //recalculateDocument();

    }

    // *********************     Методы документа ****************************************
    // закрытие документа    
    @Override
     public void dispose() {
          super.dispose();          
          documentDAO.ev(docArrival);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocArrivalFrameOpen(false);
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
            jButton7.setEnabled(false);            
            jButton9.setEnabled(false);
            jTextField2.setEnabled(false);
            jComboBox1.setEnabled(false);            
            jButton6.setEnabled(false);            
            jButton4.setEnabled(false); 
            jFormattedTextField1.setEditable(false);
            jButton8.setEnabled(false);
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
        jTable1.getColumnModel().getColumn(5).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(100);        
        
        getStorageList(); 
                
               
//        Subordin sd = null;
//        if (docArrival.getId() != 0) {
//            sd = new SubordinDAO().getSubDocument(docArrival);
//        }
//        if (sd != null) {
//            parentDoc = new DocumentUtil().getMainDocument(sd);
//            jLabel24.setText(parentDoc.getDocuments().getName()+" №"+parentDoc.getId()+" от "+format.format(parentDoc.getIndate()));
//        } else {
//            jLabel24.setText("Нет");
//        }         
//        
        tableModel.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 6 && e.getColumn() != 0) {                  
                    recalculateDocument();
                }
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
                                docArrival.setIndate(format.parse(jFormattedTextField1.getText()));
                            } catch (ParseException ex) {
                                docArrival.setIndate(new Date());
                            }
                    }
            }
        });         
    } 
    
    // инициализировать новый документ
    @Override
    public void docInit() {
        docArrival.init(2L);
        docArrival.setStorage(getSelectedStorage());
        recalculateDocument();      
    }   
    
    // заполнить отображение документа
    @Override
    public void repaintDocument() {
         // Заголовок 
        titleComplete.setLength(0);
        titleComplete.append(frameTitle).append(" /").append(docArrival.getOrganization().getName())
                .append("/ [").append(docArrival.getStatus().getName()).append("]");
        if (!saved)
            titleComplete.append("*");        
        this.setTitle(titleComplete.toString());
        // номер документа
        jLabel2.setText(docArrival.getFormattedID(docArrival.getId())); 
        // дата
        jFormattedTextField1.setText(format.format(docArrival.getIndate()));
        // описание
        jTextField2.setText(docArrival.getDescr());
        // пользователь
        jLabel14.setText(docArrival.getUsr().getName());
        // вылюта
        jLabel6.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        jLabel16.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        jLabel22.setText(MainFrame.sessionParams.getParam().getOkv().getRus());         
        //вес
        jLabel17.setText(String.format("%.3f", docArrival.getWeight()));
        // подразделение
        jTextField1.setText(docArrival.getDepartment().getName());
        // Склад
        jComboBox1.setSelectedItem(docArrival.getStorage().getName()); 
        // Cуммы без НДС
        jLabel10.setText(String.format("%.2f", getSum()));
        // НДС
        jLabel21.setText(String.format("%.2f", getNDS()));        
        // Сумма
        jLabel11.setText(String.format("%.2f", getResult()));
        // наименований
        jLabel4.setText(String.format("%d", tableModel.getRowCount()));  
        // контрагент
        if (docArrival.getContragent() != null)        
            jTextField3.setText(docArrival.getContragent().getName());
        
        if (docArrival.getStatus().getId() == 2 || docArrival.getStatus().getId() == 3) {
            closeButtons();
        }              
        // основание
        if (parentDoc != null)
            jLabel24.setText(parentDoc.getDocumentName()+" №"+docArrival.getFormattedID(parentDoc.getId()) + " от " +format.format(parentDoc.getIndate()));
        else
            jLabel24.setText("НЕТ");                     
    }  
    // сумма без ндс
    private float getSum() {
        float total = getResult();        
        return (total - getNDS());
    }
    // НДС
    private float getNDS() {
        return docArrival.getTotal()*docArrival.getOrganization().getNds()/100;
    }    
    // private float сумма с НДС
    private float getResult() {
        return docArrival.getTotal();
    }     
    
    // расчитать вес
    Float getWeight() {
        float weight = 0f;
        for (ArrivalProduct ap: products) 
            weight += ap.getProduct().getWeight()*ap.getCount();
        docArrival.setWeight(weight);
        return weight;
    }    
    
    // пересчитать таблицу
    void recalculateDocument() {
        Float sum = 0f;
        Float cost;
        for (int i = 0; i < tableModel.getRowCount(); i++) { 
            cost = Float.parseFloat(jTable1.getValueAt(i, 3).toString()) *
                    Float.parseFloat(jTable1.getValueAt(i, 5).toString());

            sum += cost;
            tableModel.setValueAt(i+1, i, 0);
            tableModel.setValueAt(cost, i, 6);
            products.get(i).setCount(Float.parseFloat(jTable1.getValueAt(i, 3).toString()));
            products.get(i).setCost(Float.parseFloat(jTable1.getValueAt(i, 5).toString()));
        }
        docArrival.setTotal(sum);
        getWeight();
        repaintDocument();
    } 
    
    // получить товары из документа и заполнить таблицу
    void getArrivalProducts(Arrival arrival) {      
        products.clear();
        if (!arrival.getArrivalProducts().isEmpty())
            products.addAll(arrival.getArrivalProducts());
        updateProductsTable();
    }
    
    // изменить таблицу несохраненными товарами
    void updateProductsTable() {

        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);
        int i = 0;
        
        try {
            Iterator it = products.iterator();
            while (it.hasNext()) {
                arrivalProduct = (ArrivalProduct) it.next();                
                product = arrivalProduct.getProduct();
                i++;
                tableModel.addRow(new Object[]{i, product.getArticul(), product.getName(), 
                    arrivalProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), arrivalProduct.getCost(),  
                    arrivalProduct.getCost()*arrivalProduct.getCount()});
            }
        } catch (Exception e) {System.out.print("ERROR! " + e);}
        
        tableModel.fireTableDataChanged();
        recalculateDocument();
    }   
    // добавить продукты в документ
    private void setArrivalProducts(ArrayList<? extends DocumentProduct> prods) throws IllegalAccessException, InvocationTargetException {
        newProducts.clear();
        ArrivalProduct ap;
        for (DocumentProduct pr : prods) {
            ap = new ArrivalProduct();
            if (pr.getCount() > 0) {
                BeanUtils.copyProperties(ap, pr);
                newProducts.add(ap);
            }
        }
        products = (ArrayList<ArrivalProduct>)newProducts.clone();
        for (ArrivalProduct pr : products) {
            pr.setArrival(docArrival);
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
            Logger.getLogger(DocArrival.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return storage;
    }
    
    // выполнить документ
    @Override
    public void executeDocument() {
        new DocumentCompletionDAO().completion(docArrival, products);
        closeButtons();        
    }    
    
    // сохранить документ
    @Override
    public void saveDocument(long status) {
        
        if (docArrival.getContragent() != null) {
        try {            
            docArrival.setDescr(jTextField2.getText());
            docArrival.setStatus(documentDAO.getStatus(status));                
            docArrival.setTotal(getResult());
            docArrival.setArrivalProducts(products);                                            
            
            documentDAO.updateDocument(docArrival);
            if (parentDoc != null)
                new DocumentUtil().saveParent(parentDoc, docArrival);              
            if (status == 2)
                executeDocument();            
            saved = true;
            repaintDocument();            
            
        } catch (Exception ex) {
             MainFrame.ifManager.showWarningDialog(this, "Ошибка", "Не удалось сохранить документ. " +ex);} 
        } else {
             MainFrame.ifManager.showWarningDialog(this, "Ошибка", "Не удалось сохранить документ. \nЗаполните поле контрагент.");
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
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 109, 240), 3));
        setClosable(true);
        setIconifiable(true);
        setTitle(frameTitle);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrival.png"))); // NOI18N
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

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setText("Сумма без НДС");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setText("Итого с НДС");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("0.00");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("0.00");

        jButton1.setText("Закрыть");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 88, 102)));
        jButton1.setPreferredSize(new java.awt.Dimension(70, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel12.setText("Примечание");

        jTextField2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField2.setMaximumSize(new java.awt.Dimension(300, 20));
        jTextField2.setMinimumSize(new java.awt.Dimension(200, 20));
        jTextField2.setPreferredSize(new java.awt.Dimension(300, 20));

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel13.setText("Пользователь:");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("_____");
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        jLabel6.setText("__");

        jLabel16.setText("__");

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel20.setText("НДС");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("0.00");

        jLabel22.setText("__");

        jLabel23.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel23.setText("Документ основание:");

        jLabel24.setText("_____");

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel26.setText("Всего наименований:");

        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel26)
                                            .addComponent(jLabel23))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 179, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jLabel16))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(791, 120));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("№");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("______");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Дата");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Контрагент");

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel15.setText("Склад:");

        jComboBox1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jComboBox1.setMaximumSize(new java.awt.Dimension(200, 22));
        jComboBox1.setMinimumSize(new java.awt.Dimension(200, 22));
        jComboBox1.setPreferredSize(new java.awt.Dimension(200, 22));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTextField3.setEditable(false);
        jTextField3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel19.setText("Подразделение");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/trash.png"))); // NOI18N
        jButton2.setToolTipText("Удалить");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton2.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton2.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton9.setToolTipText("Сохранить");
        jButton9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton9.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton9.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton5.setToolTipText("Сохранить документ");
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton5.setFocusPainted(false);
        jButton5.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton5.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton5.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton6.setToolTipText("Подбор товара");
        jButton6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton6.setFocusPainted(false);
        jButton6.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton6.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton6.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/documentadd.png"))); // NOI18N
        jButton4.setToolTipText("Ввести на основании");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setFocusPainted(false);
        jButton4.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton4.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton4.setPreferredSize(new java.awt.Dimension(24, 24));
        jToolBar1.add(jButton4);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton3.setToolTipText("Подчиненные документы");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setFocusPainted(false);
        jButton3.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton3.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jTextField1.setEditable(false);
        jTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jLabel8.setText("Вес");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("0.000");

        jLabel18.setText("кг.");

        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd.MM.yyyy H:mm"))));
        jFormattedTextField1.setMaximumSize(new java.awt.Dimension(105, 20));
        jFormattedTextField1.setMinimumSize(new java.awt.Dimension(105, 20));
        jFormattedTextField1.setPreferredSize(new java.awt.Dimension(105, 20));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calendar.png"))); // NOI18N
        jButton8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton8.setFocusPainted(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addGap(18, 333, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    // кнопка ЗАКРЫТЬ
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // подчиненные документы
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        docArrival.showSubordins();
    }//GEN-LAST:event_jButton3ActionPerformed

    // комбобокс СКЛАД
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == 2) {
            try {
                docArrival.setStorage(getSelectedStorage());
        } catch (Exception ex) {
            Logger.getLogger(DocArrival.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    // кнопка ДОБАВИТЬ ТОВАРЫ
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {            
            PriceName p = new PriceNameDAO().getPriceName(1);
            ProductAddDialog pad = new ProductAddDialog(null, true, docArrival.getStorage(), p, products);
            pad.setVisible(true);                   

            setArrivalProducts(products);
            updateProductsTable(); 
                            
        } catch (Exception ex) {
            Logger.getLogger(DocAct.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    // клавиши в таблице    
    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = jTable1.getSelectedRow();
            products.remove(index);
            tableModel.removeRow(index);
            updateProductsTable();

        }  
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            try {
                PriceName p = new PriceNameDAO().getPriceName(1);
                ProductAddDialog pad = new ProductAddDialog(null, true, docArrival.getStorage(), p, products);
                pad.setVisible(true);
                
                setArrivalProducts(products);          
                updateProductsTable();
            } catch (Exception ex) {
                Logger.getLogger(DocDeduct.class.getName()).log(Level.SEVERE, null, ex);
            }         
        }
    }//GEN-LAST:event_jTable1KeyPressed

    // кнопка СОХРАНИТЬ ШАБЛОН
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        
        try {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            JasperReport jr;
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/Arrival.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("docId", jLabel2.getText());
                parameters.put("date",  new MonthToTextBean().dateToText(jFormattedTextField1.getText()));
                parameters.put("storage", docArrival.getStorage().getName());
                parameters.put("company", docArrival.getUsr().getName());
                parameters.put("contragent", docArrival.getContragent().getName());
                parameters.put("sum", jLabel10.getText());
                parameters.put("weight", jLabel17.getText());
                parameters.put("count", tableModel.getRowCount());
                parameters.put("strsum", new NumberToTextBean().numberToText(Double.valueOf(jLabel11.getText())));
                
            List<ArrivalBean> ab = new ArrayList<>();
            ab.add(null);           
            for (int i = 0; i < tableModel.getRowCount(); i++)
                ab.add(new ArrivalBean(tableModel.getValueAt(i, 0).toString(),
                        tableModel.getValueAt(i, 1).toString(), tableModel.getValueAt(i, 2).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 3).toString()), tableModel.getValueAt(i, 4).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 5).toString()), Float.valueOf(tableModel.getValueAt(i, 6).toString())
                ));            

            JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
               parameters,  new JRBeanCollectionDataSource(ab));
            
            MainFrame.ifManager.showPreviw(jasperPrint);
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));            
            
        } catch (JRException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);

        }   // конец файлчузера         
    }//GEN-LAST:event_jButton5ActionPerformed

    // Выбрать контрагента  
    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        docArrival.showContragentDialog(docArrival);
        jTextField3.setText(docArrival.getContragent().getName());        
    }//GEN-LAST:event_jTextField3MouseClicked

    // Выполнить    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveDocument(2L);                        
    }//GEN-LAST:event_jButton7ActionPerformed

    // Сохранить
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        saveDocument(1L);
    }//GEN-LAST:event_jButton9ActionPerformed

    // скрыть при сворачивании
    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // Подразделение
        docArrival.showDepartmentDialog(docArrival);
        jTextField1.setText(docArrival.getDepartment().getName());
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        calendar.getPopup(jButton8).show();
        docArrival.setIndate(calendar.getDate());
        saved = false;          
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        // TODO add your handling code here:
        docArrival.showUserDialog(docArrival);
        jLabel14.setText(docArrival.getUsr().getName());        
    }//GEN-LAST:event_jLabel14MouseClicked


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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
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
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
