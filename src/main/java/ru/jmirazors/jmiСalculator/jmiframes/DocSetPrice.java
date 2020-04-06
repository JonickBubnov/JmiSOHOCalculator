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
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
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
import ru.jmirazors.jmiCalculator.beans.SetPriceBean;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.PriceNameDAO;
import ru.jmirazors.jmiСalculator.entity.DocumentType;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.SetPrice;
import ru.jmirazors.jmiСalculator.entity.SetPriceProduct;

/**
 *
 * @author User
 */
public final class DocSetPrice extends javax.swing.JInternalFrame implements DocumentImpl {

    /**
     * Creates new form DocSetPrice
     */
    
    JToolBar tb;
    JButton dockButton = new JButton("Док. уст. цен");     
    
    String frameTitle = "Установка цен номенклатуры";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static ArrayList<SetPriceProduct> products = new ArrayList<>();
    SetPriceProduct setPriceProduct;
    Product product;
    SetPrice docSetPrice;
    DocumentDAO documentDAO;
    DocumentUtil documentUtil = new DocumentUtil();
    DocumentType docType;
    // флаг первое открытие документа
    boolean firstOpen = true;    
    
    
        DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                if (col == 3)
                    return true;
                return false; 
            }
    };
    
    
    public DocSetPrice() {
        
        initTableColumns();
        docSetPrice = new SetPrice();
        products.clear();        
        initComponents();
        initVisualComponents();

        documentDAO = new DocumentDAO();
        
        docInit();
        repaintDocument();
    }
    
    public DocSetPrice(String id) {   

        documentDAO = new DocumentDAO();
        initTableColumns();
        try {
            docSetPrice = (SetPrice) documentDAO.getDocument(id.trim(), SetPrice.class);
        } catch (Exception ex) {
            Logger.getLogger(DocSetPrice.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        initComponents();
        initVisualComponents();
        getSetPriceProducts(docSetPrice);
        repaintDocument();                        
    }
    
    // *********************   методы документа **********************************
    // закрытие документа    
    @Override
     public void dispose() {
          super.dispose();          
          documentDAO.ev(docSetPrice);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocSetPriceFrameOpen(false);
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
    public void closeButtons(){
        jButton10.setEnabled(false);
        jButton9.setEnabled(false);
        jButton7.setEnabled(false);
        jButton6.setEnabled(false);
        jComboBox1.setEnabled(false);
        jTable1.setEnabled(false);
        jTextField1.setEnabled(false);
    }     
    
    // инициализировать таблицу
    @Override
    public void initTableColumns() {
        tableModel.addColumn("№");
        tableModel.addColumn("Артикул");
        tableModel.addColumn("Наименование");
        tableModel.addColumn("Цена");        
        tableModel.addColumn("Ед.изм.");
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
        jTable1.getColumnModel().getColumn(3).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(60);              
        
        tableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                recalculateTable();
            }
        });        
        
        getPriceNameList();
    } 
    
    // Инициализировать новый документ
    @Override
    public void docInit() {
        docSetPrice.setDocuments(documentDAO.getDocumentType(5l));
        docSetPrice.setOrganization(MainFrame.sessionParams.getOrganization());        
        docSetPrice.setUsr(MainFrame.sessionParams.getUser());
        docSetPrice.setIndate(new Date());
        docSetPrice.setStatus(documentDAO.getStatus(1l));
        docSetPrice.setPriceName(getSelectedPriceName());
        docSetPrice.setDescr("");
    }
    
    // заполнить отображение документа
    @Override
    public void repaintDocument() {
        this.setTitle(frameTitle + " ["+docSetPrice.getStatus().getName()+"]");
        if (docSetPrice.getId() != 0)
            jLabel4.setText(""+docSetPrice.getId());
        jDateChooser1.setDate(docSetPrice.getIndate());
        jTextField2.setText(docSetPrice.getOrganization().getName());
        jLabel8.setText(docSetPrice.getUsr().getName());
        //jFormattedTextField1.setText(String.valueOf(docInvoice.getDiscount()).replace(".", ","));
        jTextField1.setText(docSetPrice.getDescr());
        jComboBox1.setSelectedItem(docSetPrice.getPriceName().getName());
        if (docSetPrice.getStatus().getId() == 2 || docSetPrice.getStatus().getId() == 3) {
            closeButtons();
        }
        firstOpen = false;
    }    
    
    // пересчитать таблицу
    void recalculateTable() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {            
            products.get(i).setPrice(Float.parseFloat(jTable1.getValueAt(i, 3).toString()));
        }
    }
    
    // установить типы цен
    void getPriceNameList() {        
        jComboBox1.removeAllItems();        
        try {
            List priceNames = new PriceNameDAO().list("FROM PriceName WHERE del=1");
            Iterator it = priceNames.iterator();
            while (it.hasNext()) {
                PriceName priceName = (PriceName)it.next();
                jComboBox1.addItem(priceName.getName());
            }
        } catch (Exception e) {System.out.println(e);}
    } 
    
    // получить выбраный тип цены
    public PriceName getSelectedPriceName() {
        PriceName priceName;
        try {
            priceName = new PriceNameDAO().findPriceName(jComboBox1.getSelectedItem().toString());
        } catch (Exception ex) {
            Logger.getLogger(DocOffer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return priceName;        
    }    
    
    // получить товары из документа и заполнить таблицу
    void getSetPriceProducts(SetPrice setPrice) {
        products.clear();
            if (!setPrice.getSetPriceProducts().isEmpty())
                products.addAll(setPrice.getSetPriceProducts());                
            
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
                setPriceProduct = (SetPriceProduct)it.next();                
                product = setPriceProduct.getProduct();
                index++;
                tableModel.addRow(new Object[]{index, product.getArticul(), product.getName(), 
                    setPriceProduct.getPrice(), product.getUnit().getName()  });               
            }
        } catch (Exception e) {System.out.println("ERROR! " + e);}
        tableModel.fireTableDataChanged();

    }    
    
    // выполнить документ
    @Override
    public void executeDocument() {
            new DocumentCompletionDAO().completion(docSetPrice, products);
            closeButtons();
    }
    
    // Сохранить документ
    @Override
    public void saveDocument(long status) {
        try {
            docSetPrice.setIndate(jDateChooser1.getDate());
            docSetPrice.setDescr(jTextField1.getText());
            docSetPrice.setPriceName(getSelectedPriceName());
            docSetPrice.setUsr(MainFrame.sessionParams.getUser());
            docSetPrice.setOrganization(MainFrame.sessionParams.getOrganization());
            docSetPrice.setStatus(documentDAO.getStatus(status));                
            
            for (int i = 0; i < products.size(); i++) {                    
                    products.get(i).setSetPrice(docSetPrice);
            }              
            
            docSetPrice.setSetPriceProducts(products);
            
            documentDAO.updateDocument(docSetPrice);  
            
            if (status == 2)
                executeDocument();
                        
            repaintDocument();            

        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Не удалось сохранить документ \n" + e, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jToolBar1 = new javax.swing.JToolBar();
        jButton10 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(840, 460));

        jPanel1.setPreferredSize(new java.awt.Dimension(677, 110));

        jLabel1.setText("№");

        jLabel2.setText("Дата");

        jLabel4.setText("____");

        jComboBox1.setMaximumSize(new java.awt.Dimension(32767, 20));

        jLabel5.setText("Тип цен");

        jButton8.setText("Расчитать");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel9.setText("Формула");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+", "x", "-", "/" }));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setText("0");
        jFormattedTextField1.setPreferredSize(new java.awt.Dimension(12, 25));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%", "ед." }));
        jComboBox3.setMaximumSize(new java.awt.Dimension(42, 20));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton10.setToolTipText("Выполнить");
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

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton7.setToolTipText("Сохранить");
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

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton5.setToolTipText(" Сохранить документ");
        jButton5.setFocusPainted(false);
        jButton5.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/family-tree.png"))); // NOI18N
        jButton1.setToolTipText("Подчиненные документы");
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new java.awt.Dimension(20, 20));
        jToolBar1.add(jButton1);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down-arrow.png"))); // NOI18N
        jButton9.setToolTipText("Заполнить цены из справочника");
        jButton9.setFocusPainted(false);
        jButton9.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton9.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton9.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jLabel10.setText("Организация");

        jTextField2.setEditable(false);

        jDateChooser1.setEnabled(false);
        jDateChooser1.setPreferredSize(new java.awt.Dimension(129, 22));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2))
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(677, 80));

        jButton2.setText("Закрыть");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Примечание");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Пользователь");

        jLabel8.setText("_____");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(24, 24, 24)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap(14, Short.MAX_VALUE))
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Кнопка добавить товары
        ProductToSetPriceDialog pdtspd = new ProductToSetPriceDialog(null, true, products);
        pdtspd.setLocationRelativeTo(this);
        pdtspd.setVisible(true); 

        updateProductsTable();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < products.size(); i++) {
            tableModel.setValueAt(products.get(i).getProduct().getActualPrice(getSelectedPriceName()).getPrice(), i, 3);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        //добавить проверки на запятые
        float newPrice = 0;
        switch (jComboBox3.getSelectedItem().toString()) {
            case "%":
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    switch(jComboBox2.getSelectedItem().toString()) {
                        case "+":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString()) + 
                                    Float.valueOf(tableModel.getValueAt(i, 3).toString())*Float.valueOf(jFormattedTextField1.getText().replace(",", "."))/100;
                            break;
                        case "-":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString()) - 
                                    Float.valueOf(tableModel.getValueAt(i, 3).toString())*Float.valueOf(jFormattedTextField1.getText().replace(",", "."))/100;
                            break;
                        case "x":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString())*Float.valueOf(jFormattedTextField1.getText().replace(",", "."))/100;
                            break;
                        case "/":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString())*100/Float.valueOf(jFormattedTextField1.getText().replace(",", "."));
                            break;
                    }
                    tableModel.setValueAt(newPrice, i, 3);
                    products.get(i).setPrice(newPrice);
        }
                break;
            case "ед.":
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    switch (jComboBox2.getSelectedItem().toString()) {
                        case "+":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString()) + Float.valueOf(jFormattedTextField1.getText().replace(",", "."));
                            break;
                        case "-":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString()) - Float.valueOf(jFormattedTextField1.getText().replace(",", "."));
                            break;
                        case "/":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString()) / Float.valueOf(jFormattedTextField1.getText().replace(",", "."));
                            break;                            
                        case "x":
                            newPrice = Float.valueOf(tableModel.getValueAt(i, 3).toString()) * Float.valueOf(jFormattedTextField1.getText().replace(",", "."));
                            break;                            
                    }
                    tableModel.setValueAt(newPrice, i, 3);
                    products.get(i).setPrice(newPrice);
                }
                break;
    }
    }//GEN-LAST:event_jButton8ActionPerformed

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
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/setPrice.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("docId", jLabel4.getText());
                parameters.put("date",  new MonthToTextBean().dateToText(format.format(jDateChooser1.getDate())));
                parameters.put("count", tableModel.getRowCount());
                parameters.put("priceName", docSetPrice.getPriceName().getName());
                
            List<SetPriceBean> sb = new ArrayList<SetPriceBean>();
            sb.add(null);           
            for (int i = 0; i < tableModel.getRowCount(); i++)
                sb.add(new SetPriceBean(tableModel.getValueAt(i, 0).toString(),
                        tableModel.getValueAt(i, 1).toString(), tableModel.getValueAt(i, 2).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 3).toString()), "руб."
                )); 
            JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
               parameters,  new JRBeanCollectionDataSource(sb));

            JasperExportManager.exportReportToPdfFile(jasperPrint,
               fileToSave.getCanonicalPath());  
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            MainFrame.infoPanel.append("Файл " + fileToSave.getCanonicalPath() + " сохранен. \n"); 
        } catch (JRException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) { 
                Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }   // конец файлчузера              
               
    }//GEN-LAST:event_jButton5ActionPerformed
    // Сохранить документ
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveDocument(1L);        
    }//GEN-LAST:event_jButton7ActionPerformed

    // Выполнить документ
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        saveDocument(2L);              
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
           if (jTable1.getSelectedRow() >= 0) {
            int val = JOptionPane.showConfirmDialog(null, "Удалить товар: \n"+products.get(jTable1.getSelectedRow()).getProduct().getName(), "Вопрос", JOptionPane.YES_NO_OPTION);
                if (val == JOptionPane.YES_OPTION) {                    
                    products.remove(jTable1.getSelectedRow());
                    tableModel.removeRow(jTable1.getSelectedRow());
                }
           }
       }
    }//GEN-LAST:event_jTable1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
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
