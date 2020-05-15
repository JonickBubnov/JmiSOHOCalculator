/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.Documents;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.beanutils.BeanUtils;
import ru.jmirazors.jmiCalculator.MainFrame;
import ru.jmirazors.jmiCalculator.beans.CalendarPopup;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.Act;
import ru.jmirazors.jmiСalculator.entity.ActProduct;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.ProductAddDialog;


/**
 *
 * @author User
 */
public class DocAct extends javax.swing.JInternalFrame implements DocumentImpl {

    /**
     * Creates new form DocAct
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. Акт");    
    
    String frameTitle = "Акт выполненных работ";
    StringBuffer titleComplete = new StringBuffer();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    ArrayList<ActProduct> products = new ArrayList<>();
    ArrayList<ActProduct> newProducts = new ArrayList<>();
    ActProduct actProduct;
    Product product;
    Act docAct;
    DocumentDAO documentDAO;
    Document parentDoc = null;
    CalendarPopup calendar;
        
    
    private boolean saved = true;
    
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
    
    public DocAct() {
        
        calendar = new CalendarPopup();        
        documentDAO = new DocumentDAO();         
        initTableColumns();
        docAct = new Act();        
        products.clear();               
        initComponents();
        initVisualComponents();                        
        docInit();                
        
    }
    
    public DocAct(String id) { 
        
        calendar = new CalendarPopup();
        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
            docAct = (Act)documentDAO.getDocument(id.trim(), Act.class);            
        } catch (Exception ex) {
            MainFrame.ifManager.showWarningDialog(this, "Ошибка!", "Не удалось получить документ.\n"+ex);
        }
        initComponents();
        initVisualComponents();
        getActProducts(docAct);
        recalculateDocument();                
    } 
    
    // ************ имплементированные методы документа ********************
    // закрытие документа
     @Override
     public void dispose() {
          int val = -1;
          if (!saved)
              val = MainFrame.ifManager.showYesNoDialog(this, "Вопрос", "Документ был изменен. \nСохранить?");
          if (val == JOptionPane.YES_OPTION) {
              saveDocument(1L);
          } 
          super.dispose();
          documentDAO.ev(docAct);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocActFrameOpen(false);          

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
    
    // Инициализировать новый документ
    @Override
    public void docInit() {                        
        docAct.init(11L);
        recalculateDocument();
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
        jTable1.getColumnModel().getColumn(1).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(90);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(100);
               
        
        jFormattedTextField1.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                docAct.setDiscount(Integer.valueOf(jFormattedTextField1.getText()));
                totalRenew();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {
                saved = false;
            }
        });
        
        tableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 6 && e.getColumn() != 0) {
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
                            jFormattedTextField2.setText(calendar.getStringDate());
                            try {
                                docAct.setIndate(format.parse(jFormattedTextField2.getText()));
                            } catch (ParseException ex) {
                                docAct.setIndate(new Date());
                            }
                    }
            }
        });        
    }  
    
    
    // **************************************************************************************
    
    // заполнить отображение документа
    @Override
    public void repaintDocument() {
        // Заголовок 
        titleComplete.setLength(0);
        titleComplete.append(frameTitle).append(" /").append(docAct.getOrganization().getName())
                .append("/ [").append(docAct.getStatus().getName()).append("]");
        if (!saved)
            titleComplete.append("*");        
        this.setTitle(titleComplete.toString());                                       
        // номер документа
        jLabel2.setText(docAct.getFormattedID(docAct.getId()));                              
        // Дата документа
        jFormattedTextField2.setText(format.format(docAct.getIndate()));  
        // Подразделение
        jTextField1.setText(docAct.getDepartment().getName());
        // Пользователь
        jLabel10.setText(docAct.getUsr().getName());   
        // Скидка
        jFormattedTextField1.setText(String.valueOf(docAct.getDiscount())); 
        // Примечание
        jTextField2.setText(docAct.getDescr());                 
        // Валюта
        jLabel8.setText(MainFrame.sessionParams.getParam().getOkv().getRus());                    
        jLabel19.setText(MainFrame.sessionParams.getParam().getOkv().getRus());                   
        jLabel13.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        // Cуммы без НДС
        jLabel7.setText(String.format("%.2f", getSum()));
        // НДС
        jLabel18.setText(String.format("%.2f", getNDS()));
        // Итого
        jLabel12.setText(String.format("%.2f", getResult()));
        // наименований
        jLabel20.setText(String.format("%d", tableModel.getRowCount()));
        // основание
        if (parentDoc != null)
            jLabel21.setText(parentDoc.getDocumentName()+" №"+docAct.getFormattedID(parentDoc.getId()) + " от " +format.format(parentDoc.getIndate()));
        else
            jLabel21.setText("НЕТ");
        
        // Контрагент
        if (docAct.getContragent() != null)
            jTextField3.setText(docAct.getContragent().getName());                            
        if (docAct.getStatus().getId() == 2 || docAct.getStatus().getId() == 3) {
            closeButtons();
        }
        jTable1.requestFocus();
        //firstOpen = false;
    }     
    // сумма без ндс
    private float getSum() {
        float total = getResult();        
        return (total - getNDS());
    }
    // НДС
    private float getNDS() {
        return docAct.getTotal()*docAct.getOrganization().getNds()/100;
    }
    // private float сумма с НДС
    private float getResult() {
        return docAct.getTotal() - docAct.getTotal()*docAct.getDiscount()/100;
    }
    
    private void totalRenew() {
//        docAct.setDiscount(Integer.valueOf(jFormattedTextField1.getText()));
//        docAct.setTotal(docAct.getTotal() - docAct.getTotal()*docAct.getDiscount()/100);
        jLabel12.setText(String.format("%.2f", getResult()));
        jLabel18.setText(String.format("%.2f", getNDS()));
        jLabel7.setText(String.format("%.2f", getSum()));
        jLabel20.setText(String.format("%d", tableModel.getRowCount()));
//        repaintDocument();        
    }
    
    // пересчитать суммы
    void recalculateDocument() {
        Float sum = 0f;
        Float cost;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) { 
            cost = Float.parseFloat(jTable1.getValueAt(i, 3).toString())*Float.parseFloat(jTable1.getValueAt(i, 5).toString());            
            sum += cost;
            tableModel.setValueAt(i+1, i, 0);
            tableModel.setValueAt(cost, i, 6);
            products.get(i).setCount(Float.parseFloat(jTable1.getValueAt(i, 3).toString()));
            products.get(i).setCost(Float.parseFloat(jTable1.getValueAt(i, 5).toString()));
        }
        docAct.setTotal(sum);
        repaintDocument();
    }    
    
    // получить товары из документа и заполнить таблицу
    void getActProducts(Act act) {
        products.clear();
            if (!act.getActProducts().isEmpty())
                products.addAll(act.getActProducts());                
            
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
                actProduct = (ActProduct)it.next();                
                product = actProduct.getProduct();
                index++;
                tableModel.addRow(new Object[]{index, product.getArticul(), product.getName(), 
                    actProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), actProduct.getCost(), 
                    actProduct.getCost()*actProduct.getCount() });               
            }
        } catch (Exception e) {System.out.println("ERROR! " + e);}
        tableModel.fireTableDataChanged();
        recalculateDocument();
    }  
    
    // добавить продукты в документ
    private void setActProducts(ArrayList<? extends DocumentProduct> prods) throws IllegalAccessException, InvocationTargetException {
        newProducts.clear();
        ActProduct ap;
        for (DocumentProduct pr : prods) {
            ap = new ActProduct();
            if (pr.getCount() > 0) {
                BeanUtils.copyProperties(ap, pr);
                newProducts.add(ap);
            }
        }
        products = (ArrayList<ActProduct>)newProducts.clone();
        for (ActProduct pr : products) {
            pr.setAct(docAct);
        }
    }
    
    @Override
    public void executeDocument() {
        //new DocumentCompletionDAO().completion(docAct);
        //closeButtons();
    }    
    @Override
    public void saveDocument(long status) {
        if (docAct.getContragent() != null) {                               
            try {            
                docAct.setDescr(jTextField2.getText());
                docAct.setStatus(documentDAO.getStatus(status));
                docAct.setTotal(getResult());
                docAct.setActProducts(products);
            
                documentDAO.updateDocument(docAct);
                
                if (status == 2)
                    executeDocument();
                saved = true;
                repaintDocument();
            
            } catch (Exception ex) {
                MainFrame.ifManager.showWarningDialog(this, "Ошибка", ex.toString());
            }
        } else { 
            MainFrame.ifManager.showWarningDialog(this, "Ошибка", "Не удалось сохранить документ. \n Заполните поле контрагент.");
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
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 109, 240), 3));
        setClosable(true);
        setIconifiable(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/complete.png"))); // NOI18N
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

        jPanel1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(836, 120));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(76, 26));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton1.setToolTipText("Выполнить документ");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton1.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton1.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton3.setToolTipText("Сохранить документ");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton3.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton3.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton4.setToolTipText("Сохранить файл");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setFocusPainted(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton4.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton4.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton5.setToolTipText("Подбор товара");
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton5.setFocusPainted(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton5.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/documentadd.png"))); // NOI18N
        jButton6.setToolTipText("Ввести на основании");
        jButton6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton6.setFocusPainted(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton6.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton6);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton7.setToolTipText("Подчиненные документы");
        jButton7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton7.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("№");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("_______");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Дата");
        jLabel3.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Подразделение");

        jTextField1.setEditable(false);
        jTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.setPreferredSize(new java.awt.Dimension(73, 20));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jLabel14.setText("%");

        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.setText("0");

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel15.setText("Скидка");

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel16.setText("Контрагент");

        jTextField3.setEditable(false);
        jTextField3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField3.setPreferredSize(new java.awt.Dimension(73, 20));
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm"))));
        jFormattedTextField2.setPreferredSize(new java.awt.Dimension(105, 20));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calendar.png"))); // NOI18N
        jButton8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton8.setFocusPainted(false);
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
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 418, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(838, 110));

        jLabel5.setText("Сумма без НДС");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Примечание");

        jTextField2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField2.setMaximumSize(new java.awt.Dimension(300, 20));
        jTextField2.setMinimumSize(new java.awt.Dimension(200, 20));
        jTextField2.setPreferredSize(new java.awt.Dimension(300, 20));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("jLabel7");

        jButton2.setText("Закрыть");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.setFocusPainted(false);
        jButton2.setMaximumSize(new java.awt.Dimension(70, 24));
        jButton2.setMinimumSize(new java.awt.Dimension(70, 24));
        jButton2.setPreferredSize(new java.awt.Dimension(70, 24));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel11.setText("Итого с НДС");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("jLabel12");

        jLabel13.setText("jLabel13");

        jLabel17.setText("НДС");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("jLabel18");

        jLabel19.setText("jLabel19");

        jLabel21.setText("___");

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel22.setText("Документ основание:");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setText("Всего наименований:");

        jLabel20.setText("jLabel20");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("jLabel10");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel23.setText("Пользователь:");

        jLabel8.setText("jLabel8");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel17)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addGap(46, 46, 46)
                                    .addComponent(jLabel9))))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22)))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 159, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel9)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // TODO add your handling code here:
        docAct.showDepartmentDialog(docAct);
        saved = false;
        repaintDocument();
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        // TODO add your handling code here:
        docAct.showContragentDialog(docAct);
        saved = false;
        repaintDocument();
    }//GEN-LAST:event_jTextField3MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
        docAct.showUserDialog(docAct);
        saved = false;
        repaintDocument();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        docAct.showSubordins();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Диалог выбор товара
        try {
            Storage s = new StorageDAO().getStorage("1");
            PriceName p = new PriceNameDAO().getPriceName(1);
            ProductAddDialog pad = new ProductAddDialog(null, true, s, p, products);
            pad.setVisible(true);                   

            setActProducts(products);
            updateProductsTable(); 
                
            
        } catch (Exception ex) {
            Logger.getLogger(DocAct.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Календарь   
        calendar.getPopup(jButton8).show();
        docAct.setIndate(calendar.getDate());
        saved = false;
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Сохранить документ
        saveDocument(1L);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // кнопки в таблице
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {            
            int index = jTable1.getSelectedRow();
            products.remove(index);
            tableModel.removeRow(index);
            updateProductsTable();
        }
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            try {
                Storage s = new StorageDAO().getStorage("1");
                PriceName p = new PriceNameDAO().getPriceName(1);  
                ProductAddDialog pad = new ProductAddDialog(null, true, s, p, products);
                pad.setVisible(true);                   

                setActProducts(products);
                updateProductsTable();             
            } catch (Exception e) {}
        }
    }//GEN-LAST:event_jTable1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
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

    @Override
    public void closeButtons() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




}
