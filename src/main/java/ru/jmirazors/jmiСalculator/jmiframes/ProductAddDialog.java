/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import ru.jmirazors.jmiСalculator.DAO.GroupDAO;
import ru.jmirazors.jmiСalculator.DAO.ProductDAO;
import ru.jmirazors.jmiСalculator.DAO.UnitDAO;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Unit;

/**
 *
 * @author User
 */
public class ProductAddDialog extends javax.swing.JDialog {

    /**
     * Creates new form ProductAddDialog
     */
    Product product;
    Group groupId;
    Unit unit;
    ArrayList<Unit> units = new ArrayList();
    
    public ProductAddDialog(java.awt.Frame parent, boolean modal, long groupId) {
        super(parent, modal);
        initComponents();
        getUnits();
        try {
            this.groupId = new GroupDAO().getById(groupId);
            System.out.println(this.groupId.getName());
        } catch (Exception ex) {
            Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ProductAddDialog(java.awt.Frame parent, boolean modal, Product product) {
        super(parent, modal);
        initComponents();
        getUnits();
        this.product = product;
        jTextField1.setText(product.getArticul());
        jTextField2.setText(product.getName());
        jTextField3.setText(product.getDescription());
        long size = product.getImage().length/1024;
        jTextField4.setText("Изображение: "+size+" Kb");
        jFormattedTextField1.setText((""+product.getWeight()).replace(".", ","));
        jComboBox1.setSelectedItem(product.getUnit().getName());
        
    }
    
    void getUnits() {
        try {
            units = (ArrayList<Unit>) new UnitDAO().list("from Unit where del=1");
        } catch (Exception ex) {
            Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        jComboBox1.removeAllItems();
        for (Unit ul: units) {
            jComboBox1.addItem(ul.getName());
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
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Товар");
        setModal(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel1.setText("Наименование:");

        jLabel2.setText("Описание:");

        jLabel3.setText("Изображение:");

        jButton1.setText("...");
        jButton1.setPreferredSize(new java.awt.Dimension(32, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Отмена");
        jButton2.setMaximumSize(new java.awt.Dimension(87, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(87, 23));
        jButton2.setPreferredSize(new java.awt.Dimension(87, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Сохранить");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Вес брутто (кг):");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.000"))));
        jFormattedTextField1.setText("0,000");
        jFormattedTextField1.setToolTipText("");
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Артикул:");

        jTextField4.setEditable(false);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Хранить на складе");
        jCheckBox1.setFocusPainted(false);

        jLabel6.setText("Ед. измерения:");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });

        jLabel7.setText("Штрихкод");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField2))))
                        .addGap(0, 43, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите изображение");
        fileChooser.showOpenDialog(null);
        jTextField4.setText(fileChooser.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (product == null) {
            product = new Product();
                product.setArticul(jTextField1.getText());
                product.setName(jTextField2.getText());
                product.setDescription(jTextField3.getText());
                product.setWeight(Float.parseFloat(jFormattedTextField1.getText().replace(",", ".")));
                //product.setParent((int)groupId);
                product.setBarcode(jTextField5.getText());
                product.setGroup(groupId);
                product.setUnit(unit);
                product.setDel((byte)1);
                product.setOnstock(jCheckBox1.isSelected()?(byte)1:(byte)0);
            
                File imgFile = new File(getClass().getResource("/images/no-image-icon.png").getFile());
                if (!jTextField4.getText().equals(""))
                    imgFile = new File(jTextField4.getText());

                byte[] bFile = new byte[(int) imgFile.length()];
                FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(imgFile);
                fileInputStream.read(bFile);
                fileInputStream.close();
                product.setImage(bFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
                new ProductDAO().updateProduct(product);
            } catch (Exception ex) {
                Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
                product.setArticul(jTextField1.getText());
                product.setName(jTextField2.getText());
                product.setDescription(jTextField3.getText());
                //product.setPrice(Float.parseFloat(jFormattedTextField1.getText().replace(",", ".")));
                product.setDel((byte)1);
                product.setOnstock(jCheckBox1.isSelected()?(byte)1:(byte)0);
                if (!jTextField4.getText().endsWith("Kb")) {
                    File imgFile = new File(jTextField4.getText());
                    byte[] bFile = new byte[(int) imgFile.length()];
                    FileInputStream fileInputStream;
                    try {
                        fileInputStream = new FileInputStream(imgFile);

                    fileInputStream.read(bFile);
                    fileInputStream.close();
                    product.setImage(bFile);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            try {
                new ProductDAO().updateProduct(product);
            } catch (Exception ex) {
                Logger.getLogger(ProductAddDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox1PropertyChange
        // TODO add your handling code here:
        System.out.println(evt.getPropertyName());
    }//GEN-LAST:event_jComboBox1PropertyChange

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == 2) {
            for (Unit un: units)
                if (un.getName().equals(jComboBox1.getSelectedItem()))
                    unit = un;
        }
        System.out.println(evt.getStateChange());
    }//GEN-LAST:event_jComboBox1ItemStateChanged



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
