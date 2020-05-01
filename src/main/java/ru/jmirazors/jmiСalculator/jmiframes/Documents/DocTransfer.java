/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.Documents;

import ru.jmirazors.jmiCalculator.MainFrame;
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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ru.jmirazors.jmiCalculator.beans.MonthToTextBean;
import ru.jmirazors.jmiCalculator.beans.ProductBean;
import ru.jmirazors.jmiСalculator.DAO.DocumentCompletionDAO;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.StorageDAO;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.Transfer;
import ru.jmirazors.jmiСalculator.entity.TransferProduct;
import ru.jmirazors.jmiСalculator.jmiframes.ProductToCartDialog;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.DepartmentSelectDialog;

/**
 *
 * @author User
 */
public class DocTransfer extends javax.swing.JInternalFrame implements DocumentImpl{

    /**
     * Creates new form DocTransfer
     */
    JToolBar tb;
    JButton dockButton = new JButton("Док. перемещение|");     
    
    String frameTitle = "Перемещение товаров";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static ArrayList<TransferProduct> products = new ArrayList<>();
    TransferProduct transferProduct;
    Product product;
    Transfer docTransfer;
    DocumentDAO documentDAO;
    
    DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int col)
            {
                if (col == 3 || col == 5 || col == 6)
                    return true;
                return false; 
            }
    };
    
    public DocTransfer() {
        initTableColumns();
        docTransfer = new Transfer();
        products.clear();
        initComponents();
        initVisualComponents(); 
        
        documentDAO = new DocumentDAO();
        docInit();
        repaintDocument();        
        
//        jLabel4.setText(format.format(new Date()));
//        jLabel14.setText(MainFrame.sessionParams.getUser().getName());
//        jTextField1.setText(MainFrame.sessionParams.getOrganization().getName());
//        
//        documentDAO = new DocumentDAO();
//        docTransfer.setDocuments(documentDAO.getDocumentType(4l));
//        this.setTitle(frameTitle);
    }
    
    public DocTransfer(String id) {
        
        documentDAO = new DocumentDAO();        
        initTableColumns();
        try {
            docTransfer = (Transfer)new DocumentDAO().getDocument(id.trim(), Transfer.class);
        } catch (Exception ex) {
            Logger.getLogger(DocArrival.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        initVisualComponents();
        getTransferProducts(docTransfer);
        repaintDocument();         
        
//        products.clear();
//        initComponents();
//        initVisualComponents();
//        getTransferProducts(docTransfer);
//        
//        jLabel2.setText(""+docTransfer.getId());
//        jLabel4.setText(format.format(docTransfer.getIndate()));
//        jTextField1.setText(docTransfer.getOrganization().getName());
//        jTextField2.setText(docTransfer.getDescr());
//        jLabel14.setText(docTransfer.getUsr().getName());
//        jLabel11.setText(""+docTransfer.getTotal());
//        jComboBox1.setSelectedItem(docTransfer.getStorage_from().getName());
//        jComboBox2.setSelectedItem(docTransfer.getStorage_to().getName());
//        jLabel6.setText(String.format("%.3f", docTransfer.getWeight()));
//        this.setTitle(title+" ["+docTransfer.getStatus().getName()+"]");
//        
//        if (docTransfer.getStatus().getId() == 2 || docTransfer.getStatus().getId() == 3) {
//            closeButtons();
//        }          
    }
    // *********************     Методы документа ****************************************
    // закрытие документа    
    @Override
     public void dispose() {
          super.dispose();          
          documentDAO.ev(docTransfer);
          tb.remove(dockButton);
          tb.repaint();
          MainFrame.ifManager.setDocTransferFrameOpen(false);
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
            jButton8.setEnabled(false); 
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
        jTable1.getColumnModel().getColumn(5).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(100);        
        
        getStorageList();        
        
        tableModel.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != 6 && e.getColumn() != 0) {                  
                    recalculateDocument();
                }
            }
        });
    }
    
    // инициализировать новый документ
    @Override
    public void docInit() {
        docTransfer.setDocuments(documentDAO.getDocumentType(4L));
        docTransfer.setOrganization(docTransfer.getSessionOrganization());        
        docTransfer.setUsr(docTransfer.getSessionUser());
        docTransfer.setDepartment(docTransfer.getSessionUser().getDepartment());
        docTransfer.setWeight(0);
        docTransfer.setTotal(0);
        docTransfer.setIndate(new Date());
        docTransfer.setStatus(documentDAO.getStatus(1L));
        docTransfer.setStorage_from(getSelectedStorage(jComboBox1));
        docTransfer.setStorage_to(getSelectedStorage(jComboBox2));
        docTransfer.setDescr("");       
    }    
    
    // заполнить отображение документа
    @Override
    public void repaintDocument() {
        this.setTitle(frameTitle + " ["+docTransfer.getStatus().getName()+"]");        
        if (docTransfer.getId() != 0)
            jLabel2.setText(""+docTransfer.getId()); 
        jDateChooser1.setDate(docTransfer.getIndate());
        jTextField2.setText(docTransfer.getDescr());
        jLabel14.setText(docTransfer.getUsr().getName());
        jLabel11.setText(""+docTransfer.getTotal());
        jTextField1.setText(docTransfer.getDepartment().getName());
        jLabel16.setText(MainFrame.sessionParams.getParam().getOkv().getRus());
        jLabel8.setText(docTransfer.getOrganization().getName());

        jComboBox1.setSelectedItem(docTransfer.getStorage_from().getName());
        jComboBox2.setSelectedItem(docTransfer.getStorage_to().getName());
    
        if (docTransfer.getStatus().getId() == 2 || docTransfer.getStatus().getId() == 3) {
            closeButtons();
        }          
    }        
    
    // расчитать вес
    Float getWeight() {
        float weight = 0f;
        for (TransferProduct ip: products) 
            weight += ip.getProduct().getWeight()*ip.getCount();
        docTransfer.setWeight(weight);
        return weight;
    }    
    
     // пересчитать таблицу
    private void recalculateDocument() {
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
        jLabel11.setText(""+sum);
        jLabel6.setText(""+getWeight());
        docTransfer.setTotal(Float.parseFloat(jLabel11.getText()));
    } 
    
    // получить товары из документа и заполнить таблицу
    public void getTransferProducts(Transfer transfer) {
        products.clear();
            if (transfer.getTransferProducts() != null)
                products.addAll(transfer.getTransferProducts());
            
            updateProductsTable();        
    }
    
    // обновить таблицу с товарами    
    private void updateProductsTable() {
        for (int i=tableModel.getRowCount(); i > 0; i--) {
            tableModel.removeRow(i-1);
        }
        tableModel.setRowCount(0);
        int i = 0;
        
        try {
            Iterator it = products.iterator();
            while (it.hasNext()) {
                transferProduct = (TransferProduct) it.next();                
                product = transferProduct.getProduct();
                i++;
                tableModel.addRow(new Object[]{i, product.getArticul(), product.getName(), 
                    transferProduct.getCount(),  
                    product.getUnit().getName()==null?"":product.getUnit().getName(), transferProduct.getCost(),  
                    transferProduct.getCost()*transferProduct.getCount()});
            }
        } catch (Exception e) {System.out.print("ERROR! " + e);}
        
        tableModel.fireTableDataChanged();
        recalculateDocument();
    }
    
    // установить склады
    private void getStorageList() {
        
        jComboBox1.removeAllItems();
        jComboBox2.removeAllItems();
        try {
            
            List storages = new StorageDAO().list('e');
            Iterator it = storages.iterator();
            while (it.hasNext()) {
                Storage storage = (Storage)it.next();
                jComboBox1.addItem(storage.getName());
                jComboBox2.addItem(storage.getName());
            }            
        } catch (Exception e) {System.out.print(e);}   
    }  
    
    // получить выбранный склад    
    public Storage getSelectedStorage(JComboBox comboBox) {
        Storage storage;
        try {
            storage = new StorageDAO().findStorage(comboBox.getSelectedItem().toString());
        } catch (Exception ex) {
            Logger.getLogger(DocTransfer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return storage;
    }
    
    @Override
    public void executeDocument() {
        new DocumentCompletionDAO().completion(docTransfer, products);
        closeButtons();
    }    
    
    // сохранить документ
    @Override
    public void saveDocument(long status) {
        
        try {            
            docTransfer.setIndate(new Date());
            docTransfer.setDescr(jTextField2.getText());
            docTransfer.setStorage_from(getSelectedStorage(jComboBox1));
            docTransfer.setStorage_to(getSelectedStorage(jComboBox2));
            docTransfer.setUsr(MainFrame.sessionParams.getUser());
            docTransfer.setOrganization(MainFrame.sessionParams.getOrganization());
            docTransfer.setTotal(Float.valueOf(jLabel11.getText().replace(",", ".")));
            docTransfer.setStatus(documentDAO.getStatus(status));                        
            
            for (int i = 0; i < products.size(); i++) {                    
                    products.get(i).setTransfer(docTransfer);
            }  
            
            docTransfer.setTransferProducts(products);             
            
            documentDAO.updateDocument(docTransfer);
            
            if (status == 2)
                executeDocument();
            
            repaintDocument();             
            
            
//            if (!execute)
//                docTransfer.setStatus(documentDAO.getStatus(1l));
//            else 
//                docTransfer.setStatus(documentDAO.getStatus(2l));
//            
//            for (int i = 0; i < products.size(); i++) {                    
//                    products.get(i).setTransfer(docTransfer);
//            }  
//            
//            docTransfer.setTransferProducts(products);             
//            
//            docTransfer = (Transfer) documentDAO.updateDocument(docTransfer);
            
//           if (execute) {
//                new UtilDAO().stockMinus2(products, docTransfer.getStorage_from());
//                new UtilDAO().stockPlus2(products, docTransfer.getStorage_to());
//                //new UtilDAO().updateSkladDeduct(products, docDeduct);         
//            }             
//            
//            jLabel2.setText(""+docTransfer.getId()); 
//            this.setTitle(frameTitle + " ["+docTransfer.getStatus().getName()+"]");
            
        } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Не удалось сохранить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);} 
    
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
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jToolBar1 = new javax.swing.JToolBar();
        jButton10 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle(title);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/transfer.png"))); // NOI18N
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

        jLabel9.setText("Итого");

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

        jLabel16.setText("___");

        jLabel5.setText("Вес");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("0.000");

        jLabel4.setText("Организация");

        jLabel8.setText("_____");

        jLabel10.setText("кг.");

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
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(362, 362, 362))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(21, 21, 21)
                        .addComponent(jTextField2)
                        .addGap(152, 152, 152))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabel16)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jLabel1.setText("№");

        jLabel2.setText("_____");

        jLabel3.setText("Дата");

        jLabel15.setText("Со склада");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel21.setText("На склад");

        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/trash.png"))); // NOI18N
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton10);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exe.png"))); // NOI18N
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file_save.png"))); // NOI18N
        jButton5.setToolTipText("Сохранить документ");
        jButton5.setFocusPainted(false);
        jButton5.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cartadd.png"))); // NOI18N
        jButton6.setToolTipText("Добавить товар");
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

        jLabel7.setText("Подразделение");

        jTextField1.setEditable(false);
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jDateChooser1.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1)))
                .addGap(80, 203, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1, 0, 220, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, 220, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(396, 396, 396))
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
                        .addComponent(jLabel15)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel7)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // подчиненные документы
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        docTransfer.showSubordins();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == 2) {
            try {
                docTransfer.setStorage_from(getSelectedStorage(jComboBox1));
            } catch (Exception ex) {
                Logger.getLogger(DocTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        
        ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(jComboBox1), products, null, "transfer");
        pdtcd.setLocationRelativeTo(this);
        pdtcd.setVisible(true);

        updateProductsTable();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = jTable1.getSelectedRow();
            docTransfer.getTransferProducts().remove((TransferProduct)products.get(index));
            products.remove(index);
            tableModel.removeRow(index);

            updateProductsTable();
        }
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            ProductToCartDialog pdtcd = new ProductToCartDialog(null, true, getSelectedStorage(jComboBox1), products, null, "transfer");
            pdtcd.setLocationRelativeTo(this);
            pdtcd.setVisible(true);
        
            updateProductsTable();            
        }          
    }//GEN-LAST:event_jTable1KeyPressed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == 2) {
            try {
                docTransfer.setStorage_to(getSelectedStorage(jComboBox2));
            } catch (Exception ex) {
                Logger.getLogger(DocTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }//GEN-LAST:event_jComboBox2ItemStateChanged

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
            jr = JasperCompileManager.compileReport( getClass().getClassLoader().getResource("reports/transfer.jrxml").getFile() );
            Map<String, Object> parameters = new HashMap<>();
                parameters.put("docId", jLabel2.getText());
                parameters.put("date",  new MonthToTextBean().dateToText(format.format(jDateChooser1.getDate())));
                parameters.put("user", docTransfer.getUsr().getName());
                parameters.put("skladFrom", docTransfer.getStorage_from().getName());
                parameters.put("skladTo", docTransfer.getStorage_to().getName());
                //parameters.put("contragent", docInvoice.getContragent().getName());
                //parameters.put("sum", jLabel11.getText());
                //parameters.put("discount", jFormattedTextField1.getText());
                parameters.put("ptotal", String.valueOf(tableModel.getRowCount()));
                parameters.put("weight", jLabel6.getText()+" кг");
                //parameters.put("count", tableModel.getRowCount());
                //parameters.put("strsum", new NumberToTextBean().numberToText(Double.valueOf(jLabel11.getText())));
                
            List<ProductBean> ib = new ArrayList<>();
            ib.add(null);           
            for (int i = 0; i < tableModel.getRowCount(); i++)
                ib.add(new ProductBean(Integer.parseInt(tableModel.getValueAt(i, 0).toString()),
                        tableModel.getValueAt(i, 1).toString(), "", tableModel.getValueAt(i, 2).toString(),
                        Float.valueOf(tableModel.getValueAt(i, 3).toString()), tableModel.getValueAt(i, 4).toString(),
                        0f, 0f, 
                        0f
                ));            

            JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
               parameters,  new JRBeanCollectionDataSource(ib));

            JasperExportManager.exportReportToPdfFile(jasperPrint,
               fileToSave.getCanonicalPath());  
            super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            MainFrame.ifManager.infoMessage("Файл " + fileToSave.getCanonicalPath() + " сохранен.");
            
        } catch (JRException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) { 
                Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }   // конец файлчузера    
    
    }//GEN-LAST:event_jButton5ActionPerformed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        hide();
    }//GEN-LAST:event_formInternalFrameIconified

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
//        OrganizationSelectDialog osd = new OrganizationSelectDialog(null, true, docTransfer);
//        osd.setLocationRelativeTo(this);
//        osd.setVisible(true);
//        jTextField1.setText(docTransfer.getOrganization().getName());
//        recalculateDocument();
          DepartmentSelectDialog sdd = new DepartmentSelectDialog(null, true, docTransfer);
          sdd.setLocationRelativeTo(this);
          sdd.setVisible(true);
          jTextField1.setText(docTransfer.getDepartment().getName()); 
    }//GEN-LAST:event_jTextField1MouseClicked
    // сохранить
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        saveDocument(1L);
    }//GEN-LAST:event_jButton8ActionPerformed

    // выполнить
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        saveDocument(2L);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // печать
        
    }//GEN-LAST:event_jButton5MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
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
